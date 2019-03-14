package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import uems.biowaste.R;
import uems.biowaste.async.CreateGWasteTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class GwasteCreateFragments extends Fragment implements View.OnClickListener {

    String month;
    String date;
    private Calendar startDate;
    public UserVo me;

    TextView detailsMonthTextView ;
    TextView detailsDateTextView ;
    EditText detailsWeightTextView ;
    EditText detailsNoOfHaulageTextView;
    EditText detailsDisposalFeeTextView;
    EditText detailsHuelageChargeTextView ;
    TextView detailsTotalDisposaFeeTextView ;

    private GwasteCreateFragments.OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GwasteCreateFragments.OnFragmentInteractionListener) {
            mListener = (GwasteCreateFragments.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void  startFragment(String fragmentName,boolean addToBackStack,boolean isAdd);
        void  startFragment(Fragment fragment,boolean addToBackStack,boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gwastedetails, container, false);
        startDate = Calendar.getInstance();
        me = Utils.getUser(getContext());
        initLayout(view);
        return view;
    }

    public void initLayout(View view) {

        TextView detailsNameTextView = view. findViewById(R.id.detailsNameTextView);

        detailsWeightTextView = view.findViewById(R.id.detailsWeightTextView);
        detailsNoOfHaulageTextView = view.findViewById(R.id.detailsNoOfHaulageTextView);

        detailsDisposalFeeTextView = view.findViewById(R.id.detailsDisposalFeeTextView);
        detailsHuelageChargeTextView = view. findViewById(R.id.detailsHuelageChargeTextView);

        detailsTotalDisposaFeeTextView = view. findViewById(R.id.detailsTotalDisposaFeeTextView);
        detailsMonthTextView = view. findViewById(R.id.detailsMonthTextView);
        detailsDateTextView = view. findViewById(R.id.detailsDateTextView);

        view.findViewById(R.id.detailsSubmitButton).setOnClickListener(this);
        detailsMonthTextView.setOnClickListener(this);
        detailsDateTextView.setOnClickListener(this);
        String monthName = (String) android.text.format.DateFormat.format("MMMM", startDate.getTime());
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
        detailsMonthTextView.setText(monthName);
        detailsDateTextView.setText(date);
        detailsNameTextView.setText(me.getUserName());
        detailsNoOfHaulageTextView.requestFocus();
        detailsDisposalFeeTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double disfee =0;
                if(s!=null&&s.length()>0){
                    disfee=     Double.parseDouble(s.toString());
                }
                double halCharge=0;
                if(!ZValidation.isEmpty(detailsHuelageChargeTextView)){
                    String detailsHuelageCharge = Utils.getText(detailsHuelageChargeTextView);
                    if(detailsHuelageCharge != null)
                    halCharge = Double.parseDouble(detailsHuelageCharge);
                }
                detailsTotalDisposaFeeTextView.setText(String.format("%s", disfee + halCharge));

            }
        });
        detailsHuelageChargeTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double disfee = 0;
                double halCharge=0;
                if(s!=null&&s.length()>0){
                    halCharge=Double.parseDouble(s.toString());
                }

                if(!ZValidation.isEmpty(detailsDisposalFeeTextView)){
                    String disposalFee = Utils.getText(detailsDisposalFeeTextView);
                    if(disposalFee != null)
                        disfee = Double.parseDouble(disposalFee);
                }
                detailsTotalDisposaFeeTextView.setText(String.format("%s", disfee + halCharge));

            }
        });
    }

    public void saveResponse(TResponse<String> result) {

        if (result == null) {
            Utils.showError(" please check network connection", detailsDateTextView);
        } else if (result.isHasError()) {
            Utils.showError("please try later", detailsDateTextView);

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                boolean status = jsonObject.getBoolean("status");
                if(status){
                    Toast.makeText(getContext(),"Successfully saved item",Toast.LENGTH_SHORT).show();
                    mListener.startFragment(Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE,false,true);
                }else {
                    Utils.showError("Failed to save item", detailsDateTextView);
                }
            } catch (Exception e) {
                Utils.showError("please try later", detailsDateTextView);

                Log.e("parse order", e.toString());
            }
        }
    }


    public void showMonthMenu(View v) {
        String month = (String) android.text.format.DateFormat.format("M", new Date());
        int monthValue = Integer.parseInt(month)-1;

        if(getContext() != null){
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenu().add("Select");
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
            datePickerDialog.show();
        }
    }


    public void saveItem() {
        if(Utils.getText(detailsDateTextView) != null){
            if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
                Utils.showError("Please select a date", detailsMonthTextView);
                return;
            }
        }
        if (ZValidation.checkEmpty(detailsWeightTextView)) {
            Utils.showError("Please enter total weight", detailsMonthTextView);
            detailsWeightTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsDisposalFeeTextView)) {
            Utils.showError("Please enter Disposal Fee $", detailsMonthTextView);
            detailsDisposalFeeTextView.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(detailsHuelageChargeTextView)) {
            Utils.showError("Please enter Hualage Charge $", detailsMonthTextView);
            detailsHuelageChargeTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsNoOfHaulageTextView)) {
            Utils.showError("Please enter No of Haulage", detailsMonthTextView);
            detailsNoOfHaulageTextView.requestFocus();
            return;

        }
        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month",month);
            jsonObject.put("TotalWeight", Utils.getText(detailsWeightTextView));
            jsonObject.put("NoOfHaulage", Utils.getText(detailsNoOfHaulageTextView));
            jsonObject.put("HualageCharge", Utils.getText(detailsWeightTextView));
            jsonObject.put("DisposalFee", Utils.getText(detailsNoOfHaulageTextView));
            jsonObject.put("TotalDisposalFee", Utils.getText(detailsTotalDisposaFeeTextView));
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);
            new CreateGWasteTask(getContext()).execute(jArray.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }





    /*    SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputFormat.parse(item.getTitle()));
        SimpleDateFormat outputFormat = new SimpleDateFormat("M"); // 01-12
        println(outputFormat.format(cal.getTime()));*/
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.detailsMonthTextView:
                showMonthMenu(v);
                break;
            case R.id.detailsDateTextView:
                showStartDate();
                break;
            case R.id.detailsSubmitButton:
                saveItem();
                break;
        }

    }
}
