package guilledelacruz.masterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by guilledelacruz
 */
public class BuscarSala extends AppCompatActivity {

    private String ip = "";
    private String nombre = "";
    private String pass = "";
    public static Handler handler = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_sala);

        final EditText editjugador = (EditText) findViewById(R.id.editjugador);
        final EditText editip = (EditText) findViewById(R.id.editip);
        final CheckBox checkauto = (CheckBox) findViewById(R.id.checkbusqueda);
        final Button botonbusqueda = (Button) findViewById(R.id.botonbuscar);
        final ListView viewservidores = (ListView) findViewById(R.id.listaservidores);

        if(checkauto.isChecked()) editip.setEnabled(false);

        checkauto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editip.setEnabled(!isChecked);
            }
        });

        createHandler();

        botonbusqueda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nombre = editjugador.getText().toString();
                if (!checkauto.isChecked()) {
                    ip = editip.getText().toString();
                    if(!validate(ip)){
                        editip.setTextColor(Color.RED);
                        return;
                    }
                }

                startClientService();
            }
        });
    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public void createHandler(){
       handler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        Message msgNombre = new Message();
                        msgNombre.what = 0;
                        msgNombre.obj = nombre;
                        TCPClient.handler.sendMessage(msgNombre);
                        break;
                    case 1:
                        Message msgIP = new Message();
                        msgIP.what = 1;
                        msgIP.obj = ip;
                        TCPClient.handler.sendMessage(msgIP);
                        break;
                    case 10:
                        goToRoom(); break;
                    case 11:
                        passwordDialog();
                        break;
                    default:
                        Toast.makeText(BuscarSala.this, "Could not connect", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void goToRoom(){
        Intent intent = new Intent(BuscarSala.this, Sala.class);
        intent.putExtra("rol", "player");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void passwordDialog(){
        final EditText editpass = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(R.string.textopassword)
                .setView(editpass)
                .setPositiveButton(R.string.textoconectar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        pass = editpass.getText().toString();
                        Message msgPass = new Message();
                        msgPass.what = 2;
                        msgPass.obj = pass;
                        TCPClient.handler.sendMessage(msgPass);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stopClientService();
                    }
                }).show();
    }

    public void startClientService() {
        startService(new Intent(getBaseContext(), TCPClient.class));
        Toast.makeText(BuscarSala.this, "Client service started", Toast.LENGTH_SHORT).show();
    }

    public void stopClientService() {
        stopService(new Intent(getBaseContext(), TCPClient.class));
        Toast.makeText(BuscarSala.this,"Client service stopped", Toast.LENGTH_SHORT).show();
    }
}
