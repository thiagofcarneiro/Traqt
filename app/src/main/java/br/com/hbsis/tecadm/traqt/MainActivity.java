package br.com.hbsis.tecadm.traqt;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;
import java.util.TimerTask;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    // Armazena as repetições da sessão                                (1.1)
    private int currentRepetitions = 0;

    // Contagem de tempo                                               (2.1)
    private long startTime = 0;
    private Timer chronoTimer;
    private TimerTask chronoTimerTask;

    // Usado para atualizar a interface na thread principal            (2.1)
    final private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);                     (1.2)
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        //
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // fab.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View view) {
        // Snackbar.make(view, "Replace with your own action",
        ///        Snackbar.LENGTH_LONG)
        // .setAction("Action", null).show();
        // }
        // });

        // Cria um Label para o cronometro                             (2.2)
        final TextView elapsedTimeTextView = new TextView(this);
        elapsedTimeTextView.setText("00:00:00");

        // Cria um botão de repetições                                 (1.3)
        Button repetitionsButton = new Button(this);
        repetitionsButton.setText("Repetições: 0");
        repetitionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém o botão que disparou o click
                Button b = (Button) v;
                currentRepetitions++;
                b.setText("Repetições: " + currentRepetitions);
            }
        });

        // Cria um botão de Re-inicio da sessão
        Button resetButton = new Button(this);
        resetButton.setText("Re-iniciar");
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancela o Timer
                if (chronoTimer != null) {
                    chronoTimer.cancel();
                    chronoTimer = null;
                }
            }
        });

        // Cria um Layout base e adiciona o botão                      (1.4)
        LinearLayout baseLayout = new LinearLayout(this);
        baseLayout.addView(repetitionsButton);
        setContentView(baseLayout);

        baseLayout.addView(resetButton);
        baseLayout.addView(elapsedTimeTextView);

        /**
         * Retorna a representação em String de uma duração representada em nanosegundos.
         * @param nanosecs A duração em nano-segundos.
         * @return Retorna a duração formatada como "mm:ss" onde: m - minutos e s -
        segundos.
         */
        String formatDuration(long nanosecs) {

            // Extrai as unidades do tempo transcorrido
            long minutes = 0;
            long seconds = TimeUnit.NANOSECONDS.toSeconds(nanosecs);
            if (seconds > 60) {
                minutes = seconds / 60;
                seconds = seconds - (minutes * 60);
            }
            // Retorna a duração formatada
            return ((minutes >= 10) ? "" : "0") + minutes + ":" +
                    ((seconds >= 10) ? "" : "0") + seconds;
        }

        // Verifica se existe um timer o que indica uma sessão ativa    (2.3)
        if (chronoTimer == null) {
            // Configura os dados sessão
            currentRepetitions = 0;
            startTime = System.nanoTime();
            // Cria a Task que será executada pelo Timer e que atualiza a interface
            // com o tempo que transcorreu desde o início
            chronoTimerTask = new TimerTask() {
                @Override
                public void run() {
                    // A atualização do Timer ocorre em uma thread separada
                    // Precisamos usar o handler para fazer com que a atualização
                    // dos elementos de interface aconteça na thread principal.
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            // Calcula o tempo transcorrido e atualiza a interface
                            long elapsedTime = System.nanoTime() - startTime;
                            elapsedTimeTextView.setText(formatDuration(elapsedTime));

                        }
                    });
                }
            };
            // Configura o timer para executar a cada segundo atualizando a interface
            chronoTimer = new Timer();
            chronoTimer.schedule(chronoTimerTask, 0, 1000);
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
}
