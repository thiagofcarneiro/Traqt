package br.com.hbsis.tecadm.traqt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //   setContentView(R.layout.activity_main);
   //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
   //   setSupportActionBar(toolbar);
   //   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
   //   fab.setOnClickListener(new View.OnClickListener() {
   //       @Override
   //       public void onClick(View view) {
   //           Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
   //                   .setAction("Action", null).show();
   //       }
   //   });

        // Cria um botão de repetições
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
        // Cria um Layout base e adiciona o botão
        LinearLayout baseLayout = new LinearLayout(this);
        baseLayout.addView(repetitionsButton);
        setContentView(baseLayout);
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
