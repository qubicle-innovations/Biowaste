package uems.biowaste.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import uems.biowaste.R;
import uems.biowaste.utils.Utils;
import uems.biowaste.vo.ItemVo;
import uems.biowaste.vo.UserVo;

public class PatientDetailsFragment extends Fragment {

    private ItemVo vo;
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
        void  startFragment(String fragmentName,boolean addToBackStack,boolean isAdd);
        void  startFragment(Fragment fragment,boolean addToBackStack,boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients_details, container, false);
        me = Utils.getUser(getContext());
        if( getArguments() != null)
            vo = (ItemVo) getArguments().getSerializable("vo");
        initLayout(view);
        return view;
    }

    public void initLayout(View view) {

        TextView detailsMonthTextView = view.findViewById(R.id.detailsMonthTextView);
        TextView detailsDateTextView = view.findViewById(R.id.detailsDateTextView);
        TextView detailsNameTextView = view.findViewById(R.id.detailsNameTextView);

        EditText patientDetailsTotalTextView = view.findViewById(R.id.patientDetailsTotalTextView);
        view.findViewById(R.id.detailsSubmitButton).setVisibility(View.GONE);
        patientDetailsTotalTextView.setFocusable(false);
        detailsMonthTextView.setText(vo.getMonth());
        detailsDateTextView.setText(vo.getDate());
        detailsNameTextView.setText(vo.getCreatedBy());
        patientDetailsTotalTextView.setText(vo.getTotalPatients());

    }


}
