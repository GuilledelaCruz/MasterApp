package guilledelacruz.masterapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by guilledelacruz on 2/02/16.
 */
public class ClientConnection implements Runnable{

    private Socket cliente;
    private TCPServer.ServerThread server;
    private DataInputStream in;
    private DataOutputStream out;
    private Player player; // nickname and ipaddress to identify player

    public ClientConnection (Socket socket, TCPServer.ServerThread server){
        cliente = socket;
        this.server = server;
        //player = new Player("Jugador"+new Random().nextInt(10), cliente.getInetAddress().getHostAddress());
    }

    public void run() {
        try{
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());
            out.writeUTF("");
            String nick = in.readUTF();
            player = new Player(nick, cliente.getInetAddress().getHostAddress());
            server.messageToRoom();
            messaging();
        }catch (Exception exc){}
        close();
    }

    private void messaging() throws IOException{
        String read;
        while(cliente.isConnected()){
            read = in.readUTF();
        }
    }

    public void sendMessage(int command){
        switch(command){
            case 0:

                break;

            default: break;
        }
    }

    public Player getPlayer(){
        return player;
    }

    public boolean close() {
        server.disconnect(this);
        try {
            cliente.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
