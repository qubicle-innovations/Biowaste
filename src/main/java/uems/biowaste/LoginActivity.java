package uems.biowaste;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import uems.biowaste.async.LoginTask;
import uems.biowaste.utils.Utils;
import uems.biowaste.utils.ZValidation;
import uems.biowaste.vo.TResponse;
import uems.biowaste.vo.UserPojo;
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
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        if(ZValidation.checkEmpty(etUsername)|| Utils.getText(etUsername).length()<1){
            showError("Enter valid username", etUsername);
            etUsername.requestFocus();
            return;


        }else  if(ZValidation.checkEmpty(etPassword)|| Utils.getText(etPassword).length()<2){
            showError("Enter valid password", etUsername);
            etPassword.requestFocus();
            return;


        }

        new LoginTask(context).execute(new String[]{Utils.getText(etUsername),Utils.getText(etPassword)});

    }


    public void loginResponse(TResponse<String> result) {

        // TODO Auto-generated method stub
        if (result == null) {
            showError(" please check network connection", findViewById(R.id.btLogin));
        } else if (result.isHasError()) {
            showError("please try later", findViewById(R.id.btLogin));

        } else if (result.getResponseContent() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
                  UserPojo ad = mapper.readValue(result.getResponseContent(), new TypeReference<UserPojo>() {
                    });

                 if(ad!=null&&ad.getListUserDetails()!=null&&!ad.getListUserDetails().isEmpty()){
                     Utils.setUser(context,ad.getListUserDetails().get(0));
                    // startActivity(new Intent(context,Dashboard.class));
                     //finish();
                 }else {
                     showError("Invalid username or password",findViewById(R.id.btLogin));
                 }



            } catch (Exception e) {
                showError("please try later", findViewById(R.id.btLogin));

                Log.e("parse order", e.toString());
            }
        }
    }



}
