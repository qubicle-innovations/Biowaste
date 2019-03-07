package uems.biowaste;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import uems.biowaste.utils.Utils;
import uems.biowaste.vo.UserVo;


public class Splashscreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_act);
        initSplashLayout();
    }



    public void initSplashLayout() {
        new Thread() {
            public void run() {
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                UserVo user = Utils.getUser(context);
                if (Utils.getUser(context) != null && Utils.getUser(context).getUserID() != null) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            doLogin();
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startActivity(new Intent(Splashscreen.this,HomeActivity.class) );
                            finish();
                        }
                    });

                }
            }
        }.start();
    }


    public void doLogin(){
          startActivity(new Intent(Splashscreen.this,LoginActivity.class) );
         finish();
    }
}
