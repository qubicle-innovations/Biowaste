package uems.biowaste;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import uems.biowaste.adapter.PatientListAdapter;
import uems.biowaste.async.FetchPatientListTask;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;

public class PatientListActivity extends BaseActivity {
    private Calendar startDate;
    private String date = "";
    private ScheduledExecutorService scheduleTaskExecutor;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gwaste);
        startDate = Calendar.getInstance();
        initLayout();
    }


    public void initLayout() {

        new FetchPatientListTask(context).execute(new String[]{date, me.getEmailID(), ""});
        findViewById(R.id.tvMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthMenu(v);
            }
        });
        initNavigationMenu("Food & General waste");
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                new FetchPatientListTask(context).execute(new String[]{date, me.getEmailID(), ""});


            }
        });
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
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
                        date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
                        TextView textView = (TextView) findViewById(R.id.tvDate);
                        textView.setText(date);
                        new FetchPatientListTask(context).execute(new String[]{date, me.getEmailID(), ""});


                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }


    public void listResponse(TResponse<String> result) {

        ((SwipeRefreshLayout) findViewById(R.id.swiperefresh)).setRefreshing(false);

        if (result == null) {
            showError(" please check network connection", findViewById(R.id.listView));
        } else if (result.isHasError()) {
            showError("please try later", findViewById(R.id.listView));

        } else if (result.getResponseContent() != null) {
            ListView listView = (ListView) findViewById(R.id.listView);
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListMonthlypatients");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                List<ItemVo> ad = mapper.readValue(jsonArray.toString(), new TypeReference<List<ItemVo>>() {
                });

                if (ad != null && ad != null && !ad.isEmpty()) {

                    final PatientListAdapter adapter = new PatientListAdapter(context, (ArrayList<ItemVo>) ad);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Intent intent = new Intent(context,IssueDetailsActivity.class);
//                            intent.putExtra("vo",adapter.getProduct(position));
//                            startActivity(intent);
                        }
                    });
                } else {
                    PatientListAdapter adapter = new PatientListAdapter(context, new ArrayList<ItemVo>());
                    listView.setAdapter(adapter);
                    showError("No record found", findViewById(R.id.listView));
                }


            } catch (Exception e) {
                showError("please try later", findViewById(R.id.listView));

                Log.e("parse order", e.toString());
            }
        }
    }

    public void showQRCodeScanner() {

        //  Intent i = new Intent(this, SimpleScannerActivity.class);
        //startActivityForResult(i, 1);

    }


    public void checkPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                showQRCodeScanner();
                //   Toast.makeText(GoValetApplication.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //  Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,App may not work properly\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("setting")
                .setPermissions(android.Manifest.permission.INTERNET, android.Manifest.permission.CAMERA, android.Manifest.permission.WAKE_LOCK, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    public void timerTask() {
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                PatientListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.haveNetworkConnection(context)) {
                            new FetchPatientListTask(context).execute(new String[]{date, me.getEmailID(), ""});
                        }
                    }
                });
                // If you need update UI, simply do this:

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


    public void showMonthMenu(View v) {

        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenu().add("Select");
        popup.getMenu().add("January");
        popup.getMenu().add("February");
        popup.getMenu().add("March");
        popup.getMenu().add("April");
        popup.getMenu().add("May");
        popup.getMenu().add("June");
        popup.getMenu().add("July");
        popup.getMenu().add("August");
        popup.getMenu().add("September");
        popup.getMenu().add("October");
        popup.getMenu().add("November");
        popup.getMenu().add("December");

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                TextView tvMonth = (TextView) findViewById(R.id.tvMonth);
                tvMonth.setText(item.getTitle());
                if (item.getTitle().toString().equalsIgnoreCase("select"))
                    date = "";
                else
                    date = item.getTitle().toString();
                new FetchPatientListTask(context).execute(new String[]{date, me.getEmailID(), ""});

                return false;
            }
        });
        popup.show();
    }
}
