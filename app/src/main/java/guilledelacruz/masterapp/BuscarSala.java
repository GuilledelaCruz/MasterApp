package guilledelacruz.masterapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.util.regex.Pattern;

public class BuscarSala extends AppCompatActivity {

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
                String ip;

                if (!checkauto.isChecked()) {
                    ip = editip.getText().toString();
                    if(!validate(ip)){
                        editip.setTextColor(Color.RED);
                        return;
                    }
                }


            }
        });
    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
