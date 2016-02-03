package guilledelacruz.masterapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
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

public class BuscarSala extends AppCompatActivity {

    private String ip = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_sala);

        final EditText editjugador = (EditText) findViewById(R.id.editjugador);
        final EditText editip = (EditText) findViewById(R.id.editip);
        final CheckBox checkauto = (CheckBox) findViewById(R.id.checkbusqueda);
        final Button botonbusqueda = (Button) findViewById(R.id.botonbuscar);
        final ListView viewservidores = (ListView) findViewById(R.id.listaservidores);

        if(checkauto.isChecked()){
            editip.setEnabled(false);
        }

        checkauto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editip.setEnabled(!isChecked);
            }
        });

        botonbusqueda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = editjugador.getText().toString();

                if (!checkauto.isChecked()) {
                    ip = editip.getText().toString();
                    if(!validate(ip)){
                        editip.setTextColor(Color.RED);
                        return;
                    }
                }

                //startClientService();

                Intent intent = new Intent(BuscarSala.this, Sala.class);
                intent.putExtra("rol", "player");
                intent.putExtra("nombre", name);
                intent.putExtra("ip", ip);
                intent.putExtra("pass", "");

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public void startClientService() {
        startService(new Intent(getBaseContext(), TCPClient.class));
        Toast.makeText(BuscarSala.this, "Client service started", Toast.LENGTH_SHORT).show();
        Message mstToTCPClient = new Message();
        mstToTCPClient.what = 0;
        mstToTCPClient.obj = ip;
        //TCPServer.handler.sendMessage(mstToTCPClient);
    }

    public void stopClientService() {
        stopService(new Intent(getBaseContext(), TCPClient.class));
        Toast.makeText(BuscarSala.this,"Client service stopped", Toast.LENGTH_SHORT).show();
    }
}
