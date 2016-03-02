package fr.univpau.projet_waam;



import java.net.URLEncoder;
import java.util.ArrayList;










import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;












import fr.univpau.projet_waam_listener.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class PrincipalPageActivity extends Activity{

	private static final String HOMME="1";
	private static final String FEMME="2";

	private static final String MSG = "items";
	private static final String TIME = "time";
	private static final String TAG_MSG = "msg";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_METERS = "meters";
	private static final String LOC = "geo";

	// URL to get parkings JSON
	public static final String URL ="http://www.iut-adouretud.univ-pau.fr/~olegoaer/waam/wallMessages.php?";
	private HttpClient client;
	private JSONObject JSON_Object;
	private JSONArray JSON_Array;
	private Location location;

	private ProgressDialog progree_Dialog;

	private int _genre = 0;
	private Integer _radius=1000;

	//Sert a verifier le code de retour d'un intent
	public static final int CODE_RETOUR = 0;

	MenuItem myStartStopPaginationItem;

	//String postion = "null";
	ListView lis_view;
	private TextView title;
	private Button btn_prev;
	private Button btn_next;


	public int TOTAL_LIST_ITEMS = 0;
	public int NUM_ITEMS_PAGE   = 15;
	private int pageCount ;

	//used to move list value
	private int increment = 0;
	private Boolean PremierFois=true;
	private String etat_pagination="ON"; //on

	ArrayAdapter<Message> sd;

	private ArrayList<Message> ListMess;
	private MessageAdapterPersistance MessAdapt;



	//Get Element list
	GPSTracker gps;
	double latitude,longitude;
	double latitudeItemSelected,longitudeItemSelected;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		//recuperation des valeurs reçu Sexe et rayon
		Intent intent = getIntent();
		_radius=intent.getIntExtra("EXTRA_RAYON", 50);
		if(intent.getStringExtra("EXTRA_SEXE").equals("HOMME"))
			_genre = 1;
		else _genre = 2;

		Log.i("RADIUS_RESSU=", String.valueOf(_radius));
		//GET POSITION GPS
		gps = new GPSTracker(PrincipalPageActivity.this);

		if(gps.canGetLocation()) {
			longitude = gps.getLongitude();
			latitude = gps.getLatitude();

			Log.i("Mon Latitude++++++++++++>",String.valueOf(latitude));
			Log.i("Mon Longitud++++++++++++>",String.valueOf(longitude));
		}
		/***************************************/

		// Objet ListMess
		ListMess = new ArrayList<Message>();


		// Objet lis_view
		lis_view = (ListView) findViewById(R.id.listView);
		btn_prev     = (Button)findViewById(R.id.prev);
		btn_next     = (Button)findViewById(R.id.next);
		title    = (TextView)findViewById(R.id.title);

		btn_prev.setEnabled(false);
		//btn_next.setEnabled(false);

		// Objet MessAdapt
		MessAdapt = new MessageAdapterPersistance(this, R.layout.activity_main3_mod, ListMess);



		lis_view.setAdapter(MessAdapt);


		lis_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				//Get the coordinate of member

				Message message = ListMess.get(position);
				double geoLoc[] = new double[2];
				geoLoc = message.get_geo();
				latitudeItemSelected = geoLoc[0];
				Log.i("Mon LatitudeSELITEM>",String.valueOf(latitudeItemSelected));

				longitudeItemSelected = geoLoc[1];
				Log.i("Mon LongitudSELECTITEM>",String.valueOf(longitudeItemSelected));

				LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
				boolean enabled = service
						.isProviderEnabled(LocationManager.GPS_PROVIDER);

				if (!enabled) {
					showAlertDialog(PrincipalPageActivity.this, "GPS turn off!!",
							"Please turn on your GPS Location", false);
				}
				else {
					Location mylocation = new Location("");
					Location dest_location = new Location("");
					dest_location.setLatitude(latitudeItemSelected);
					dest_location.setLongitude(longitudeItemSelected);
					mylocation.setLatitude(latitude);
					mylocation.setLongitude(longitude);
					float distance = mylocation.distanceTo(dest_location);
					int dist= (int)distance;

					showAlert(PrincipalPageActivity.this, "Distance To Me","Cet utilisateur se trouve à "+dist+" mètres de vous", false);

				}

			}

		});


		// Calling async task to get json


		//new GetMessages().execute();
		new GetMessages(this,latitude,longitude,_radius).execute();


		//Bouton pagination
		btn_next.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				increment++;
				loadList(ListMess,increment);
				CheckEnable();
			}
		});

		btn_prev.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				increment--;
				loadList(ListMess,increment);
				CheckEnable();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Vérification du code de retour
		if(requestCode == CODE_RETOUR) {

			// Vérifie que le résultat est OK
			if(resultCode == RESULT_OK) {

				//ajout_new_01_intent_reçu

				//ListMess.add((Message) data.getSerializableExtra("MessageClass"));//getIntent().getSerializableExtra("MessageClass"));
				//MessAdapt.notifyDataSetChanged();
				refreshActivity(location);


			}
			// Si l'activité est annulé
			else if (resultCode == RESULT_CANCELED) {

				// On affiche que l'opération est annulée
				Toast.makeText(this, "Opération annulé", Toast.LENGTH_SHORT).show();

			}

		}


		//super.onActivityResult(requestCode, resultCode, data);
	}

	public void showAlert(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		//alertDialog.setIcon((status) ? R.drawable.park : R.drawable.park);

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OPEN", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				Uri uri = Uri.parse("geo:"+latitudeItemSelected*1E6+","+longitudeItemSelected*1E6+"?q="+latitudeItemSelected*1E6+","+longitudeItemSelected*1E6);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);

			}}); 
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}}); 
		// Showing Alert Message
		alertDialog.show();
	}


	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.fail : R.drawable.fail);

		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}}); 

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Turn it On", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);

			} });

		// Showing Alert Message
		alertDialog.show();
	}


	//ACTION_BAR_MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_bar_menu, menu);

		myStartStopPaginationItem = menu.findItem(R.id.menu_PginationSwitch);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_edit:
			Intent intent = new Intent(PrincipalPageActivity.this,Activity_message_edit.class);
			intent.putExtra("EXTRA_SEXE", _genre);
			startActivityForResult(intent,CODE_RETOUR);
			return true;

		case R.id.menu_refresh:
			refreshActivity(location);
			return true;


		case R.id.menu_PginationSwitch:
			if(etat_pagination.equals("ON"))
			{				
				myStartStopPaginationItem.setIcon(R.drawable.ic_action_paginationoff);
				ShowMessOnListView(ListMess, 0);
				btn_prev.setEnabled(false);
				btn_next.setEnabled(false);
				btn_prev.setVisibility(Button.INVISIBLE);
				btn_next.setVisibility(Button.INVISIBLE);
				title.setVisibility(View.INVISIBLE);
				etat_pagination = "OFF";
			}
			else if(etat_pagination.equals("OFF")){	
				myStartStopPaginationItem.setIcon(R.drawable.ic_action_paginationon);
				loadList(ListMess,0);
				testeNbpageSupZero();
				btn_prev.setVisibility(Button.VISIBLE);
				btn_next.setVisibility(Button.VISIBLE);
				title.setVisibility(View.VISIBLE);
				etat_pagination = "ON";
			}

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}



	public void ShowMessOnListView(List<Message> messages, Integer ent){
		ListMess= new ArrayList<Message>();

		ListMess= (ArrayList<Message>)messages;

		MessAdapt = new MessageAdapterPersistance(this, R.layout.activity_main3_mod, ListMess);
		lis_view.setAdapter(MessAdapt);
		MessAdapt.notifyDataSetChanged();    

	}

	public void refreshActivity(Location l){
		MessAdapt.clear();
		if(gps.canGetLocation()) {
			longitude = gps.getLongitude();
			latitude = gps.getLatitude();

			Log.i("Mon Latitude++++++++++++>",String.valueOf(latitude));
			Log.i("Mon Longitud++++++++++++>",String.valueOf(longitude));
		}
		new GetMessages(this,latitude,longitude, _radius).execute();
	}

	/******************************************************************************/
	//GESTION DE LA PAGINATION

	public void testeNbpageSupZero()
	{
		if(pageCount>1)
		{
			btn_next.setEnabled(true);
		}
		else
		{
			btn_prev.setEnabled(false);
			btn_next.setEnabled(false);
		}
	}
	/**
	 * Method for enabling and disabling Buttons
	 */
	private void CheckEnable()
	{
		if(increment+1 == pageCount)
		{
			btn_next.setEnabled(false);
		}
		else if(increment == 0)
		{
			btn_prev.setEnabled(false);
		}
		else
		{
			btn_prev.setEnabled(true);
			btn_next.setEnabled(true);
		}
	}

	/**
	 * Method for loading data in listview
	 * @param number
	 */
	public void loadList(List<Message> messages, Integer number)
	{

		if(PremierFois==true)
		{	
			/**
			 * Calcul du nombre de pages à prévoir
			 * ====================================================
			 */

			TOTAL_LIST_ITEMS=messages.size();
			int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
			val = val==0?0:1;
			pageCount = TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
			
			CheckEnable();
			//ChargeNbPage();
			PremierFois = false;
		}

		//ListMess= new ArrayList<Message>();

		ListMess= (ArrayList<Message>)messages;


		ArrayList<Message> sort = new ArrayList<Message>();
		title.setText("Page "+(number+1)+" of "+pageCount);

		int start = number * NUM_ITEMS_PAGE;
		for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
		{
			if(i<ListMess.size())
			{
				sort.add(ListMess.get(i));
			}
			else
			{
				break;
			}
		}
		sd = new MessageAdapterPersistance(this, R.layout.activity_main3_mod,sort);
		lis_view.setAdapter(sd);
		MessAdapt.notifyDataSetChanged();
	}


}


