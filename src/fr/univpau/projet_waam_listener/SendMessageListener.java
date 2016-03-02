package fr.univpau.projet_waam_listener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import fr.univpau.projet_waam.Activity_message_edit;
import fr.univpau.projet_waam.GPSTracker;
import fr.univpau.projet_waam.PrincipalPageActivity;
import fr.univpau.projet_waam.SendMessage;

public class SendMessageListener implements OnClickListener {

	public static final String URL = "http://www.iut-adouretud.univ-pau.fr/~olegoaer/waam/newMessage.php";



	//private Activity context;
	private EditText msgEditText;
	private TextView countTxt;
	private Class PrincipalPageActivity;
	private Activity_message_edit context;

	GPSTracker gps;
	private int gender;
	private String longitude;
	private String latitude;



	public SendMessageListener(Activity_message_edit context, EditText messageEditText,	int gender,TextView txtcount, Class RetourPagePrincipal) {
		this.context = context;
		this.msgEditText = messageEditText;
		this.msgEditText.addTextChangedListener(mTextEditorWatcher);

		this.gender = gender;
		this.countTxt = txtcount;

		this.PrincipalPageActivity = RetourPagePrincipal;
	}



	@Override
	public void onClick(View v) {
		HttpClient client = new DefaultHttpClient();		
		HttpPost request = new HttpPost(URL);
		List<NameValuePair> msg = new ArrayList<NameValuePair>();


		//Recuperation de la position actuel
		gps = new GPSTracker(context);
		if(gps.canGetLocation()) {
			this.longitude = String.valueOf(gps.getLongitude());
			this.latitude = String.valueOf(gps.getLatitude());
		}



		msg.add(new BasicNameValuePair("my_latitude", latitude));//"43.3079985"));
		msg.add(new BasicNameValuePair("my_longitude",longitude));//"-0.3319874" ));
		msg.add(new BasicNameValuePair("my_message", msgEditText.getText().toString()));
		msg.add(new BasicNameValuePair("my_gender", Integer.toString(gender)));
		try {
			request.setEntity(new UrlEncodedFormEntity(msg));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		Toast.makeText(context, "Message à été bien envoyé",Toast.LENGTH_LONG).show();

		new SendMessage(context ,client, request,PrincipalPageActivity.class).execute();



	}

	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

			countTxt.setText("Nombre de caratères = "+String.valueOf(s.length()+" /140"));
		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	};

}
