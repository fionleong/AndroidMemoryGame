package com.example.fion.memorygame2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.Button;
import android.widget.GridLayout;


public class MemoryButton extends Button{

    protected int row, col, frontDrawableId;
    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    public MemoryButton(Context context, int r, int c, int frontImageDrawableId)
    {
        super(context);
        row = r;
        col = c;
        frontDrawableId = frontImageDrawableId;

        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.qm);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
        tempParams.width = (int) getResources().getDisplayMetrics().density * 75;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 75;
        tempParams.bottomMargin = 20;
        tempParams.topMargin = 20;
        tempParams.leftMargin = 15;
        tempParams.rightMargin = 15;



        setLayoutParams(tempParams);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    public void flip()
    {
        if (isMatched)
            return;

        if (isFlipped)
        {
            setBackground(back);
            isFlipped = false;
        }
        else
        {
            setBackground(front);
            isFlipped = true;
        }
    }

}
