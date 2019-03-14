package uems.biowaste.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import uems.biowaste.R;
import uems.biowaste.async.FetchGWasteDetailsTask;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class GwasteDetailsFragment extends Fragment {

    private ItemVo vo;

    public UserVo me;
    private GwasteDetailsFragment.OnFragmentInteractionListener mListener;

    TextView detailsDateTextView;
    TextView detailsMonthTextView;
    TextView detailsNameTextView;
    EditText detailsWeightTextView ;
    EditText detailsNoOfHaulageTextView ;
    EditText detailsDisposalFeeTextView;
    EditText detailsHuelageChargeTextView ;
    TextView detailsTotalDisposaFeeTextView;
    Button detailsSubmitButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GwasteDetailsFragment.OnFragmentInteractionListener) {
            mListener = (GwasteDetailsFragment.OnFragmentInteractionListener) context;
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
        View view = inflater.inflate(R.layout.fragment_gwastedetails, container, false);
        me = Utils.getUser(getContext());
        if(getArguments() != null)
            vo = (ItemVo) getArguments().getSerializable("vo");
        initLayout(view);
        new FetchGWasteDetailsTask(getContext()).execute(vo.getItemID(),me.getEmailID());
        return view;
    }

    public void initLayout(View view) {

        detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        detailsNameTextView = view.findViewById(R.id.detailsNameTextView);

        detailsWeightTextView = view.findViewById(R.id.detailsWeightTextView);
        detailsNoOfHaulageTextView = view.findViewById(R.id.detailsNoOfHaulageTextView);

        detailsDisposalFeeTextView = view.findViewById(R.id.detailsDisposalFeeTextView);
        detailsHuelageChargeTextView = view.findViewById(R.id.detailsHuelageChargeTextView);

        detailsTotalDisposaFeeTextView = view.findViewById(R.id.detailsTotalDisposaFeeTextView);
        detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        detailsSubmitButton = view.findViewById(R.id.detailsSubmitButton);
        detailsSubmitButton.setVisibility(View.GONE);

        detailsWeightTextView.setFocusable(false);
        detailsNoOfHaulageTextView.setFocusable(false);
        detailsDisposalFeeTextView.setFocusable(false);
        detailsHuelageChargeTextView.setFocusable(false);

        setData();
    }

    public void  setData(){

        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());
        detailsWeightTextView.setText(vo.getTotalWeight());
        detailsNoOfHaulageTextView.setText(vo.getNoOfHaulage());
        detailsDisposalFeeTextView.setText(vo.getDisposalFee());
        detailsHuelageChargeTextView.setText(vo.getHualageCharge());
        detailsTotalDisposaFeeTextView.setText(String.format("$%s", vo.getTotalDisposalFee()));


    }

    public void detailsResponse(TResponse<String> result) {

        if (result == null) {
            Utils.showError(" please check network connection", detailsDateTextView);
        } else if (result.isHasError()) {
            Utils.showError("please try later", detailsDateTextView);

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetFoodandGeneralwasteDetails");
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
}
