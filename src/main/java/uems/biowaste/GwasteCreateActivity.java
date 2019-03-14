package uems.biowaste;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import uems.biowaste.async.CreateGWasteTask;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.TResponse;

public class GwasteCreateActivity extends BaseBackActivity implements View.OnClickListener {

    String month;
    String date;
    private Calendar startDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gwastedetails);
        setToolbar("Food & General waste");
        startDate = Calendar.getInstance();
        initLayout();

    }

    public void initLayout() {

        TextView detailsMonthTextView =  findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView =  findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView =  findViewById(R.id.detailsNameTextView);

        EditText detailsWeightTextView =  findViewById(R.id.detailsWeightTextView);
        EditText detailsNoOfHaulageTextView =  findViewById(R.id.detailsNoOfHaulageTextView);

        EditText detailsDisposalFeeTextView =  findViewById(R.id.detailsDisposalFeeTextView);
        EditText detailsHuelageChargeTextView =  findViewById(R.id.detailsHuelageChargeTextView);

        TextView detailsTotalDisposaFeeTextView =  findViewById(R.id.detailsTotalDisposaFeeTextView);

        findViewById(R.id.detailsSubmitButton).setOnClickListener(this);
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
                EditText detailsHuelageChargeTextView =  findViewById(R.id.detailsHuelageChargeTextView);
                TextView detailsTotalDisposaFeeTextView =  findViewById(R.id.detailsTotalDisposaFeeTextView);

                double disfee =0;
                if(s!=null&&s.length()>0){
                    disfee=     Double.parseDouble(s.toString());
                }
                double halCharge=0;
                if(!ZValidation.isEmpty(detailsHuelageChargeTextView))
                  halCharge = Double.parseDouble(Utils.getText(detailsHuelageChargeTextView));
                detailsTotalDisposaFeeTextView.setText( Utils.roundOff(disfee+halCharge));

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
                EditText detailsDisposalFeeTextView = (EditText) findViewById(R.id.detailsDisposalFeeTextView);
                TextView detailsTotalDisposaFeeTextView = (TextView) findViewById(R.id.detailsTotalDisposaFeeTextView);

                double disfee = 0;
                double halCharge=0;
                if(s!=null&&s.length()>0){
                    halCharge=Double.parseDouble(s.toString());
                }

                    if(!ZValidation.isEmpty(detailsDisposalFeeTextView))
                    disfee = Double.parseDouble(Utils.getText(detailsDisposalFeeTextView));

                detailsTotalDisposaFeeTextView.setText(  Utils.roundOff(disfee+halCharge));

            }
        });
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
                if(status){
                    Toast.makeText(context,"Successfully saved item",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
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
                // TODO Auto-generated method stub
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


    public void saveItem() {
        TextView detailsDateTextView = (TextView) findViewById(R.id.detailsDateTextView);
        TextView detailsTotalDisposaFeeTextView = (TextView) findViewById(R.id.detailsTotalDisposaFeeTextView);

        EditText detailsWeightTextView = (EditText) findViewById(R.id.detailsWeightTextView);
        EditText detailsNoOfHaulageTextView = (EditText) findViewById(R.id.detailsNoOfHaulageTextView);

        EditText detailsDisposalFeeTextView = (EditText) findViewById(R.id.detailsDisposalFeeTextView);
        EditText detailsHuelageChargeTextView = (EditText) findViewById(R.id.detailsHuelageChargeTextView);
        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            showError("Please select a date", findViewById(R.id.detailsMonthTextView));
            return;
        }
        if (ZValidation.checkEmpty(detailsWeightTextView)) {
            showError("Please enter total weight", findViewById(R.id.detailsMonthTextView));
            detailsWeightTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsDisposalFeeTextView)) {
            showError("Please enter Disposal Fee $", findViewById(R.id.detailsMonthTextView));
            detailsDisposalFeeTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsHuelageChargeTextView)) {
            showError("Please enter Hualage Charge $", findViewById(R.id.detailsMonthTextView));
            detailsHuelageChargeTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsNoOfHaulageTextView)) {
            showError("Please enter No of Haulage", findViewById(R.id.detailsMonthTextView));
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
            new CreateGWasteTask(context).execute(jArray.toString());

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
