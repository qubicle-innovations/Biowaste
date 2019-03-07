package uems.biowaste;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import uems.biowaste.async.FetchBioWasteDetailsTask;
import uems.biowaste.vo.BioWasteItemVo;
import uems.biowaste.vo.TResponse;

public class BiowasteDetailsActivity extends BaseBackActivity {

    private BioWasteItemVo vo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_biowaste_details);
        setToolbar("Biowaste");
        vo = (BioWasteItemVo) getIntent().getSerializableExtra("vo");
        initLayout();
        new FetchBioWasteDetailsTask(context).execute(new String[]{vo.getItemID(),me.getEmailID()});

    }

    public void initLayout() {

        TextView detailsMonthTextView = (TextView) findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView = (TextView) findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView = (TextView) findViewById(R.id.detailsNameTextView);
        TextView detailsWeightTextView = (TextView) findViewById(R.id.detailsWeightTextView);
        TextView detailsNoOfHaulageTextView = (TextView) findViewById(R.id.detailsNoOfHaulageTextView);
        findViewById(R.id.detailsSubmitButton).setVisibility(View.GONE);
        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());
        detailsWeightTextView.setText(vo.getTotalBin());
        detailsNoOfHaulageTextView.setText(vo.getTotalCost());

    }

    public void detailsResponse(TResponse<String> result) {

        if (result == null) {
            showError(" please check network connection", findViewById(R.id.detailsDateTextView));
        } else if (result.isHasError()) {
            showError("please try later", findViewById(R.id.detailsDateTextView));

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetBiowasteDetails");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                vo = mapper.readValue(jsonArray.getJSONObject(0).toString(), new TypeReference<BioWasteItemVo>() {
                });
                initLayout();

            } catch (Exception e) {
                showError("please try later", findViewById(R.id.detailsDateTextView));

                Log.e("parse order", e.toString());
            }
        }
    }
}
