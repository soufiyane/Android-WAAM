package fr.univpau.projet_waam;

import java.io.Serializable;

public class Message implements Serializable  {
	
	private String sexe;
	private String message;
	private String datePost;
	private String DistanceFromMe;
	private double _geo[] = new double[2];;
	
	public Message(String sexe, String message, String datePost,
			String distanceFromMe) {
		this.sexe = sexe;
		this.message = message;
		this.datePost = datePost;
		this.DistanceFromMe = distanceFromMe;
	}

	public double[] get_geo() {
		return _geo;
	}
	
	public String getSexe() {
		return sexe;
	}


	public void setSexe(String sexe) {
		this.sexe = sexe;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getDatePost() {
		return datePost;
	}


	public void setDatePost(String datePost) {
		this.datePost = datePost;
	}


	public String getDistanceFromMe() {
		return DistanceFromMe;
	}


	public void setDistanceFromMe(String distanceFromMe) {
		DistanceFromMe = distanceFromMe;
	}
	
	public void set_geo(double[] geo) {
		this._geo[0] = geo[0];
		this._geo[1] = geo[1];
	}
	
}
