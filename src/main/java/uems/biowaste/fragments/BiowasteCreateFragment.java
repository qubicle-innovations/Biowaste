package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputFilter;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uems.biowaste.R;
import uems.biowaste.async.FetchBioWasteCreateTask;
import uems.biowaste.async.FetchBioWasteValueTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.MoneyValueFilter;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.BioWasteItemVo;
import uems.biowaste.vo.BioWasteValueVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class BiowasteCreateFragment extends Fragment implements View.OnClickListener {

    String month;
    String date;
    private Calendar startDate;
    public UserVo me;

    TextView detailsDateTextView ;
    EditText detailTotalCost;
    EditText detailsTotalBin;
    TextView detailsMonthTextView ;
    TextView detailsNameTextView ;
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
    EditText detailsNoOfHaulageTextView;
    EditText detailsWeightTextView;
    EditText bioHazardWasteBinsCountEdTxt;
    EditText bioHazardWasteBinsCostEdTxt;
    EditText bioHazardWasteBinsTotalEdTxt;


    BioWasteValueVo vo;

    private BiowasteCreateFragment.OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BiowasteCreateFragment.OnFragmentInteractionListener) {
            mListener = (BiowasteCreateFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void listResponseBioWasteValue(TResponse<String> result) {

        if (result == null) {
            Utils.showError(" please check network connection", detailsDateTextView);
        } else if (result.isHasError()) {
            Utils.showError("please try later", detailsDateTextView);
        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("BIoWasteValList");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                vo = mapper.readValue(jsonArray.getJSONObject(0).toString(), new TypeReference<BioWasteValueVo>() { });
                setData();
            } catch (Exception e) {
                Utils.showError("please try later", detailsDateTextView);
                Log.e("parse order", e.toString());
            }
        }
    }

    public void setData(){
        if(vo != null ){
            if(cytotoxicWasteCostEdTxt != null)
                cytotoxicWasteCostEdTxt.setText(vo.getBiowasteValue());
            if(radioActiveWasteCostEdtTxt != null)
                radioActiveWasteCostEdtTxt.setText(vo.getBiowasteValue());
            if(chemicalWasteCostEdtTxt != null)
                chemicalWasteCostEdtTxt.setText(vo.getBiowasteValue());
            if(otherBiowasteCostEdTxt != null)
                otherBiowasteCostEdTxt.setText(vo.getBiowasteValue());
            if(bioHazardWasteBinsCostEdTxt != null)
                bioHazardWasteBinsCostEdTxt.setText(vo.getBiowasteValue());
        }
    }


    public interface OnFragmentInteractionListener {
        void  popupFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_biowaste_details, container, false);
        startDate = Calendar.getInstance();
        me = Utils.getUser(getContext());
        initLayout(view);
        new FetchBioWasteValueTask(getContext()).execute();

        return view;
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

        detailsNoOfHaulageTextView.setText(String.format("%s", totalBin));

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

        detailsWeightTextView.setText(String.format("%s", total));
    }

    public void initLayout(View view) {

        detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        detailsNameTextView = view.findViewById(R.id.detailsNameTextView);
        detailTotalCost = view.findViewById(R.id.detailsWeightTextView);
        detailsTotalBin = view.findViewById(R.id.detailsNoOfHaulageTextView);

        detailsWeightTextView = view.findViewById(R.id.detailsWeightTextView);
        detailsNoOfHaulageTextView = view.findViewById(R.id.detailsNoOfHaulageTextView);
        view.findViewById(R.id.detailsSubmitButton).setOnClickListener(this);
        detailsNoOfHaulageTextView.requestFocus();
        detailsMonthTextView.setOnClickListener(this);
        detailsDateTextView.setOnClickListener(this);
        String monthName = (String) android.text.format.DateFormat.format("MMMM", startDate.getTime());
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
        detailsMonthTextView.setText(monthName);
        detailsDateTextView.setText(date);
        detailsNameTextView.setText(me.getUserName());
        if(getContext() != null){
            detailsWeightTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.backgroundGray));
            detailsWeightTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.textBlack));
        }
        detailsWeightTextView.setFilters(new InputFilter[]{new MoneyValueFilter()});


        cycloneToxicWasteBinsCountEdTxt =  view.findViewById(R.id.cycloneToxicWasteBinsCountEdTxt);
        radioActiveWasteBinCountEdTxt =  view.findViewById(R.id.radioActiveWasteBinCountEdTxt);
        chemicalWasteBinCountEdTxt =  view.findViewById(R.id.chemicalWasteBinCountEdTxt);
        otherBiowasteCountEdTxt =  view.findViewById(R.id.otherBiowasteCountEdTxt);
        bioHazardWasteBinsCountEdTxt = view.findViewById(R.id.bioHazardWasteBinsCountEdTxt);

        cytotoxicWasteCostEdTxt =  view.findViewById(R.id.cytotoxicWasteCostEdTxt);
        radioActiveWasteCostEdtTxt =  view.findViewById(R.id.radioActiveWasteCostEdtTxt);
        chemicalWasteCostEdtTxt =  view.findViewById(R.id.chemicalWasteCostEdtTxt);
        otherBiowasteCostEdTxt =  view.findViewById(R.id.otherBiowasteCostEdTxt);
        bioHazardWasteBinsCostEdTxt = view.findViewById(R.id.bioHazardWasteBinsCostEdTxt);

        cytotoxicWasteTotalEdTxt =  view.findViewById(R.id.cytotoxicWasteTotalEdTxt);
        radioActiveWasteTotalEdtTxt =  view.findViewById(R.id.radioActiveWasteTotalEdtTxt);
        chemicalWasteEdtTotalTxt =  view.findViewById(R.id.chemicalWasteTotalEdtTxt);
        otherBiowasteTotalEdTxt =  view.findViewById(R.id.otherBiowasteTotalEdTxt);
        bioHazardWasteBinsTotalEdTxt = view.findViewById(R.id.bioHazardWasteBinsTotalEdTxt);

        setData();


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
                if (status) {
                    Toast.makeText(getContext(), "Successfully saved item", Toast.LENGTH_SHORT).show();
                    mListener.popupFragment(new BioWasteListFragment(),Constants.FRAGMENT_BIOWASTE,false,false);
                } else {
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
 /*       if (ZValidation.checkEmpty(detailTotalCost)) {
            Utils.showError("Please enter total bin", detailsMonthTextView);
            detailTotalCost.requestFocus();
            return;

        }

        if (ZValidation.checkEmpty(detailsTotalBin)) {
            Utils.showError("Please enter total cost", detailsMonthTextView);
            detailsTotalBin.requestFocus();
            return;

        }
*/

        /*if (ZValidation.checkEmpty(cytotoxicWasteCostEdTxt)) {
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
        }*/

        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month", month);

            String totalBin = Utils.getText(detailsTotalBin);
            if(totalBin == null || totalBin.isEmpty())
                totalBin = "0";
            jsonObject.put("TotalBin", ((int) Double.parseDouble(totalBin))+"");

            String totalCost = Utils.getText(detailTotalCost);
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

            String OtherWasteTotal = Utils.getText(otherBiowasteTotalEdTxt);
            if(OtherWasteTotal == null || OtherWasteTotal.isEmpty())
                OtherWasteTotal = "0";
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

            jsonObject.put("OtherWasteTotal", OtherWasteTotal);
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);

            Log.d("JSONARRya",jArray.toString());

           new FetchBioWasteCreateTask(getContext()).execute(jArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
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
