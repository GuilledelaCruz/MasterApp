package guilledelacruz.masterapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer extends Service {

    private ServerThread hilo = null;
    public static Handler handler = null;

    private ServerSocket server;
    private final int puerto = 13473;
    private List<ClientConnection> connections;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        connections = new ArrayList<>();
        hilo = new ServerThread();
        hilo.start();
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        hilo.closeServer();
    }

    class ServerThread extends Thread{

        public ServerThread (){
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case 0:
                            messageToRoom();
                            break;
                        default:
                            break;
                    }
                }
            };
        }

        public void messageToRoom(){
            if (Sala.handler != null) {
                Message msgToRoom = new Message();
                msgToRoom.what = 0;
                List<Player> players = new ArrayList<>();
                for (ClientConnection c : connections) {
                    players.add(c.getPlayer());
                }
                msgToRoom.obj = players;
                Sala.handler.sendMessage(msgToRoom);
            }
        }

        public void run() {
            try {
                server = new ServerSocket(puerto);
                //Log.i("TCPServer", "ServerSocket started");
                while(!server.isClosed()){
                    // accepts a new client, save it in list, start thread and message to Room
                    Socket cliente = server.accept();
                    ClientConnection client = new ClientConnection(cliente, this);
                    connections.add(client);
                    new Thread(client).start();
                    //messageToRoom();
                }
            }catch (Exception exc){}
        }

        private void closeServer(){
            for (ClientConnection c: connections){
                c.close();
            }
            connections.clear();
            try {
                server.close();
            }catch (Exception exc){}
        }

        public void disconnect(ClientConnection cc){
            connections.remove(cc);
            messageToRoom();
        }
    }
}
