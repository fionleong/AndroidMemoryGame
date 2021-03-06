package com.example.fion.memorygame2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class MemoryButton extends Button implements Animation.AnimationListener {

    protected int row, col, frontDrawableId;
    protected boolean isFlipped = false;
    protected boolean isMatched = false;
    protected boolean isChecked = false;

    protected Drawable front;
    protected Drawable back;

    private Animation animation1;
    private Animation animation2;
    private boolean isBackOfCardShowing = true;

    public MemoryButton(Context context, int r, int c, int frontImageDrawableId) {
        super(context);
        row = r;
        col = c;
        frontDrawableId = frontImageDrawableId;

        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.qm2);

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


    public void setFrontDrawableId(int frontDrawableId) {
        this.frontDrawableId = frontDrawableId;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    public Drawable getFront() {
        return front;
    }

    public void setFront(Drawable front) {
        this.front = front;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public void flip() {

        if (isMatched)
            return;

        if (isFlipped) {
            setBackground(back);
            isFlipped = false;
        } else {
            setBackground(front);
            isFlipped = true;
        }
    }
    public void setChecked(boolean checked)
    {
        isChecked = checked;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public  void setFlipped(boolean flipped)
    {
        isFlipped = flipped;
    }

    public boolean isFlipped()
    {
        return isFlipped;
    }

    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (animation == animation1) {
            if (isBackOfCardShowing) {
                //selectedButton1.setBackground(front);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}
