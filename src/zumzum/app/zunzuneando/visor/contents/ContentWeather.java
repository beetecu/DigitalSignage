package zumzum.app.zunzuneando.visor.contents;

import android.graphics.Bitmap;

public class ContentWeather extends Content {


    private String _lat;
	private String _lon;
	
	public ContentWeather(String source, String theme){
		
		super(source, theme);
				
		
	}

	public String getLat() {
		return _lat;
	}

	public void setLat(String lat) {
		this._lat = lat;
	}

	public String getLon() {
		return _lon;
	}

	public void setLon(String lon) {
		this._lon = lon;
	}




}
