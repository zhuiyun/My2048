package ru.game.arnis.a2048.GameCore;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import ru.game.arnis.a2048.GameElements.Cell;

/**
 * Created by arnis on 26.07.2016.
 */
public class Anim {
    public static void popupAnimation(View v,boolean reverse){
        float animValue;
        if (!reverse)
            animValue=1.4f;
        else animValue=0.7f;
        PropertyValuesHolder pvhXs = PropertyValuesHolder.ofFloat(View.SCALE_X,animValue);
        PropertyValuesHolder pvhYs = PropertyValuesHolder.ofFloat(View.SCALE_Y,animValue);
        PropertyValuesHolder pvhXe = PropertyValuesHolder.ofFloat(View.SCALE_X,1f);
        PropertyValuesHolder pvhYe = PropertyValuesHolder.ofFloat(View.SCALE_Y,1f);
        ObjectAnimator animatorStart = ObjectAnimator.ofPropertyValuesHolder(v,pvhXs,pvhYs);
        ObjectAnimator animatorEnd = ObjectAnimator.ofPropertyValuesHolder(v,pvhXe,pvhYe);
        animatorStart.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorStart.setDuration(150);
        animatorEnd.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorEnd.setDuration(150);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animatorStart,animatorEnd);
        set.start();
    }

    public static void migration(final Cell cell, Coordinates pos){
        float animValue;
        animValue=1.2f;
        PropertyValuesHolder pvhXcoo = PropertyValuesHolder.ofFloat(View.TRANSLATION_X,pos.x);
        PropertyValuesHolder pvhYcoo = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,pos.y);
        PropertyValuesHolder pvhXs = PropertyValuesHolder.ofFloat(View.SCALE_X,animValue);
        PropertyValuesHolder pvhYs = PropertyValuesHolder.ofFloat(View.SCALE_Y,animValue);
        PropertyValuesHolder pvhXe = PropertyValuesHolder.ofFloat(View.SCALE_X,1f);
        PropertyValuesHolder pvhYe = PropertyValuesHolder.ofFloat(View.SCALE_Y,1f);
        ObjectAnimator animatorTrans = ObjectAnimator.ofPropertyValuesHolder(cell.getLook(),pvhXcoo,pvhYcoo);
        ObjectAnimator animatorStart = ObjectAnimator.ofPropertyValuesHolder(cell.getLook(),pvhXs,pvhYs);
        ObjectAnimator animatorEnd = ObjectAnimator.ofPropertyValuesHolder(cell.getLook(),pvhXe,pvhYe);
        animatorTrans.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorTrans.setDuration(150);
        animatorTrans.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cell.updateView();
//                mergePop(cell.getLook());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorStart.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorStart.setDuration(50);
        animatorEnd.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorEnd.setDuration(50);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animatorTrans,animatorStart,animatorEnd);
        set.start();


    }

    public static void dimAnim(final View dim){
        dim.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.argb(0,0,0,0),Color.argb(180,0,0,0));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dim.setBackgroundColor((Integer)animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    public static void undimAnim(final View dim){
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),Color.argb(180,0,0,0), Color.argb(0,0,0,0));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dim.setBackgroundColor((Integer)animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dim.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();

    }

}
