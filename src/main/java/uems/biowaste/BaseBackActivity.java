package uems.biowaste;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import uems.biowaste.utils.Utils;
import uems.biowaste.vo.UserVo;

/**
 * Created by aswin on 20/03/17.
 */


public class BaseBackActivity extends AppCompatActivity {

    public Context context;
    public UserVo me;
    public String lan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        me = Utils.getUser(context);

         //  checkPermissions();

    }

    public void setToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvTitle = findViewById(R.id.toolbarTitle);
        tvTitle.setText(title);
        tvTitle.setTextColor(ActivityCompat.getColor(context, R.color.white));
      //  tvTitle.setTypeface(ZTypeface.robotoMedium(context));
        setSupportActionBar(toolbar);
        //  tvTitle.setTypeface(ZTypeface.robotoRegular(context));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        findViewById(R.id.imBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.imBack).setVisibility(View.VISIBLE);
        toolbar.setContentInsetsAbsolute(0, 0);


    }
  /*  public void checkPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {


                //   Toast.makeText(GoValetApplication.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //  Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,App may not work properly\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("setting")
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
*/
    public void dismissKeyboard(EditText myEditText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
    }

    public void showError(String msg, View view) {

        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    // Snackbar closed on its own
                    //showSystemUI();

                }
            }

            @Override
            public void onShown(Snackbar snackbar) {

                //showSystemUI();
            }
        });


        snackbar.show();


    }



}
