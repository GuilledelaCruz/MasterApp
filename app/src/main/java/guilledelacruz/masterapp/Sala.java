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

/**
 * Created by guilledelacruz
 */
public class Sala extends AppCompatActivity {

    private String sala = "";
    private String ip = "";
    private String pass = "";
    private String rol = "";
    private String nombre = "";
    private Integer cap = 0;
    private Boolean anon = false;
    private List<Player> jugadores = new ArrayList<>();
    public static Handler handler = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        createHandler();

        // get views
        final TextView viewvotado = (TextView) findViewById(R.id.textosalavotado);
        final Button botonvotacion = (Button) findViewById(R.id.botonsalavotacion);
        final ListView listjugadores = (ListView) findViewById(R.id.listsalajugadores);

        Intent intent = getIntent();
        rol = intent.getStringExtra("rol");

        if (rol.equals("master")) master(intent);

        // prepare list of players
        JugadorAdapter adapter = new JugadorAdapter(this, jugadores.toArray(new Player[jugadores.size()]));
        listjugadores.setAdapter(adapter);
        listjugadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView texto = (TextView) view.findViewById(R.id.adaptertext);
                TableRow row = (TableRow) findViewById(R.id.tablerowsalavotacion);
                if (row.getVisibility() == View.VISIBLE)
                    viewvotado.setText(texto.getText());
            }
        });

        botonvotacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    public void master(Intent intent){
        startMasterService();

        // get values sended in previous screen
        sala = intent.getStringExtra("sala");
        ip = intent.getStringExtra("ip");
        nombre = intent.getStringExtra("nombre");
        cap = Integer.parseInt(intent.getStringExtra("cap"));
        anon = intent.getBooleanExtra("anon", false);
        pass = intent.getStringExtra("pass");

        final TextView viewmaster = (TextView) findViewById(R.id.textosalamaster);
        final TextView viewanonimo = (TextView) findViewById(R.id.textosalaanonimo);
        final Button botonvotacion = (Button) findViewById(R.id.botonsalavotacion);
        viewmaster.setText(nombre);
        botonvotacion.setVisibility(View.VISIBLE);
        // put info on screen
        setTitle(jugadores.size() + "/" + cap + " - " + sala);
        if (anon) viewanonimo.setText(getResources().getString(R.string.textoanonimo));
        else viewanonimo.setText(getResources().getString(R.string.textopublico));
    }

    public void player(){

    }

    public void createHandler(){
        handler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        Object[] info = {sala, nombre, cap, anon};
                        break;
                    case 1:
                        List<Player> jug = (List<Player>) msg.obj;
                        jugadores = new ArrayList<>(jug);
                        final ListView listjugadores = (ListView) findViewById(R.id.listsalajugadores);
                        JugadorAdapter adapter = new JugadorAdapter(Sala.this, jugadores.toArray(new Player[jugadores.size()]));
                        listjugadores.setAdapter(adapter);
                        break;
                    case 2:
                        Message msgPass = new Message();
                        msgPass.what = 1;
                        if (pass.equals("")) msgPass.obj = null;
                        else msg.obj = pass;
                        TCPServer.handler.sendMessage(msgPass);
                        break;
                    case 10:
                        ip = (String) msg.obj;
                        final TextView textoip = (TextView) findViewById(R.id.textosalaip);
                        textoip.setText(ip);
                        break;
                    case 11:
                        nombre = (String) msg.obj;
                        final TextView textomaster = (TextView) findViewById(R.id.textosalamaster);
                        textomaster.setText(nombre);
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

    public void stopClientService() {
        stopService(new Intent(getBaseContext(), TCPClient.class));
        Toast.makeText(Sala.this,"Client service stopped", Toast.LENGTH_SHORT).show();
    }
}
