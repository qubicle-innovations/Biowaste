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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.List;

import uems.biowaste.R;
import uems.biowaste.async.LoginTask;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.MappingVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class LoginFragment extends android.support.v4.app.Fragment {

    EditText etUsername ;
    EditText etPassword ;
    Button btLogin;

    private LoginFragment.OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (LoginFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void  startFragment(Fragment fragment, String fragmentName, boolean addToBackStack, boolean isAdd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.loginactivity, container, false);
        init(view);
        return view;
    }

    public void init(View view){
        etUsername =  view.findViewById(R.id.etUsername);
        etPassword =  view.findViewById(R.id.etPassword);
        btLogin =  view.findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    
    public void login(){

        if(ZValidation.checkEmpty(etUsername)|| (Utils.getText(etUsername) != null && Utils.getText(etUsername).length() < 1)){
            Utils.showError("Enter valid username", etUsername);
            etUsername.requestFocus();
            return;
        }else  if(ZValidation.checkEmpty(etPassword)|| (Utils.getText(etPassword) != null && Utils.getText(etPassword).length()<2)){
            Utils.showError("Enter valid password", etUsername);
            etPassword.requestFocus();
            return;
        }

        new LoginTask(getContext()).execute(Utils.getText(etUsername),Utils.getText(etPassword));

    }



    public void loginResponse(TResponse<String> result) {

        if (result == null) {
            Utils.showError(getContext().getString(R.string.please_check_network_connection), btLogin);
        } else if (result.isHasError()) {
            Utils.showError(getContext().getString(R.string.please_try_later), btLogin);
        } else if (result.getResponseContent() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);

                JSONObject jsonObject = new JSONObject(result.getResponseContent());
                  UserVo ad = mapper.readValue(jsonObject.getJSONObject("ListValidateUser").toString(), new TypeReference<UserVo>() {
                    });

                 if(ad!=null&&ad.getUserID()!=null){
                  //
                     List<MappingVo> pList = mapper.readValue(jsonObject.getJSONArray("ListProductURLS").toString(), new TypeReference<List<MappingVo>>() {
                     });
                     String url=null;
                     for(MappingVo vo : pList){
                             if(vo.getProductName().equalsIgnoreCase("BioWaste")){
                             url = vo.getURL();
                         }
                     }
                     if (url == null) {
                         Utils.showError(getContext().getString(R.string.product_url_not_found),btLogin);
                            return;
                     }else if (getContext() != null ){
                         Utils.setUser(getContext(),ad);
                         Utils.setSharedPreference(getContext(),"url",url);
                     }
                     if (getContext() != null )

                         mListener.startFragment(new DashboardFragment( ),Constants.FRAGMENT_DASHBOARD,false,false);
                 }else {
                     Utils.showError(getContext().getString(R.string.invalid_username_or_password),btLogin);
                 }

            } catch (Exception e) {
                Utils.showError(getContext().getString(R.string.please_try_later), btLogin);

                Log.e("parse order", e.toString());
            }
        }
    }



}
