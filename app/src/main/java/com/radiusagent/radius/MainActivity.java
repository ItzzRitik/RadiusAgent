package com.radiusagent.radius;

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ImageView icosplash;
    RelativeLayout splash;
    CoordinatorLayout mainpane;
    Point screenSize;
    double diagonal;
    Animator animator;
    TextView namesplash,title;
    RotateAnimation rotate;
    AppBarLayout appbar;
    OkHttpClient client;
    RecyclerView userView;
    List<Users> users;
    boolean loading=true,endSplash=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setLightTheme(true,true);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        diagonal=Math.sqrt((screenSize.x*screenSize.x) + (screenSize.y*screenSize.y));

        appbar=findViewById(R.id.appbar);
        appbar.setPadding(0,getHeightStatusNav(0)+dptopx(8),0,0);

        splash=findViewById(R.id.splash);
        client = new OkHttpClient();

        mainpane=findViewById(R.id.mainpane);
        splash=findViewById(R.id.splash);

        userView=findViewById(R.id.userView);
        userView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        userView.addItemDecoration(new GridSpacingItemDecoration(1,dptopx(10),true));
        userView.setItemAnimator(new DefaultItemAnimator());

        title=findViewById(R.id.title);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vdub.ttf"));
        splash();
    }
    public void splash(){
        Request request = new Request.Builder().url("https://raw.githubusercontent.com/iranjith4/radius-intern-mobile/master/users.json").get()
                .addHeader("Content-Type", "application/json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("failure", e.getMessage());
                call.cancel();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String mMessage = Objects.requireNonNull(response.body()).string();
                if (response.isSuccessful()){
                    try {
                        JSONArray postsArray = new JSONArray(new JSONObject(mMessage).getString("results"));
                        users = new ArrayList<>();
                        for (int i = 0; i < postsArray.length(); i++) {
                            JSONObject obj = postsArray.getJSONObject(i);
                            Users user=new Users();
                            JSONObject nobj = new JSONObject(obj.getString("name"));
                            user.setName(toTitleCase(nobj.getString("title"))+". "+
                                    toTitleCase(nobj.getString("first"))+" "+
                                    toTitleCase(nobj.getString("last")));
                            user.setEmail(obj.getString("email"));
                            user.setGender(obj.getString("gender").substring(0,1));
                            user.setAge(new JSONObject(obj.getString("dob")).getString("age"));
                            user.setDp(new JSONObject(obj.getString("picture")).getString("large"));
                            users.add(user);
                        }
                        new Handler(Looper.getMainLooper()).post(() -> {
                            loading=false;
                            userView.setAdapter(null);
                            userView.setAdapter(new UserAdapter(MainActivity.this,users));
                            if(endSplash)endSplash();
                        });
                    }
                    catch (JSONException e) {
                        Log.w("jsonDTA error", e.toString());
                    }
                }
            }
        });
        icosplash=findViewById(R.id.icosplash);
        namesplash=findViewById(R.id.namesplash);
        namesplash.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vdub.ttf"));
        namesplash.setLetterSpacing(0.8f);

        rotate = new RotateAnimation(0, 360*3, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new AccelerateInterpolator());rotate.setDuration(2000);
        new Handler().postDelayed(() -> {
            endSplash=true;
            rotate.setInterpolator(new LinearInterpolator());rotate.setDuration(1000);
            rotate.setRepeatCount(Animation.INFINITE);icosplash.startAnimation(rotate);
            if(!loading){
                endSplash();
            }
        },2000);
        icosplash.startAnimation(rotate);
    }
    public void endSplash(){
        int cx=screenSize.x/2;
        int cy=icosplash.getBottom()-(icosplash.getHeight()/2);
        animator = ViewAnimationUtils.createCircularReveal(mainpane,cx,cy,0,(float)diagonal);
        animator.setInterpolator(new AccelerateInterpolator());animator.setDuration(1000);
        mainpane.setVisibility(View.VISIBLE);splash.setElevation(1);mainpane.setElevation(2);animator.start();
        icosplash.animate().scaleX(30f).scaleY(30f).setDuration(1000).start();
        new Handler().postDelayed(() -> {
            splash.setVisibility(View.GONE);
        },800);
    }
    public String toTitleCase(String str){
        str=str.toLowerCase();
        return (str.substring(0,1).toUpperCase()).concat(str.substring(1,str.length()));
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;
        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;
            if (includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;
                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
    public int dptopx(float dp) { return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT)); }
    public int getHeightStatusNav(int viewid) {
        int result = 0;
        String view=(viewid==0)?"status_bar_height":"navigation_bar_height";
        int resourceId = getResources().getIdentifier(view, "dimen", "android");
        if (resourceId > 0) { result = getResources().getDimensionPixelSize(resourceId); }
        if(viewid==1){result = result* 5/8;}
        return result;
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
