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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uems.biowaste.R;
import uems.biowaste.async.CreateGWasteTask;
import uems.biowaste.async.DeleteTask;
import uems.biowaste.async.FetchBioWasteCreateTask;
import uems.biowaste.async.FetchGWasteDetailsTask;
import uems.biowaste.async.UpdateGWasteTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class GwasteDetailsFragment extends Fragment implements View.OnClickListener {

    private ItemVo vo;

    public UserVo me;
    private GwasteDetailsFragment.OnFragmentInteractionListener mListener;
    String month;
    private Calendar startDate;

    TextView detailsDateTextView;
    TextView detailsMonthTextView;
    TextView detailsNameTextView;
    EditText detailsWeightTextView ;
    EditText detailsNoOfHaulageTextView ;
    EditText detailsDisposalFeeTextView;
    EditText detailsHuelageChargeTextView ;
    TextView detailsTotalDisposaFeeTextView;
    Button detailsSubmitButton;
    Button deleteButtonGwaste,editButtonGwaste;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GwasteDetailsFragment.OnFragmentInteractionListener) {
            mListener = (GwasteDetailsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void  popupFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
        void  startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
    }

    public void updated(){
        if(getContext() != null){
            Toast.makeText(getContext(),getText(R.string.updated),Toast.LENGTH_SHORT).show();
            mListener.popupFragment(new GWasteListFragment(),Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE,false,true);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gwastedetails, container, false);
        me = Utils.getUser(getContext());
        if(getArguments() != null)
            vo = (ItemVo) getArguments().getSerializable("vo");
        initLayout(view);
        new FetchGWasteDetailsTask(getContext()).execute(vo.getItemID(),me.getEmailID());
        return view;
    }

    public void initLayout(View view) {

        detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        detailsNameTextView = view.findViewById(R.id.detailsNameTextView);
        detailsMonthTextView.setOnClickListener(this);

        detailsWeightTextView = view.findViewById(R.id.detailsWeightTextView);
        detailsNoOfHaulageTextView = view.findViewById(R.id.detailsNoOfHaulageTextView);

        detailsDisposalFeeTextView = view.findViewById(R.id.detailsDisposalFeeTextView);
        detailsHuelageChargeTextView = view.findViewById(R.id.detailsHuelageChargeTextView);

        detailsTotalDisposaFeeTextView = view.findViewById(R.id.detailsTotalDisposaFeeTextView);
        detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        detailsDateTextView.setOnClickListener(this);
        detailsSubmitButton = view.findViewById(R.id.detailsSubmitButton);
        detailsSubmitButton.setVisibility(View.GONE);

        detailsWeightTextView.setFocusable(false);
        detailsNoOfHaulageTextView.setFocusable(false);
        detailsDisposalFeeTextView.setFocusable(false);
        detailsHuelageChargeTextView.setFocusable(false);

        startDate = Calendar.getInstance();
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());

        editButtonGwaste = view.findViewById(R.id.editButtonGwaste);
        deleteButtonGwaste = view.findViewById(R.id.deleteButtonGwaste);
        editButtonGwaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsWeightTextView.setFocusableInTouchMode(true);
                detailsNoOfHaulageTextView.setFocusableInTouchMode(true);
                detailsDisposalFeeTextView.setFocusableInTouchMode(true);
                detailsHuelageChargeTextView.setFocusableInTouchMode(true);
                detailsWeightTextView.setFocusable(true);
                detailsNoOfHaulageTextView.setFocusable(true);
                detailsDisposalFeeTextView.setFocusable(true);
                detailsHuelageChargeTextView.setFocusable(true);

                editButtonGwaste.setVisibility(View.GONE);
                deleteButtonGwaste.setVisibility(View.GONE);
                detailsSubmitButton.setVisibility(View.VISIBLE);
            }
        });

        detailsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        deleteButtonGwaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });
        editButtonGwaste.setVisibility(View.GONE);
        deleteButtonGwaste.setVisibility(View.GONE);
        setData();
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

            if(getMonth(Utils.getText(detailsMonthTextView)).isEmpty()){
                jsonObject.put("Month",month);
            }else{
                jsonObject.put("Month", getMonth(Utils.getText(detailsMonthTextView)));
            }
            jsonObject.put("TotalWeight", Utils.getText(detailsWeightTextView));
            jsonObject.put("NoOfHaulage", Utils.getText(detailsNoOfHaulageTextView));
            jsonObject.put("HualageCharge", Utils.getText(detailsHuelageChargeTextView));
            jsonObject.put("DisposalFee", Utils.getText(detailsDisposalFeeTextView).replace("$",""));
            jsonObject.put("TotalDisposalFee", Utils.getText(detailsTotalDisposaFeeTextView).replace("$",""));
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);
            new UpdateGWasteTask(getContext()).execute(jArray.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }

    /*    SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputFormat.parse(item.getTitle()));
        SimpleDateFormat outputFormat = new SimpleDateFormat("M"); // 01-12
        println(outputFormat.format(cal.getTime()));*/
    }

    public void deleteRecord(){
        JSONArray jArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month", Utils.getText(detailsMonthTextView));
            jsonObject.put("Type", Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_TYPE_ID);
            jArray.put(jsonObject);
            new DeleteTask(getContext()).execute(jArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void  setData(){

        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());
        detailsWeightTextView.setText(vo.getTotalWeight());
        detailsNoOfHaulageTextView.setText(vo.getNoOfHaulage());
        detailsDisposalFeeTextView.setText(vo.getDisposalFee());
        detailsHuelageChargeTextView.setText(vo.getHualageCharge());
        detailsTotalDisposaFeeTextView.setText("$"+ vo.getTotalDisposalFee());

        if(vo.getCreatedBy().equals(me.getUserName())){
            editButtonGwaste.setVisibility(View.VISIBLE);
            deleteButtonGwaste.setVisibility(View.VISIBLE);
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

    public void detailsResponse(TResponse<String> result) {

        if (result == null) {
            Utils.showError(" please check network connection", detailsDateTextView);
        } else if (result.isHasError()) {
            Utils.showError("please try later", detailsDateTextView);
        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetFoodandGeneralwasteDetails");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                vo = mapper.readValue(jsonArray.getJSONObject(0).toString(), new TypeReference<ItemVo>() {});
                setData();
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
