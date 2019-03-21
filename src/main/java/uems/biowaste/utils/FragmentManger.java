package uems.biowaste.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uems.biowaste.HomeActivity;
import uems.biowaste.R;
import uems.biowaste.fragments.BioWasteListFragment;
import uems.biowaste.fragments.DashboardFragment;
import uems.biowaste.fragments.GWasteListFragment;
import uems.biowaste.fragments.PatientListFragment;
import uems.biowaste.fragments.RecycledListFragment;

public class FragmentManger {

    private Context context;
    private Activity activity;
    private ImageView toolbarMenuImageView;
    private ImageView toolbarBackImageView;
    private Toolbar toolbar;
    private TextView toolbarTextView;
    private ImageView toolbarUETrackImageView;
    private TabLayout tabLayout;

    private boolean isTabSelectProgrammatically = false;
    private boolean isTabClicked = false;
    private boolean isFirstClick = true;

    public FragmentManger(Context context, Activity activity, ImageView toolbarMenuImageView, ImageView toolbarBackImageView,
                          Toolbar toolbar, TextView toolbarTextView, ImageView toolbarUETrackImageView, TabLayout tabLayout){
        this.context = context;
        this.tabLayout = tabLayout;
        this.activity = activity;
        this.toolbarMenuImageView = toolbarMenuImageView;
        this.toolbarBackImageView = toolbarBackImageView;
        this.toolbar = toolbar;
        this.toolbarTextView = toolbarTextView;
        this.toolbarUETrackImageView = toolbarUETrackImageView;
        initTabBar();
    }

    private void initTabBar(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(!isTabSelectProgrammatically){
                    isTabClicked = true;
                    switch (tab.getPosition()) {
                        case 0:
                            startFragment(new DashboardFragment(),Constants.FRAGMENT_DASHBOARD,false,false);
                            break;
                        case 1:
                            startFragment(new GWasteListFragment(),Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE,false,false);
                            break;
                        case 2:
                            startFragment(new BioWasteListFragment(),Constants.FRAGMENT_BIOWASTE,false,false);
                            break;
                        case 3:
                            startFragment(new RecycledListFragment(),Constants.FRAGMENT_RECYCLED_ITEMS,false,false);
                            break;
                        case 4:
                            startFragment(new PatientListFragment(),Constants.FRAGMENT_MONTHLY_PATIENTS,false,false);
                            break;
                    }
                }else{
                    isTabSelectProgrammatically = false;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void popFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd){

        FragmentManager fragmentManger = ((HomeActivity)activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        fragmentManger.popBackStackImmediate();
        switch (fragmentName) {
            case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE:
                fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                fragmentManger.popBackStackImmediate();
                switchToolBar(true, true, context.getText(R.string.food_and_general_waste).toString());
                tabLayout.setVisibility(View.VISIBLE);
                ((GWasteListFragment)fragment).reload(context);
                break;
            case Constants.FRAGMENT_BIOWASTE:
                fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE);
                fragmentManger.popBackStackImmediate();
                switchToolBar(true, true, context.getText(R.string.biowaste).toString());
                tabLayout.setVisibility(View.VISIBLE);
                ((BioWasteListFragment)fragment).reload(context);
                break;
            case Constants.FRAGMENT_RECYCLED_ITEMS:
                fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS);
                fragmentManger.popBackStackImmediate();
                switchToolBar(true, true, context.getText(R.string.recycled_items).toString());
                tabLayout.setVisibility(View.VISIBLE);
                ((RecycledListFragment)fragment).reload(context);
                break;
            case Constants.FRAGMENT_MONTHLY_PATIENTS:
                fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_MONTHLY_PATIENTS);
                fragmentManger.popBackStackImmediate();
                switchToolBar(true, true, context.getText(R.string.monthly_payments).toString());
                tabLayout.setVisibility(View.VISIBLE);
                ((PatientListFragment)fragment).reload(context);
                break;
        }

        fragmentTransaction.commit();

    }

    public void startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd){

        FragmentTransaction fragmentTransaction = ((HomeActivity)activity).getSupportFragmentManager().beginTransaction();
        switch (fragmentName) {
            case Constants.FRAGMENT_LOGIN:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_LOGIN);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_LOGIN);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_LOGIN);
                switchToolBar(false, false, null);
                tabLayout.setVisibility(View.GONE);
                break;
            case Constants.FRAGMENT_DASHBOARD:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_DASHBOARD);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_DASHBOARD);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_DASHBOARD);
                switchToolBar(true, true, null);
                tabLayout.setVisibility(View.VISIBLE);
                selectTab(0);
                break;
            case Constants.FRAGMENT_BIOWASTE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_BIOWASTE);
                switchToolBar(true, true, context.getText(R.string.biowaste).toString());
                tabLayout.setVisibility(View.VISIBLE);
                selectTab(2);
                break;
            case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container,fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                else
                    fragmentTransaction.replace(R.id.container,fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                switchToolBar(true, true, context.getText(R.string.food_and_general_waste).toString());
                tabLayout.setVisibility(View.VISIBLE);
                selectTab(1);
                break;

            case Constants.FRAGMENT_MONTHLY_PATIENTS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_MONTHLY_PATIENTS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_MONTHLY_PATIENTS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_MONTHLY_PATIENTS);
                switchToolBar(true, true, context.getText(R.string.monthly_payments).toString());
                tabLayout.setVisibility(View.VISIBLE);
                selectTab(4);
                break;
            case Constants.FRAGMENT_RECYCLED_ITEMS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_RECYCLED_ITEMS);
                switchToolBar(true, true, context.getText(R.string.recycled_items).toString());
                tabLayout.setVisibility(View.VISIBLE);
                selectTab(3);
                break;
            case Constants.FRAGMENT_BIOWASTE_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_BIOWASTE_DETAILS);
                switchToolBar(true, false, context.getText(R.string.biowaste_details).toString());
                tabLayout.setVisibility(View.GONE);
                break;
            case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                switchToolBar(true, false, context.getText(R.string.food_and_general_waste_details).toString());
                tabLayout.setVisibility(View.GONE);
                break;
            case Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                tabLayout.setVisibility(View.GONE);
                switchToolBar(true, false, context.getText(R.string.recycled_items_details).toString());
                break;

            case Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                switchToolBar(true, false, context.getText(R.string.monthly_payments_details).toString());
                tabLayout.setVisibility(View.GONE);
                break;
            case Constants.FRAGMENT_BIOWASTE_CREATE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE_CREATE);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE_CREATE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_BIOWASTE_CREATE);
                switchToolBar(true, false, context.getText(R.string.biowaste_create).toString());
                tabLayout.setVisibility(View.GONE);
                break;
            case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container,fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE);
                else
                    fragmentTransaction.replace(R.id.container,fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE);
                switchToolBar(true, false, context.getText(R.string.food_and_general_waste_create).toString());
                tabLayout.setVisibility(View.GONE);
                break;
            case Constants.FRAGMENT_MONTHLY_PATIENTS_CREATE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_MONTHLY_PATIENTS_CREATE);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_MONTHLY_PATIENTS_CREATE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_MONTHLY_PATIENTS_CREATE);
                switchToolBar(true, false, context.getText(R.string.monthly_payments_create).toString());
                tabLayout.setVisibility(View.GONE);
                break;
            case Constants.FRAGMENT_RECYCLED_ITEMS_CREATE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS_CREATE);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS_CREATE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_RECYCLED_ITEMS_CREATE);
                switchToolBar(true, false, context.getText(R.string.recycled_items_create).toString());
                tabLayout.setVisibility(View.GONE);
                break;
        }

        fragmentTransaction.commit();

    }


    private void switchToolBar(boolean visibility,boolean homeButton,String title){
        if(visibility){
            toolbar.setVisibility(View.VISIBLE);
            if(homeButton){
                toolbarMenuImageView.setVisibility(View.VISIBLE);
                toolbarBackImageView.setVisibility(View.GONE);
            }else{
                toolbarMenuImageView.setVisibility(View.GONE);
                toolbarBackImageView.setVisibility(View.VISIBLE);
            }

            if(title == null){
                toolbarTextView.setVisibility(View.GONE);
                toolbarUETrackImageView.setVisibility(View.VISIBLE);
            }else{
                toolbarTextView.setVisibility(View.VISIBLE);
                toolbarUETrackImageView.setVisibility(View.GONE);
                toolbarTextView.setText(title);
            }
        }else{
            toolbar.setVisibility(View.GONE);
        }
    }

    private void selectTab(int tabIndex){
        if(isFirstClick)
        {
            isFirstClick = false;
            return;
        }
        if(isTabClicked){
            isTabClicked = false;
            return;
        }

        if (tabIndex >= 0 && tabLayout != null ) {
            TabLayout.Tab tab =  tabLayout.getTabAt(tabIndex);
            if(tab != null) {
                isTabSelectProgrammatically = true;
                tab.select();
            }
        }
    }



    public void onBackPressedAfter() {
        List<Fragment> fragmentList = ((HomeActivity)activity).getSupportFragmentManager().getFragments();
        if (!fragmentList.isEmpty() ) {
            String fragmentName = fragmentList.get(fragmentList.size()-1).getTag();
            if (fragmentName != null) {
                switch (fragmentName) {
                    case Constants.FRAGMENT_DASHBOARD:
                        switchToolBar(true, true,null);
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case Constants.FRAGMENT_MONTHLY_PATIENTS:
                        switchToolBar(true, true, context.getText(R.string.monthly_patients_forms).toString());
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case Constants.FRAGMENT_RECYCLED_ITEMS:
                        switchToolBar(true, true, context.getText(R.string.recycled_items).toString());
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE:
                        switchToolBar(true, true, context.getText(R.string.food_and_general_waste).toString());
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case Constants.FRAGMENT_BIOWASTE:
                        switchToolBar(true, true, context.getText(R.string.biowaste).toString());
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS:
                        switchToolBar(true, false, context.getText(R.string.monthly_payments).toString());
                        tabLayout.setVisibility(View.GONE);
                        break;
                    case Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS:
                        switchToolBar(true, false, context.getText(R.string.recycled_items_details).toString());
                        tabLayout.setVisibility(View.GONE);
                        break;
                    case Constants.FRAGMENT_BIOWASTE_DETAILS:
                        switchToolBar(true, false, context.getText(R.string.biowaste_details).toString());
                        tabLayout.setVisibility(View.GONE);
                        break;
                    case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS:
                        switchToolBar(true, false, context.getText(R.string.food_and_general_waste_details).toString());
                        tabLayout.setVisibility(View.GONE);
                        break;
                    case Constants.FRAGMENT_MONTHLY_PATIENTS_CREATE:
                        switchToolBar(true, false, context.getText(R.string.monthly_payments_create).toString());
                        tabLayout.setVisibility(View.GONE);
                        break;
                    case Constants.FRAGMENT_RECYCLED_ITEMS_CREATE:
                        switchToolBar(true, false, context.getText(R.string.recycled_items_create).toString());
                        tabLayout.setVisibility(View.GONE);
                        break;
                    case Constants.FRAGMENT_BIOWASTE_CREATE:
                        switchToolBar(true, false, context.getText(R.string.biowaste_create).toString());
                        tabLayout.setVisibility(View.GONE);
                        break;
                    case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE:
                        switchToolBar(true, false, context.getText(R.string.food_and_general_waste_create).toString());
                        tabLayout.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }
}
