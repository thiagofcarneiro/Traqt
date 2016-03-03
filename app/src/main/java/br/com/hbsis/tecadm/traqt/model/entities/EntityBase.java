package br.com.hbsis.tecadm.traqt.model.entities;

import android.content.ContentValues;

/**
 * Created by Lab01 on 02/03/2016.
 */
public abstract class EntityBase {
    //
    // -- CAMPOS
    protected int id;
    //
    // -- MÉTODOS DE ACESSO DAS PROPRIEDADES
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    //
    // -- MÉTODOS ABSTRATOS
    /**
     * Obtém uma instância do objeto ContentValues com os valores da entidade, para
     ser usado em operações com o banco de dados.
     * @return Um objeto do tipo ContentValues contendo os valores dessa instância.
     */
    public abstract ContentValues getContentValues();
    //
    // -- CONSTANTES
    public static final String COLUMN_ID = "id";

}
