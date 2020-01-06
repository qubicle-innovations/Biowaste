package uems.biowaste;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
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

import uems.biowaste.async.CreatePatientTask;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.TResponse;

public class PatientCreateActivity extends BaseBackActivity implements View.OnClickListener {

     String month;
    String date;
    private Calendar startDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_patients_details);
        setToolbar(getApplicationContext().getString(R.string.monthly_patient_details));
        startDate = Calendar.getInstance();
        initLayout();

    }

    public void initLayout() {

        TextView detailsMonthTextView = findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView = findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView = findViewById(R.id.detailsNameTextView);

        EditText patientDetailsTotalTextView = findViewById(R.id.patientDetailsTotalTextView);
        findViewById(R.id.detailsSubmitButton).setOnClickListener(this);
        detailsMonthTextView.setOnClickListener(this);
        detailsDateTextView.setOnClickListener(this);
        String monthName = (String) android.text.format.DateFormat.format("MMMM", startDate.getTime());
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
        detailsMonthTextView.setText(monthName);
        detailsDateTextView.setText(date);
        detailsNameTextView.setText(me.getUserName());

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

        EditText patientDetailsTotalTextView = findViewById(R.id.patientDetailsTotalTextView);
        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            showError(getApplicationContext().getString(R.string.please_select_a_date), findViewById(R.id.detailsMonthTextView));
            return;
        }
        if (ZValidation.checkEmpty(patientDetailsTotalTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_total_patients), findViewById(R.id.detailsMonthTextView));
            patientDetailsTotalTextView.requestFocus();
            return;

        }

        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month",month);
            jsonObject.put("TotalPatients", Utils.getText(patientDetailsTotalTextView));
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);
            new CreatePatientTask(context).execute(jArray.toString());

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
