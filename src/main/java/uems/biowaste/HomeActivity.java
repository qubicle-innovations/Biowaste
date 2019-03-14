package uems.biowaste;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import uems.biowaste.fragments.BioWasteListFragment;
import uems.biowaste.fragments.BiowasteCreateFragment;
import uems.biowaste.fragments.BiowasteDetailsFragment;
import uems.biowaste.fragments.DashboardFragment;
import uems.biowaste.fragments.DetailsFragment;
import uems.biowaste.fragments.GWasteListFragment;
import uems.biowaste.fragments.GwasteCreateFragments;
import uems.biowaste.fragments.GwasteDetailsFragment;
import uems.biowaste.fragments.ItemFragment;
import uems.biowaste.fragments.LoginFragment;
import uems.biowaste.fragments.PatientCreateFragment;
import uems.biowaste.fragments.PatientDetailsFragment;
import uems.biowaste.fragments.PatientListFragment;
import uems.biowaste.fragments.RecycledCreateFragment;
import uems.biowaste.fragments.RecycledDetailsFragment;
import uems.biowaste.fragments.RecycledListFragment;
import uems.biowaste.fragments.dummy.DummyContent;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.CustomTypefaceSpan;
import uems.biowaste.utils.FragmentManger;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class HomeActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener,
        DashboardFragment.OnFragmentInteractionListener,LoginFragment.OnFragmentInteractionListener,GwasteCreateFragments.OnFragmentInteractionListener,
        GWasteListFragment.OnFragmentInteractionListener,GwasteDetailsFragment.OnFragmentInteractionListener,PatientListFragment.OnFragmentInteractionListener,
        PatientDetailsFragment.OnFragmentInteractionListener, PatientCreateFragment.OnFragmentInteractionListener,RecycledListFragment.OnFragmentInteractionListener,
        RecycledDetailsFragment.OnFragmentInteractionListener, RecycledCreateFragment.OnFragmentInteractionListener,BiowasteDetailsFragment.OnFragmentInteractionListener,
        BioWasteListFragment.OnFragmentInteractionListener, BiowasteCreateFragment.OnFragmentInteractionListener
        {

    private Long backButtonPressedTime = -1L ;

    private DrawerLayout mDrawerLayout;
    private ImageView toolbarMenuImageView;
    private ImageView toolbarBackImageView;
    private ImageView toolbarUETrackImageView;
    public UserVo me;

    Toolbar toolbar ;
    TextView tvUsername;
    TextView tvEmpID ;
    TextView toolbarTextView ;

    FragmentManger fragmentManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        me = Utils.getUser(getApplicationContext());
        init();
        fragmentManger = new FragmentManger(getApplicationContext(), this, toolbarMenuImageView, toolbarBackImageView,
                 toolbar,  toolbarTextView,  toolbarUETrackImageView);
        if (me != null && me.getUserID() != null) {
            startFragment(new DashboardFragment(),Constants.FRAGMENT_DASHBOARD,false,true);
        }else{
            startFragment(new LoginFragment(),Constants.FRAGMENT_LOGIN,false,true);
        }

    }

    @Override
    public void startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd){
        fragmentManger.startFragment(fragment,fragmentName,addToBackStack,isAdd);
    }

    private void init() {
        initNavigationMenu();
        initToolBar(null);
    }

    @Override
    public void onBackPressed() {
        boolean isBackButtonPressDelayed = false;
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (!fragmentList.isEmpty() ) {
            String fragmentName = fragmentList.get(fragmentList.size()-1).getTag();
            if (fragmentName != null) {
                if (fragmentName.equals(Constants.FRAGMENT_DASHBOARD)
                        || fragmentName.equals(Constants.FRAGMENT_BIOWASTE)
                        || fragmentName.equals(Constants.FRAGMENT_RECYCLED_ITEMS)
                        || fragmentName.equals(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE)
                        || fragmentName.equals(Constants.FRAGMENT_MONTHLY_PATIENTS)) {
                    Long currentTime = Calendar.getInstance().getTimeInMillis();
                    if(backButtonPressedTime == -1 || backButtonPressedTime < currentTime - 3000){
                        backButtonPressedTime = currentTime;
                        isBackButtonPressDelayed = true;
                        Toast.makeText(getApplicationContext(), getText(R.string.back_button_delay_message), Toast.LENGTH_SHORT).show();
                    }else{
                       finish();
                    }
                }
            }
        }
        if(!isBackButtonPressDelayed)
            super.onBackPressed();
        fragmentManger.onBackPressedAfter();
    }

    public void initToolBar(String title) {

        toolbarMenuImageView = findViewById(R.id.toolbarMenuImageView);
        toolbarBackImageView = findViewById(R.id.toolbarBackImageView);
        toolbarUETrackImageView = findViewById(R.id.toolbarUETrackImageView);
        toolbarMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
        toolbarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar =  findViewById(R.id.toolbar2);
        toolbarTextView =  findViewById(R.id.toolbarTextView);
        toolbarTextView.setTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if(title == null){
            toolbarTextView.setVisibility(View.GONE);
            toolbarUETrackImageView.setVisibility(View.VISIBLE);
        }else{
            toolbarTextView.setVisibility(View.VISIBLE);
            toolbarUETrackImageView.setVisibility(View.GONE);
            setTitle(title);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

    }

    public void initNavigationMenu() {

        mDrawerLayout =  findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        tvUsername =  findViewById(R.id.navigationNameTextView);
        tvEmpID =  findViewById(R.id.navigationIdTextView);
        if(me != null && me.getUserName() != null)
            tvUsername.setText(Utils.toTitleCase(me.getUserName()));
        tvEmpID.setText("");

        findViewById(R.id.navigationLogoutFrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
                Utils.setUser(getApplicationContext(),null);
                startFragment(new LoginFragment(),Constants.FRAGMENT_LOGIN,false,false);
            }
        });
        findViewById(R.id.navigationGeneralWasteFrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
                startFragment(new GWasteListFragment(),Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE,true,true);
            }
        });
        findViewById(R.id.navigationBioWasteDisposalFrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ic_view) {
                showMenu();
                startFragment(new BioWasteListFragment(),Constants.FRAGMENT_BIOWASTE,true,true);
            }
        });
        findViewById(R.id.navigationRecycleItemsFrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ic_view) {
                showMenu();
                startFragment(new RecycledListFragment(),Constants.FRAGMENT_RECYCLED_ITEMS,true,true);
            }
        });
        findViewById(R.id.navigationMonthlyPatientFrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
                startFragment(new PatientListFragment(),Constants.FRAGMENT_MONTHLY_PATIENTS,true,true);
            }
        });
        findViewById(R.id.navigationDashboardFrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ic_view) {
                showMenu();
                startFragment(new DashboardFragment(),Constants.FRAGMENT_DASHBOARD,false,true);
            }
        });

    }

    public void showMenu() {
        if(mDrawerLayout!=null)
            if (!mDrawerLayout.isDrawerOpen(Gravity.START)) {
                mDrawerLayout.openDrawer(Gravity.START);
            } else {
                mDrawerLayout.closeDrawer(Gravity.START);
            }
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font =  ResourcesCompat.getFont(this, R.font.montserrat_semi_bold);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    public void loginResponse(TResponse<String> result) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_LOGIN);
        if(fragment != null && fragment.isVisible()){
            ((LoginFragment)fragment).loginResponse(result);
        }
    }

    public void saveResponse(TResponse<String> result) {
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE);
            if(fragment != null && fragment.isVisible()){
                ((GwasteCreateFragments)fragment).saveResponse(result);
            }
            fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_MONTHLY_PATIENTS_CREATE);
            if(fragment != null && fragment.isVisible()){
                ((PatientCreateFragment)fragment).saveResponse(result);
            }
            fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_RECYCLED_ITEMS_CREATE);
            if(fragment != null && fragment.isVisible()){
                ((RecycledCreateFragment)fragment).saveResponse(result);
            }
            fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_BIOWASTE_CREATE);
            if(fragment != null && fragment.isVisible()){
                ((BiowasteCreateFragment)fragment).saveResponse(result);
            }
        }catch (ClassCastException ex){
            ex.printStackTrace();
        }
    }

    public void listResponse(TResponse<String> result) {
        try{
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_MONTHLY_PATIENTS);
            if(fragment != null && fragment.isVisible() && fragment.isResumed()){
                ((PatientListFragment)fragment).listResponse(result);
            }
            fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
            if(fragment != null && fragment.isVisible() && fragment.isResumed()){
                ((GWasteListFragment)fragment).listResponse(result);
            }
            fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_BIOWASTE);
            if(fragment != null && fragment.isVisible() && fragment.isResumed()){
                ((BioWasteListFragment)fragment).listResponse(result);
            }
            fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_RECYCLED_ITEMS);
            if(fragment != null && fragment.isVisible() && fragment.isResumed()){
                ((RecycledListFragment)fragment).listResponse(result);
            }
        }catch (ClassCastException ex){
            ex.printStackTrace();
        }
    }

    public void detailsResponse(TResponse<String> result) {
        try{
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_BIOWASTE_DETAILS);
            if(fragment != null && fragment.isVisible()){
                ((BiowasteDetailsFragment)fragment).detailsResponse(result);
            }
            fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
            if(fragment != null && fragment.isVisible()){
                ((GwasteDetailsFragment)fragment).detailsResponse(result);
            }
            fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
            if(fragment != null && fragment.isVisible()){
                ((RecycledDetailsFragment)fragment).detailsResponse(result);
            }
        }catch (ClassCastException ex){
            ex.printStackTrace();
        }
    }

}
