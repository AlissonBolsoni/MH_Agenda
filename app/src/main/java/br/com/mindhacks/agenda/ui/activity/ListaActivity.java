package br.com.mindhacks.agenda.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.mindhacks.agenda.R;
import br.com.mindhacks.agenda.dao.ContatoDao;
import br.com.mindhacks.agenda.objetos.Contato;
import br.com.mindhacks.agenda.ui.adapter.ListaAdapter;

public class ListaActivity extends AppCompatActivity {

    private ListView listaListView;
    private ArrayList<Contato> contatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.lista_contatos);

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
}
