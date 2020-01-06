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
        setToolbar(getApplicationContext().getString(R.string.food_and_general_waste));
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
                EditText detailsDisposalFeeTextView = findViewById(R.id.detailsDisposalFeeTextView);
                TextView detailsTotalDisposaFeeTextView = findViewById(R.id.detailsTotalDisposaFeeTextView);

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
            showError(getApplicationContext().getString(R.string.please_check_network_connection), findViewById(R.id.detailsDateTextView));
        } else if (result.isHasError()) {
            showError(getApplicationContext().getString(R.string.please_try_later), findViewById(R.id.detailsDateTextView));

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                boolean status = jsonObject.getBoolean("status");
                if(status){
                    Toast.makeText(context,getApplicationContext().getString(R.string.successfully_saved_item),Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    showError(getApplicationContext().getString(R.string.failed_to_save_item), findViewById(R.id.detailsDateTextView));
                }

            } catch (Exception e) {
                showError(getApplicationContext().getString(R.string.please_try_later), findViewById(R.id.detailsDateTextView));

                Log.e("parse order", e.toString());
            }
        }
    }


    public void showMonthMenu(View v) {
        String month = (String) android.text.format.DateFormat.format("M", new Date());
        int monthValue = Integer.parseInt(month)-1;


        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenu().add(getApplicationContext().getString(R.string.select));
        if ((monthValue - 1) > 0) {
            popup.getMenu().add(Utils.getMonths(monthValue - 1));

        } else
            popup.getMenu().add(Utils.getMonths(12));

        popup.getMenu().add(Utils.getMonths(monthValue));


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                TextView detailsDateTextView = findViewById(R.id.detailsDateTextView);
                TextView tvMonth = findViewById(R.id.detailsMonthTextView);
                tvMonth.setText(item.getTitle());
                detailsDateTextView.setText(getApplicationContext().getString(R.string.select));
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
                        TextView detailsMonthTextView = findViewById(R.id.detailsMonthTextView);
                        TextView detailsDateTextView = findViewById(R.id.detailsDateTextView);
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
        TextView detailsDateTextView = findViewById(R.id.detailsDateTextView);
        TextView detailsTotalDisposaFeeTextView = findViewById(R.id.detailsTotalDisposaFeeTextView);

        EditText detailsWeightTextView = findViewById(R.id.detailsWeightTextView);
        EditText detailsNoOfHaulageTextView = findViewById(R.id.detailsNoOfHaulageTextView);

        EditText detailsDisposalFeeTextView = findViewById(R.id.detailsDisposalFeeTextView);
        EditText detailsHuelageChargeTextView = findViewById(R.id.detailsHuelageChargeTextView);
        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            showError(getApplicationContext().getString(R.string.please_select_a_date), findViewById(R.id.detailsMonthTextView));
            return;
        }
        if (ZValidation.checkEmpty(detailsWeightTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_total_weight), findViewById(R.id.detailsMonthTextView));
            detailsWeightTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsDisposalFeeTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_disposal_fee), findViewById(R.id.detailsMonthTextView));
            detailsDisposalFeeTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsHuelageChargeTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_hualage_charge), findViewById(R.id.detailsMonthTextView));
            detailsHuelageChargeTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(detailsNoOfHaulageTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_no_of_haulage), findViewById(R.id.detailsMonthTextView));
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
