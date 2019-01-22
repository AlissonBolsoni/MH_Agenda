package br.com.mindhacks.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.com.mindhacks.agenda.objetos.Contato;

public class ContatoDao extends SQLiteOpenHelper {


    public ContatoDao(Context context) {
        super(context, "mhAgenda", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Contatos ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "email TEXT, " +
                "imagem TEXT, " +
                "telefone TEXT, " +
                "excluido INT DEFAULT 0);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Contato> pegaTodosContatos(){
        String sql = "SELECT * FROM Contatos WHERE excluido = 0";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<Contato> contatos = new ArrayList<>();

        if (cursor != null){
            while (cursor.moveToNext()){
                Contato contato = new Contato();
                contato.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                contato.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                contato.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                contato.setImagem(cursor.getString(cursor.getColumnIndexOrThrow("imagem")));
                contato.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                contato.setExcluido(cursor.getInt(cursor.getColumnIndexOrThrow("excluido")));

                contatos.add(contato);
            }

            cursor.close();
        }
        return contatos;
    }

    public void insere(Contato contato){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = montaContato(contato);
        Long id = db.insert("Contatos", null, contentValues);
        contato.setId(id.intValue());
    }

    public void update(Contato contato){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "id = " + contato.getId();
        db.update("Contatos", montaContato(contato), sql, null);
    }

    private ContentValues montaContato(Contato contato) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", contato.getNome());
        contentValues.put("email", contato.getEmail());
        contentValues.put("imagem", contato.getImagem());
        contentValues.put("telefone", contato.getTelefone());
        contentValues.put("excluido", contato.getExcluido());

        return contentValues;
    }

    public void remove(Contato contato) {
        contato.exclui();
        update(contato);
    }
}
