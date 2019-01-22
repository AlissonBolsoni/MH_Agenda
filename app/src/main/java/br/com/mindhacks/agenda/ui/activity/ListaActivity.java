package br.com.mindhacks.agenda.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.mindhacks.agenda.R;
import br.com.mindhacks.agenda.objetos.Contato;

public class ListaActivity extends AppCompatActivity {

    private FloatingActionButton listaAdd;
    private ListView listaListView;
    private HashMap<Long, Contato> contatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.lista_contatos);

        contatos = new HashMap<Long, Contato>();

        listaAdd = (FloatingActionButton) findViewById(R.id.lista_fab);
        listaListView = (ListView) findViewById(R.id.lista_listview);

        listaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaActivity.this, FormularioActivity.class);
                startActivityForResult(intent, FormularioActivity.REQUEST_CODE_FORMULARIO);
            }
        });

        criaAdapter();

        listaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) contatos.values().toArray()[position];

                Intent intent = new Intent(ListaActivity.this, FormularioActivity.class);
                intent.putExtra(FormularioActivity.CONTATO, contato);
                startActivityForResult(intent, FormularioActivity.REQUEST_CODE_FORMULARIO);
            }
        });
    }

    private void criaAdapter() {
        ArrayAdapter adapter = new ArrayAdapter<Contato>(this, android.R.layout.simple_expandable_list_item_1, new ArrayList<Contato>(contatos.values()));
        listaListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == FormularioActivity.REQUEST_CODE_FORMULARIO && resultCode == Activity.RESULT_OK) {
                Contato contato = (Contato) data.getSerializableExtra(FormularioActivity.CONTATO);
                if (contato.getId() == 0)
                    contato.setId(contatos.values().size() + 1);

                contatos.put(contato.getId(), contato);
                criaAdapter();
            }
        }
    }
}
