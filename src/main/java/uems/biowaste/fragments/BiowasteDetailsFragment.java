package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
    EditText cycloneToxicWasteBinsCountEdTxt;
    EditText radioActiveWasteBinCountEdTxt;
    EditText chemicalWasteBinCountEdTxt;
    EditText otherBiowasteCountEdTxt ;
    EditText cytotoxicWasteCostEdTxt;
    EditText radioActiveWasteCostEdtTxt;
    EditText chemicalWasteCostEdtTxt;
    EditText otherBiowasteCostEdTxt;
    EditText cytotoxicWasteTotalEdTxt;
    EditText radioActiveWasteTotalEdtTxt;
    EditText chemicalWasteEdtTotalTxt;
    EditText otherBiowasteTotalEdTxt;
    EditText bioHazardWasteBinsCountEdTxt;
    EditText bioHazardWasteBinsCostEdTxt;
    EditText bioHazardWasteBinsTotalEdTxt;

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
        cycloneToxicWasteBinsCountEdTxt =  view.findViewById(R.id.cycloneToxicWasteBinsCountEdTxt);
        radioActiveWasteBinCountEdTxt =  view.findViewById(R.id.radioActiveWasteBinCountEdTxt);
        chemicalWasteBinCountEdTxt =  view.findViewById(R.id.chemicalWasteBinCountEdTxt);
        otherBiowasteCountEdTxt =  view.findViewById(R.id.otherBiowasteCountEdTxt);
        bioHazardWasteBinsCountEdTxt =  view.findViewById(R.id.bioHazardWasteBinsCountEdTxt);

        cytotoxicWasteCostEdTxt =  view.findViewById(R.id.cytotoxicWasteCostEdTxt);
        radioActiveWasteCostEdtTxt =  view.findViewById(R.id.radioActiveWasteCostEdtTxt);
        chemicalWasteCostEdtTxt =  view.findViewById(R.id.chemicalWasteCostEdtTxt);
        otherBiowasteCostEdTxt =  view.findViewById(R.id.otherBiowasteCostEdTxt);
        bioHazardWasteBinsCostEdTxt =  view.findViewById(R.id.bioHazardWasteBinsCostEdTxt);

        cytotoxicWasteTotalEdTxt =  view.findViewById(R.id.cytotoxicWasteTotalEdTxt);
        radioActiveWasteTotalEdtTxt =  view.findViewById(R.id.radioActiveWasteTotalEdtTxt);
        chemicalWasteEdtTotalTxt =  view.findViewById(R.id.chemicalWasteTotalEdtTxt);
        otherBiowasteTotalEdTxt =  view.findViewById(R.id.otherBiowasteTotalEdTxt);
        bioHazardWasteBinsTotalEdTxt =  view.findViewById(R.id.bioHazardWasteBinsTotalEdTxt);

        cycloneToxicWasteBinsCountEdTxt.setFocusable(false);
        radioActiveWasteBinCountEdTxt.setFocusable(false);
        chemicalWasteBinCountEdTxt.setFocusable(false);
        chemicalWasteBinCountEdTxt.setFocusable(false);
        otherBiowasteCountEdTxt.setFocusable(false);
        bioHazardWasteBinsCountEdTxt.setFocusable(false);
        cytotoxicWasteCostEdTxt.setFocusable(false);
        radioActiveWasteCostEdtTxt.setFocusable(false);
        chemicalWasteCostEdtTxt.setFocusable(false);
        otherBiowasteCostEdTxt.setFocusable(false);
        bioHazardWasteBinsCostEdTxt.setFocusable(false);

        bioHazardWasteBinsCountEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!bioHazardWasteBinsCountEdTxt.getText().toString().isEmpty() && !bioHazardWasteBinsCostEdTxt.getText().toString().isEmpty())
                    bioHazardWasteBinsTotalEdTxt.setText(String.format("%s", Double.parseDouble(bioHazardWasteBinsCostEdTxt.getText().toString()) * Double.parseDouble(bioHazardWasteBinsCountEdTxt.getText().toString())));
                setTotal();
            }
        });
        bioHazardWasteBinsCostEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!bioHazardWasteBinsCountEdTxt.getText().toString().isEmpty() && !bioHazardWasteBinsCostEdTxt.getText().toString().isEmpty())
                    bioHazardWasteBinsTotalEdTxt.setText(String.format("%s", Double.parseDouble(bioHazardWasteBinsCostEdTxt.getText().toString()) * Double.parseDouble(bioHazardWasteBinsCountEdTxt.getText().toString())));
                setTotal();
            }
        });
        otherBiowasteCountEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!otherBiowasteCountEdTxt.getText().toString().isEmpty() && !otherBiowasteCostEdTxt.getText().toString().isEmpty())
                    otherBiowasteTotalEdTxt.setText(String.format("%s", Double.parseDouble(otherBiowasteCountEdTxt.getText().toString()) * Double.parseDouble(otherBiowasteCostEdTxt.getText().toString())));
                setTotal();
            }
        });
        otherBiowasteCostEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!otherBiowasteCountEdTxt.getText().toString().isEmpty() && !otherBiowasteCostEdTxt.getText().toString().isEmpty())
                    otherBiowasteTotalEdTxt.setText(String.format("%s", Double.parseDouble(otherBiowasteCountEdTxt.getText().toString()) * Double.parseDouble(otherBiowasteCostEdTxt.getText().toString())));
                setTotal();
            }
        });

        chemicalWasteCostEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!chemicalWasteCostEdtTxt.getText().toString().isEmpty() && !chemicalWasteBinCountEdTxt.getText().toString().isEmpty())
                    chemicalWasteEdtTotalTxt.setText(String.format("%s", Double.parseDouble(chemicalWasteCostEdtTxt.getText().toString()) * Double.parseDouble(chemicalWasteBinCountEdTxt.getText().toString())));
                setTotal();
            }
        });
        chemicalWasteBinCountEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!chemicalWasteCostEdtTxt.getText().toString().isEmpty() && !chemicalWasteBinCountEdTxt.getText().toString().isEmpty())
                    chemicalWasteEdtTotalTxt.setText(String.format("%s", Double.parseDouble(chemicalWasteCostEdtTxt.getText().toString()) * Double.parseDouble(chemicalWasteBinCountEdTxt.getText().toString())));
                setTotal();
            }
        });
        radioActiveWasteBinCountEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!radioActiveWasteBinCountEdTxt.getText().toString().isEmpty() && !radioActiveWasteCostEdtTxt.getText().toString().isEmpty())
                    radioActiveWasteTotalEdtTxt.setText(String.format("%s", Double.parseDouble(radioActiveWasteBinCountEdTxt.getText().toString()) * Double.parseDouble(radioActiveWasteCostEdtTxt.getText().toString())));
                setTotal();
            }
        });
        radioActiveWasteCostEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!radioActiveWasteBinCountEdTxt.getText().toString().isEmpty() && !radioActiveWasteCostEdtTxt.getText().toString().isEmpty())
                    radioActiveWasteTotalEdtTxt.setText(String.format("%s", Double.parseDouble(radioActiveWasteBinCountEdTxt.getText().toString()) * Double.parseDouble(radioActiveWasteCostEdtTxt.getText().toString())));
                setTotal();
            }
        });
        cytotoxicWasteCostEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!cycloneToxicWasteBinsCountEdTxt.getText().toString().isEmpty() && !cytotoxicWasteCostEdTxt.getText().toString().isEmpty())
                    cytotoxicWasteTotalEdTxt.setText(String.format("%s", Double.parseDouble(cycloneToxicWasteBinsCountEdTxt.getText().toString()) * Double.parseDouble(cytotoxicWasteCostEdTxt.getText().toString())));
                setTotal();
            }
        });
        cycloneToxicWasteBinsCountEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!cycloneToxicWasteBinsCountEdTxt.getText().toString().isEmpty() && !cytotoxicWasteCostEdTxt.getText().toString().isEmpty())
                    cytotoxicWasteTotalEdTxt.setText(String.format("%s", Double.parseDouble(cycloneToxicWasteBinsCountEdTxt.getText().toString()) * Double.parseDouble(cytotoxicWasteCostEdTxt.getText().toString())));
                setTotal();
            }
        });


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
                cycloneToxicWasteBinsCountEdTxt.setFocusableInTouchMode(true);
                radioActiveWasteBinCountEdTxt.setFocusableInTouchMode(true);
                chemicalWasteBinCountEdTxt.setFocusableInTouchMode(true);
                otherBiowasteCountEdTxt.setFocusableInTouchMode(true);
                cytotoxicWasteCostEdTxt.setFocusableInTouchMode(true);
                radioActiveWasteCostEdtTxt.setFocusableInTouchMode(true);
                chemicalWasteCostEdtTxt.setFocusableInTouchMode(true);
                otherBiowasteCostEdTxt.setFocusableInTouchMode(true);
                cycloneToxicWasteBinsCountEdTxt.setFocusable(true);
                radioActiveWasteBinCountEdTxt.setFocusable(true);
                chemicalWasteBinCountEdTxt.setFocusable(true);
                otherBiowasteCountEdTxt.setFocusable(true);
                cytotoxicWasteCostEdTxt.setFocusable(true);
                radioActiveWasteCostEdtTxt.setFocusable(true);
                chemicalWasteCostEdtTxt.setFocusable(true);
                otherBiowasteCostEdTxt.setFocusable(true);
                bioHazardWasteBinsCostEdTxt.setFocusableInTouchMode(true);
                bioHazardWasteBinsCountEdTxt.setFocusableInTouchMode(true);
                bioHazardWasteBinsTotalEdTxt.setFocusableInTouchMode(true);
                bioHazardWasteBinsCostEdTxt.setFocusable(true);
                bioHazardWasteBinsCountEdTxt.setFocusable(true);
                bioHazardWasteBinsTotalEdTxt.setFocusable(true);

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

            cytotoxicWasteCostEdTxt.setText(vo.getChemicalWasteCost());
            cycloneToxicWasteBinsCountEdTxt.setText(vo.getCytotoxicWaste());
            cytotoxicWasteCostEdTxt.setText(vo.getCytotoxicWasteTotal());

            radioActiveWasteBinCountEdTxt.setText(vo.getRadioactiveWaste());
            radioActiveWasteCostEdtTxt.setText(vo.getRadioactiveWasteCost());
            radioActiveWasteTotalEdtTxt.setText(vo.getRadioactiveWasteTotal());

            chemicalWasteBinCountEdTxt.setText(vo.getChemicalWaste());
            chemicalWasteCostEdtTxt.setText(vo.getChemicalWasteCost());
            chemicalWasteEdtTotalTxt.setText(vo.getChemicalWasteTotal());

            otherBiowasteCostEdTxt.setText(vo.getOtherWasteCost());
            otherBiowasteCountEdTxt.setText(vo.getOtherWaste());
            otherBiowasteTotalEdTxt.setText(vo.getOtherWasteTotal());


            if(vo.getBiohazardWasteCost() != null && !vo.getBiohazardWasteCost().isEmpty())
                bioHazardWasteBinsCostEdTxt.setText(vo.getBiohazardWasteCost());
            if(vo.getBiohazardWasteCost() != null && !vo.getBiohazardWaste().isEmpty())
                bioHazardWasteBinsCountEdTxt.setText(vo.getBiohazardWaste());
            if(vo.getBiohazardWasteCost() != null && !vo.getOtherWasteTotal().isEmpty())
                bioHazardWasteBinsTotalEdTxt.setText(vo.getBiohazardWasteTotal());


            detailsMonthTextView.setText(vo.getMonth());
            detailsDateTextView.setText(vo.getDate());
            detailsNameTextView.setText(vo.getCreatedBy());
            detailsTotalCost.setText(vo.getTotalCost());
            detailsTotalBin.setText(vo.getTotalBin());

            if(vo.getCreatedBy().toLowerCase().equals(me.getUserName().toLowerCase())){
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

    public void setTotal(){
        double total = 0;
        double totalBin = 0;

        if(cycloneToxicWasteBinsCountEdTxt.getText() != null && !cycloneToxicWasteBinsCountEdTxt.getText().toString().isEmpty())
            totalBin += Double.parseDouble(cycloneToxicWasteBinsCountEdTxt.getText().toString());
        if(radioActiveWasteBinCountEdTxt.getText() != null && !radioActiveWasteBinCountEdTxt.getText().toString().isEmpty())
            totalBin += Double.parseDouble(radioActiveWasteBinCountEdTxt.getText().toString());
        if(chemicalWasteBinCountEdTxt.getText() != null && !chemicalWasteBinCountEdTxt.getText().toString().isEmpty())
            totalBin += Double.parseDouble(chemicalWasteBinCountEdTxt.getText().toString());
        if(otherBiowasteCountEdTxt.getText() != null && !otherBiowasteCountEdTxt.getText().toString().isEmpty())
            totalBin += Double.parseDouble(otherBiowasteCountEdTxt.getText().toString());
        if(bioHazardWasteBinsCountEdTxt.getText() != null && !bioHazardWasteBinsCountEdTxt.getText().toString().isEmpty())
            totalBin += Double.parseDouble(bioHazardWasteBinsCountEdTxt.getText().toString());


        if(cytotoxicWasteTotalEdTxt.getText() != null && !cytotoxicWasteTotalEdTxt.getText().toString().isEmpty())
            total += Double.parseDouble(cytotoxicWasteTotalEdTxt.getText().toString());
        if(radioActiveWasteTotalEdtTxt.getText() != null && !radioActiveWasteTotalEdtTxt.getText().toString().isEmpty())
            total += Double.parseDouble(radioActiveWasteTotalEdtTxt.getText().toString());
        if(chemicalWasteEdtTotalTxt.getText() != null && !chemicalWasteEdtTotalTxt.getText().toString().isEmpty())
            total += Double.parseDouble(chemicalWasteEdtTotalTxt.getText().toString());
        if(otherBiowasteTotalEdTxt.getText() != null && !otherBiowasteTotalEdTxt.getText().toString().isEmpty())
            total += Double.parseDouble(otherBiowasteTotalEdTxt.getText().toString());
        if(bioHazardWasteBinsTotalEdTxt.getText() != null && !bioHazardWasteBinsTotalEdTxt.getText().toString().isEmpty())
            total += Double.parseDouble(bioHazardWasteBinsTotalEdTxt.getText().toString());

        detailsTotalBin.setText(String.format("%s", totalBin));
        detailsTotalCost.setText(String.format("%s", total));
    }

    public void saveItem() {
        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            Utils.showError("Please select a date", detailsMonthTextView);
            return;
        }
/*
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

        if (ZValidation.checkEmpty(cytotoxicWasteCostEdTxt)) {
            Utils.showError("Please enter cyclone toxic cost", cytotoxicWasteCostEdTxt);
            cytotoxicWasteCostEdTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(radioActiveWasteCostEdtTxt)) {
            Utils.showError("Please enter cyclone toxic cost", radioActiveWasteCostEdtTxt);
            radioActiveWasteCostEdtTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(chemicalWasteCostEdtTxt)) {
            Utils.showError("Please enter cyclone toxic cost", chemicalWasteCostEdtTxt);
            chemicalWasteCostEdtTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(otherBiowasteCostEdTxt)) {
            Utils.showError("Please enter cyclone toxic cost", otherBiowasteCostEdTxt);
            otherBiowasteCostEdTxt.requestFocus();
            return;
        }

        if (ZValidation.checkEmpty(cycloneToxicWasteBinsCountEdTxt)) {
            Utils.showError("Please enter cyclone toxic number", cycloneToxicWasteBinsCountEdTxt);
            cycloneToxicWasteBinsCountEdTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(radioActiveWasteBinCountEdTxt)) {
            Utils.showError("Please enter radio active waste number", radioActiveWasteBinCountEdTxt);
            radioActiveWasteBinCountEdTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(chemicalWasteBinCountEdTxt)) {
            Utils.showError("Please enter chemical number", chemicalWasteBinCountEdTxt);
            chemicalWasteBinCountEdTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(otherBiowasteCountEdTxt)) {
            Utils.showError("Please enter other biowaste number", otherBiowasteCountEdTxt);
            otherBiowasteCountEdTxt.requestFocus();
            return;
        }
*/

        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month", month);

            String totalBin = Utils.getText(detailsTotalBin);
            if(totalBin == null || totalBin.isEmpty())
                totalBin = "0";
            jsonObject.put("TotalBin", ((int) Double.parseDouble(totalBin))+"");

            String totalCost = Utils.getText(detailsTotalCost);
            if(totalCost == null || totalCost.isEmpty())
                totalCost = "0";
            jsonObject.put("TotalCost",totalCost);

            String CytotoxicWaste = Utils.getText(cycloneToxicWasteBinsCountEdTxt);
            if(CytotoxicWaste == null || CytotoxicWaste.isEmpty())
                CytotoxicWaste = "0";
            jsonObject.put("CytotoxicWaste",((int) Double.parseDouble(CytotoxicWaste))+"" );

            String RadioactiveWaste = Utils.getText(radioActiveWasteBinCountEdTxt);
            if(RadioactiveWaste == null || RadioactiveWaste.isEmpty())
                RadioactiveWaste = "0";
            jsonObject.put("RadioactiveWaste",((int) Double.parseDouble(RadioactiveWaste))+"" );

            String ChemicalWaste = Utils.getText(chemicalWasteBinCountEdTxt);
            if(ChemicalWaste == null || ChemicalWaste.isEmpty())
                ChemicalWaste = "0";
            jsonObject.put("ChemicalWaste",((int) Double.parseDouble(ChemicalWaste))+"" );

            String OtherWaste = Utils.getText(otherBiowasteCountEdTxt);
            if(OtherWaste == null || OtherWaste.isEmpty())
                OtherWaste = "0";
            jsonObject.put("OtherWaste",((int) Double.parseDouble(OtherWaste))+"" );

            String CytotoxicWasteCost = Utils.getText(cytotoxicWasteCostEdTxt);
            if(CytotoxicWasteCost == null || CytotoxicWasteCost.isEmpty())
                CytotoxicWasteCost = "0";
            jsonObject.put("CytotoxicWasteCost", CytotoxicWasteCost);

            String RadioactiveWasteCost = Utils.getText(radioActiveWasteCostEdtTxt);
            if(RadioactiveWasteCost == null || RadioactiveWasteCost.isEmpty())
                RadioactiveWasteCost = "0";
            jsonObject.put("RadioactiveWasteCost", RadioactiveWasteCost);

            String ChemicalWasteCost = Utils.getText(chemicalWasteCostEdtTxt);
            if(ChemicalWasteCost == null || ChemicalWasteCost.isEmpty())
                ChemicalWasteCost = "0";
            jsonObject.put("ChemicalWasteCost", ChemicalWasteCost);

            String OtherWasteCost = Utils.getText(otherBiowasteCostEdTxt);
            if(OtherWasteCost == null || OtherWasteCost.isEmpty())
                OtherWasteCost = "0";
            jsonObject.put("OtherWasteCost", OtherWasteCost);

            String CytotoxicWasteTotal = Utils.getText(cytotoxicWasteTotalEdTxt);
            if(CytotoxicWasteTotal == null || CytotoxicWasteTotal.isEmpty())
                CytotoxicWasteTotal = "0";
            jsonObject.put("CytotoxicWasteTotal", CytotoxicWasteTotal);

            String RadioactiveWasteTotal = Utils.getText(radioActiveWasteTotalEdtTxt);
            if(RadioactiveWasteTotal == null || RadioactiveWasteTotal.isEmpty())
                RadioactiveWasteTotal = "0";
            jsonObject.put("RadioactiveWasteTotal",RadioactiveWasteTotal);

            String ChemicalWasteTotal = Utils.getText(chemicalWasteEdtTotalTxt);
            if(ChemicalWasteTotal == null || ChemicalWasteTotal.isEmpty())
                ChemicalWasteTotal = "0";
            jsonObject.put("ChemicalWasteTotal", ChemicalWasteTotal);


            String bioHazardCost = Utils.getText(bioHazardWasteBinsCostEdTxt);
            if(bioHazardCost == null || bioHazardCost.isEmpty())
                bioHazardCost = "0";
            jsonObject.put("BiohazardWasteCost", bioHazardCost);

            String bioHazardCount = Utils.getText(bioHazardWasteBinsCountEdTxt);
            if(bioHazardCount == null || bioHazardCount.isEmpty())
                bioHazardCount = "0";
            jsonObject.put("BiohazardWaste", bioHazardCount);

            String bioHazardTotal = Utils.getText(bioHazardWasteBinsTotalEdTxt);
            if(bioHazardTotal == null || bioHazardTotal.isEmpty())
                bioHazardTotal = "0";
            jsonObject.put("BiohazardWasteTotal", bioHazardTotal);

            String OtherWasteTotal = Utils.getText(otherBiowasteTotalEdTxt);
            if(OtherWasteTotal == null || OtherWasteTotal.isEmpty())
                OtherWasteTotal = "0";
            jsonObject.put("OtherWasteTotal", OtherWasteTotal);
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);

            Log.d("JSONARRya",jArray.toString());
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
