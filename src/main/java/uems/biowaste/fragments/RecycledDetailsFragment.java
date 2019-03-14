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
import uems.biowaste.async.FetchRecycledDetailsTask;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class RecycledDetailsFragment extends Fragment {

    private ItemVo vo;
    public UserVo me;
    private RecycledDetailsFragment.OnFragmentInteractionListener mListener;

    TextView detailsMonthTextView;
    TextView detailsDateTextView;
    TextView detailsNameTextView ;
    TextView itemDisposalTotalTextView;

    EditText itemDisposalPlasticTextView;
    EditText itemDisposalCansTextView;
    EditText itemDisposalPaperTextView;
    EditText itemDisposalCarbonBoxTextView;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_item_disposal_details, container, false);
        me = Utils.getUser(getContext());
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

        itemDisposalPlasticTextView.setFocusable(false);
        itemDisposalCansTextView.setFocusable(false);
        itemDisposalPaperTextView.setFocusable(false);
        itemDisposalCarbonBoxTextView.setFocusable(false);

        view.findViewById(R.id.detailsSubmitButton).setVisibility(View.GONE);
        setData();
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
}
