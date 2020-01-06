package uems.biowaste.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import java.util.Calendar;
import java.util.List;
import uems.biowaste.R;
import uems.biowaste.adapter.GwasteListAdapter;
import uems.biowaste.async.FetchBioWasteListTask;
import uems.biowaste.async.FetchGWasteListTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class GWasteListFragment extends Fragment {
    
    private String date = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    private String lastItem = "0";
    private int previousTotal = 0;
    private boolean loading = true;
    private GwasteListAdapter adapter;

    public UserVo me;
    private GWasteListFragment.OnFragmentInteractionListener mListener;
    ProgressBar progressBar;
    ImageView imSearch;
    ListView listView;
    TextView tvMonth ;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GWasteListFragment.OnFragmentInteractionListener) {
            mListener = (GWasteListFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void reload(Context context){
        me = Utils.getUser(context);
        String date = DateUtil.dateToString(Calendar.getInstance().getTime(), DateUtil.DATE_START_DATE);
        new FetchGWasteListTask(context).execute(date, me.getEmailID(), "0");
    }


    public interface OnFragmentInteractionListener {
        void  startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
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
        new FetchGWasteListTask(getContext()).execute(date, me.getEmailID(), "0");

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
                mListener.startFragment(new GwasteCreateFragments(), Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_CREATE,true,true);
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                progressBar.setVisibility(View.VISIBLE);
                imSearch.setVisibility(View.INVISIBLE);
                new FetchGWasteListTask(getContext()).execute(date, me.getEmailID(), lastItem);


            }
        });
        EditText etSearch = view.findViewById(R.id.etSearch);
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
                    new FetchGWasteListTask(getContext()).execute(date, me.getEmailID(), "0");

                } else if (s.length() > 1) {
                    date=s.toString();
                    adapter=null;
                    progressBar.setVisibility(View.VISIBLE);
                    imSearch.setVisibility(View.INVISIBLE);
                    new FetchGWasteListTask(getContext()).execute(date, me.getEmailID(), "0");

                }

            }
        });
    }




    public void listResponse(TResponse<String> result) {

        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.INVISIBLE);
        imSearch.setVisibility(View.VISIBLE);

        if (result == null) {
            Utils.showError(getContext().getString(R.string.please_check_network_connection), listView);
        } else if (result.isHasError()) {
            Utils.showError(getContext().getString(R.string.please_try_later), listView);

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetFoodandGeneralwaste");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                List<ItemVo> ad = mapper.readValue(jsonArray.toString(), new TypeReference<List<ItemVo>>() {
                });

                if (ad != null  && !ad.isEmpty()) {
                    if(adapter==null) {
                        if(getContext() != null){
                            previousTotal = 0;
                            adapter = new GwasteListAdapter(getContext(), (ArrayList<ItemVo>) ad,ContextCompat.getColor(getContext(), R.color.orange));
                            listView.setAdapter(adapter);
                            listView.setOnScrollListener(new EndlessScrollListener());
                        }

                    }else {
                        if(!lastItem.equalsIgnoreCase(ad.get(ad.size()-1).getItemID()))
                             adapter.addItems((ArrayList<ItemVo>) ad);
                    }
                    lastItem = ad.get(ad.size()-1).getItemID();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Fragment fragment = new GwasteDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("vo",adapter.getProduct(position));
                            fragment.setArguments(bundle);
                            mListener.startFragment(fragment,Constants.FRAGMENT_FOOD_AND_GENERAL_WASTE_DETAILS,true,true);
                        }
                    });
                }
                else if(ad != null &&adapter!=null){

                }
                else if(getContext() != null){
                    previousTotal = 0;
                    GwasteListAdapter adapter = new GwasteListAdapter(getContext(), new ArrayList<ItemVo>(), ContextCompat.getColor(getContext(), R.color.orange));
                    listView.setAdapter(adapter);
                    Utils.showError(getContext().getString(R.string.no_records_found), listView);
                }


            } catch (Exception e) {
                Utils.showError(getContext().getString(R.string.please_try_later), listView);

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

    public void showMonthMenu(View v) {
        if(getContext() != null){
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenu().add(getContext().getString(R.string.select));
            popup.getMenu().add(getContext().getString(R.string.january));
            popup.getMenu().add(getContext().getString(R.string.february));
            popup.getMenu().add(getContext().getString(R.string.march));
            popup.getMenu().add(getContext().getString(R.string.april));
            popup.getMenu().add(getContext().getString(R.string.may));
            popup.getMenu().add(getContext().getString(R.string.june));
            popup.getMenu().add(getContext().getString(R.string.july));
            popup.getMenu().add(getContext().getString(R.string.august));
            popup.getMenu().add(getContext().getString(R.string.september));
            popup.getMenu().add(getContext().getString(R.string.october));
            popup.getMenu().add(getContext().getString(R.string.november));
            popup.getMenu().add(getContext().getString(R.string.december));

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
                    new FetchGWasteListTask(getContext()).execute(date, me.getEmailID(), "0");

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
                    new FetchGWasteListTask(getContext()).execute(date, me.getEmailID(), lastItem);
                }

                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
}
