package fr.univpau.projet_waam;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

public class SendMessage extends AsyncTask<Void, Void, Void> {

	HttpClient client;
	HttpPost request;
    private ProgressDialog progress;
    
    private Class PrincipalPageActivity;

	private Activity_message_edit context;
    
    
   // Activity context;
	
	public SendMessage(Activity_message_edit context, HttpClient client, HttpPost request,Class RetourPagePrincipal) {
		this.client = client;
		this.request = request;
		this.context = context;
		
		this.PrincipalPageActivity = RetourPagePrincipal;
		
		this.progress = new ProgressDialog(this.context);
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			HttpResponse response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	 @Override
	    protected void onPreExecute() {                           
	        progress.setTitle("Veuillez patienter");
	        progress.setMessage("Envoi en cours...");
	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);      
	        progress.show();
	    }

	@Override
	protected void onPostExecute(Void result) {
				if(progress.isShowing()) 
					progress.dismiss();
				
				
				//retrour a la page principal
				Intent intent2 = new Intent(context,PrincipalPageActivity);
				// On retourne le résultat avec l'intent
				intent2.putExtra("MessageClass", "LOL");
				context.setResult(context.RESULT_OK, intent2);
				//terminer cette activité
				context.finish();

	}
	
}
