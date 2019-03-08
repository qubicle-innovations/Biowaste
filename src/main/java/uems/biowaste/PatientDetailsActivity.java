package uems.biowaste;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import uems.biowaste.vo.ItemVo;

public class PatientDetailsActivity extends BaseBackActivity {

    private ItemVo vo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_patients_details);
        setToolbar("Monthly Patient details");
        vo = (ItemVo) getIntent().getSerializableExtra("vo");
        initLayout();

    }

    public void initLayout() {

        TextView detailsMonthTextView = (TextView) findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView = (TextView) findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView = (TextView) findViewById(R.id.detailsNameTextView);

        EditText patientDetailsTotalTextView = (EditText) findViewById(R.id.patientDetailsTotalTextView);
        findViewById(R.id.detailsSubmitButton).setVisibility(View.GONE);
        patientDetailsTotalTextView.setFocusable(false);
        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());
        patientDetailsTotalTextView.setText(vo.getTotalPatients());

    }


}
