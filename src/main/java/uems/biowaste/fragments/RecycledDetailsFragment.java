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
import uems.biowaste.async.DeleteTask;
import uems.biowaste.async.FetchRecycledDetailsTask;
import uems.biowaste.async.UpdateRecycledTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.DateUtil;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class RecycledDetailsFragment extends Fragment implements View.OnClickListener {

    private ItemVo vo;
    public UserVo me;
    private RecycledDetailsFragment.OnFragmentInteractionListener mListener;
    String month;
    private Calendar startDate;

    Button detailsSubmitButton;
    boolean editable = false;

    TextView detailsMonthTextView;
    TextView detailsDateTextView;
    TextView detailsNameTextView;
    TextView itemDisposalTotalTextView;

    EditText itemDisposalPlasticTextView;
    EditText itemDisposalCansTextView;
    EditText itemDisposalPaperTextView;
    EditText itemDisposalCarbonBoxTextView;
    EditText itemNonConfDocTextView;
    EditText itemConfDocTextView;
    EditText itemGlassTextView;
    EditText itemNewsPaperTextView;
    EditText itemOthersTextView;
    Button deleteButtonRecycle, editButtonRecycle;

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
        void startFragment(Fragment fragment, String fragmentName, boolean addToBackStack, boolean isAdd);

        void popupFragment(Fragment fragment, String fragmentName, boolean addToBackStack, boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_item_disposal_details, container, false);
        me = Utils.getUser(getContext());
        startDate = Calendar.getInstance();
        month = (String) android.text.format.DateFormat.format("M", startDate.getTime());
        if (getArguments() != null)
            vo = (ItemVo) getArguments().getSerializable("vo");
        initLayout(view);
        new FetchRecycledDetailsTask(getContext()).execute(vo.getItemID(), me.getEmailID());
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
        itemNonConfDocTextView = view.findViewById(R.id.itemNonConfDocTextView);
        itemConfDocTextView = view.findViewById(R.id.itemConfDocTextView);
        itemGlassTextView = view.findViewById(R.id.itemGlassTextView);
        itemNewsPaperTextView = view.findViewById(R.id.itemNewsPaperTextView);
        itemOthersTextView = view.findViewById(R.id.itemOthersTextView);
        editButtonRecycle = view.findViewById(R.id.editButtonRecycle);
        deleteButtonRecycle = view.findViewById(R.id.deleteButtonRecycle);
        detailsSubmitButton = view.findViewById(R.id.detailsSubmitButton);


        detailsSubmitButton.setVisibility(View.GONE);
        deleteButtonRecycle.setVisibility(View.GONE);
        editButtonRecycle.setVisibility(View.GONE);
        detailsMonthTextView.setOnClickListener(this);
        detailsDateTextView.setOnClickListener(this);

        editButtonRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDisposalPlasticTextView.setFocusableInTouchMode(true);
                itemDisposalCansTextView.setFocusableInTouchMode(true);
                itemDisposalPaperTextView.setFocusableInTouchMode(true);
                itemDisposalCarbonBoxTextView.setFocusableInTouchMode(true);

                itemNonConfDocTextView.setFocusableInTouchMode(true);
                itemConfDocTextView.setFocusableInTouchMode(true);
                itemGlassTextView.setFocusableInTouchMode(true);
                itemNewsPaperTextView.setFocusableInTouchMode(true);
                itemOthersTextView.setFocusableInTouchMode(true);

                itemDisposalPlasticTextView.setFocusable(true);
                itemDisposalCansTextView.setFocusable(true);
                itemDisposalPaperTextView.setFocusable(true);
                itemDisposalCarbonBoxTextView.setFocusable(true);

                 itemNonConfDocTextView.setFocusable(true);
                 itemConfDocTextView.setFocusable(true);
                 itemGlassTextView.setFocusable(true);
                 itemNewsPaperTextView.setFocusable(true);
                 itemOthersTextView.setFocusable(true);
                editable = true;

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
        itemNonConfDocTextView.setFocusable(false);
        itemConfDocTextView.setFocusable(false);
        itemGlassTextView.setFocusable(false);
        itemNewsPaperTextView.setFocusable(false);
        itemOthersTextView.setFocusable(false);


        itemDisposalPlasticTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (s != null && s.length() > 0)
                    plastic = Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));

                if (!ZValidation.isEmpty(itemConfDocTextView))
                    confDoc = Double.parseDouble(Utils.getText(itemConfDocTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

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

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (s != null && s.length() > 0)
                    cans = Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                if (!ZValidation.isEmpty(itemConfDocTextView))
                    confDoc = Double.parseDouble(Utils.getText(itemConfDocTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

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

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (s != null && s.length() > 0)
                    paper = Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                if (!ZValidation.isEmpty(itemConfDocTextView))
                    confDoc = Double.parseDouble(Utils.getText(itemConfDocTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

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

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (s != null && s.length() > 0)
                    box = Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemConfDocTextView))
                    confDoc = Double.parseDouble(Utils.getText(itemConfDocTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

            }
        });
        itemConfDocTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (s != null && s.length() > 0)
                    confDoc = Double.parseDouble(s.toString());
                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

            }
        });

        itemNonConfDocTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                if (!ZValidation.isEmpty(itemConfDocTextView))
                    confDoc = Double.parseDouble(Utils.getText(itemConfDocTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

            }
        });
        itemGlassTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                if (!ZValidation.isEmpty(itemConfDocTextView))
                    confDoc = Double.parseDouble(Utils.getText(itemConfDocTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

            }
        });
        itemNewsPaperTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                if (!ZValidation.isEmpty(itemConfDocTextView))
                    confDoc = Double.parseDouble(Utils.getText(itemConfDocTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

            }
        });
        itemOthersTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double plastic = 0,cans = 0, paper = 0, box = 0;
                double nonConf = 0,confDoc = 0, glass = 0, newspaper = 0, others = 0;

                if (!ZValidation.isEmpty(itemDisposalPlasticTextView))
                    plastic = Double.parseDouble(Utils.getText(itemDisposalPlasticTextView));
                if (!ZValidation.isEmpty(itemDisposalCansTextView))
                    cans = Double.parseDouble(Utils.getText(itemDisposalCansTextView));
                if (!ZValidation.isEmpty(itemDisposalPaperTextView))
                    paper = Double.parseDouble(Utils.getText(itemDisposalPaperTextView));
                if (!ZValidation.isEmpty(itemDisposalCarbonBoxTextView))
                    box = Double.parseDouble(Utils.getText(itemDisposalCarbonBoxTextView));
                if (!ZValidation.isEmpty(itemConfDocTextView))
                    confDoc = Double.parseDouble(Utils.getText(itemConfDocTextView));
                if (!ZValidation.isEmpty(itemNonConfDocTextView))
                    nonConf = Double.parseDouble(Utils.getText(itemNonConfDocTextView));
                if (!ZValidation.isEmpty(itemGlassTextView))
                    glass = Double.parseDouble(Utils.getText(itemGlassTextView));
                if (!ZValidation.isEmpty(itemNewsPaperTextView))
                    newspaper = Double.parseDouble(Utils.getText(itemNewsPaperTextView));
                if (!ZValidation.isEmpty(itemOthersTextView))
                    others = Double.parseDouble(Utils.getText(itemOthersTextView));

                itemDisposalTotalTextView.setText(Utils.roundOff(plastic + cans + paper + box+nonConf+confDoc+glass+newspaper+others));

            }
        });
        setData();
    }

    public void deleteRecord() {
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

    public void setData() {
        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());

        itemDisposalPlasticTextView.setText(vo.getPlastic());
        itemDisposalCansTextView.setText(vo.getCans());
        itemDisposalPaperTextView.setText(vo.getPaper());
        itemDisposalCarbonBoxTextView.setText(vo.getCartonBox());
        itemDisposalTotalTextView.setText(String.format("%sKg", vo.getTotalWeight()));
        itemNonConfDocTextView.setText(vo.getNonConfigDoc());
        itemConfDocTextView.setText(vo.getConfigDoc());
        itemGlassTextView.setText(vo.getGlass());
        itemNewsPaperTextView.setText(vo.getNewspapers());
        itemOthersTextView.setText(vo.getOthers());

        if(vo.getCreatedBy().toLowerCase().equals(me.getUserName().toLowerCase())){
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
        int monthValue = Integer.parseInt(month) - 1;

        if (getContext() != null) {
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

    public void updated() {
        if (getContext() != null) {
            Toast.makeText(getContext(), getText(R.string.updated), Toast.LENGTH_SHORT).show();
            mListener.popupFragment(new RecycledListFragment(), Constants.FRAGMENT_RECYCLED_ITEMS, false, true);
        }
    }

    public void recordDelete() {
        if (getContext() != null) {
            Toast.makeText(getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
            mListener.popupFragment(new RecycledListFragment(), Constants.FRAGMENT_RECYCLED_ITEMS, false, true);
        }
    }


    public void showStartDate() {

        int mYear = startDate.get(Calendar.YEAR);
        int mMonth = startDate.get(Calendar.MONTH);
        int mDay = startDate.get(Calendar.DAY_OF_MONTH);


        if (getContext() != null) {
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
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
                Calendar cal = Calendar.getInstance();
                cal.setTime(inputFormat.parse(detailsMonthTextView.getText().toString()));
                SimpleDateFormat outputFormat = new SimpleDateFormat("M"); // 01-12
                int month = Integer.parseInt(outputFormat.format(cal.getTime()));
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                calendar.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());


            } catch (Exception e) {

            }

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
        if (ZValidation.checkEmpty(itemConfDocTextView)) {
            Utils.showError("Please enter Conf. Doc. weight", detailsMonthTextView);
            itemConfDocTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemNonConfDocTextView)) {
            Utils.showError("Please enter Non-Conf. Doc weight", detailsMonthTextView);
            itemNonConfDocTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemGlassTextView)) {
            Utils.showError("Please enter glass weight", detailsMonthTextView);
            itemGlassTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemNewsPaperTextView)) {
            Utils.showError("Please enter Newspapers weight", detailsMonthTextView);
            itemNewsPaperTextView.requestFocus();
            return;

        }
        if (ZValidation.checkEmpty(itemOthersTextView)) {
            Utils.showError("Please enter Others ", detailsMonthTextView);
            itemOthersTextView.requestFocus();
            return;

        }
        try {
            JSONArray jArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Date", Utils.getText(detailsDateTextView));
            if (getMonth(Utils.getText(detailsMonthTextView)).isEmpty()) {
                jsonObject.put("Month", month);
            } else {
                jsonObject.put("Month", getMonth(Utils.getText(detailsMonthTextView)));
            }

            jsonObject.put("CartonBox", Utils.getText(itemDisposalCarbonBoxTextView));
            jsonObject.put("Paper", Utils.getText(itemDisposalPaperTextView));
            jsonObject.put("Cans", Utils.getText(itemDisposalCansTextView));
            jsonObject.put("Plastic", Utils.getText(itemDisposalPlasticTextView));
            jsonObject.put("ConfigDoc", Utils.getText(itemConfDocTextView));
            jsonObject.put("NonConfigDoc", Utils.getText(itemNonConfDocTextView));
            jsonObject.put("Glass", Utils.getText(itemGlassTextView));
            jsonObject.put("Newspapers", Utils.getText(itemNewsPaperTextView));
            jsonObject.put("Others", Utils.getText(itemOthersTextView));  jsonObject.put("TotalWeight", Utils.getText(itemDisposalTotalTextView).replace("KgKg", ""));
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


    public String getMonth(String month) {
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return (cal.get(Calendar.MONTH) + 1) + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.detailsMonthTextView:
                if (editable)
                    showMonthMenu(v);
                break;
            case R.id.detailsDateTextView:
                if (editable)
                    showStartDate();
                break;
            case R.id.detailsSubmitButton:
                saveItem();
                break;
        }
    }

}
