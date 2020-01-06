package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uems.biowaste.R;
import uems.biowaste.async.DeleteTask;
import uems.biowaste.async.UpdatePatientTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.UserVo;

public class PatientDetailsFragment extends Fragment implements View.OnClickListener{

    private ItemVo vo;
    public UserVo me;
    String month;
    private Calendar startDate;

    private PatientDetailsFragment.OnFragmentInteractionListener mListener;
    Button deleteButtonPatient,editButtonPatient;

    TextView detailsMonthTextView ;
    TextView detailsDateTextView ;
    TextView detailsNameTextView ;
    EditText patientDetailsTotalTextView;
    Button detailsSubmitButton;
    boolean editable = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PatientDetailsFragment.OnFragmentInteractionListener) {
            mListener = (PatientDetailsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void  startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
        void  popupFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients_details, container, false);
        me = Utils.getUser(getContext());
        startDate = Calendar.getInstance();
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        if( getArguments() != null)
            vo = (ItemVo) getArguments().getSerializable("vo");
        initLayout(view);
        return view;
    }

    public void initLayout(View view) {

        detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        detailsNameTextView = view.findViewById(R.id.detailsNameTextView);

        patientDetailsTotalTextView = view.findViewById(R.id.patientDetailsTotalTextView);
        detailsSubmitButton = view.findViewById(R.id.detailsSubmitButton);
        detailsSubmitButton.setVisibility(View.GONE);
        patientDetailsTotalTextView.setFocusable(false);
        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());
        patientDetailsTotalTextView.setText(vo.getTotalPatients());

        editButtonPatient = view.findViewById(R.id.editButtonPatient);
        deleteButtonPatient = view.findViewById(R.id.deleteButtonPatient);
        detailsMonthTextView.setOnClickListener(this);
        detailsDateTextView.setOnClickListener(this);


        editButtonPatient.setVisibility(View.GONE);
        deleteButtonPatient.setVisibility(View.GONE);

        editButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientDetailsTotalTextView.setFocusableInTouchMode(true);
                patientDetailsTotalTextView.setFocusable(true);
                editable = true;
                editButtonPatient.setVisibility(View.GONE);
                deleteButtonPatient.setVisibility(View.GONE);
                detailsSubmitButton.setVisibility(View.VISIBLE);
            }
        });

        detailsSubmitButton.setVisibility(View.GONE);
        detailsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        deleteButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });

        if(vo.getCreatedBy().toLowerCase().equals(me.getUserName().toLowerCase())){
            editButtonPatient.setVisibility(View.VISIBLE);
            deleteButtonPatient.setVisibility(View.VISIBLE);
        }

    }

    public void deleteRecord(){
        JSONArray jArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month", Utils.getText(detailsMonthTextView));
            jsonObject.put("Type", Constants.FRAGMENT_MONTHLY_PATIENTS_TYPE_ID);
            jArray.put(jsonObject);
            new DeleteTask(getContext()).execute(jArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void showMonthMenu(View v) {
        String month = (String) android.text.format.DateFormat.format("M", new Date());
        int monthValue = Integer.parseInt(month)-1;

        if(getContext() != null){
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenu().add(getContext().getString(R.string.select));
            if ((monthValue - 1) > 0) {
                popup.getMenu().add(Utils.getMonths(monthValue - 1));

            } else
                popup.getMenu().add(Utils.getMonths(12));

            popup.getMenu().add(Utils.getMonths(monthValue));


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    detailsMonthTextView.setText(item.getTitle());
                    detailsDateTextView.setText(getText(R.string.select));
                    return false;
                }
            });
            popup.show();
        }
    }

    public void updated(){
        if(getContext() != null){
            Toast.makeText(getContext(),getText(R.string.updated),Toast.LENGTH_SHORT).show();
            mListener.popupFragment(new PatientListFragment(), Constants.FRAGMENT_MONTHLY_PATIENTS,false,true);
        }
    }

    public void recordDelete(){
        if(getContext() != null){
            Toast.makeText(getContext(),getContext().getString(R.string.successfully_deleted),Toast.LENGTH_SHORT).show();
            mListener.popupFragment(new PatientListFragment(), Constants.FRAGMENT_MONTHLY_PATIENTS,false,true);
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
                            String monthname = (String) android.text.format.DateFormat.format("MMMM", startDate.getTime());
                            month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
                            detailsMonthTextView.setText(monthname);
                            detailsDateTextView.setText(date);
                        }
                    }, mYear, mMonth, mDay);
            try{
                SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
                Calendar cal = Calendar.getInstance();
                cal.setTime(inputFormat.parse(detailsMonthTextView.getText().toString()));
                SimpleDateFormat outputFormat = new SimpleDateFormat("M"); // 01-12
                int month = Integer.parseInt(outputFormat.format(cal.getTime()));
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, month-1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                calendar.set(Calendar.DAY_OF_MONTH,  cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());



            }catch (Exception e){

            }datePickerDialog.show();
        }
    }


    public void saveItem() {

        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            Utils.showError(getContext().getString(R.string.please_select_a_date), detailsMonthTextView);
            return;
        }
        if (ZValidation.checkEmpty(patientDetailsTotalTextView)) {
            Utils.showError(getContext().getString(R.string.please_enter_total_patients), detailsMonthTextView);
            patientDetailsTotalTextView.requestFocus();
            return;
        }

        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            if(getMonth(Utils.getText(detailsMonthTextView)).isEmpty()){
                jsonObject.put("Month",month);
            }else{
                jsonObject.put("Month", getMonth(Utils.getText(detailsMonthTextView)));
            }
            jsonObject.put("TotalPatients", Utils.getText(patientDetailsTotalTextView));
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);
            new UpdatePatientTask(getContext()).execute(jArray.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String getMonth(String month){
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return (cal.get(Calendar.MONTH)+1)+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.detailsMonthTextView:
                if(editable)
                    showMonthMenu(v);
                break;
            case R.id.detailsDateTextView:
                if(editable)
                    showStartDate();
                break;
            case R.id.detailsSubmitButton:
                saveItem();
                break;
        }
    }

}


