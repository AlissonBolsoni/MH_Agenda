package br.com.mindhacks.agenda.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.mindhacks.agenda.R;
import br.com.mindhacks.agenda.objetos.Contato;

public class FormularioActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FORMULARIO = 485;
    public static final String CONTATO = "contato";
    private static final String CRIAR_CONTATO = "Criar Contato";

    private FloatingActionButton fabCamera;
    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextTelefone;
    private ImageView imageViewImagem;
    private Contato contato = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        setTitle(CRIAR_CONTATO);

        fabCamera = (FloatingActionButton) findViewById(R.id.formulario_fab_camera);
        imageViewImagem = (ImageView) findViewById(R.id.formulario_imagem);
        editTextNome = (EditText) findViewById(R.id.formulario_nome);
        editTextEmail = (EditText) findViewById(R.id.formulario_email);
        editTextTelefone = (EditText) findViewById(R.id.formulario_telefone);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO - Chamar a câmera
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(CONTATO)) {
            this.contato = (Contato) intent.getSerializableExtra(CONTATO);
            preencheFormulario();
        }
    }

    private void preencheFormulario() {
        editTextNome.setText(this.contato.getNome());
        editTextEmail.setText(this.contato.getEmail());
        editTextTelefone.setText(this.contato.getTelefone());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.formulario_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.formulario_menu_salvar) {
            salvarContato();
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvarContato() {
        if (contato == null) {
            contato = new Contato();
        }
        contato.setNome(editTextNome.getText().toString());
        contato.setEmail(editTextEmail.getText().toString());
        contato.setTelefone(editTextTelefone.getText().toString());

        retornaContato();
    }

    private void retornaContato() {
        Intent intent = new Intent();
        intent.putExtra(CONTATO, this.contato);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
