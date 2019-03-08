package uems.biowaste;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import uems.biowaste.async.FetchRecycledDetailsTask;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.TResponse;

public class RecycledDetailsActivity extends BaseBackActivity {

    private ItemVo vo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recycle_item_disposal_details);
        setToolbar("Recycled items disposed");
        vo = (ItemVo) getIntent().getSerializableExtra("vo");
        initLayout();
        new FetchRecycledDetailsTask(context).execute(new String[]{vo.getItemID(),me.getEmailID()});

    }

    public void initLayout() {

        TextView detailsMonthTextView = (TextView) findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView = (TextView) findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView = (TextView) findViewById(R.id.detailsNameTextView);
        TextView itemDisposalTotalTextView = (TextView) findViewById(R.id.itemDisposalTotalTextView);

        EditText itemDisposalPlasticTextView = (EditText) findViewById(R.id.itemDisposalPlasticTextView);
        EditText itemDisposalCansTextView = (EditText) findViewById(R.id.itemDisposalCansTextView);
        EditText itemDisposalPaperTextView = (EditText) findViewById(R.id.itemDisposalPaperTextView);
        EditText itemDisposalCarbonBoxTextView = (EditText) findViewById(R.id.itemDisposalCarbonBoxTextView);

        itemDisposalPlasticTextView.setFocusable(false);
        itemDisposalCansTextView.setFocusable(false);
        itemDisposalPaperTextView.setFocusable(false);
        itemDisposalCarbonBoxTextView.setFocusable(false);

        findViewById(R.id.detailsSubmitButton).setVisibility(View.GONE);
        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());

        itemDisposalPlasticTextView.setText(vo.getPlastic() );
        itemDisposalCansTextView.setText(vo.getCans());
        itemDisposalPaperTextView.setText(vo.getPaper());
        itemDisposalCarbonBoxTextView.setText(vo.getCartonBox());
        itemDisposalTotalTextView.setText( vo.getTotalWeight()+"Kg");

    }

    public void detailsResponse(TResponse<String> result) {

        if (result == null) {
            showError(" please check network connection", findViewById(R.id.detailsDateTextView));
        } else if (result.isHasError()) {
            showError("please try later", findViewById(R.id.detailsDateTextView));

        } else if (result.getResponseContent() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                JSONArray jsonArray = jsonObject.getJSONArray("ListGetRecycleditemsDetails");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                vo = mapper.readValue(jsonArray.getJSONObject(0).toString(), new TypeReference<ItemVo>() {
                });
                initLayout();

            } catch (Exception e) {
                showError("please try later", findViewById(R.id.detailsDateTextView));

                Log.e("parse order", e.toString());
            }
        }
    }
}
