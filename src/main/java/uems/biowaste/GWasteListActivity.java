package uems.biowaste;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import uems.biowaste.adapter.GwasteListAdapter;
import uems.biowaste.async.FetchGWasteListTask;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;

public class GWasteListActivity extends BaseActivity {
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

    @Override
    protected void onResume() {
        super.onResume();
        new FetchGWasteListTask(context).execute(new String[]{date, me.getEmailID(), ""});

    }

    public void initLayout() {

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
                startActivity(new Intent(context,GwasteCreateActivity.class));

            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                new FetchGWasteListTask(context).execute(new String[]{date, me.getEmailID(), ""});


            }
        });
        EditText etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() < 1) {
                    date="";
                    new FetchGWasteListTask(context).execute(new String[]{date, me.getEmailID(), ""});

                } else if (s.length() > 1) {
                    date=s.toString();
                    new FetchGWasteListTask(context).execute(new String[]{date, me.getEmailID(), ""});

                }

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
                        new FetchGWasteListTask(context).execute(new String[]{date, me.getEmailID(), ""});


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
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetFoodandGeneralwaste");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                List<ItemVo> ad = mapper.readValue(jsonArray.toString(), new TypeReference<List<ItemVo>>() {
                });

                if (ad != null && ad != null && !ad.isEmpty()) {

                    final GwasteListAdapter adapter = new GwasteListAdapter(context, (ArrayList<ItemVo>) ad,ContextCompat.getColor(context, R.color.orange));
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(context,GwasteDetailsActivity.class);
                            intent.putExtra("vo",adapter.getProduct(position));
                            startActivity(intent);
                        }
                    });
                } else {
                    GwasteListAdapter adapter = new GwasteListAdapter(context, new ArrayList<ItemVo>(), ContextCompat.getColor(context, R.color.orange));
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




    public void timerTask() {
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                GWasteListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.haveNetworkConnection(context)) {
                            new FetchGWasteListTask(context).execute(new String[]{date, me.getEmailID(), ""});
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
                new FetchGWasteListTask(context).execute(new String[]{date, me.getEmailID(), ""});

                return false;
            }
        });
        popup.show();
    }
}
