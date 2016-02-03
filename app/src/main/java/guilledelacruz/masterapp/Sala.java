package guilledelacruz.masterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Sala extends AppCompatActivity {

    private String sala = "";
    private static String ip = "";
    private String rol = "";
    private static String nombre = "";
    private String pass = "";
    private Integer cap = 0;
    private Boolean anon = false;
    private List<String> jugadores = new ArrayList<String>();
    public static Handler handler = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);
        // get views
        final TextView viewanonimo = (TextView) findViewById(R.id.textosalaanonimo);
        final TextView viewvotado = (TextView) findViewById(R.id.textosalavotado);
        final Button botonvotacion = (Button) findViewById(R.id.botonsalavotacion);
        final ListView listjugadores = (ListView) findViewById(R.id.listsalajugadores);

        // get values sended in previous screen
        Intent intent = getIntent();
        sala = intent.getStringExtra("sala");
        ip = intent.getStringExtra("ip");
        nombre = intent.getStringExtra("nombre");
        rol = intent.getStringExtra("rol");
        pass = intent.getStringExtra("pass");
        if (rol.equals("master"))
            cap = Integer.parseInt(intent.getStringExtra("cap"));
        anon = intent.getBooleanExtra("anon", false);

        // put info on screen
        setTitle(jugadores.size() + "/" + cap + " - " + sala);
        if (anon) viewanonimo.setText(getResources().getString(R.string.textoanonimo));
        else viewanonimo.setText(getResources().getString(R.string.textopublico));

        // prepare list of players
        JugadorAdapter adapter = new JugadorAdapter(this, jugadores.toArray(new String[jugadores.size()]));
        listjugadores.setAdapter(adapter);
        listjugadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView texto = (TextView) view.findViewById(R.id.adaptertext);
                TableRow row = (TableRow) findViewById(R.id.tablerowsalavotacion);
                if(row.getVisibility() == View.VISIBLE)
                    viewvotado.setText(texto.getText());
            }
        });

        if (rol.equals("master")) master();
        else player();

        botonvotacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TCPServer.handler != null) {
                    Toast.makeText(Sala.this, "Message sent", Toast.LENGTH_SHORT).show();
                    Message mstToTCPServer = new Message();
                    mstToTCPServer.what = 0;
                    TCPServer.handler.sendMessage(mstToTCPServer);
                }
            }
        });

        createHandler();
    }

    public void master(){
        final TextView viewmaster = (TextView) findViewById(R.id.textosalamaster);
        final Button botonvotacion = (Button) findViewById(R.id.botonsalavotacion);
        viewmaster.setText(nombre);
        botonvotacion.setVisibility(View.VISIBLE);
        startMasterService();
    }

    public void player(){
        startClientService();
    }

    public void createHandler(){
        handler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        List<Player> jug = (List<Player>) msg.obj;
                        jugadores = new ArrayList<>();
                        for(Player p: jug){
                            jugadores.add(p.getNickname());
                        }
                        final ListView listjugadores = (ListView) findViewById(R.id.listsalajugadores);
                        JugadorAdapter adapter = new JugadorAdapter(Sala.this, jugadores.toArray(new String[jugadores.size()]));
                        listjugadores.setAdapter(adapter);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                alert();
                return true;
            default: return false;
        }
    }

    private void alert(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.textosalaalerttitle)
                .setMessage(R.string.textosalaalertdialog)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (rol.equals("master")) stopMasterService();
                        else stopClientService();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).show();
    }

    // Method to start the service
    public void startMasterService() {
        startService(new Intent(getBaseContext(), TCPServer.class));
        Toast.makeText(Sala.this, "Master service started", Toast.LENGTH_SHORT).show();
    }

    // Method to stop the service
    public void stopMasterService() {
        stopService(new Intent(getBaseContext(), TCPServer.class));
        Toast.makeText(Sala.this, "Master service stopped", Toast.LENGTH_SHORT).show();
    }

    public void startClientService() {
        startService(new Intent(getBaseContext(), TCPClient.class));
        Toast.makeText(Sala.this, "Client service started", Toast.LENGTH_SHORT).show();
    }

    public void stopClientService() {
        stopService(new Intent(getBaseContext(), TCPClient.class));
        Toast.makeText(Sala.this,"Client service stopped", Toast.LENGTH_SHORT).show();
    }

    public static String getNombre(){
        return nombre;
    }

    public static String getIP(){
        return ip;
    }
}
