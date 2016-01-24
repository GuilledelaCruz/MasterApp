package guilledelacruz.masterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Sala extends AppCompatActivity {

    private String sala;
    private String pass;
    private String cap;
    private Boolean anon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        Intent intent = getIntent();
        sala = intent.getStringExtra("sala");
        pass = intent.getStringExtra("pass");
        cap = intent.getStringExtra("cap");
        anon = intent.getBooleanExtra("anon", false);

        setTitle("Sala: " + sala);
    }
}
