 package zumzum.app.zunzuneando.visor.livingCities;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.contents.ContentLivingCity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LivingCities extends Visor{



	String TAG = "LivingCities";

	private String _source;

	public LivingCities(Context context) {
		// TODO Auto-generated constructor stub
		super(context);

		_context = context;
		
		long duration = 100*1000;

		this.setDuration(duration);
		
		this.setReady(false);
		

		

	}

	@Override
	public void Prepare() {
		// TODO Auto-generated method stub
		this.setReady(true);
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void Play() {
		// TODO Auto-generated method stub
		
		lVideoIntent = new Intent(_context, zumzum.app.zunzuneando.visor.livingCities.LivingCitiesActivity.class);


		Bundle bundle = new Bundle();
		bundle.putSerializable("Filename", this.getSource());
		bundle.putSerializable("Duration", this.getDuration());

		Log.e(TAG, "file Source: " + this.getSource());
		
		this.lVideoIntent.putExtras(bundle);

	}

	@Override
	public void Stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isError() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getSource() {
		return _source;
	}

	public void setSource(String _source) {
		this._source = _source;
	}


	////////////////////////////////////////////////////////////////////////////////////
}