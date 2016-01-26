package guilledelacruz.masterapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Sala extends AppCompatActivity {

    private String sala = "";
    private String master = "";
    private String pass = "";
    private Integer cap = 0;
    private Boolean anon = false;
    private List<String> jugadores = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        jugadores.add("Jugador1");
        jugadores.add("Jugador2");
        jugadores.add("Jugador3");
        jugadores.add("Jugador4");
        jugadores.add("Jugador1");
        jugadores.add("Jugador2");
        jugadores.add("Jugador3");
        jugadores.add("Jugador4");

        // get views
        final TextView viewmaster = (TextView) findViewById(R.id.textosalamaster);
        final TextView viewanonimo = (TextView) findViewById(R.id.textosalaanonimo);
        final Button botonvotacion = (Button) findViewById(R.id.botonsalavotacion);
        final ListView listjugadores = (ListView) findViewById(R.id.listsalajugadores);

        // get values sended in previous screen
        Intent intent = getIntent();
        sala = intent.getStringExtra("sala");
        master = intent.getStringExtra("master");
        pass = intent.getStringExtra("pass");
        cap = Integer.parseInt(intent.getStringExtra("cap"));
        anon = intent.getBooleanExtra("anon", false);

        // put info on screen
        setTitle(jugadores.size() + "/" + cap + " - " + sala);
        viewmaster.setText(getResources().getString(R.string.textomaster) +": " + master);

        if (anon)
            viewanonimo.setText(getResources().getString(R.string.textoprivacidad) + ": "
                    + getResources().getString(R.string.textoanonimo));
        else
            viewanonimo.setText(getResources().getString(R.string.textoprivacidad) + ": "
                    + getResources().getString(R.string.textopublico));

        JugadorAdapter adapter = new JugadorAdapter(this, jugadores.toArray(new String[jugadores.size()]));
        listjugadores.setAdapter(adapter);

        listjugadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getCount(); i++) {
                    View v = parent.getChildAt(i);
                    TextView t = (TextView) v.findViewById(R.id.adaptertext);
                    if (t.getCurrentTextColor() == Color.RED && i != position) {
                        t.setTextColor(Color.DKGRAY);
                    }
                }

                View v = parent.getChildAt(position);
                TextView t = (TextView) v.findViewById(R.id.adaptertext);
                t.setTextColor(Color.RED);
            }
        });

        botonvotacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
            default: return false;
        }
    }
}
