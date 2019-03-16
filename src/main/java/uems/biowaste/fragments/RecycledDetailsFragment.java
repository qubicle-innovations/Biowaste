package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uems.biowaste.R;
import uems.biowaste.async.CreateRecycledTask;
import uems.biowaste.async.DeleteTask;
import uems.biowaste.async.FetchBioWasteCreateTask;
import uems.biowaste.async.FetchRecycledDetailsTask;
import uems.biowaste.async.UpdateRecycledTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class RecycledDetailsFragment extends Fragment implements View.OnClickListener{

    private ItemVo vo;
    public UserVo me;
    private RecycledDetailsFragment.OnFragmentInteractionListener mListener;
    String month;
    private Calendar startDate;

    Button detailsSubmitButton;

    TextView detailsMonthTextView;
    TextView detailsDateTextView;
    TextView detailsNameTextView ;
    TextView itemDisposalTotalTextView;

    EditText itemDisposalPlasticTextView;
    EditText itemDisposalCansTextView;
    EditText itemDisposalPaperTextView;
    EditText itemDisposalCarbonBoxTextView;
    Button deleteButtonRecycle,editButtonRecycle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecycledDetailsFragment.OnFragmentInteractionListener) {
            mListener = (RecycledDetailsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void  startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
        void  popupFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_item_disposal_details, container, false);
        me = Utils.getUser(getContext());
        startDate = Calendar.getInstance();
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        if(getArguments() != null)
            vo = (ItemVo) getArguments().getSerializable("vo");
        initLayout(view);
        new FetchRecycledDetailsTask(getContext()).execute(vo.getItemID(),me.getEmailID());
        return view;
    }


    public void initLayout(View view) {

        detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        detailsNameTextView = view.findViewById(R.id.detailsNameTextView);
        itemDisposalTotalTextView = view.findViewById(R.id.itemDisposalTotalTextView);

        itemDisposalPlasticTextView = view.findViewById(R.id.itemDisposalPlasticTextView);
        itemDisposalCansTextView = view.findViewById(R.id.itemDisposalCansTextView);
        itemDisposalPaperTextView = view.findViewById(R.id.itemDisposalPaperTextView);
        itemDisposalCarbonBoxTextView = view.findViewById(R.id.itemDisposalCarbonBoxTextView);

        editButtonRecycle = view.findViewById(R.id.editButtonRecycle);
        deleteButtonRecycle = view.findViewById(R.id.deleteButtonRecycle);
        detailsSubmitButton = view.findViewById(R.id.detailsSubmitButton);


        detailsSubmitButton.setVisibility(View.GONE);
        deleteButtonRecycle.setVisibility(View.GONE);
        editButtonRecycle.setVisibility(View.GONE);

        editButtonRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDisposalPlasticTextView.setFocusableInTouchMode(true);
                itemDisposalCansTextView.setFocusableInTouchMode(true);
                itemDisposalPaperTextView.setFocusableInTouchMode(true);
                itemDisposalCarbonBoxTextView.setFocusableInTouchMode(true);
                itemDisposalPlasticTextView.setFocusable(true);
                itemDisposalCansTextView.setFocusable(true);
                itemDisposalPaperTextView.setFocusable(true);
                itemDisposalCarbonBoxTextView.setFocusable(true);

                editButtonRecycle.setVisibility(View.GONE);
                deleteButtonRecycle.setVisibility(View.GONE);
                detailsSubmitButton.setVisibility(View.VISIBLE);
            }
        });

        detailsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        deleteButtonRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });


        itemDisposalPlasticTextView.setFocusable(false);
        itemDisposalCansTextView.setFocusable(false);
        itemDisposalPaperTextView.setFocusable(false);
        itemDisposalCarbonBoxTextView.setFocusable(false);

        setData();
    }

    public void deleteRecord(){
        JSONArray jArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            jsonObject.put("Month", Utils.getText(detailsMonthTextView));
            jsonObject.put("Type", Constants.FRAGMENT_RECYCLED_ITEMS_TYPE_ID);
            jArray.put(jsonObject);
            new DeleteTask(getContext()).execute(jArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void setData(){
        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());

        itemDisposalPlasticTextView.setText(vo.getPlastic() );
        itemDisposalCansTextView.setText(vo.getCans());
        itemDisposalPaperTextView.setText(vo.getPaper());
        itemDisposalCarbonBoxTextView.setText(vo.getCartonBox());
        itemDisposalTotalTextView.setText(String.format("%sKg", vo.getTotalWeight()));
        if(vo.getCreatedBy().equals(me.getUserName())){
            editButtonRecycle.setVisibility(View.VISIBLE);
            deleteButtonRecycle.setVisibility(View.VISIBLE);
        }
    }

    public void detailsResponse(TResponse<String> result) {

        if (result == null) {
            Utils.showError(" please check network connection", detailsDateTextView);
        } else if (result.isHasError()) {
            Utils.showError("please try later", detailsDateTextView);

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetRecycleditemsDetails");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                vo = mapper.readValue(jsonArray.getJSONObject(0).toString(), new TypeReference<ItemVo>() {
                });
                setData();

            } catch (Exception e) {
                Utils.showError("please try later", detailsDateTextView);
                Log.e("parse order", e.toString());
            }
        }
    }


    public void showMonthMenu(View v) {
        String month = (String) android.text.format.DateFormat.format("M", new Date());
        int monthValue = Integer.parseInt(month)-1;

        if(getContext() != null){
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.getMenu().add("Select");
            if ((monthValue - 1) > 0) {
                popup.getMenu().add(Utils.getMonths(monthValue - 1));

            } else
                popup.getMenu().add(Utils.getMonths(12));

            popup.getMenu().add(Utils.getMonths(monthValue));


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    detailsMonthTextView.setText(item.getTitle());
                    detailsDateTextView.setText(getText(R.string.select));
                    return false;
                }
            });
            popup.show();
        }
    }

    public void updated(){
        if(getContext() != null){
            Toast.makeText(getContext(),getText(R.string.updated),Toast.LENGTH_SHORT).show();
            mListener.popupFragment(new RecycledListFragment(), Constants.FRAGMENT_RECYCLED_ITEMS,false,true);
        }
    }


    public void showStartDate() {

        int mYear = startDate.get(Calendar.YEAR);
        int mMonth = startDate.get(Calendar.MONTH);
        int mDay = startDate.get(Calendar.DAY_OF_MONTH);


        if(getContext() != null){
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

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
    }


    public void saveItem() {


        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            Utils.showError("Please select a date", detailsMonthTextView);
            return;
        }
        if (ZValidation.checkEmpty(itemDisposalPlasticTextView)) {
            Utils.showError("Please enter plastic weight", detailsMonthTextView);
            itemDisposalPlasticTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemDisposalCansTextView)) {
            Utils.showError("Please enter cans weight", detailsMonthTextView);
            itemDisposalCansTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemDisposalPaperTextView)) {
            Utils.showError("Please enter paper weight", detailsMonthTextView);
            itemDisposalPaperTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemDisposalCarbonBoxTextView)) {
            Utils.showError("Please enter carton box weight", detailsMonthTextView);
            itemDisposalCarbonBoxTextView.requestFocus();
            return;

        }
        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            if(getMonth(Utils.getText(detailsMonthTextView)).isEmpty()){
                jsonObject.put("Month",month);
            }else{
                jsonObject.put("Month", getMonth(Utils.getText(detailsMonthTextView)));
            }

            jsonObject.put("CartonBox", Utils.getText(itemDisposalCarbonBoxTextView));
            jsonObject.put("Paper", Utils.getText(itemDisposalPaperTextView));
            jsonObject.put("Cans", Utils.getText(itemDisposalCansTextView));
            jsonObject.put("Plastic", Utils.getText(itemDisposalPlasticTextView));
            jsonObject.put("TotalWeight", Utils.getText(itemDisposalTotalTextView).replace("KgKg",""));
            jsonObject.put("UserEmailID", me.getEmailID());
            jArray.put(jsonObject);
            new UpdateRecycledTask(getContext()).execute(jArray.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }





    /*    SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputFormat.parse(item.getTitle()));
        SimpleDateFormat outputFormat = new SimpleDateFormat("M"); // 01-12
        println(outputFormat.format(cal.getTime()));*/
    }


    public String getMonth(String month){
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return (cal.get(Calendar.MONTH)+1)+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
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
