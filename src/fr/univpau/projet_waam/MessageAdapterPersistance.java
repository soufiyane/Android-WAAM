package fr.univpau.projet_waam;


import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageAdapterPersistance extends ArrayAdapter<Message>{

	public MessageAdapterPersistance(Context context, int textViewResourceId, List<Message> objects) {
		super(context, textViewResourceId, objects);
		
		Log.i("Task_adapter", "task adapter");
	}


	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.i("getView", "inside getView");
		
		Message message = getItem(position);

		if(convertView==null)
		{
			Log.i("convertView", "convertVieww == null");
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main3_mod, null);
		}
		
		LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.LinearLayoutText);
		
		ImageView image = (ImageView) convertView.findViewById(R.id.imaggenre1);
		
		TextView messText = (TextView) convertView.findViewById(R.id.textGras);
		TextView date = (TextView) convertView.findViewById(R.id.textDate);
		TextView positionDis = (TextView) convertView.findViewById(R.id.textPosition);
		
		if(message.getSexe().equals("2")){
				image.setImageResource(R.drawable.female);
				//layout.setBackgroundColor(R.color.pinkLigth);
				
		}
		messText.setText(message.getMessage());
		date.setText(message.getDatePost());
		positionDis.setText(message.getDistanceFromMe());
		
		
		return convertView;
	}

	
}
