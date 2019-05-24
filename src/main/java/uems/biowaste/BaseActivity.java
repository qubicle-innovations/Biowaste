package uems.biowaste;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import uems.biowaste.utils.Constants;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.UserVo;

/**
 * Created by aswin on 20/03/17.
 */


public class BaseActivity extends AppCompatActivity {

    public Context context;
    public UserVo me;
    public String lan;
    public ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        me = Utils.getUser(context);
        String url = Utils.getSharedPreference(context,"url");
        if(url!=null){
            Constants.SERVER_URL = url;
        }
         //  checkPermissions();
    }

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

    public void initNavigationMenu(String title) {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvEmpID = (TextView) findViewById(R.id.tvEmpID);
        TextView tvDrawerTitle = (TextView) findViewById(R.id.tvDrawerTitle);
    //    TextView toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        tvDrawerTitle.setTextColor(Color.WHITE);
        SpannableStringBuilder sb = new SpannableStringBuilder("UETrack™ - Biowaste");
        StyleSpan iss = new StyleSpan(Typeface.BOLD_ITALIC); //Span to make text italic
        StyleSpan bss = new StyleSpan(Typeface.BOLD); //Span to make text italic
        sb.setSpan(iss, 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
        sb.setSpan(bss, 3, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
        tvDrawerTitle.setText(sb);
        setSupportActionBar(toolbar);
      if(title==null){
          setTitle(sb);
      }else{
          setTitle(title);
      }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //  getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //  toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();

            }
        });
        //    getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size);

        findViewById(R.id.llLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
                logout();
                Intent intent =  new Intent(BaseActivity.this,LoginActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                Utils.setUser(context,null);
                finishAffinity();
            }
        });
       findViewById(R.id.llGWaste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
                startActivity(new Intent(context, GWasteListActivity.class));
                finish();
            }
        });
        findViewById(R.id.llBioWaste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ic_view) {
                showMenu();
                startActivity(new Intent(context, BioWasteListActivity.class));
                finish();
            }
        });
        findViewById(R.id.llRecycled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ic_view) {
                showMenu();
                startActivity(new Intent(context, RecycledListActivity.class));
                finish();

            }
        });
        findViewById(R.id.llPatients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
                startActivity(new Intent(context, PatientListActivity.class));
                finish();

            }
        });
        findViewById(R.id.llDashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ic_view) {
                showMenu();
                startActivity(new Intent(context, Dashboard.class));
                finish();
            }
        });

        tvUsername.setText(Utils.toTitleCase(me.getUserName()));
        tvEmpID.setText("");

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(mDrawerToggle!=null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mDrawerToggle!=null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void showMenu() {
        if(mDrawerLayout!=null)
            if (!mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.openDrawer(Gravity.START);
        } else {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    public void logout() {
        Utils.setUser(context, null);
        startActivity(new Intent(context, LoginActivity.class));
        finishAffinity();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK twice to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2500);
    }
}
