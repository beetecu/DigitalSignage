package zumzum.app.zunzuneando.visor.rssreader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.TextView;

import zumzum.app.zunzuneando.R;
import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.contents.ContentRSS;

public class Rss extends Visor{

	protected static final String TAG = "RSS";
	
	private String _source;
	

	public Rss(Context context) {
		super(context);
		// TODO Auto-generated constructor stub



		this.setDuration(40*1000); // duracion en milisegundos


	}

	@Override
	public void Prepare() {
		

		_ready = true;

	}

	
	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return _ready;
	}

	@Override
	public void Play() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub




		// lVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		lVideoIntent = new Intent(_context, zumzum.app.zunzuneando.visor.rssreader.RssActivity.class);


		Bundle bundle = new Bundle();
		bundle.putSerializable("Term", this.getSource());
		bundle.putSerializable("Duration", this.getDuration());

		this.lVideoIntent.putExtras(bundle);

		// context.startActivity(lVideoIntent);
		// context.startActivityForResult(lVideoIntent,1);

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

	
}
