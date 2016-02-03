package guilledelacruz.masterapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by guilledelacruz on 2/02/16.
 */
public class TCPClient extends Service {

    private ClientThread hilo = null;
    public static Handler handler = null;
    private Socket server;
    private String ip;
    private final int puerto = 13473;
    private DataInputStream in;
    private DataOutputStream out;

    public IBinder onBind(Intent intent) {return null;}

    public int onStartCommand(Intent intent, int flags, int startId) {
        hilo = new ClientThread();
        hilo.start();
        Log.i("TCP", "Created thread");
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        hilo.closeClient();
    }

    class ClientThread extends Thread{

        public ClientThread (){
            Log.i("TCP", "Create handler");
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case 1:
                            ip = (String) msg.obj;
                            break;
                        default:
                            break;
                    }
                }
            };
        }

        public void run() {
            Log.i("TCP", "Intent to connect");
            try{
                server = new Socket(Sala.getIP(), puerto);
                Log.i("TCP", "ClientConection");
                in = new DataInputStream(server.getInputStream());
                out = new DataOutputStream(server.getOutputStream());
                in.readUTF();
                Log.i("TCP", "User: "+Sala.getNombre());
                out.writeUTF(Sala.getNombre());
                messaging();
            }catch (Exception exc){}
        }

        private void messaging() throws IOException {
            String read;
            while(server.isConnected()){
                read = in.readUTF();
            }
        }

        private void closeClient(){
            try {
                server.close();
            }catch (Exception exc){}
        }
    }
}
