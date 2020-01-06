package uems.biowaste;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.List;

import uems.biowaste.async.LoginTask;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.MappingVo;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserVo;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        if(ZValidation.checkEmpty(etUsername)|| Utils.getText(etUsername).length()<1){
            showError(getApplicationContext().getString(R.string.please_enter_a_valid_username), etUsername);
            etUsername.requestFocus();
            return;


        }else  if(ZValidation.checkEmpty(etPassword)|| Utils.getText(etPassword).length()<2){
            showError(getApplicationContext().getString(R.string.please_enter_a_valid_password), etUsername);
            etPassword.requestFocus();
            return;


        }

        new LoginTask(context).execute(Utils.getText(etUsername),Utils.getText(etPassword));

    }



    public void loginResponse(TResponse<String> result) {

        // TODO Auto-generated method stub
        if (result == null) {
            showError(getApplicationContext().getString(R.string.please_check_network_connection), findViewById(R.id.btLogin));
        } else if (result.isHasError()) {
            showError(getApplicationContext().getString(R.string.please_try_later), findViewById(R.id.btLogin));

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

                         showError(getApplicationContext().getString(R.string.product_url_not_found),findViewById(R.id.btLogin));
                            return;
                     }else {
                         Utils.setUser(context,ad);
                         Utils.setSharedPreference(context,"url",url);
                     }
                     startActivity(new Intent(context,Dashboard.class));
                      finish();
                 }else {
                     showError(getApplicationContext().getString(R.string.invalid_username_or_password),findViewById(R.id.btLogin));
                 }



            } catch (Exception e) {
                showError(getApplicationContext().getString(R.string.please_try_later), findViewById(R.id.btLogin));

                Log.e("parse order", e.toString());
            }
        }
    }



}
