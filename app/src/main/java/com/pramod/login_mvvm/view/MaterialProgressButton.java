package com.pramod.login_mvvm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.material.button.MaterialButton;
import com.pramod.login_mvvm.R;

public class MaterialProgressButton extends MaterialButton {
    private final long TEXT_FADE_ANIMATION_DURATION = 200;
    private String buttonText;
    private CircularProgressDrawable circularProgressDrawable;

    public MaterialProgressButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MaterialProgressButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        buttonText = getText().toString();
        circularProgressDrawable = (CircularProgressDrawable) new CircularProgressDrawable(context).mutate();
        circularProgressDrawable.setColorSchemeColors(getColorByAttrId(R.attr.colorPrimary));
        circularProgressDrawable.setStrokeCap(Paint.Cap.ROUND);
        circularProgressDrawable.setStyle(CircularProgressDrawable.DEFAULT);
        int size = (int) (circularProgressDrawable.getCenterRadius() + circularProgressDrawable.getStrokeWidth()) * 2;
        circularProgressDrawable.setBounds(0, 0, size, size);
    }


    public void showProgress(Boolean show) {
        if (show == null) {
            return;
        }
        if (show) {
            SpannableString progressSpanString = new SpannableString(" ");
            DynamicDrawableSpan drawableSpan = new DynamicDrawableSpan() {
                @Override
                public Drawable getDrawable() {
                    return circularProgressDrawable;
                }
            };
            circularProgressDrawable.setCallback(progressCallback);
            circularProgressDrawable.start();
            progressSpanString.setSpan(drawableSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(progressSpanString);
        } else {
            circularProgressDrawable.stop();
            circularProgressDrawable.setCallback(null);
            SpannableString buttonTextSpan = new SpannableString(buttonText);
            setText(buttonTextSpan);
        }
        setEnabled(!show);
    }


    private void setTextUsingFadeAnimation(SpannableString spannableString) {
        //text fading animation
        //this is to retain color state
        ColorStateList colorStateList = getTextColors();

        int textColor = getTextColors().getColorForState(getDrawableState(), ContextCompat.getColor(getContext(), android.R.color.white));
        ObjectAnimator textFadeOut = ObjectAnimator.ofArgb(this, "textColor", textColor, ColorUtils.setAlphaComponent(textColor, 0))
                .setDuration(TEXT_FADE_ANIMATION_DURATION);
        ObjectAnimator textFadeIn = ObjectAnimator.ofArgb(this, "textColor", ColorUtils.setAlphaComponent(textColor, 0), textColor)
                .setDuration(TEXT_FADE_ANIMATION_DURATION);

        textFadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setText(spannableString);
                textFadeIn.start();
            }
        });
        textFadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //setting the color state
                setTextColor(colorStateList);
            }
        });
        textFadeOut.start();
    }

    @Override
    public void setEnabled(boolean enabled) {
        setEnabled(enabled, false);
    }

    public void setEnabled(boolean enabled, boolean animate) {
        if (animate) {
            //background fading animation
            ColorStateList backgroundColorList = getBackgroundTintList();
            int enabledBackgroundColor = getBackgroundTintList().getColorForState(ENABLED_STATE_SET, getColorByAttrId(R.attr.colorPrimary));
            int disabledBackgroundColor = ColorUtils.setAlphaComponent(getColorByAttrId(R.attr.colorOnSurface), (int) (0.18f * 255f));
            ObjectAnimator backgroundEnabledToDisabledFade = ObjectAnimator.ofArgb(this, "backgroundColor", enabledBackgroundColor, disabledBackgroundColor).setDuration(50);
            ObjectAnimator backgroundDisabledToEnabledFade = ObjectAnimator.ofArgb(this, "backgroundColor", disabledBackgroundColor, enabledBackgroundColor).setDuration(50);
            if (enabled) {
                backgroundDisabledToEnabledFade.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setBackgroundTintList(backgroundColorList);
                    }
                });
                backgroundDisabledToEnabledFade.start();
            } else {
                backgroundEnabledToDisabledFade.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setBackgroundTintList(backgroundColorList);
                    }
                });
                backgroundEnabledToDisabledFade.start();
            }
        }
        super.setEnabled(enabled);
    }


    /**
     * This callback is use to call material button onDraw() method by calling invalidate() of MaterialButton
     * inside invalidateDrawable() method of progress drawable
     */
    private Drawable.Callback progressCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(@NonNull Drawable who) {
            MaterialProgressButton.this.invalidate();
        }

        @Override
        public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {

        }

        @Override
        public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {

        }
    };

    private int getColorByAttrId(int attrId) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

}
