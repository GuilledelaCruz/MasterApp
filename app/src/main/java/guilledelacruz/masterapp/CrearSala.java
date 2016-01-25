package guilledelacruz.masterapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class CrearSala extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_sala);

        //get views
        final EditText editnombre = (EditText) findViewById(R.id.editsala);
        final EditText editmaster = (EditText) findViewById(R.id.editmaster);
        final EditText editpass = (EditText) findViewById(R.id.editpass);
        final EditText editcap = (EditText) findViewById(R.id.editcapacidad);
        final CheckBox checkan = (CheckBox) findViewById(R.id.checkanonimo);
        final Button botoncrear = (Button) findViewById(R.id.botoncrear);

        // click event
        botoncrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get values from views
                String sala = editnombre.getText().toString();
                String master = editmaster.getText().toString();
                String pass = editpass.getText().toString();
                String cap = editcap.getText().toString();
                Boolean anon = checkan.isChecked();

                // try if text of capacity is a number, if not, exception launched and exit
                try {
                    Integer capacidad = Integer.parseInt(cap);
                }catch (Exception e){
                    editcap.setTextColor(Color.RED);
                    return;
                }

                // send values to room and start activity
                Intent intent = new Intent(CrearSala.this, Sala.class);
                intent.putExtra("sala", sala);
                intent.putExtra("master", master);
                intent.putExtra("pass", pass);
                intent.putExtra("cap", cap);
                intent.putExtra("anon", anon);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
