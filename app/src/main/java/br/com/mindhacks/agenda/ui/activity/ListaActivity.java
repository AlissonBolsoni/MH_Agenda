package br.com.mindhacks.agenda.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.mindhacks.agenda.R;
import br.com.mindhacks.agenda.objetos.Contato;

public class ListaActivity extends AppCompatActivity {

    private FloatingActionButton listaAdd;
    private ListView listaListView;
    private List<Contato> contatoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.lista_contatos);

        contatoes = new ArrayList<Contato>();

        listaAdd = (FloatingActionButton) findViewById(R.id.lista_fab);
        listaListView = (ListView) findViewById(R.id.lista_listview);

        listaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaActivity.this, FormularioActivity.class);
                startActivityForResult(intent, FormularioActivity.REQUEST_CODE_FORMULARIO);
            }
        });

        ArrayAdapter adapter = new ArrayAdapter<Contato>(this, android.R.layout.simple_expandable_list_item_1, contatoes);
        listaListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            if (requestCode == FormularioActivity.REQUEST_CODE_FORMULARIO && resultCode == Activity.RESULT_OK){
                contatoes.add((Contato) data.getSerializableExtra(FormularioActivity.CONTATO));
            }
        }


    }
}
