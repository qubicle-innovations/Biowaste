package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uems.biowaste.R;
import uems.biowaste.async.FetchCountTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    private Calendar startDate;
    public UserVo me;
    TextView textView ;

    TextView tvBioCount;
    TextView tvPatientsCount ;
    TextView tvRecycleCount;
    TextView tvGwasteCount;
    TextView itemTextViewMonth;

    private OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void  startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
    }

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        startDate = Calendar.getInstance();
        me = Utils.getUser(getContext());
        initLayout(view);

        return view;
    }


    public void initLayout(View view) {

        tvBioCount = view.findViewById(R.id.tvBioCount);
        tvPatientsCount = view.findViewById(R.id.tvPatientsCount);
        tvRecycleCount = view.findViewById(R.id.tvRecycleCount);
        tvGwasteCount = view.findViewById(R.id.tvGwasteCount);

        String date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
        startDate = Calendar.getInstance();
        String month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        String monthName = (String) android.text.format.DateFormat.format("MMMM", startDate.getTime());

        new FetchCountTask(getContext()).execute(month, me.getEmailID());
//        view.findViewById(R.id.itemTextViewMonth).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.rlBioWaste).setOnClickListener(this);
        view.findViewById(R.id.rlRecycle).setOnClickListener(this);
        view.findViewById(R.id.rlPatients).setOnClickListener(this);
        view.findViewById(R.id.rlGwaste).setOnClickListener(this);
        itemTextViewMonth =  view.findViewById(R.id.itemTextViewMonth);
        itemTextViewMonth.setText(monthName);
        itemTextViewMonth.setOnClickListener(this);

       TextView tvUsername =  getActivity().findViewById(R.id.navigationNameTextView);
         if(me != null && me.getUserName() != null)
            tvUsername.setText(Utils.toTitleCase(me.getUserName()));

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.itemTextViewMonth:
                showMonthMenu(v);
                break;
            case R.id.rlBioWaste:
                mListener.startFragment(new BioWasteListFragment(), Constants.FRAGMENT_BIOWASTE,false,false);
                break;
            case R.id.rlGwaste:
                mListener.startFragment(new GWasteListFragment(),Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE,false,false);
                break;
            case R.id.rlPatients:
                mListener.startFragment(new PatientListFragment(),Constants.FRAGMENT_MONTHLY_PATIENTS,false,false);
                break;
            case R.id.rlRecycle:
                mListener.startFragment(new RecycledListFragment(),Constants.FRAGMENT_RECYCLED_ITEMS,false,false);
                break;
        }
    }

    public void countResponse(TResponse<String> result) {


        if (result == null) {
            Utils.showError(getContext().getString(R.string.please_check_network_connection), tvGwasteCount);
        } else if (result.isHasError()) {
            Utils.showError(getContext().getString(R.string.please_try_later), tvGwasteCount);

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("MonthlyDetailList");
                if (jsonArray.length() > 0) {
                    JSONObject jObject = jsonArray.getJSONObject(0);
                    if (jObject.getString("TotalCostBW").equalsIgnoreCase(""))
                        tvBioCount.setText("0");
                    else
                        tvBioCount.setText(jObject.getString("TotalCostBW"));

                    if (jObject.getString("TotalPatientsMP").equalsIgnoreCase(""))
                        tvPatientsCount.setText("0");
                    else
                        tvPatientsCount.setText(jObject.getString("TotalPatientsMP"));

                    if (jObject.getString("TotalWeightRI").equalsIgnoreCase(""))
                        tvRecycleCount.setText("0");
                    else
                        tvRecycleCount.setText(jObject.getString("TotalWeightRI"));

                    if (jObject.getString("TotalWeightFW").equalsIgnoreCase(""))
                        tvGwasteCount.setText("0");
                    else
                        tvGwasteCount.setText(jObject.getString("TotalWeightFW"));
                }

            } catch (Exception e) {
                Utils.showError(getContext().getString(R.string.please_try_later), tvGwasteCount);

                Log.e("parse order", e.toString());
            }
        }
    }


    public void showStartDate() {

        int mYear = startDate.get(Calendar.YEAR);
        int mMonth = startDate.get(Calendar.MONTH);
        int mDay = startDate.get(Calendar.DAY_OF_MONTH);

        if(getContext() != null){
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            startDate.set(year, monthOfYear, dayOfMonth);
                            String date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
                            String month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
                            itemTextViewMonth.setText(date);
                            new FetchCountTask(getContext()).execute(month, me.getEmailID());

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }


    }
    public void showMonthMenu(View v) {
        String month = (String) android.text.format.DateFormat.format("M", new Date());
        int monthValue = Integer.parseInt(month)-1;

        if(getContext() != null){
            PopupMenu popup = new PopupMenu(getContext(), v);
          //  popup.getMenu().add("Select");
            if ((monthValue - 1) > 0) {
                popup.getMenu().add(Utils.getMonths(monthValue - 1));

            } else
                popup.getMenu().add(Utils.getMonths(12));

            popup.getMenu().add(Utils.getMonths(monthValue));


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    itemTextViewMonth.setText(item.getTitle().toString());
                    try{
                        if(!item.getTitle().toString().equalsIgnoreCase("select")){
                            SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(inputFormat.parse(item.getTitle().toString()));
                            SimpleDateFormat outputFormat = new SimpleDateFormat("M"); // 01-12

                            new FetchCountTask(getContext()).execute(outputFormat.format(cal.getTime()), me.getEmailID());
                        }else {
                            new FetchCountTask(getContext()).execute("", me.getEmailID());

                        }

                    }catch (Exception e) {

                    }
                    return false;
                }
            });
            popup.show();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
