package com.example.fion.memorygame2;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

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
    private boolean gameInProgress = false;

    @BindView(R.id.expandButton)
    ImageButton expandButton;

    @BindView(R.id.gridLayout)
    GridLayout gridLayout;

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
        ButterKnife.bind(this);

        // Creating the popup menu
        expandButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(GameActivity.this, expandButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.startOver:
                                resetGame();
                                return true;

                            case R.id.shuffleCards:
                                shuffleCurrentGameGraphics();
                                return true;

                            case R.id.showAll:
                                showAll();
                                return true;

                            case R.id.closeAll:
                                closeAll();
                                return true;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

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

        for (int i = 0; i < numberOfElements; i++) {
            int temp = buttonGraphicLocation[i];

            int swapIndex = random.nextInt(20);

            buttonGraphicLocation[i] = buttonGraphicLocation[swapIndex];

            buttonGraphicLocation[swapIndex] = temp;
        }
    }

    protected void shuffleCurrentGameGraphics() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(this);

            if (buttons[i].isMatched() && buttons[i].getVisibility() != View.INVISIBLE)
            {
                //set all the isMatched buttons to invisible
                buttons[i].setVisibility(View.INVISIBLE);

                // Swap button[i] with the next button that is not matched and not invisible
                for (int j = i + 1; j < buttons.length; j++) {
                    if (buttons[j].getVisibility() != View.INVISIBLE) {
                        float tempX = buttons[i].getX();
                        float tempY = buttons[i].getY();

                        buttons[i].setX(buttons[j].getX());
                        buttons[i].setY(buttons[j].getY());

                        buttons[j].setX(tempX);
                        buttons[j].setY(tempY);
                    }
                }
            }
        }
    }

    public void showAll() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].flip();
            buttons[i].setFlipped(true);
        }
    }

    public void closeAll() {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isFlipped()) {
                buttons[i].flip();
            }
        }
    }

    public void resetGame() {
        if (points != 0) {
            points = 0;
        }
        this.recreate();
    }

    @Override
    public void onClick(View view) {
        gameInProgress = true;

        if (isBusy) {
            return;
        }

        MemoryButton button = (MemoryButton) view;

        // Animation of the flipping cards
        ObjectAnimator animation = ObjectAnimator.ofFloat(button, "rotationY", 0.0f, 180f);
        animation.setDuration(1000);

        //animation.setRepeatCount();
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();

        // If the buttons are already matched, do nothing
        if (button.isMatched) {

            return;
        }

        // if this is the first button user clicked, flip it
        if (selectedButton1 == null) {
            selectedButton1 = button;

            selectedButton1.flip();
            return;
        }


        if (selectedButton1.getId() == button.getId()) {

            return;
        }

        // If both of the images are the same
        if ((selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()) || (selectedButton1.getFront() == button.getFront())) {
            button.flip();

            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false); // button cannot be clicked
            button.setEnabled(false);

            selectedButton1 = null;
            points++;

            checkPoints();
            TextView pointsText = (TextView) findViewById(R.id.pointsText);
            pointsText.setText("Points: " + points);

            return;

        } else {
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
            }, 1400);

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
        if (gameInProgress == true) {
            askUser();
        }
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

    // Override the backpress button to disable the activity from being destroyed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

        Log.i("Lifecycle", "Back Button Pressed");
    }

    // Check the current points in game
    public void checkPoints() {
        if (points == 10) {
            Toast.makeText(this, "You've won! ", Toast.LENGTH_LONG).show();
            askUser();
        }
    }

    // Ask user if they want to resume or start new game
    public void askUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked New Game button
                dialog.cancel();
                resetGame();
            }
        });
        builder.setNegativeButton("Resume", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Resume dialog
                dialog.cancel();
            }
        });

        builder.show();
    }
}