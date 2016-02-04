package guilledelacruz.masterapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by guilledelacruz
 */
public class TCPClient extends Service {

    private ClientThread hilo = null;
    public static Handler handler = null;
    private Socket server;
    private String nombre;
    private String pass;
    private String ip = null;
    private final int puerto = 13473;
    private DataInputStream in;
    private DataOutputStream out;

    public IBinder onBind(Intent intent) {return null;}

    public int onStartCommand(Intent intent, int flags, int startId) {
        hilo = new ClientThread();
        hilo.start();
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        hilo.closeClient();
    }

    class ClientThread extends Thread{

        public ClientThread (){
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case 0:
                            nombre = (String) msg.obj;
                            break;
                        case 1:
                            ip = (String) msg.obj;
                            break;
                        case 2:
                            pass = (String) msg.obj;
                            break;
                        default:
                            break;
                    }
                }
            };

            getInfo();
        }

        private void getInfo(){
            Message getnombre = new Message();
            getnombre.what = 0;
            BuscarSala.handler.sendMessage(getnombre);

            Message getip = new Message();
            getip.what = 1;
            BuscarSala.handler.sendMessage(getip);
        }

        public void run() {
            while(ip == null);
            try{
                server = new Socket(ip, puerto);
                connected();
                in = new DataInputStream(server.getInputStream());
                out = new DataOutputStream(server.getOutputStream());
                in.readUTF();
                out.writeUTF(nombre);
                messaging();
            }catch (Exception exc){
                onDestroy();
            }
        }

        private void connected(){
            Message getip = new Message();
            getip.what = 10;
            BuscarSala.handler.sendMessage(getip);
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
