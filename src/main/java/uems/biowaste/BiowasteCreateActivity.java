package uems.biowaste;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import uems.biowaste.async.FetchBioWasteCreateTask;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.TResponse;

public class BiowasteCreateActivity extends BaseBackActivity implements View.OnClickListener {

    String month;
    String date;
    private Calendar startDate;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_biowaste_details);
        setToolbar("Biowaste");
        startDate = Calendar.getInstance();
        initLayout();
    }

    public void initLayout() {

        TextView detailsMonthTextView = (TextView) findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView = (TextView) findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView = (TextView) findViewById(R.id.detailsNameTextView);

        EditText detailsWeightTextView = (EditText) findViewById(R.id.detailsWeightTextView);
        EditText detailsNoOfHaulageTextView = (EditText) findViewById(R.id.detailsNoOfHaulageTextView);
        findViewById(R.id.detailsSubmitButton).setOnClickListener(this);
        detailsNoOfHaulageTextView.requestFocus();
        detailsMonthTextView.setOnClickListener(this);
        detailsDateTextView.setOnClickListener(this);
        String monthName = (String) android.text.format.DateFormat.format("MMMM", startDate.getTime());
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
        detailsMonthTextView.setText(monthName);
        detailsDateTextView.setText(date);
        detailsNameTextView.setText(me.getUserName());
        detailsWeightTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundGray));
        detailsWeightTextView.setTextColor(ContextCompat.getColor(context, R.color.textBlack));

    }

    public void saveResponse(TResponse<String> result) {

        if (result == null) {
            showError(" please check network connection", findViewById(R.id.detailsDateTextView));
        } else if (result.isHasError()) {
            showError("please try later", findViewById(R.id.detailsDateTextView));
        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                boolean status = jsonObject.getBoolean("status");
                if (status) {
                    Toast.makeText(context, "Successfully saved item", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showError("Failed to save item", findViewById(R.id.detailsDateTextView));
                }
            } catch (Exception e) {
                showError("please try later", findViewById(R.id.detailsDateTextView));

                Log.e("parse order", e.toString());
            }
        }
    }

    public void showMonthMenu(View v) {
        String month = (String) android.text.format.DateFormat.format("M", new Date());
        int monthValue = Integer.parseInt(month)-1;

        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenu().add("Select");
        if ((monthValue - 1) > 0) {
            popup.getMenu().add(Utils.getMonths(monthValue - 1));
        } else
            popup.getMenu().add(Utils.getMonths(12));

        popup.getMenu().add(Utils.getMonths(monthValue));


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                TextView detailsDateTextView = (TextView) findViewById(R.id.detailsDateTextView);
                TextView tvMonth = (TextView) findViewById(R.id.detailsMonthTextView);
                tvMonth.setText(item.getTitle());
                detailsDateTextView.setText("Select");
                return false;
            }
        });
        popup.show();
    }

    public void showStartDate() {

        int mYear = startDate.get(Calendar.YEAR);
        int mMonth = startDate.get(Calendar.MONTH);
        int mDay = startDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        TextView detailsMonthTextView = (TextView) findViewById(R.id.detailsMonthTextView);
                        TextView detailsDateTextView = (TextView) findViewById(R.id.detailsDateTextView);
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
    
    public void setTotal(){
        double total = 0;
        double totalBin = 0;

        if(cycloneToxicWasteBinsCountEdTxt.getText() != null && !cycloneToxicWasteBinsCountEdTxt.getText().toString().isEmpty())
            total = Double.parseDouble(cycloneToxicWasteBinsCountEdTxt.getText().toString());
        if(radioActiveWasteBinCountEdTxt.getText() != null && !radioActiveWasteBinCountEdTxt.getText().toString().isEmpty())
            total = Double.parseDouble(radioActiveWasteBinCountEdTxt.getText().toString());
        if(chemicalWasteBinCountEdTxt.getText() != null && !chemicalWasteBinCountEdTxt.getText().toString().isEmpty())
            total = Double.parseDouble(chemicalWasteBinCountEdTxt.getText().toString());
        if(otherBiowasteCountEdTxt.getText() != null && !otherBiowasteCountEdTxt.getText().toString().isEmpty())
            total = Double.parseDouble(otherBiowasteCountEdTxt.getText().toString());

        EditText detailsNoOfHaulageTextView = findViewById(R.id.detailsNoOfHaulageTextView);
        detailsNoOfHaulageTextView.setText(String.format("%s", total));

        if(cytotoxicWasteCostEdTxt.getText() != null && !cytotoxicWasteCostEdTxt.getText().toString().isEmpty())
            total = Double.parseDouble(cytotoxicWasteCostEdTxt.getText().toString());
        if(radioActiveWasteTotalEdtTxt.getText() != null && !radioActiveWasteTotalEdtTxt.getText().toString().isEmpty())
            total = Double.parseDouble(radioActiveWasteTotalEdtTxt.getText().toString());
        if(chemicalWasteEdtTotalTxt.getText() != null && !chemicalWasteEdtTotalTxt.getText().toString().isEmpty())
            total = Double.parseDouble(chemicalWasteEdtTotalTxt.getText().toString());
        if(otherBiowasteTotalEdTxt.getText() != null && !otherBiowasteTotalEdTxt.getText().toString().isEmpty())
            total = Double.parseDouble(otherBiowasteTotalEdTxt.getText().toString());
        
        EditText detailsWeightTextView = findViewById(R.id.detailsWeightTextView);
        detailsWeightTextView.setText(String.format("%s", total));
    }


    public void saveItem() {
        cycloneToxicWasteBinsCountEdTxt =  findViewById(R.id.cycloneToxicWasteBinsCountEdTxt);
        radioActiveWasteBinCountEdTxt =  findViewById(R.id.radioActiveWasteBinCountEdTxt);
        chemicalWasteBinCountEdTxt =  findViewById(R.id.chemicalWasteBinCountEdTxt);
        otherBiowasteCountEdTxt =  findViewById(R.id.otherBiowasteCountEdTxt);

        cytotoxicWasteCostEdTxt =  findViewById(R.id.cytotoxicWasteCostEdTxt);
        radioActiveWasteCostEdtTxt =  findViewById(R.id.radioActiveWasteCostEdtTxt);
        chemicalWasteCostEdtTxt =  findViewById(R.id.chemicalWasteCostEdtTxt);
        otherBiowasteCostEdTxt =  findViewById(R.id.otherBiowasteCostEdTxt);

        cytotoxicWasteTotalEdTxt =  findViewById(R.id.cytotoxicWasteTotalEdTxt);
        radioActiveWasteTotalEdtTxt =  findViewById(R.id.radioActiveWasteTotalEdtTxt);
        chemicalWasteEdtTotalTxt =  findViewById(R.id.chemicalWasteTotalEdtTxt);
        otherBiowasteTotalEdTxt =  findViewById(R.id.bioHazardWasteBinsTotalEdTxt);

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
                if(!chemicalWasteCostEdtTxt.getText().toString().isEmpty() && !chemicalWasteBinCountEdTxt.getText().toString().isEmpty())
                    chemicalWasteEdtTotalTxt.setText(String.format("%s", Double.parseDouble(chemicalWasteCostEdtTxt.getText().toString()) * Double.parseDouble(chemicalWasteBinCountEdTxt.getText().toString())));
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
                    radioActiveWasteTotalEdtTxt.setText(String.format("%s", Double.parseDouble(cycloneToxicWasteBinsCountEdTxt.getText().toString()) * Double.parseDouble(cytotoxicWasteCostEdTxt.getText().toString())));
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
                    radioActiveWasteTotalEdtTxt.setText(String.format("%s", Double.parseDouble(cycloneToxicWasteBinsCountEdTxt.getText().toString()) * Double.parseDouble(cytotoxicWasteCostEdTxt.getText().toString())));
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


        if (ZValidation.checkEmpty(cytotoxicWasteCostEdTxt)) {
            showError("Please enter cyclone toxic cost", cytotoxicWasteCostEdTxt);
            cytotoxicWasteCostEdTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(radioActiveWasteCostEdtTxt)) {
            showError("Please enter cyclone toxic cost", radioActiveWasteCostEdtTxt);
            radioActiveWasteCostEdtTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(chemicalWasteCostEdtTxt)) {
            showError("Please enter cyclone toxic cost", chemicalWasteCostEdtTxt);
            chemicalWasteCostEdtTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(otherBiowasteCostEdTxt)) {
            showError("Please enter cyclone toxic cost", otherBiowasteCostEdTxt);
            otherBiowasteCostEdTxt.requestFocus();
            return;
        }

        if (ZValidation.checkEmpty(cycloneToxicWasteBinsCountEdTxt)) {
            showError("Please enter cyclone toxic number", cycloneToxicWasteBinsCountEdTxt);
            cycloneToxicWasteBinsCountEdTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(radioActiveWasteBinCountEdTxt)) {
            showError("Please enter radio active waste number", radioActiveWasteBinCountEdTxt);
            radioActiveWasteBinCountEdTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(chemicalWasteBinCountEdTxt)) {
            showError("Please enter chemical number", chemicalWasteBinCountEdTxt);
            chemicalWasteBinCountEdTxt.requestFocus();
            return;
        }
        if (ZValidation.checkEmpty(otherBiowasteCountEdTxt)) {
            showError("Please enter other biowaste number", otherBiowasteCountEdTxt);
            otherBiowasteCountEdTxt.requestFocus();
            return;
        }
        TextView detailsDateTextView = (TextView) findViewById(R.id.detailsDateTextView);

        EditText detailsWeightTextView = (EditText) findViewById(R.id.detailsWeightTextView);
        EditText detailsNoOfHaulageTextView = (EditText) findViewById(R.id.detailsNoOfHaulageTextView);
        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            showError("Please select a date", findViewById(R.id.detailsMonthTextView));
            return;
        }
        if (ZValidation.checkEmpty(detailsWeightTextView)) {
            showError("Please enter total bin", findViewById(R.id.detailsMonthTextView));
            detailsWeightTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsNoOfHaulageTextView)) {
            showError("Please enter total cost", findViewById(R.id.detailsMonthTextView));
            detailsNoOfHaulageTextView.requestFocus();
            return;
        }

        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month", month);
            jsonObject.put("TotalBin", Utils.getText(detailsWeightTextView));
            jsonObject.put("TotalCost", Utils.getText(detailsNoOfHaulageTextView));
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);
            new FetchBioWasteCreateTask(context).execute(jArray.toString());

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
