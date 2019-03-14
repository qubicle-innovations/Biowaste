package uems.biowaste.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import uems.biowaste.HomeActivity;
import uems.biowaste.R;
import uems.biowaste.fragments.DashboardFragment;
import uems.biowaste.fragments.GwasteCreateFragments;
import uems.biowaste.fragments.LoginFragment;

public class FragmentManger {

    private Context context;
    private Activity activity;
    private ImageView toolbarMenuImageView;
    private ImageView toolbarBackImageView;
    private Toolbar toolbar;
    private TextView toolbarTextView;
    private ImageView toolbarUETrackImageView;
    private Long backButtonPressedTime = -1L ;

    public FragmentManger(Context context, Activity activity, ImageView toolbarMenuImageView, ImageView toolbarBackImageView,
                          Toolbar toolbar, TextView toolbarTextView, ImageView toolbarUETrackImageView){
        this.context = context;
        this.activity = activity;
        this.toolbarMenuImageView = toolbarMenuImageView;
        this.toolbarBackImageView = toolbarBackImageView;
        this.toolbar = toolbar;
        this.toolbarTextView = toolbarTextView;
        this.toolbarUETrackImageView = toolbarUETrackImageView;

    }

    public void startFragment(Fragment fragment,boolean addToBackStack,boolean isAdd){

        FragmentTransaction fragmentTransaction = ((HomeActivity)activity).getSupportFragmentManager().beginTransaction();
        String fragmentName = fragment.getTag();
        switch (fragmentName) {
            case Constants.FRAGMENT_LOGIN:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_LOGIN);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_LOGIN);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_LOGIN);
                switchToolBar(false, false, null);
                break;
            case Constants.FRAGMENT_DASHBOARD:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_DASHBOARD);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_DASHBOARD);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_DASHBOARD);
                switchToolBar(true, true, null);
                break;
            case Constants.FRAGMENT_BIOWASTE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_BIOWASTE);
                switchToolBar(true, true, context.getText(R.string.biowaste).toString());
                break;
            case Constants.FRAGMENT_BIOWASTE_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_BIOWASTE_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_BIOWASTE_DETAILS);
                switchToolBar(true, false, context.getText(R.string.biowaste_details).toString());
                break;
            case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new GwasteCreateFragments(), Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                else
                    fragmentTransaction.replace(R.id.container, new GwasteCreateFragments(), Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                switchToolBar(true, true, context.getText(R.string.food_and_general_waste).toString());
                break;
            case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                switchToolBar(true, false, context.getText(R.string.food_and_general_waste_details).toString());
                break;
            case Constants.FRAGMENT_RECYCLED_ITEMS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_RECYCLED_ITEMS);
                switchToolBar(true, true, context.getText(R.string.recycled_items).toString());
                break;
            case Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                switchToolBar(true, false, context.getText(R.string.recycled_items_details).toString());
                break;
            case Constants.FRAGMENT_MONTHLY_PATIENTS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_MONTHLY_PATIENTS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_MONTHLY_PATIENTS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_MONTHLY_PATIENTS);
                switchToolBar(true, true, context.getText(R.string.monthly_payments).toString());
                break;
            case Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, fragment, Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, fragment, Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                switchToolBar(true, false, context.getText(R.string.monthly_payments_details).toString());
                break;
        }

        fragmentTransaction.commit();

    }

    public void startFragment(String fragmentName,boolean addToBackStack,boolean isAdd){

        FragmentTransaction fragmentTransaction = ((HomeActivity)activity).getSupportFragmentManager().beginTransaction();
        switch (fragmentName) {
            case Constants.FRAGMENT_LOGIN:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new LoginFragment(), Constants.FRAGMENT_LOGIN);
                else
                    fragmentTransaction.replace(R.id.container, new LoginFragment(), Constants.FRAGMENT_LOGIN);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_LOGIN);
                switchToolBar(false, false, null);
                break;
            case Constants.FRAGMENT_DASHBOARD:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new DashboardFragment(), Constants.FRAGMENT_DASHBOARD);
                else
                    fragmentTransaction.replace(R.id.container, new DashboardFragment(), Constants.FRAGMENT_DASHBOARD);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_DASHBOARD);
                switchToolBar(true, true, null);
                break;
            case Constants.FRAGMENT_BIOWASTE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new DashboardFragment(), Constants.FRAGMENT_BIOWASTE);
                else
                    fragmentTransaction.replace(R.id.container, new DashboardFragment(), Constants.FRAGMENT_BIOWASTE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_BIOWASTE);
                switchToolBar(true, true, context.getText(R.string.biowaste).toString());
                break;
            case Constants.FRAGMENT_BIOWASTE_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new DashboardFragment(), Constants.FRAGMENT_BIOWASTE_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, new DashboardFragment(), Constants.FRAGMENT_BIOWASTE_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_BIOWASTE_DETAILS);
                switchToolBar(true, false, context.getText(R.string.biowaste_details).toString());
                break;
            case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new GwasteCreateFragments(), Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                else
                    fragmentTransaction.replace(R.id.container, new GwasteCreateFragments(), Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE);
                switchToolBar(true, true, context.getText(R.string.food_and_general_waste).toString());
                break;
            case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new DashboardFragment(), Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, new DashboardFragment(), Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS);
                switchToolBar(true, false, context.getText(R.string.food_and_general_waste_details).toString());
                break;
            case Constants.FRAGMENT_RECYCLED_ITEMS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new DashboardFragment(), Constants.FRAGMENT_RECYCLED_ITEMS);
                else
                    fragmentTransaction.replace(R.id.container, new DashboardFragment(), Constants.FRAGMENT_RECYCLED_ITEMS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_RECYCLED_ITEMS);
                switchToolBar(true, true, context.getText(R.string.recycled_items).toString());
                break;
            case Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new DashboardFragment(), Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, new DashboardFragment(), Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS);
                switchToolBar(true, false, context.getText(R.string.recycled_items_details).toString());
                break;
            case Constants.FRAGMENT_MONTHLY_PATIENTS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new DashboardFragment(), Constants.FRAGMENT_MONTHLY_PATIENTS);
                else
                    fragmentTransaction.replace(R.id.container, new DashboardFragment(), Constants.FRAGMENT_MONTHLY_PATIENTS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_MONTHLY_PATIENTS);
                switchToolBar(true, true, context.getText(R.string.monthly_payments).toString());
                break;
            case Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS:
                if (isAdd)
                    fragmentTransaction.add(R.id.container, new DashboardFragment(), Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                else
                    fragmentTransaction.replace(R.id.container, new DashboardFragment(), Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                if (addToBackStack)
                    fragmentTransaction.addToBackStack(Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS);
                switchToolBar(true, false, context.getText(R.string.monthly_payments_details).toString());
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
                activity.setTitle(title);
            }
        }else{
            toolbar.setVisibility(View.GONE);
        }
    }

    public void onBackPressedBefore() {
        List<Fragment> fragmentList = ((HomeActivity)activity).getSupportFragmentManager().getFragments();
        if (!fragmentList.isEmpty() ) {
            String fragmentName = fragmentList.get(0).getTag();
            if (fragmentName != null) {
                if (fragmentName.equals(Constants.FRAGMENT_DASHBOARD)
                        || fragmentName.equals(Constants.FRAGMENT_BIOWASTE)
                        || fragmentName.equals(Constants.FRAGMENT_RECYCLED_ITEMS)
                        || fragmentName.equals(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE)
                        || fragmentName.equals(Constants.FRAGMENT_MONTHLY_PATIENTS)) {

                    Long currentTime = Calendar.getInstance().getTimeInMillis();
                    if(backButtonPressedTime == -1 || backButtonPressedTime < currentTime - 3000){
                        backButtonPressedTime = currentTime;
                        Toast.makeText(context, context.getText(R.string.back_button_delay_message), Toast.LENGTH_SHORT).show();
                    }else{
                        (activity).finish();
                    }
                }
            }
        }
    }

    public void onBackPressedAfter() {
        List<Fragment> fragmentList = ((HomeActivity)activity).getSupportFragmentManager().getFragments();
        if (!fragmentList.isEmpty() ) {
            String fragmentName = fragmentList.get(0).getTag();
            if (fragmentName != null) {
                switch (fragmentName) {
                    case Constants.FRAGMENT_MONTHLY_PATIENTS:
                        switchToolBar(true, true, context.getText(R.string.biowaste).toString());
                        break;
                    case Constants.FRAGMENT_RECYCLED_ITEMS:
                        switchToolBar(true, true, context.getText(R.string.recycled_items).toString());
                        break;
                    case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE:
                        switchToolBar(true, true, context.getText(R.string.food_and_general_waste).toString());
                        break;
                    case Constants.FRAGMENT_BIOWASTE:
                        switchToolBar(true, true, context.getText(R.string.biowaste).toString());
                        break;
                    case Constants.FRAGMENT_MONTHLSY_PATIENTS_DETAILS:
                        switchToolBar(true, false, context.getText(R.string.monthly_payments_details).toString());
                        break;
                    case Constants.FRAGMENT_RECYCLED_ITEMS_DETAILS:
                        switchToolBar(true, false, context.getText(R.string.recycled_items_details).toString());
                        break;
                    case Constants.FRAGMENT_BIOWASTE_DETAILS:
                        switchToolBar(true, false, context.getText(R.string.biowaste_details).toString());
                        break;
                    case Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS:
                        switchToolBar(true, false, context.getText(R.string.food_and_general_waste_details).toString());
                        break;

                }
            }
        }
    }
}
