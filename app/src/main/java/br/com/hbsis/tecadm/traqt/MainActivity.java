package br.com.hbsis.tecadm.traqt;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.hbsis.tecadm.traqt.model.SessionParameters;
import br.com.hbsis.tecadm.traqt.model.SessionState;
import br.com.hbsis.tecadm.traqt.model.SessionTrack;
import br.com.hbsis.tecadm.traqt.model.TraqtDbHelper;
import br.com.hbsis.tecadm.traqt.model.entities.TraqtActivity;
import br.com.hbsis.tecadm.traqt.utils.Duration;
import br.com.hbsis.tecadm.traqt.R;

public class MainActivity
        extends AppCompatActivity
        implements SessionParameters {

    //
    // -- CAMPOS

    TextView elapsedTimeTextView;
    EditText repetitionsLimitEditText;
    EditText timeLimitEditText;
    CheckBox vibrationCheckBox;

    // Gerencia a sessão atual
    SessionTrack sessionTrack;

    //
    // -- OUTROS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Cria um campo para configurar o limite de repetições
        repetitionsLimitEditText = new EditText(this);
        repetitionsLimitEditText.setHint("Quantidade máxima de repetições");
        repetitionsLimitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Cria um campo para configurar o limite de tempo
        timeLimitEditText = new EditText(this);
        timeLimitEditText.setHint("Tempo máximo (em segundos)");
        timeLimitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Cria um Label para o cronometro
        elapsedTimeTextView = new TextView(this);
        elapsedTimeTextView.setText("00:00:00");

        // Cria um botão de repetições
        Button repetitionsButton = new Button(this);
        repetitionsButton.setText("Repetições: 0");
        repetitionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se deve iniciar uma sessão nova
                if (sessionTrack.getSessionState() == SessionState.New) {
                    sessionTrack.startSession();
                }

                // Computa a repetição
                sessionTrack.addRepetition();

                // Atualiza o contador de repetições
                Button b = (Button)v;
                b.setText("Repetições: " + sessionTrack.getCurrentRepetitions());
            }
        });

        // Cria um botão de Re-inicio da sessão
        Button resetButton = new Button(this);
        resetButton.setText("Re-iniciar");
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionTrack.cancelSession();
                configSession();
            }
        });

        // Cria um checkbox para determinar se deve ou não vibrar nas repetições
        vibrationCheckBox = new CheckBox(this);
        vibrationCheckBox.setText("Habilitar vibrações");

        LinearLayout baseLayout = new LinearLayout(this);
        baseLayout.setOrientation(LinearLayout.VERTICAL);
        baseLayout.addView(repetitionsLimitEditText);
        baseLayout.addView(timeLimitEditText);
        baseLayout.addView(repetitionsButton);
        baseLayout.addView(resetButton);
        baseLayout.addView(elapsedTimeTextView);
        baseLayout.addView(vibrationCheckBox);

        setContentView(baseLayout);

        // Cria uma sessão inicial
        configSession();

        /// -- Testes do Banco de Dados
        // 1. Criar um objeto do tipo DB Helper
        TraqtDbHelper dbHelper = new TraqtDbHelper(this);
        // 2. Inserir alguns registros na tabela de atividades
        ContentValues values = new ContentValues();
        values.put(TraqtActivity.COLUMN_NAME, "Abdominais");
        values.put(TraqtActivity.COLUMN_REPETITIONS, 10);
        dbHelper.getWritableDatabase().insert(TraqtActivity.TABLE_NAME, null, values);
        values.put(TraqtActivity.COLUMN_NAME, "Caminhada");
        values.put(TraqtActivity.COLUMN_TIME_LIMIT, 300);
        values.put(TraqtActivity.COLUMN_REPETITIONS, 0);
        dbHelper.getWritableDatabase().insert(TraqtActivity.TABLE_NAME, null, values);
        // 3. Consultar os registros inseridos
        Cursor cursor = dbHelper.getReadableDatabase().query(TraqtActivity.TABLE_NAME,
                new String[] {
                        TraqtActivity.COLUMN_ID,
                        TraqtActivity.COLUMN_NAME,
                        TraqtActivity.COLUMN_REPETITIONS,
                        TraqtActivity.COLUMN_TIME_LIMIT
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            String log = "Atividade - ID: " + cursor.getInt(0) +
                    "\nNome: " + cursor.getString(1) +
                    "\nRepetições: " + cursor.getInt(2) +
                    "\nTempo Limite: " + new
                    Duration(cursor.getLong(3)).getFormattedDuration();
            Log.i("MainActivity", log);
        }
    }

    /**
     * Tenta extrair um inteiro de uma string.
     * @param text A string com o valor inteiro para ser extraído.
     * @return O número inteiro extraído ou nulo caso a conversão não seja possível.
     */
    public static Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Configura uma nova sessão do Traqt.
     */
    private void configSession() {
        sessionTrack = new SessionTrack(this, this);
        sessionTrack.setOnUpdateTimeListener(new SessionTrack.OnUpdateTimeListener() {
            @Override
            public void onUpdateTime(long elapsedTime, Long remainingTime) {
                elapsedTimeTextView.setText(new Duration(elapsedTime).getFormattedDuration());
            }
        });
        sessionTrack.setOnCompleteListener(new SessionTrack.OnCompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "Sessão concluída!", Toast.LENGTH_LONG).show();
                configSession();
            }
        });
    }

    /**
     * Tenta extrair um long de uma string.
     * @param text A string com o valor long para ser extraído.
     * @return O número long extraído ou nulo caso a conversão não seja possível.
     */
    public static Long tryParseLong(String text) {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //
    // -- IMPLEMENTAÇÃO DA INTERFACE: SessionParameters

    @Override
    public int getRepetitions() {
        // Obtém a configuração do limite de repetições a partir do EditText da tela.
        Integer maxRepetitions = tryParseInt(repetitionsLimitEditText.getText().toString());
        if (maxRepetitions != null)
            return maxRepetitions;

        return 0;
    }

    @Override
    public long getTimeLimit() {
        // Obtém a configuração do limite de tempo a partir do EditText da tela.
        Long duration = tryParseLong(timeLimitEditText.getText().toString());
        if (duration != null)
            return duration;

        return 0;
    }

    @Override
    public boolean isEnableVibration() {
        // Obtém a configuração de se deve habilitar a vibração a partir do Checkbox
        return vibrationCheckBox.isChecked();
    }

}
