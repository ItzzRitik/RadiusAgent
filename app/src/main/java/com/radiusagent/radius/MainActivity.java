package com.radiusagent.radius;

import android.animation.Animator;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
