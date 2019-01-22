package br.com.mindhacks.agenda.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import br.com.mindhacks.agenda.R;
import br.com.mindhacks.agenda.objetos.Contato;
import br.com.mindhacks.agenda.utils.ImageUtils;

public class ListaAdapter extends BaseAdapter {

    private ArrayList<Contato> contatos;
    private Context context;

    public ListaAdapter(ArrayList<Contato> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.contatos.size();
    }

    @Override
    public Contato getItem(int position) {
        return this.contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.lista_contato_item, parent, false);

        Contato contato = getItem(position);

        TextView nome = (TextView) convertView.findViewById(R.id.lista_item_nome);
        TextView email = (TextView) convertView.findViewById(R.id.lista_item_email);
        ImageView imagem = (ImageView) convertView.findViewById(R.id.lista_item_imagem);

        nome.setText(contato.getNome());
        email.setText(contato.getEmail());
        ImageUtils.setImage(imagem, contato.getImagem(), 60, 60);

        return convertView;
    }
}
