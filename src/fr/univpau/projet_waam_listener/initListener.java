package fr.univpau.projet_waam_listener;

import fr.univpau.projet_waam.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.Spinner;

public class initListener implements OnClickListener {

	
	private Spinner genreUser;
	private int seekBarValue;
	
	private MainActivity ecranInit;
	private Class ecranSuivante;
	
	//private Bundle extrasData = new Bundle();
	
	public initListener(MainActivity ecranInit, Spinner choix1, Class cible1) {
		super();
		this.genreUser = choix1;
		this.ecranInit = ecranInit;
		this.ecranSuivante = cible1;
		//this.seekBarValue = MainActivity._progressChanged;
	}

	@Override
	public void onClick(View v) {
		
		this.seekBarValue = MainActivity._progressChanged;

		Intent intent = new Intent(ecranInit,ecranSuivante);

		Log.i("**Rayon ooo=", String.valueOf(seekBarValue));
		
		//on envois les donnees sup
	
		intent.putExtra("EXTRA_SEXE",genreUser.getSelectedItem().toString());
		intent.putExtra("EXTRA_RAYON",seekBarValue);
		
		//intent.setClass(ecranInit, ecranSuivante);
		ecranInit.startActivity(intent);
	}

}
