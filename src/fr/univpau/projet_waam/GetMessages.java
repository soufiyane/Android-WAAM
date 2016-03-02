package fr.univpau.projet_waam;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class GetMessages extends AsyncTask <Void, Void, ArrayList<Message>> {

    private static final String MSG = "items";
    private static final String TIME = "time";
    private static final String TAG_MSG = "msg";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_METERS = "meters";
    private static final String LOC = "geo";
	private PrincipalPageActivity activiti;
    private HttpClient client;
    private ProgressDialog progress;
    private JSONObject JSON_Object;
    private JSONArray JSON_Array;
    private Location location;
    private List<Message> messages;
    private int radius=5;


    private double lati;
    private double longti;
	
  
    
    @Override
    protected void onPreExecute() {                           
        progress.setTitle("Veuillez patienter");
        progress.setMessage("telechargement en cours...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);      
        progress.show();
    } 

    public GetMessages(PrincipalPageActivity s,double lat,double longt,/*Location l,*/ int rad) {
    	activiti = s;
		client = new DefaultHttpClient();
        progress = new ProgressDialog(activiti);
      //  this.location = l;
        messages = new ArrayList<Message>();
        this.radius = rad>5?rad:5;
        
        this.lati=lat;
        this.longti=longt;

	}
    
	@Override
	protected ArrayList<Message> doInBackground(Void... params) {

        try {
           //	Double latitude = new Double(location.getLatitude());
//        	
           //	Double longitude = new Double(location.getLongitude());
        	
        	//****************************** 
        	// c'est ici qu'il faut mettre lat et longt de ta position
        	//**********************
        	
           	String uriStr = String.format("%smy_latitude=%s&my_longitude=%s&my_radius=%s", activiti.URL,this.lati,this.longti,radius);//5000);// 48.856614,2.352233, 5);
        	
        	
        	
        	HttpResponse response = this.client.execute(new HttpGet(uriStr));
            String jsonStr = EntityUtils.toString(response.getEntity());
            Log.i("Response: ", "> " + jsonStr);
            if (jsonStr != null) { 
            try {
            JSON_Object = new JSONObject(jsonStr);
            JSON_Array = JSON_Object.getJSONArray(MSG);           		
            for(int i=0; i<JSON_Array.length(); i++) {
             JSONObject jo = JSON_Array.getJSONObject(i);
             String date = jo.getString(TIME);
             String msg = jo.getString(TAG_MSG);
             String genderstr = jo.getString(TAG_GENDER);
             //Boolean gender=(genderstr=="1");
             String meters = jo.getString(TAG_METERS)+" m";
             JSONArray JSON_Array2 = jo.getJSONArray(LOC);
             double geo[] = new double[2];
             geo[0] = Double.parseDouble(JSON_Array2.getString(0));
             geo[1] = Double.parseDouble(JSON_Array2.getString(1));
             Log.i("geo", geo[0] + " " + geo[1]);
             Message Mssage = new Message(genderstr, msg, date, meters);
             Mssage.set_geo(geo); messages.add(Mssage);
            			
            }
            		
            } catch (Exception e){
            	Log.e("JSON", "JSON error...", e);
            	}
        	}
            
           } catch (Exception e) { Log.e("RPC","Exception", e); }
        
		   return null;
        
	}

	@Override
	protected void onPostExecute(ArrayList<Message> result) {
		Log.i("kjk", "j,n,");
		if(progress.isShowing()) 
			progress.dismiss();

				activiti.loadList(messages, 0);

		
	}

	
 
}