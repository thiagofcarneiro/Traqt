package br.com.hbsis.tecadm.traqt.model;

/**
 * Created by Lab01 on 01/03/2016.
 */
public enum  SessionState {
    /**
     * Uma sessão recém criada que ainda não foi iniciada.
     */
    New,
    /**
     * Sessão esta em andamento.
     */
    Running,
    /**
     * Sessão esta em pausa.
     */
    Paused,
    /**
     * Sessão foi concluída conforme os parâmetros que o usuário estipulou.
     */
    Completed,
    /**
     * O usuário cancelou a sessão antes de conclui-lá conforme seus parâmetros.
     */
    Cancelled
}
