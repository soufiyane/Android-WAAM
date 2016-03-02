package fr.univpau.projet_waam;




import fr.univpau.projet_waam_listener.initListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

public class MainActivity extends Activity {

	private Spinner sexe;
	public SeekBar SeekBarDistance;
	private TextView Valdistance;
	public static int _progressChanged = 0;
	private String rayon = "null";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button  continuerButton = (Button) findViewById(R.id.continuer);
		sexe = (Spinner) findViewById(R.id.spinnerSexe);
		SeekBarDistance = (SeekBar) findViewById(R.id.seekBar1);
		Valdistance = (TextView) findViewById(R.id.textDistance);




		//recuperer les changements du seekBar
		SeekBarDistance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {


			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				_progressChanged = progress;
				rayon = String.valueOf(_progressChanged);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				rayon = String.valueOf(_progressChanged);
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				Valdistance.setText("rayon = " + _progressChanged + " metres");
				rayon = String.valueOf(_progressChanged);
			}



		});




		//on envois le genre de l'utilisateur et le rayon d'action souhaiter
		continuerButton.setOnClickListener(new initListener(this,sexe,PrincipalPageActivity.class));
	}

}
