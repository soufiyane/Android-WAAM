package fr.univpau.projet_waam_listener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.univpau.projet_waam.Activity_message_edit;
import fr.univpau.projet_waam.Message;
import fr.univpau.projet_waam.PrincipalPageActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class MessageListener implements OnClickListener {

	private TextView countTxt;
	private EditText textMess;
	private String position;
	private Class PrincipalPageActivity;

	private Activity_message_edit pagePrincipal;
	
	
	public MessageListener(Activity_message_edit wall,EditText textMess,TextView txtcount,Class RetourPagePrincipal) {
		super();
		this.pagePrincipal = wall;
		this.textMess = textMess;
		this.position = "null";
		this.countTxt = txtcount;
		this.PrincipalPageActivity = RetourPagePrincipal;
	}



	@Override
	public void onClick(View v) {

		if(textMess.getText() != null)
		{

		    Log.i("position get",position);
			
			
			SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			Date date = new Date();
			String dateList = formater.format(date);
			
			Message mess = new Message("1", textMess.getText().toString(), dateList, position );

			
			Intent intent2 = new Intent(pagePrincipal,PrincipalPageActivity);
			intent2.putExtra("MessageClass", mess);
			// On retourne le résultat avec l'intent
			pagePrincipal.setResult(pagePrincipal.RESULT_OK, intent2);
			
			//terminer cette activité
			pagePrincipal.finish();

		}
		
		
	}
	
	//private TextView mTextView;
	//private EditText mEditText;
	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	        }

	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	
	        	textMess.setText(String.valueOf(s.length()));
	        }
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
	};

}
