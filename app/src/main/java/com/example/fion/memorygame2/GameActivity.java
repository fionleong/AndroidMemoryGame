package com.example.fion.memorygame2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;

    private MemoryButton[] buttons;

    private int[] buttonGraphicLocation;
    private int[] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private boolean isBusy = false;

    private int points = 0;

    private static final String POINTS_STATE = "points_state";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Lifecycle", "onCreate()");

        // Check if there is a savedInstance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            points = savedInstanceState.getInt(POINTS_STATE);
        }

        setContentView(R.layout.activity_game);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout4x4);

        int numCol = gridLayout.getColumnCount();
        int numRow = gridLayout.getRowCount();

        numberOfElements = numCol * numRow;

        buttons = new MemoryButton[numberOfElements];

        buttonGraphics = new int[numberOfElements / 2];

        buttonGraphics[0] = R.drawable.p1;
        buttonGraphics[1] = R.drawable.p2;
        buttonGraphics[2] = R.drawable.p3;
        buttonGraphics[3] = R.drawable.p4;
        buttonGraphics[4] = R.drawable.p5;
        buttonGraphics[5] = R.drawable.p6;
        buttonGraphics[6] = R.drawable.p7;
        buttonGraphics[7] = R.drawable.p8;
        buttonGraphics[8] = R.drawable.p9;
        buttonGraphics[9] = R.drawable.p10;

        buttonGraphicLocation = new int[numberOfElements];

        shuffleButtonGraphics();

        for (int r = 0; r < numRow; r++) {
            for (int c = 0; c < numCol; c++) {
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonGraphicLocation[r * numCol + c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r * numCol + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }

    }

    protected void shuffleButtonGraphics() {

        Random random = new Random();

        for (int i = 0; i < numberOfElements; i++) {
            buttonGraphicLocation[i] = i % (numberOfElements / 2);
        }

        for (int i = 0; i < numberOfElements; i++)
        {
            int temp = buttonGraphicLocation[i];

            int swapIndex = random.nextInt(20);

            buttonGraphicLocation[i] = buttonGraphicLocation[swapIndex];

            buttonGraphicLocation[swapIndex] = temp;
        }
    }

    @Override
    public void onClick(View view) {

        if (isBusy) {
            return;
        }

        MemoryButton button = (MemoryButton) view;

        if (button.isMatched) {
            return;
        }

        if (selectedButton1 == null) {
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }

        if (selectedButton1.getId() == button.getId()) {

            return;
        }

        if (selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()) {
            button.flip();

            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false); // disable the button
            button.setEnabled(false);

            selectedButton1 = null;
            points++;

            checkPoints();
            TextView pointsText = (TextView) findViewById(R.id.pointsText);
            pointsText.setText("Points: " + points);


            return;
        }
        else
        {
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1 = null;
                    selectedButton2 = null;
                    isBusy = false;
                }
            }, 500);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lifecycle", "onStop()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Lifecycle", "onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Lifecycle", "onPause()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(POINTS_STATE, points);

        Log.i("Lifecycle", "Saved Instance State");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        points = savedInstanceState.getInt(POINTS_STATE);
        Log.i("Lifecycle", "Restore Instance State");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

        Log.i("Lifecycle", "Back Button Pressed");
    }

    public void checkPoints() {
        if (points == 10) {
            Toast.makeText(this, "You've won! ", Toast.LENGTH_LONG).show();
        }
    }
}

