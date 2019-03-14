package uems.biowaste.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import uems.biowaste.R;
import uems.biowaste.async.FetchBioWasteDetailsTask;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.BioWasteItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class BiowasteDetailsFragment extends Fragment {

    private BioWasteItemVo vo;

    TextView detailsMonthTextView ;
    TextView detailsDateTextView ;
    TextView detailsNameTextView ;

    EditText detailsWeightTextView ;
    EditText detailsNoOfHaulageTextView ;

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
        void  startFragment(Fragment fragment,String fragmentName,boolean addToBackStack,boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biowaste_details, container, false);
        me = Utils.getUser(getContext());
        if(getArguments() != null)
            vo = (BioWasteItemVo) getArguments().getSerializable("vo");
        initLayout(view);
        new FetchBioWasteDetailsTask(getContext()).execute(vo.getItemID(),me.getEmailID());
        return view;
    }


    public void initLayout(View view) {

        detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        detailsNameTextView = view.findViewById(R.id.detailsNameTextView);

        detailsWeightTextView = view.findViewById(R.id.detailsWeightTextView);
        detailsNoOfHaulageTextView = view.findViewById(R.id.detailsNoOfHaulageTextView);
        view.findViewById(R.id.detailsSubmitButton).setVisibility(View.GONE);
        detailsWeightTextView.setFocusable(false);
        detailsNoOfHaulageTextView.setFocusable(false);

        setData();

    }

    public void setData(){
        if(vo != null){
            detailsMonthTextView.setText(vo.getMonth());
            detailsDateTextView.setText(vo.getDate());
            detailsNameTextView.setText(vo.getCreatedBy());
            detailsWeightTextView.setText(vo.getTotalBin());
            detailsNoOfHaulageTextView.setText(vo.getTotalCost());
        }

    }

    public void detailsResponse(TResponse<String> result) {

        if (result == null) {
            Utils.showError(" please check network connection",detailsDateTextView);
        } else if (result.isHasError()) {
            Utils.showError("please try later", detailsDateTextView);

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetBiowasteDetails");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                vo = mapper.readValue(jsonArray.getJSONObject(0).toString(), new TypeReference<BioWasteItemVo>() {
                });
                setData();
            } catch (Exception e) {
                Utils.showError("please try later", detailsDateTextView);
                Log.e("parse order", e.toString());
            }
        }
    }
}
