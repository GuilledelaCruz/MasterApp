package guilledelacruz.masterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button crearsala = (Button) findViewById(R.id.botoncrearsala);
        final Button buscarsala = (Button) findViewById(R.id.botonbuscarsala);

        crearsala.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CrearSala.class);
                startActivity(intent);
            }
        });

        buscarsala.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuscarSala.class);
                startActivity(intent);
            }
        });
    }
}
