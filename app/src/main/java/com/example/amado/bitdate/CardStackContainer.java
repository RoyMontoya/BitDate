package com.example.amado.bitdate;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

/**
 * Created by Amado on 03/06/2015.
 */
public class CardStackContainer extends RelativeLayout implements View.OnTouchListener {
    private static final String TAG = "CardStackContainer";
    private CardAdapter mAdapter;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPositionX;
    private float mPositionY;
    private float mOriginX;
    private float mOriginY;
    private GestureDetector mGestureDetector;
    private int mNextPosition;
    private CardView mFrontCard;
    private CardView mBackCard;


    public CardStackContainer(Context context) {
        this(context, null, 0);
    }

    public CardStackContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardStackContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(context, new FlingListener());
    }

    public void setAdapter(CardAdapter adapter) {

        mAdapter = adapter;
        DataSetObserver dataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                addFrontCard();
                addBackCard();
            }
        };
        mAdapter.registerDataSetObserver(dataSetObserver);
        addFrontCard();
        addBackCard();

    }

    private void addFrontCard() {
        if(mAdapter.getCount()>0 && mFrontCard == null){
            CardView cardView = mAdapter.getView(0, null, this);
            cardView.setCardElevation(8);
            cardView.setOnTouchListener(this);
            mFrontCard = cardView;
            addView(cardView);
            mNextPosition++;
        }
    }

    private void addBackCard() {
        if (mAdapter.getCount()> mNextPosition && mBackCard == null){
            CardView cardView = mAdapter.getView(mNextPosition, null, this);
            cardView.setTranslationY(30);
            mBackCard = cardView;
            addView(cardView);
            mNextPosition++;
        }
        bringChildToFront(mFrontCard);
    }

    public void swipeRight(){
        swipeCard(true);
    }


    public void swipeLeft(){
        swipeCard(false);
    }

    private void swipeCard(boolean swipeRight){
       if(swipeRight){
           mFrontCard.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_right));
       }else{
           mFrontCard.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_left));
       }
        removeView(mFrontCard);
        mFrontCard=null;
        if(mBackCard != null) {
            mBackCard.animate()
                    .translationY(0)
                    .setDuration(200);
            mBackCard.setOnTouchListener(this);
            mBackCard.setCardElevation(8);
            mFrontCard = mBackCard;
            mBackCard = null;
            addBackCard();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(mGestureDetector.onTouchEvent(event)){
           if(mPositionX < mOriginX){
               swipeCard(false);
           }else{
               swipeCard(true);
           }
            return true;
        }


        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();

                mPositionX = v.getX();
                mPositionY = v.getY();
                mOriginX = v.getX();
                mOriginY = v.getY();
                break;
            case MotionEvent.ACTION_UP:
                reset(v);
                break;
            case MotionEvent.ACTION_MOVE:
                float XMove = event.getX();
                float YMove = event.getY();

                float changeX = XMove - mLastTouchX;
                float changeY = YMove - mLastTouchY;

                mPositionX += changeX;
                mPositionY += changeY;

                v.setX(mPositionX);
                v.setY(mPositionY);
                break;
        }
        return true;
    }

    private class FlingListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

           return true;

        }
    }

    private void reset(View view){
        mPositionX = mOriginX;
        mPositionY = mOriginY;

        view.animate()
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .x(mOriginX)
                .y(mOriginY);
    }


}
