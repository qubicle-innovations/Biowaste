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
        setToolbar(getApplicationContext().getString(R.string.monthly_patient_details));
        vo = (ItemVo) getIntent().getSerializableExtra("vo");
        initLayout();

    }

    public void initLayout() {

        TextView detailsMonthTextView = findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView = findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView = findViewById(R.id.detailsNameTextView);

        EditText patientDetailsTotalTextView = findViewById(R.id.patientDetailsTotalTextView);
        findViewById(R.id.detailsSubmitButton).setVisibility(View.GONE);
        patientDetailsTotalTextView.setFocusable(false);
        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());
        patientDetailsTotalTextView.setText(vo.getTotalPatients());

    }


}
