package br.com.mindhacks.agenda.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.mindhacks.agenda.R;

public class FormularioActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FORMULARIO = 485;
    public static final String CONTATO = "contato";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
    }
}
