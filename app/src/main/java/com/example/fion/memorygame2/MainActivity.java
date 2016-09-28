package com.example.fion.memorygame2;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.app.DialogFragment.STYLE_NORMAL;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private Button rulesButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button)findViewById(R.id.playButton);
        rulesButton = (Button)findViewById(R.id.ruleButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                RuleFragment rf = new RuleFragment();
                rf.setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
                rf.show(fm, "Rules Fragment");
            }
        });

    }
}
