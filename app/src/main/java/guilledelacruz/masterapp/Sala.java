package guilledelacruz.masterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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

        final TextView viewmaster = (TextView) findViewById(R.id.textosalamaster);
        final TextView viewanonimo = (TextView) findViewById(R.id.textosalaanonimo);

        Intent intent = getIntent();
        sala = intent.getStringExtra("sala");
        master = intent.getStringExtra("master");
        pass = intent.getStringExtra("pass");
        cap = Integer.parseInt(intent.getStringExtra("cap"));
        anon = intent.getBooleanExtra("anon", false);

        setTitle(jugadores.size() + "/" + cap + " - " + sala);
        viewmaster.setText("Master: " + master);

        if (anon)
            viewanonimo.setText(getResources().getString(R.string.textoprivacidad) + ": "
                    + getResources().getString(R.string.textoanonimo));
        else
            viewanonimo.setText(getResources().getString(R.string.textoprivacidad) + ": "
                    +  getResources().getString(R.string.textopublico));

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
