package uems.biowaste;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import uems.biowaste.async.FetchCountTask;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.TResponse;

public class Dashboard extends BaseActivity implements View.OnClickListener {

    public ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    private Calendar startDate;
    private ScheduledExecutorService scheduleTaskExecutor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        startDate = Calendar.getInstance();
        initLayout();

    }

    public void initLayout() {

        String date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
        new FetchCountTask(context).execute(new String[]{date, me.getEmailID()});
        findViewById(R.id.itemTextViewMonth).setVisibility(View.INVISIBLE);
        findViewById(R.id.rlBioWaste).setOnClickListener(this);
        findViewById(R.id.rlRecycle).setOnClickListener(this);
        findViewById(R.id.rlPatients).setOnClickListener(this);
        findViewById(R.id.rlGwaste).setOnClickListener(this);
        initNavigationMenu(null);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        timerTask();
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
                        startDate.set(year, monthOfYear, dayOfMonth);
                        String date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
                        TextView textView = (TextView) findViewById(R.id.tvDate);
                        textView.setText(date);
                        new FetchCountTask(context).execute(new String[]{date, me.getEmailID()});

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void countResponse(TResponse<String> result) {

        TextView tvBioCount = (TextView) findViewById(R.id.tvBioCount);
        TextView tvPatientsCount = (TextView) findViewById(R.id.tvPatientsCount);
        TextView tvRecycleCount = (TextView) findViewById(R.id.tvRecycleCount);
        TextView tvGwasteCount = (TextView) findViewById(R.id.tvGwasteCount);

        if (result == null) {
            showError(" please check network connection", findViewById(R.id.tvGwasteCount));
        } else if (result.isHasError()) {
            showError("please try later", findViewById(R.id.tvGwasteCount));

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("MonthlyDetailList");
                if (jsonArray.length() > 0) {
                    JSONObject jObject = jsonArray.getJSONObject(0);
                    if(jObject.getString("TotalCostBW").equalsIgnoreCase(""))
                        tvBioCount.setText("0");
                    else
                        tvBioCount.setText(jObject.getString("TotalCostBW"));

                    if(jObject.getString("TotalPatientsMP").equalsIgnoreCase(""))
                        tvPatientsCount.setText("0");
                    else
                        tvPatientsCount.setText(jObject.getString("TotalPatientsMP"));

                    if(jObject.getString("TotalWeightRI").equalsIgnoreCase(""))
                        tvRecycleCount.setText("0");
                    else
                        tvRecycleCount.setText(jObject.getString("TotalWeightRI"));

                    if(jObject.getString("TotalWeightFW").equalsIgnoreCase(""))
                        tvGwasteCount.setText("0");
                    else
                        tvGwasteCount.setText(jObject.getString("TotalWeightFW"));

                }


            } catch (Exception e) {
                showError("please try later", findViewById(R.id.tvGwasteCount));

                Log.e("parse order", e.toString());
            }
        }
    }


    public void showReturnList() {
        //   startActivity(new Intent(context, ReturnListActivity.class));
        //  finish();
    }

    public void showIssueList() {
        //  startActivity(new Intent(context, IssueListActivity.class));
        // finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvDate:
                showStartDate();
                break;
            case R.id.rlBioWaste:
                startActivity(new Intent(context, BioWasteListActivity.class));
                finish();
                break;
            case R.id.rlGwaste:
                startActivity(new Intent(context, GWasteListActivity.class));
                finish();
                break;
            case R.id.rlPatients:
                startActivity(new Intent(context, PatientListActivity.class));
                finish();
                break;
            case R.id.rlRecycle:
                startActivity(new Intent(context, RecycledListActivity.class));
                finish();
                break;
        }
    }

    public void timerTask() {
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Dashboard.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.haveNetworkConnection(context)) {
                            String date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
                            new FetchCountTask(context).execute(new String[]{date, me.getEmailID()});
                        }
                    }
                });

            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    protected void onDestroy() {
        if (scheduleTaskExecutor != null) {
            scheduleTaskExecutor.shutdownNow();
        }

        super.onDestroy();

    }
}
