package uems.biowaste.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import uems.biowaste.HomeActivity;
import uems.biowaste.LoginActivity;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.TCustomProgressDailogue;
import uems.biowaste.vo.TResponse;


public class LoginTask extends
        AsyncTask<String, Void, TResponse<String>> {

    private Context ctx;
    private TCustomProgressDailogue pd;

    public LoginTask(Context context) {
        this.ctx = context;
        this.pd = new TCustomProgressDailogue(ctx);
        this.pd.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        pd.show();
    }

    @Override
    protected TResponse<String> doInBackground(String... params) {
        TResponse<String> response = new TResponse<String>();

        try {
            SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.METHOD_VALIDATE_USER);
            PropertyInfo UName = new PropertyInfo();
            UName.setName("Username");
            UName.setValue(params[0]);
            UName.setType(String.class);
            request.addProperty(UName);
            PropertyInfo pass1 = new PropertyInfo();
            pass1.setName("Password");
            pass1.setValue(params[1]);
            pass1.setType(String.class);
            request.addProperty(pass1);
            PropertyInfo id = new PropertyInfo();
            id.setName("RegistrationID");
            id.setValue("");
            id.setType(String.class);
            request.addProperty(id);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.USER_LOGIN);
            if (envelope != null) {
                androidHttpTransport.call(Constants.SOAP_ACTION + Constants.METHOD_VALIDATE_USER, envelope);
                response.setResponseContent(((SoapPrimitive) envelope.getResponse()).toString());
                Log.d("response", ((SoapPrimitive) envelope.getResponse()).toString());
            } else {
                Log.d("response", "reponse");

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e("parser exception", e.getMessage());
        }


        return response;
    }

    @Override
    protected void onPostExecute(TResponse<String> result) {


        if (pd.isShowing()) {
            pd.dismiss();
        }

        if (ctx instanceof LoginActivity) {
            ((LoginActivity) ctx).loginResponse(result);
        }else if (ctx instanceof HomeActivity) {
            ((HomeActivity) ctx).loginResponse(result);
        }


    }


}
