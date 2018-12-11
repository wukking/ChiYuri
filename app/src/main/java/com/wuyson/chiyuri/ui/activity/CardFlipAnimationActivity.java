package com.wuyson.chiyuri.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wuyson.chiyuri.R;
import com.wuyson.common.base.BaseActivity;

/**
 * 双面卡片效果
 */
public class CardFlipAnimationActivity extends BaseActivity {
    private boolean mShowingBack;
    private CardBackFragment mCardBackFragment;
    private Button btnAnimate;
    private TextView tvAnimate;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_card_flip_animation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        btnAnimate = findViewById(R.id.btn_animate);
        tvAnimate = findViewById(R.id.tv_animate);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        }

    }

    public void flipCard(View view){
        flipCard();
    }
    public void revealView(View view){
        revealView();
    }
    public void hideView(View view){
        hideView();
    }

    public void pathIt(View view){
        // arcTo() and PathInterpolator only available on API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Path path = new Path();
            path.arcTo(0f, 0f, 800f, 800f, 0f, 270f, true);
            ObjectAnimator animator = ObjectAnimator.ofFloat(tvAnimate,View.X,View.Y, path);
            animator.setDuration(2000);
            animator.start();
        }
    }
    private void revealView(){
        // previously invisible view
        View myView = findViewById(R.id.my_view);

        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            myView.setVisibility(View.VISIBLE);
        }
    }


    private void hideView() {
        // previously visible view
        final View myView = findViewById(R.id.my_view);

        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the initial radius for the clipping circle
            float initialRadius = (float) Math.hypot(cx, cy);

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();
        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            myView.setVisibility(View.VISIBLE);
        }
    }

    public static class CardFrontFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }


    public static class CardBackFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            mShowingBack = false;
            return;
        }

        // Flip to the back.
        mShowingBack = true;


        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.
        if (mCardBackFragment == null){
            mCardBackFragment = new CardBackFragment();
        }
        getFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources
                // representing rotations when switching to the back of the card, as
                // well as animator resources representing rotations when flipping
                // back to the front (e.g. when the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a
                // fragment representing the next page (indicated by the
                // just-incremented currentPage variable).
                .replace(R.id.container, mCardBackFragment)

                // Add this transaction to the back stack, allowing users to press
                // Back to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();
    }
}
