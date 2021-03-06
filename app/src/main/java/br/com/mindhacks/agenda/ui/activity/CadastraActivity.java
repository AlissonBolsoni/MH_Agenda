package br.com.mindhacks.agenda.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Pattern;

import br.com.mindhacks.agenda.BuildConfig;
import br.com.mindhacks.agenda.R;
import br.com.mindhacks.agenda.dao.ContatoDao;
import br.com.mindhacks.agenda.objetos.Contato;
import br.com.mindhacks.agenda.utils.ImageUtils;
import br.com.mindhacks.agenda.utils.MascarasUtils;

public class CadastraActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FORMULARIO = 485;
    private static final int CAMERA_RESULT = 637;
    private static final String CONTATO_SALVO = "ContatoSalvo";
    private static final String REGEX_EMAIL = "^(\\w)*@((\\w)+)(\\.)(\\w{3})(\\.)?(\\w{2})?";
    public static final String CONTATO = "contato";


    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextTelefone;
    private ImageView imageViewImagem;
    private Contato contato = new Contato();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        setTitle(R.string.criar_contato);

        FloatingActionButton fabCamera = (FloatingActionButton) findViewById(R.id.formulario_fab_camera);
        imageViewImagem = (ImageView) findViewById(R.id.formulario_imagem);
        editTextNome = (EditText) findViewById(R.id.formulario_nome);
        editTextEmail = (EditText) findViewById(R.id.formulario_email);
        editTextTelefone = (EditText) findViewById(R.id.formulario_telefone);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaCamera();

            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(CONTATO)) {
            this.contato = (Contato) intent.getSerializableExtra(CONTATO);
            preencheFormulario();
        }

        MascarasUtils.colocaMascara(editTextTelefone, "(NN) NNNNN-NNNN");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.contato = (Contato) savedInstanceState.getSerializable(CONTATO_SALVO);
        preencheFormulario();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CONTATO_SALVO, contato);

        super.onSaveInstanceState(outState);
    }


    private void chamaCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String caminhoImagem = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
        this.contato.setImagem(caminhoImagem);
        File arquivoFoto = new File(caminhoImagem);

        intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".provider", arquivoFoto
                )
        );

        startActivityForResult(intent, CAMERA_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_RESULT && resultCode == Activity.RESULT_OK) {
            ImageUtils.setImage(imageViewImagem, this.contato.getImagem());
        }
    }

    private void preencheFormulario() {
        editTextNome.setText(this.contato.getNome());
        editTextEmail.setText(this.contato.getEmail());
        editTextTelefone.setText(this.contato.getTelefone());
        ImageUtils.setImage(imageViewImagem, this.contato.getImagem());
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
        contato.setNome(editTextNome.getText().toString());
        contato.setEmail(editTextEmail.getText().toString());
        contato.setTelefone(editTextTelefone.getText().toString());

        if (contato.getEmail().matches(REGEX_EMAIL))
            retornaContato();
        else
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
    }

    private void retornaContato() {

        try {
            ContatoDao dao = new ContatoDao(this);
            if (contato.getId() == 0)
                dao.insere(contato);
            else
                dao.update(contato);

            dao.close();
            setResult(Activity.RESULT_OK);
        } catch (Exception e) {
            setResult(Activity.RESULT_CANCELED);
        }

        finish();
    }
}
