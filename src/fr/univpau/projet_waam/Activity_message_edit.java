package fr.univpau.projet_waam;

import java.util.ArrayList;

import fr.univpau.projet_waam_listener.MessageListener;
import fr.univpau.projet_waam_listener.SendMessageListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Activity_message_edit extends Activity {

	private int _genre = 0;
	Button button_SendMesss;
	EditText messAdd;
	TextView countTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saisie_message);

		//recuperation des valeurs reçu Sexe et rayon
		Intent intent = getIntent();
		_genre= intent.getIntExtra("EXTRA_SEXE",1);
		Log.i("GenreRessu=", String.valueOf(_genre));

		messAdd = (EditText) findViewById(R.id.editTextMess);		
		button_SendMesss = (Button) findViewById(R.id.buttonEnvoyerMess);
		countTextView = (TextView) findViewById(R.id.textViewCount);

		//button_SendMesss.setOnClickListener(new MessageListener(Activity_message_edit.this,messAdd,PrincipalPageActivity.class));
		button_SendMesss.setOnClickListener(new SendMessageListener(Activity_message_edit.this,messAdd,_genre,countTextView,PrincipalPageActivity.class));
	}

}
