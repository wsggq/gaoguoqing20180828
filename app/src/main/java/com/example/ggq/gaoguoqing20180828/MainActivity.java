package com.example.ggq.gaoguoqing20180828;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private ImageView iv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_login = findViewById(R.id.iv_login);
        iv_login.setOnClickListener(this);
    }

    /**
     * 属性平移动画
     *
     * @param view
     */
    public void translationAnimator(View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 20, 0);
        valueAnimator.setDuration(1000);
        valueAnimator.setTarget(iv_login);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float curValueFloat = (Float)animation.getAnimatedValue();
                int curValue = curValueFloat.intValue();
                iv_login.layout(curValue,curValue,curValue+iv_login.getWidth(),curValue+iv_login.getHeight());
            }
        });

        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();
    }

    /**
     * 组合动画
     *
     * @param view
     */
    public void groupAnimator(View view) {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        float curTranslationX = iv_login.getTranslationX();
        float curTranslationY = iv_login.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_login, "translationX", curTranslationX, width,curTranslationX);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv_login, "translationY", curTranslationY, height-500,curTranslationY);
        //透明
        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv_login, "alpha", 0, 1);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animator, animator1,alpha);
        animSet.setDuration(5000);
        animSet.start();

    }

    /**
     * 缩放
     *
     * @param view
     */
    public void zoom(View view) {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        float curTranslationX = iv_login.getTranslationX();
        float curTranslationY = iv_login.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_login, "scaleX", 1f, 3f, 1f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv_login, "scaleY", 1f, 3f, 1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv_login, "translationX", curTranslationX, width/2,curTranslationX);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(iv_login, "translationY", curTranslationY, height/2,curTranslationY);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animator, animator1,animator2,animator3);
        animSet.setDuration(5000);
        animSet.start();
    }

    /**
     * 旋转
     *
     * @param view
     */
    public void rotate(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_login, "rotation", 0f, 360f);
        animator.setDuration(5000);
        animator.start();
    }

    /**
     * 平移
     *
     * @param view
     */
    public void tran(View view) {
        float translationX1 = iv_login.getTranslationX();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(iv_login, "translationX", translationX1, 1000f, translationX1);
        translationX.setDuration(3000);
        translationX.start();
    }

    /**
     * 渐变
     *
     * @param view
     */
    public void gradual(View view) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv_login, "alpha", 1f, 0.1f, 0.3f, 0.5f, 1f);
        alpha.setDuration(3000);
        alpha.start();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, QQLoginActivity.class));
        finish();
    }
}
