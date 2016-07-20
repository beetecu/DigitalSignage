package zumzum.app.zunzuneando.visor.contents;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

public class ContentLivingCity extends Content {
	
	
	private String _woeid;
	private String _place;
	 private String _address;
	    
	    private List<String> _time_in;
	    
	    private List<String> _time_out;
	    
	    private int _windX;
	    
	    private int _windY;
	    
	    private int _windW;
	    
	    private int _windH;
	    
	    private String _effect;
	    
	    private boolean _isManual;
	    
	    private boolean _isOperative;
	    
	    private long _refrestime;
	    
	    private String _id;
	    


	public ContentLivingCity(String source, String theme){
		
		super(source, theme);
		
		//Log.e("ContentLivingCity", source);
				
		this._time_in =  new ArrayList<String>();
    	this._time_out =  new ArrayList<String>();
		
	}


	public String getWoeid() {
		return _woeid;
	}


	public void setWoeid(String _woeid) {
		this._woeid = _woeid;
	}


	public String getPlace() {
		return _place;
	}


	public void setPlace(String _place) {
		this._place = _place;
	}
	
	public String getAddress() {
		return _address;
	}

	public void setAddress(String address) {
		this._address = address;
	}

	public List<String> getTime_in() {
		return _time_in;
	}

	public void setTime_in(String time_in) {
		this._time_in.add(time_in);
	}

	public List<String> getTime_out() {
		return _time_out;
	}

	public void setTime_out(String time_out) {
		this._time_out.add(time_out);
	}

	public int getWindX() {
		return _windX;
	}

	public void setWindX(int _windX) {
		this._windX = _windX;
	}

	public int getWindY() {
		return _windY;
	}

	public void setWindY(int _windY) {
		this._windY = _windY;
	}

	public int getWindW() {
		return _windW;
	}

	public void setWindW(int _windW) {
		this._windW = _windW;
	}

	public int getWindH() {
		return _windH;
	}

	public void setWindH(int _windH) {
		this._windH = _windH;
	}

	public String getEffect() {
		return _effect;
	}

	public void setEffect(String _effect) {
		this._effect = _effect;
	}


	public boolean isOperative() {
		return _isOperative;
	}

	public void setOperative(boolean isOperative) {
		this._isOperative = isOperative;
	}

	public long getRefrestime() {
		return _refrestime;
	}

	public void setRefrestime(long refrestime) {
		this._refrestime = refrestime;
	}

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}
	


}