package br.com.mindhacks.agenda.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.mindhacks.agenda.R;
import br.com.mindhacks.agenda.dao.ContatoDao;
import br.com.mindhacks.agenda.objetos.Contato;
import br.com.mindhacks.agenda.ui.adapter.ListaAdapter;
import br.com.mindhacks.agenda.utils.MascarasUtils;

public class ListaActivity extends AppCompatActivity {

    private ListView listaListView;
    private ArrayList<Contato> contatos;
    private static final int REQUEST_CODE_PERMISSAO = 534;
    private String[] permissoes = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

    private boolean validaPermissoes(String[] permissoes) {

        if (Build.VERSION.SDK_INT >= 23) {

            List<String> listaPermissoes = new ArrayList<String>();

            /*Percorre as permissões passadas, verificando uma a uma
             * se já tem a permissao liberada */
            for (String permissao : permissoes) {
                Boolean validaPermissao = ContextCompat.checkSelfPermission(this, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!validaPermissao) listaPermissoes.add(permissao);
            }

            /*Caso a lista esteja vazia, não é necessário solicitar permissão*/
            if (listaPermissoes.isEmpty()) return true;

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //Solicita permissão
            ActivityCompat.requestPermissions(ListaActivity.this, novasPermissoes, REQUEST_CODE_PERMISSAO);
            return false;

        }

        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.lista_contatos);

        validaPermissoes(permissoes);

        FloatingActionButton listaAdd = (FloatingActionButton) findViewById(R.id.lista_fab);
        listaListView = (ListView) findViewById(R.id.lista_listview);
        registerForContextMenu(listaListView);

        listaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaActivity.this, CadastraActivity.class);
                startActivityForResult(intent, CadastraActivity.REQUEST_CODE_FORMULARIO);
            }
        });

        criaAdapter();

        listaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) contatos.get(position);

                Intent intent = new Intent(ListaActivity.this, CadastraActivity.class);
                intent.putExtra(CadastraActivity.CONTATO, contato);
                startActivityForResult(intent, CadastraActivity.REQUEST_CODE_FORMULARIO);
            }
        });
    }

    private void criaAdapter() {
        ContatoDao dao = new ContatoDao(this);
        contatos = dao.pegaTodosContatos();
        dao.close();
        ListAdapter adapter = new ListaAdapter(contatos, this);
        listaListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CadastraActivity.REQUEST_CODE_FORMULARIO && resultCode == Activity.RESULT_OK) {
            criaAdapter();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Contato contato = (Contato) listaListView.getItemAtPosition(info.position);

        MenuItem sms = menu.add("Enviar SMS");
        sms.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                LayoutInflater inflater = LayoutInflater.from(ListaActivity.this);
                View view = inflater.inflate(R.layout.lista_sms_dialog, null);

                TextView telefone = (TextView) view.findViewById(R.id.lista_dialog_telefone);
                final TextView mensagem = (TextView) view.findViewById(R.id.lista_dialog_mensagem);

                //MascarasUtils.colocaMascara(telefone, "(NN) NNNNN-NNNN");
                String[] split = contato.getNome().split(" ");


                telefone.setText("Para: " + split[0] + " " + contato.getTelefone());
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaActivity.this);
                builder.setTitle(getString(R.string.app_name));
                builder.setView(view);
                builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enviaSMS(contato.getTelefone(), mensagem.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();


                return false;
            }
        });

        MenuItem del = menu.add("Apagar");
        del.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ContatoDao dao = new ContatoDao(ListaActivity.this);
                dao.remove(contato);
                dao.close();
                criaAdapter();

                return false;
            }
        });


    }

    private boolean enviaSMS(String telefone, String mensagem) {

        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultado : grantResults) {

            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }

        }

    }

    private void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app, é necessário aceitar as permissões");

        builder.setPositiveButton("Tentar Novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validaPermissoes(permissoes);
            }
        });
        builder.setNegativeButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
