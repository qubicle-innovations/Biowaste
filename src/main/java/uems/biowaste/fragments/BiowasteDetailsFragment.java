package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.InputFilter;
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
import uems.biowaste.async.DeleteTask;
import uems.biowaste.async.FetchBioWasteDetailsTask;
import uems.biowaste.async.UpdateFetchBioWasteTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.MoneyValueFilter;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.BioWasteItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class BiowasteDetailsFragment extends Fragment implements View.OnClickListener {

    private BioWasteItemVo vo;

    TextView detailsMonthTextView;
    TextView detailsDateTextView;
    TextView detailsNameTextView;

    EditText detailsTotalCost;
    EditText detailsTotalBin;

    Button deleteButton, editButton;
    String month;
    Button detailsSubmitButton;
    private Calendar startDate;
    public UserVo me;
    boolean editable = false;
    private BiowasteDetailsFragment.OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BiowasteDetailsFragment.OnFragmentInteractionListener) {
            mListener = (BiowasteDetailsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void startFragment(Fragment fragment, String fragmentName, boolean addToBackStack, boolean isAdd);

        void popupFragment(Fragment fragment, String fragmentName, boolean addToBackStack, boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biowaste_details, container, false);
        me = Utils.getUser(getContext());
        if (getArguments() != null)
            vo = (BioWasteItemVo) getArguments().getSerializable("vo");
        initLayout(view);
        new FetchBioWasteDetailsTask(getContext()).execute(vo.getItemID(), me.getEmailID());
        return view;
    }

    public void deleteRecord() {
        JSONArray jArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month", Utils.getText(detailsMonthTextView));
            jsonObject.put("Type", Constants.FRAGMENT_BIOWASTE_TYPE_ID);
            jArray.put(jsonObject);
            new DeleteTask(getContext()).execute(jArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initLayout(View view) {

        detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        detailsNameTextView = view.findViewById(R.id.detailsNameTextView);
        editButton = view.findViewById(R.id.editButton);
        deleteButton = view.findViewById(R.id.deleteButton);


        startDate = Calendar.getInstance();
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });

        editButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        detailsTotalCost = view.findViewById(R.id.detailsWeightTextView);
        detailsTotalBin = view.findViewById(R.id.detailsNoOfHaulageTextView);
        detailsSubmitButton = view.findViewById(R.id.detailsSubmitButton);
        detailsSubmitButton.setVisibility(View.GONE);
        detailsTotalCost.setFocusable(false);
        detailsTotalBin.setFocusable(false);
        detailsTotalCost.setFilters(new InputFilter[]{new MoneyValueFilter()});

        detailsMonthTextView.setOnClickListener(this);
        detailsDateTextView.setOnClickListener(this);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsTotalCost.setFocusableInTouchMode(true);
                detailsTotalBin.setFocusableInTouchMode(true);
                detailsTotalCost.setFocusable(true);
                detailsTotalBin.setFocusable(true);
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                detailsSubmitButton.setVisibility(View.VISIBLE);
                editable = true;
            }
        });

        detailsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });


        setData();

    }

    public void setData() {
        if (vo != null) {
            detailsMonthTextView.setText(vo.getMonth());
            detailsDateTextView.setText(vo.getDate());
            detailsNameTextView.setText(vo.getCreatedBy());
            detailsTotalCost.setText(vo.getTotalCost());
            detailsTotalBin.setText(vo.getTotalBin());
            if (vo.getCreatedBy().equals(me.getUserName())) {
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public String getMonth(String month) {
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return (cal.get(Calendar.MONTH) + 1) + "";
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
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetBiowasteDetails");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                vo = mapper.readValue(jsonArray.getJSONObject(0).toString(), new TypeReference<BioWasteItemVo>() {
                });
                setData();
            } catch (Exception e) {
                Utils.showError("please try later", detailsDateTextView);
                Log.e("parse order", e.toString());
            }
        }
    }

    public void showMonthMenu(View v) {
        String month = (String) android.text.format.DateFormat.format("M", new Date());
        int monthValue = Integer.parseInt(month) - 1;

        if (getContext() != null) {
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

    public void updated() {
        if (getContext() != null) {
            Toast.makeText(getContext(), getText(R.string.updated), Toast.LENGTH_SHORT).show();
            mListener.popupFragment(new BioWasteListFragment(), Constants.FRAGMENT_BIOWASTE, false, true);
        }
    }

    public void recordDelete() {
        if (getContext() != null) {
            Toast.makeText(getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
            mListener.popupFragment(new BioWasteListFragment(), Constants.FRAGMENT_BIOWASTE, false, true);
        }
    }


    public void showStartDate() {

        int mYear = startDate.get(Calendar.YEAR);
        int mMonth = startDate.get(Calendar.MONTH);
        int mDay = startDate.get(Calendar.DAY_OF_MONTH);


        if (getContext() != null) {
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

            }

            datePickerDialog.show();
        }
    }

    public void saveItem() {
        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            Utils.showError("Please select a date", detailsMonthTextView);
            return;
        }
        if (ZValidation.checkEmpty(detailsTotalCost)) {
            Utils.showError("Please enter total bin", detailsMonthTextView);
            detailsTotalCost.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsTotalBin)) {
            Utils.showError("Please enter total cost", detailsMonthTextView);
            detailsTotalBin.requestFocus();
            return;

        }
        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            if (getMonth(Utils.getText(detailsMonthTextView)).isEmpty()) {
                jsonObject.put("Month", month);
            } else {
                jsonObject.put("Month", getMonth(Utils.getText(detailsMonthTextView)));
            }
            jsonObject.put("TotalBin", Utils.getText(detailsTotalBin));
            jsonObject.put("TotalCost", Utils.getText(detailsTotalCost));
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);
            new UpdateFetchBioWasteTask(getContext()).execute(jArray.toString());

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
                if (editable)
                    showMonthMenu(v);
                break;
            case R.id.detailsDateTextView:
                if (editable)
                    showStartDate();
                break;
            case R.id.detailsSubmitButton:
                saveItem();
                break;
        }
    }

}
