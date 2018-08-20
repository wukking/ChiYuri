package com.wuyson.chiyuri.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.wuyson.chiyuri.R;
import com.wuyson.common.base.BaseActivity;

public class ZoomAnimationActivity extends BaseActivity {
    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    private int mShortAnimationDuration;
    @Override
    protected int setContentViewId() {
        return R.layout.activity_zoom_animation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        final View thumb1View = findViewById(R.id.thumb_button_1);

        final View view2 = findViewById(R.id.thumb_button_2);

        SpringAnimation anim = new SpringAnimation(thumb1View, DynamicAnimation.TRANSLATION_Y, 0);
        //Setting the damping ratio to create a low bouncing effect.
        anim.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        //Setting the spring with a low stiffness.
        anim.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
        anim.setStartValue(-700);
        anim.start();

//        另一种设置force
//        SpringForce force = new SpringForce();
//        force.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
//        force.setStiffness(SpringForce.STIFFNESS_LOW);
//        anim.setSpring(force);

        // Setting up a spring animation to animate the view1 and view2 translationX and translationY properties
        final SpringAnimation anim1X = new SpringAnimation(thumb1View,
                DynamicAnimation.TRANSLATION_X);
        final SpringAnimation anim1Y = new SpringAnimation(thumb1View,
                DynamicAnimation.TRANSLATION_Y);
        final SpringAnimation anim2X = new SpringAnimation(view2,
                DynamicAnimation.TRANSLATION_X);
        final SpringAnimation anim2Y = new SpringAnimation(view2,
                DynamicAnimation.TRANSLATION_Y);
        VelocityTracker vt = VelocityTracker.obtain();
        vt.computeCurrentVelocity(1000);
        float velocity = vt.getYVelocity();
        anim.setStartVelocity(velocity);
        vt.recycle();

        float pixelPerSecond = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, velocity, getResources().getDisplayMetrics());

// Registering the update listener
        anim1X.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {

            // Overriding the method to notify view2 about the change in the view1’s property.
            @Override
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float value,
                                          float velocity) {
                anim2X.animateToFinalPosition(value);
            }
        });

        anim1Y.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {

            @Override
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float value,
                                          float velocity) {
                anim2Y.animateToFinalPosition(value);
            }
        });

//        anim1X.start();

        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb1View, R.drawable.icon_qwd_logo);
            }
        });
        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    /**
     * 1.取消动画
     * 2.设置展开图片Res
     * 3.小View.getGlobalVisibleRect() --startBound
     * 4.
     */
    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        Log.e("TAG", "zoomImageFromThumb: "+globalOffset );
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            Log.e("TAG", "width: "+deltaWidth );
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            Log.e("TAG", "startBoundsTOP1: "+startBounds.top );
            startBounds.top -= deltaHeight;
            Log.e("TAG", "startBoundsTOP2: "+startBounds.top );
            startBounds.bottom += deltaHeight;
            Log.e("TAG", "height: "+deltaHeight );
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            ActivityCompat.finishAfterTransition(this);
        }
    }
}
