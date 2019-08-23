package uems.biowaste.http;

import android.support.v4.util.Pair;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import uems.biowaste.exception.UEHttpException;


public class RestURLClient {

	// private String result="";
	// private ResponseHandler <String> res=new BasicResponseHandler();
	// private List<NameValuePair> nameValuePairs = new
	// ArrayList<NameValuePair>(6);

	private List<Pair<String,String>> params;
	private List<Pair<String,String>> headers;
	private static final String TAG = "RestClient";
	private String responseString = null;
	private boolean JSONEncode =false;
 	private String url;
	private int statusCode;
	private JSONObject jobject =null ;
	private InputStream inputStream;
	private boolean returnInputStream=false;
	private static final int TIMEOUT = 30000;


	public enum RequestMethod {
		GET, POST
	}

	public RestURLClient(String url) {
		this.url = url;
		Log.v(TAG, this.url);
		this.params = new ArrayList<Pair<String,String>>();
		this.headers = new ArrayList<Pair<String,String>>();
	}


	public RestURLClient(String url, boolean JSONEncode) {
		this.url = url;
		this.JSONEncode = JSONEncode;
		Log.v(TAG, this.url);
		jobject = new JSONObject();
		this.headers = new ArrayList<Pair<String,String>>();

	}

	public void execute(RequestMethod method) throws UEHttpException {
		if (method == RequestMethod.GET) {
			this.doGet();
		} else if (method == RequestMethod.POST) {
			this.doPost();
		}

	}
	
	public void executeForInputStream(RequestMethod method) throws UEHttpException {
		if (method == RequestMethod.GET) {
			this.doGet();
		} else if (method == RequestMethod.POST) {
			this.doPost();
		}

	}

	public String getResponseString() {
		//Log.v("Response", responseString);
		return responseString;

	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	public int getStatusCode() {
		return statusCode;
	}
	

	
	

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public InputStream getContent(){
		return inputStream;
	}
	
	public void setRequestForInputStream(boolean requireInputStream){
		returnInputStream=requireInputStream;
	}

	private void doPost() throws UEHttpException {




		try {
			URL url = new URL(this.url);
			responseString="";
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setInstanceFollowRedirects(false);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("charset", "utf-8");
			if(!this.headers.isEmpty()){
				for (Pair<String,String> nvp : this.headers) {

					urlConnection.setRequestProperty(nvp.first, nvp.second);
					Log.e(TAG, "Setting " + nvp.first
							+ " in the request header with value " + nvp.second);
				}


			}
			if(this.params!=null&&!this.params.isEmpty()) {
				//post.setEntity(new UrlEncodedFormEntity(this.params, HTTP.UTF_8));
				 for (Pair nvp : this.params) {
					Log.e(TAG, "Setting " + nvp.first
							+ " in the request body with value " + nvp.second);
				}
				String paramters= getQuery(this.params);
				Log.v(TAG, paramters);
				byte[] postData       = new byte[0];
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					postData = paramters.getBytes( StandardCharsets.UTF_8 );
				} else
					postData = paramters.getBytes(Charset.forName("UTF-8"));

				urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
				urlConnection.setUseCaches(false);

				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
						wr.write(postData);
					}
				}else{
					DataOutputStream wr = null;
					try {
						wr = new DataOutputStream(urlConnection.getOutputStream()) ;
						wr.write(postData);
					} finally {
						if (wr != null)	 wr.close();
					}
				}


			}
			if(null!=jobject) {
				urlConnection.setRequestProperty("Content-type", "application/json");
				String message = jobject.toString();
				Log.e("json",message);
				OutputStream os = urlConnection.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
				writer.write(message);
				writer.flush();
				writer.close();
				os.close();

			}

 			  statusCode = urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_OK) {
				if(returnInputStream){
					inputStream=urlConnection.getInputStream();
				}else{
					try{
						String line;
						BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
						while ((line=br.readLine()) != null) {
							responseString+=line;
						}
						Log.i(TAG, "ResponseCode: " + statusCode);
						Log.i(TAG, "Response: " + responseString);
					}catch (Exception e) {
						e.printStackTrace();
                    }

				}
				
			
			} else {
              /*  String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    responseString+=line;
                }*/
                Log.i(TAG, "ResponseCode: " + statusCode);
                Log.i(TAG, "Response: " + responseString);
                throw new UEHttpException(statusCode,
						"Server error : "+statusCode);
			}

		} catch (Exception exception) {
			Log.e(TAG + "1", exception.getMessage());
			throw new UEHttpException(
					Utils.CLIENT_PROTOCOL_EXCEPTION_STATUS_CODE,
					"ClientProtocolException error");
		}
	}
	
	

	private void doGet() throws UEHttpException {
	 	try {

			URL url = new URL(this.url);
			responseString="";
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setConnectTimeout(TIMEOUT);
			urlConnection.setReadTimeout(TIMEOUT);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setChunkedStreamingMode(0);


			statusCode =urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_OK) {

				if(returnInputStream){
					inputStream=urlConnection.getInputStream();
				}else{
					try{
						String line;
						BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
						while ((line=br.readLine()) != null) {
							responseString+=line;
						}
						Log.i(TAG, "ResponseCode: " + statusCode);
						Log.i(TAG, "Response: " + responseString);
					}catch(Exception e) {
						e.printStackTrace();
                    }

				}
			
			} else {
				// Closes the connection.
				//response.getEntity().getContent().close();
				// throw new IOException(statusLine.getReasonPhrase());
				throw new UEHttpException(statusCode, "Server error");
			}
		}  catch (Exception e) {
			Log.e(TAG + "2", e.getMessage());
			throw new UEHttpException(
					Utils.CONNECTION_TIME_OUT_STATUS_CODE, "Network error");
		}

	}

	public void addParam(String name, String value) {
		if(!JSONEncode) {
			this.params.add(new Pair<String, String>(name, value));
			Log.i("added", "added " + this.params.get(params.size()-1).second );
		}else {
			try {
				this.jobject.put(name, value);
				Log.e("ic_add paramter", name +" added to "+ value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void addParam(String name, JSONArray value) {

			try {
				this.jobject.put(name, value);
				Log.e("ic_add paramter", name +" added to "+ jobject.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}
	public void addHeaders(String name, String value) {

		headers.add(new Pair<String, String>(name, value));
	}

	private String getQuery(List<Pair<String,String>> params) throws UEHttpException
	{
		try {
			StringBuilder result = new StringBuilder();
			boolean first = true;

			for (Pair<String,String> pair : params) {
				if (first)
					first = false;
				else
					result.append("&");

				result.append(URLEncoder.encode(pair.first, "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(pair.second, "UTF-8"));
			}
			Log.v(TAG,result.toString()) ;
			return result.toString();
		}catch (Exception e){
			throw  new UEHttpException(Utils.INVALID_PARAMETERS_STATUS_CODE,e.toString());
		}

	}


}
