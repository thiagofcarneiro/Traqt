package br.com.hbsis.tecadm.traqt.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.hbsis.tecadm.traqt.model.helpers.SQLiteHelpers;
import br.com.hbsis.tecadm.traqt.model.entities.TraqtActivity;
import br.com.hbsis.tecadm.traqt.model.entities.TraqtSession;

/**
 * Implementação do SQLiteOpenHelper para abrir o banco de dados do modelo do Traqt.
 */
public class TraqtDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "traqt.sqlite";
    public static final int VERSION = 1;
    public TraqtDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
// Cria as tabelas das entidades
        db.execSQL(SQLiteHelpers.buildCreateStatement(TraqtActivity.getTableDefinition()));
        db.execSQL(SQLiteHelpers.buildCreateStatement(TraqtSession.getTableDefinition()));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Deve ser implementado quando o Schema do banco de dados é modificado
    }
}