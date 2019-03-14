package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import uems.biowaste.R;
import uems.biowaste.async.FetchCountTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.UserVo;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    private Calendar startDate;
    public UserVo me;
    TextView textView ;

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
        void  startFragment(String fragmentName,boolean addToBackStack,boolean isAdd);
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

        String date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
        new FetchCountTask(getContext()).execute(date, me.getEmailID());
        view.findViewById(R.id.itemTextViewMonth).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.rlBioWaste).setOnClickListener(this);
        view.findViewById(R.id.rlRecycle).setOnClickListener(this);
        view.findViewById(R.id.rlPatients).setOnClickListener(this);
        view.findViewById(R.id.rlGwaste).setOnClickListener(this);
        textView =  view.findViewById(R.id.tvDate);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvDate:
                showStartDate();
                break;
            case R.id.rlBioWaste:
                mListener.startFragment(Constants.FRAGMENT_BIOWASTE,true,true);
                break;
            case R.id.rlGwaste:
                mListener.startFragment(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE,true,true);
                break;
            case R.id.rlPatients:
                mListener.startFragment(Constants.FRAGMENT_MONTHLY_PATIENTS,true,true);
                break;
            case R.id.rlRecycle:
                mListener.startFragment(Constants.FRAGMENT_RECYCLED_ITEMS,true,true);
                break;
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
                            textView.setText(date);
                            new FetchCountTask(getContext()).execute(date, me.getEmailID());

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
