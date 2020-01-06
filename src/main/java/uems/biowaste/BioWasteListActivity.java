package uems.biowaste;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

import uems.biowaste.adapter.BiowasteListAdapter;
import uems.biowaste.async.FetchBioWasteListTask;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.BioWasteItemVo;
import uems.biowaste.vo.TResponse;

public class BioWasteListActivity extends BaseActivity {

    private Calendar startDate;
    private String date = "";
    private ScheduledExecutorService scheduleTaskExecutor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int visibleThreshold = 10;
    private String lastItem = "0";
    private int previousTotal = 0;
    private boolean loading = true;
    private  BiowasteListAdapter adapter;

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
        adapter=null;
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.imSearch).setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        new FetchBioWasteListTask(context).execute(date, me.getEmailID(), "0");

    }

    public void initLayout() {

        findViewById(R.id.tvMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthMenu(v);
            }
        });
        initNavigationMenu(getApplicationContext().getString(R.string.biowaste));
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,BiowasteCreateActivity.class));
            }
        });
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                findViewById(R.id.imSearch).setVisibility(View.INVISIBLE);

                new FetchBioWasteListTask(context).execute(date, me.getEmailID(), lastItem);


            }
        });
        EditText etSearch = findViewById(R.id.etSearch);
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
                     adapter=null;
                     findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                     findViewById(R.id.imSearch).setVisibility(View.INVISIBLE);
                     new FetchBioWasteListTask(context).execute(date, me.getEmailID(), "0");

                } else if (s.length() > 1) {
                         date=s.toString();
                         adapter=null;
                     findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                     findViewById(R.id.imSearch).setVisibility(View.INVISIBLE);
                     new FetchBioWasteListTask(context).execute(date, me.getEmailID(), "0");

                }

            }
        });
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        timerTask();
    }



    public void listResponse(TResponse<String> result) {

        ((SwipeRefreshLayout) findViewById(R.id.swiperefresh)).setRefreshing(false);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        findViewById(R.id.imSearch).setVisibility(View.VISIBLE);

        if (result == null) {
            showError(getApplicationContext().getString(R.string.please_check_network_connection), findViewById(R.id.listView));
        } else if (result.isHasError()) {
            showError(getApplicationContext().getString(R.string.please_try_later), findViewById(R.id.listView));

        } else if (result.getResponseContent() != null) {
            ListView listView = findViewById(R.id.listView);
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetBiowaste");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                List<BioWasteItemVo> ad = mapper.readValue(jsonArray.toString(), new TypeReference<List<BioWasteItemVo>>() {
                });

                if (ad != null && ad != null && !ad.isEmpty()) {
                    if(adapter==null) {
                        previousTotal = 0;
                        adapter = new BiowasteListAdapter(context, (ArrayList<BioWasteItemVo>) ad);
                        listView.setAdapter(adapter);
                        listView.setOnScrollListener(new EndlessScrollListener());

                    }else {
                        if(!lastItem.equalsIgnoreCase(ad.get(ad.size()-1).getItemID()))
                            adapter.addItems((ArrayList<BioWasteItemVo>) ad);
                    }
                    lastItem = ad.get(ad.size()-1).getItemID();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(context,BiowasteDetailsActivity.class);
                            intent.putExtra("vo",adapter.getProduct(position));
                            startActivity(intent);
                        }
                    });
                }else if(ad != null && ad.isEmpty()&&adapter!=null){

                } else {
                    previousTotal = 0;
                    BiowasteListAdapter adapter = new BiowasteListAdapter(context, new ArrayList<BioWasteItemVo>());
                    listView.setAdapter(adapter);
                    showError(getApplicationContext().getString(R.string.no_records_found), findViewById(R.id.listView));
                }


            } catch (Exception e) {
                showError(getApplicationContext().getString(R.string.please_try_later), findViewById(R.id.listView));

                Log.e("parse order", e.toString());
            }
        }
    }

    public void showQRCodeScanner() {

        //  Intent i = new Intent(this, SimpleScannerActivity.class);
        //startActivityForResult(i, 1);

    }


   /* public void checkPermissions() {
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
*/

    public void timerTask() {
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                BioWasteListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.haveNetworkConnection(context)) {
                          //  new FetchBioWasteListTask(context).execute(new String[]{date, me.getEmailID(), lastItem});
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
                TextView tvMonth = findViewById(R.id.tvMonth);
                tvMonth.setText(item.getTitle());
                if (item.getTitle().toString().equalsIgnoreCase("select"))
                    date = "";
                else
                    date = item.getTitle().toString();
                adapter=null;
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                findViewById(R.id.imSearch).setVisibility(View.INVISIBLE);
                new FetchBioWasteListTask(context).execute(date, me.getEmailID(), "0");

                return false;
            }
        });
        popup.show();
    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        public EndlessScrollListener() {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                if(adapter!=null) {
                    lastItem=adapter.getItem(adapter.getCount()-1).getItemID();
                    findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    findViewById(R.id.imSearch).setVisibility(View.INVISIBLE);
                    new FetchBioWasteListTask(context).execute(date, me.getEmailID(),lastItem);
                }

                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
}
