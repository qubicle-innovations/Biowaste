package uems.biowaste.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uems.biowaste.R;
import uems.biowaste.adapter.BiowasteListAdapter;
import uems.biowaste.async.FetchBioWasteListTask;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.BioWasteItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class BioWasteListFragment extends Fragment {

    private String date = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    private String lastItem = "0";
    private int previousTotal = 0;
    private boolean loading = true;
    private  BiowasteListAdapter adapter;
    ProgressBar progressBar;
    ImageView imSearch;
    ListView listView;
    TextView tvMonth ;

    public UserVo me;
    private PatientListFragment.OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PatientListFragment.OnFragmentInteractionListener) {
            mListener = (PatientListFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void  startFragment(String fragmentName,boolean addToBackStack,boolean isAdd);
        void  startFragment(Fragment fragment,boolean addToBackStack,boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gwaste, container, false);
        me = Utils.getUser(getContext());
        initLayout(view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter=null;
        if(progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        if(imSearch != null)
            imSearch.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        new FetchBioWasteListTask(getActivity()).execute(date, me.getEmailID(), "0");

    }

    public void initLayout(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        imSearch = view.findViewById(R.id.imSearch);
        tvMonth = view.findViewById(R.id.tvMonth);
        listView = view.findViewById(R.id.listView);

        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthMenu(v);
            }
        });
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(getContext(), BiowasteCreateFragment.class));
            }
        });

        swipeRefreshLayout =  view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                progressBar.setVisibility(View.VISIBLE);
                imSearch.setVisibility(View.INVISIBLE);

                new FetchBioWasteListTask(getContext()).execute(date, me.getEmailID(), lastItem);


            }
        });
        EditText etSearch =  view.findViewById(R.id.etSearch);
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
                     progressBar.setVisibility(View.VISIBLE);
                     imSearch.setVisibility(View.INVISIBLE);
                     new FetchBioWasteListTask(getContext()).execute(date, me.getEmailID(), "0");

                } else if (s.length() > 1) {
                         date=s.toString();
                         adapter=null;
                     progressBar.setVisibility(View.VISIBLE);
                     imSearch.setVisibility(View.INVISIBLE);
                     new FetchBioWasteListTask(getContext()).execute(date, me.getEmailID(), "0");

                }

            }
        });
    }



    public void listResponse(TResponse<String> result) {

        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.INVISIBLE);
        imSearch.setVisibility(View.VISIBLE);

        if (result == null) {
            Utils.showError(" please check network connection", listView);
        } else if (result.isHasError()) {
            Utils.showError("please try later", listView);

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetBiowaste");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                List<BioWasteItemVo> ad = mapper.readValue(jsonArray.toString(), new TypeReference<List<BioWasteItemVo>>() {
                });

                if (ad != null && !ad.isEmpty()) {
                    if(adapter==null) {
                        if(getContext() != null){
                            previousTotal = 0;
                            adapter = new BiowasteListAdapter(getContext(), (ArrayList<BioWasteItemVo>) ad);
                            listView.setAdapter(adapter);
                            listView.setOnScrollListener(new EndlessScrollListener());
                        }

                    }else {
                        if(!lastItem.equalsIgnoreCase(ad.get(ad.size()-1).getItemID()))
                            adapter.addItems((ArrayList<BioWasteItemVo>) ad);
                    }
                    lastItem = ad.get(ad.size()-1).getItemID();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), BiowasteDetailsFragment.class);
                            intent.putExtra("vo",adapter.getProduct(position));
                            startActivity(intent);
                        }
                    });
                }/*
                else if(ad != null && ad.isEmpty()&&adapter!=null){

                } */else if(getContext() != null){
                    previousTotal = 0;
                    BiowasteListAdapter adapter = new BiowasteListAdapter(getContext(), new ArrayList<BioWasteItemVo>());
                    listView.setAdapter(adapter);
                    Utils.showError("No record found", listView);
                }


            } catch (Exception e) {
                Utils.showError("please try later", listView);

                Log.e("parse order", e.toString());
            }
        }
    }

/*
    public void showQRCodeScanner() {

        //  Intent i = new Intent(this, SimpleScannerActivity.class);
        //startActivityForResult(i, 1);

    }
*/


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



    public void showMonthMenu(View v) {

        if(getContext() != null){
            PopupMenu popup = new PopupMenu(getContext(), v);
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
                    tvMonth.setText(item.getTitle());
                    if (item.getTitle().toString().equalsIgnoreCase("select"))
                        date = "";
                    else
                        date = item.getTitle().toString();
                    adapter=null;
                    progressBar.setVisibility(View.VISIBLE);
                    imSearch.setVisibility(View.INVISIBLE);
                    new FetchBioWasteListTask(getContext()).execute(date, me.getEmailID(), "0");

                    return false;
                }
            });
            popup.show();
        }

    }


    public class EndlessScrollListener implements AbsListView.OnScrollListener {


        EndlessScrollListener() {
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
            int visibleThreshold = 10;
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                if(adapter!=null) {
                    lastItem=adapter.getItem(adapter.getCount()-1).getItemID();
                    progressBar.setVisibility(View.VISIBLE);
                    imSearch.setVisibility(View.INVISIBLE);
                    new FetchBioWasteListTask(getContext()).execute(date, me.getEmailID(),lastItem);
                }

                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
}
