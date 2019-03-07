package uems.biowaste.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import uems.biowaste.BioWasteListActivity;
import uems.biowaste.http.RestURLClient;
import uems.biowaste.utils.Constants;
import uems.biowaste.utils.TCustomProgressDailogue;
import uems.biowaste.vo.TResponse;


public class FetchBioWasteListTask extends
        AsyncTask<String, Void, TResponse<String>> {

	private Context ctx;
 	private TCustomProgressDailogue pd;
	public FetchBioWasteListTask(Context context) {
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
		TResponse<String> response =  new TResponse<String>();

			 try{
				 RestURLClient client = new RestURLClient(Constants.GET_BIOWASTE_LIST, true);
				 client.addParam("ItemID","0");
				 client.addParam("Search",params[0]);
				 client.addParam("UserEmailID",params[1]);

				 client.execute(RestURLClient.RequestMethod.POST);

				 String responses = client.getResponseString();
			Log.d("success", responses);
			response.setResponseContent(responses);
			response.setHasError(false);

		} catch (Exception e) {
			 
			response.setResponseContent(e.toString());
			response.setHasError(true);
			Log.e("error", e.toString());
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected void onPostExecute(TResponse<String> result) {


		if (pd.isShowing()){
			pd.dismiss();
		}

		if (ctx instanceof BioWasteListActivity) {
			 ((BioWasteListActivity) ctx).listResponse(result);
		}
	

	}


}