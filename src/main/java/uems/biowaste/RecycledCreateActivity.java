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

import uems.biowaste.async.CreateRecycledTask;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.TResponse;

public class RecycledCreateActivity extends BaseBackActivity implements View.OnClickListener {

    String month;
    String date;
    private Calendar startDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recycle_item_disposal_details);
        setToolbar(getApplicationContext().getString(R.string.recycled_items_disposed));
        startDate = Calendar.getInstance();
        initLayout();

    }

    public void initLayout() {

        TextView detailsMonthTextView = findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView = findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView = findViewById(R.id.detailsNameTextView);

        final EditText itemDisposalPlasticTextView = findViewById(R.id.itemDisposalPlasticTextView);
        final EditText itemDisposalCansTextView = findViewById(R.id.itemDisposalCansTextView);
        final EditText itemDisposalPaperTextView = findViewById(R.id.itemDisposalPaperTextView);
        final EditText itemDisposalCarbonBoxTextView = findViewById(R.id.itemDisposalCarbonBoxTextView);


        findViewById(R.id.detailsSubmitButton).setOnClickListener(this);
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
                TextView itemDisposalTotalTextView = findViewById(R.id.itemDisposalTotalTextView);

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
                itemDisposalTotalTextView.setText( Utils.roundOff(plastic + cans+paper+box));

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
                TextView itemDisposalTotalTextView = findViewById(R.id.itemDisposalTotalTextView);

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
                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans+paper+box));

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
                TextView itemDisposalTotalTextView = findViewById(R.id.itemDisposalTotalTextView);

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
                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans+paper+box));

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
                TextView itemDisposalTotalTextView = findViewById(R.id.itemDisposalTotalTextView);

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
                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans+paper+box));

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
                if (status) {
                    Toast.makeText(context, getApplicationContext().getString(R.string.successfully_saved_item), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
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
        popup.getMenu().add("Select");
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
        TextView itemDisposalTotalTextView = findViewById(R.id.itemDisposalTotalTextView);
        TextView detailsDateTextView = findViewById(R.id.detailsDateTextView);
        EditText itemDisposalPlasticTextView = findViewById(R.id.itemDisposalPlasticTextView);
        EditText itemDisposalCansTextView = findViewById(R.id.itemDisposalCansTextView);
        EditText itemDisposalPaperTextView = findViewById(R.id.itemDisposalPaperTextView);
        EditText itemDisposalCarbonBoxTextView = findViewById(R.id.itemDisposalCarbonBoxTextView);


        if (Utils.getText(detailsDateTextView).equalsIgnoreCase("select")) {
            showError(getApplicationContext().getString(R.string.please_select_a_date), findViewById(R.id.detailsMonthTextView));
            return;
        }
        if (ZValidation.checkEmpty(itemDisposalPlasticTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_plastic_weight), findViewById(R.id.detailsMonthTextView));
            itemDisposalPlasticTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemDisposalCansTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_cans_weight), findViewById(R.id.detailsMonthTextView));
            itemDisposalCansTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemDisposalPaperTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_paper_weight), findViewById(R.id.detailsMonthTextView));
            itemDisposalPaperTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemDisposalCarbonBoxTextView)) {
            showError(getApplicationContext().getString(R.string.please_enter_carton_box_weight), findViewById(R.id.detailsMonthTextView));
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
            new CreateRecycledTask(context).execute(jArray.toString());

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
