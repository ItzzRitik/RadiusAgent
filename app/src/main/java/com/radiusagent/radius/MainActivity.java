package com.radiusagent.radius;

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView icosplash;
    RelativeLayout splash;
    Point screenSize;
    double diagonal;
    Animator animator;
    TextView namesplash;
    RotateAnimation rotate;
    boolean loading=false,splashEND=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setLightTheme(true,true);

        splash=findViewById(R.id.splash);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        diagonal=Math.sqrt((screenSize.x*screenSize.x) + (screenSize.y*screenSize.y));
    }
    public void splash(){
        splash.setBackgroundColor(0);
        icosplash=findViewById(R.id.icosplash);
        namesplash=findViewById(R.id.namesplash);
        namesplash.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vdub.ttf"));
        namesplash.setLetterSpacing(0.8f);

        rotate = new RotateAnimation(0, 360*5, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new AccelerateInterpolator());rotate.setDuration(3000);
        new Handler().postDelayed(() -> {
            if(loading){
                rotate.setInterpolator(new LinearInterpolator());rotate.setDuration(1000);
                rotate.setRepeatCount(Animation.INFINITE);icosplash.startAnimation(rotate);
            }
            else{
                if(!splashEND)endSplash();
            }
        },3000);
        icosplash.startAnimation(rotate);
    }
    public void endSplash(){
        splashEND=true;
        int cx=screenSize.x/2;
        int cy=icosplash.getBottom()-(icosplash.getHeight()/2);
        animator = ViewAnimationUtils.createCircularReveal(web,cx,cy,0,(float)diagonal);
        animator.setInterpolator(new AccelerateInterpolator());animator.setDuration(1000);
        web.setVisibility(View.VISIBLE);splash.setElevation(1);web.setElevation(2);animator.start();
        icosplash.animate().scaleX(20f).scaleY(20f).setDuration(1000).start();
        new Handler().postDelayed(() -> {
            setLightTheme(false,true);
        },600);
    }
    public void setLightTheme(boolean status,boolean nav){
        int flags = getWindow().getDecorView().getSystemUiVisibility();
        if(status && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        if(nav && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        if(!status && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        if(!nav && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }
}
