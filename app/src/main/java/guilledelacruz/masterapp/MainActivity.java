package guilledelacruz.masterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by guilledelacruz
 */
public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get views
        final Button crearsala = (Button) findViewById(R.id.botoncrearsala);
        final Button buscarsala = (Button) findViewById(R.id.botonbuscarsala);

        // click event, start CreateRoom activity
        crearsala.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CrearSala.class);
                startActivity(intent);
            }
        });

        // click event, start SearchRoom activity
        buscarsala.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuscarSala.class);
                startActivity(intent);
            }
        });
    }
}
