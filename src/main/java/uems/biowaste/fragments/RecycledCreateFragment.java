package uems.biowaste.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import uems.biowaste.R;
import uems.biowaste.async.CreateRecycledTask;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class RecycledCreateFragment extends Fragment implements View.OnClickListener {

    String month;
    String date;
    private Calendar startDate;
    public UserVo me;

    private RecycledCreateFragment.OnFragmentInteractionListener mListener;

    TextView tvMonth ;
    TextView detailsMonthTextView ;
    TextView detailsDateTextView ;
    TextView detailsNameTextView ;

    EditText itemDisposalPlasticTextView ;
    EditText itemDisposalCansTextView ;
    EditText itemDisposalPaperTextView ;
    EditText itemDisposalCarbonBoxTextView;
    TextView itemDisposalTotalTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecycledCreateFragment.OnFragmentInteractionListener) {
            mListener = (RecycledCreateFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void  startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycle_item_disposal_details, container, false);
        me = Utils.getUser(getContext());
        startDate = Calendar.getInstance();
        initLayout(view);
        return view;
    }


    public void initLayout(View view) {

        detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        detailsNameTextView = view.findViewById(R.id.detailsNameTextView);
        tvMonth =  view.findViewById(R.id.detailsMonthTextView);
        itemDisposalPlasticTextView = view.findViewById(R.id.itemDisposalPlasticTextView);
        itemDisposalCansTextView = view. findViewById(R.id.itemDisposalCansTextView);
        itemDisposalPaperTextView = view.findViewById(R.id.itemDisposalPaperTextView);
        itemDisposalCarbonBoxTextView = view.findViewById(R.id.itemDisposalCarbonBoxTextView);
        itemDisposalTotalTextView = view.findViewById(R.id.itemDisposalTotalTextView);


        view.findViewById(R.id.detailsSubmitButton).setOnClickListener(this);
        detailsMonthTextView.setOnClickListener(this);
        detailsDateTextView.setOnClickListener(this);
        String monthName = (String) android.text.format.DateFormat.format("MMMM", startDate.getTime());
          month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        date = DateUtil.dateToString(startDate.getTime(), DateUtil.DATE_START_DATE);
        detailsMonthTextView.setText(monthName);
        detailsDateTextView.setText(date);
        detailsNameTextView.setText(me.getUserName());
        itemDisposalCarbonBoxTextView.requestFocus();

        itemDisposalPlasticTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0;
                double cans = 0;
                double paper = 0;
                double box = 0;
                if(s!=null&&s.length()>0)
                    plastic=   Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                itemDisposalTotalTextView.setText(String.format("%s", plastic + cans + paper + box));

            }
        });
        itemDisposalCansTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0;
                double cans = 0;
                double paper = 0;
                double box = 0;
                if(s!=null&&s.length()>0)
                    cans=   Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                itemDisposalTotalTextView.setText((plastic + cans+paper+box) + "");

            }
        });

        itemDisposalPaperTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0;
                double cans = 0;
                double paper = 0;
                double box = 0;
                if(s!=null&&s.length()>0)
                    paper=   Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                itemDisposalTotalTextView.setText((plastic + cans+paper+box) + "");

            }
        });

        itemDisposalCarbonBoxTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0;
                double cans = 0;
                double paper = 0;
                double box = 0;
                if(s!=null&&s.length()>0)
                    box=   Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                itemDisposalTotalTextView.setText((plastic + cans+paper+box) + "");

            }
        });
    }


    public void saveResponse(TResponse<String> result) {

        if (result == null) {
            Utils.showError(" please check network connection", detailsDateTextView);
        } else if (result.isHasError()) {
            Utils.showError("please try later", detailsDateTextView);

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                boolean status = jsonObject.getBoolean("status");
                if (status) {
                    Toast.makeText(getContext(), "Successfully saved item", Toast.LENGTH_SHORT).show();
                } else {
                    Utils.showError("Failed to save item", detailsDateTextView);
                }

            } catch (Exception e) {
                Utils.showError("please try later", detailsDateTextView);
                Log.e("parse order", e.toString());
            }
        }
    }


    public void showMonthMenu(View v) {
        String month = (String) android.text.format.DateFormat.format("M", new Date());
        int monthValue = Integer.parseInt(month)-1;


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
            jsonObject.put("Month", month);
            jsonObject.put("CartonBox", Utils.getText(itemDisposalCarbonBoxTextView));
            jsonObject.put("Paper", Utils.getText(itemDisposalPaperTextView));
            jsonObject.put("Cans", Utils.getText(itemDisposalCansTextView));
            jsonObject.put("Plastic", Utils.getText(itemDisposalPlasticTextView));
            jsonObject.put("TotalWeight", Utils.getText(itemDisposalTotalTextView));
            jsonObject.put("UserEmailID", me.getEmailID());
               jArray.put(jsonObject);
            new CreateRecycledTask(getContext()).execute(jArray.toString());

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
