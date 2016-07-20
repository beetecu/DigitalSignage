package zumzum.app.zunzuneando;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import zumzum.app.zunzuneando.util.LogSaver;
import zumzum.app.zunzuneando.util.SystemUiHider;
import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.ads.Ads;
import zumzum.app.zunzuneando.visor.contents.ContentsManager;
import zumzum.app.zunzuneando.visor.livingCities.LivingCities;
import zumzum.app.zunzuneando.visor.rssreader.Rss;
import zumzum.app.zunzuneando.visor.videoTV.VideoTV;
import zumzum.app.zunzuneando.visor.youtube.Youtube;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import io.vov.vitamio.LibsChecker;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ZunzuneandoActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	private static final String TAG = "ZunzuneandoActivity";

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private Context _context;

	ContentsManager contentsManager;
	
	public LogSaver _logSaver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!LibsChecker.checkVitamioLibs(this))
			return;

		setContentView(R.layout.zunzuneando);


		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.


		_context = this.getBaseContext();

		CopyReadAssets("reportajes.mp4");
		CopyReadAssets("cities.mp4");
		CopyReadAssets("eltiempo.mp4");
		CopyReadAssets("noticias.mp4");

		//create file system

		File folder = new File(Environment.getExternalStorageDirectory()+ "/BarrioTV");
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}

		folder = new File(Environment.getExternalStorageDirectory()+ "/BarrioTV/Living");
		success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		folder = new File(Environment.getExternalStorageDirectory()+ "/BarrioTV/RSS");
		success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}

		folder = new File(Environment.getExternalStorageDirectory()+ "/BarrioTV/Ads");
		success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}		
		

		folder = new File(Environment.getExternalStorageDirectory()+ "/BarrioTV/data");
		success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}

		folder = new File(Environment.getExternalStorageDirectory()+ "/BarrioTV/log");
		success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}


		
		_logSaver = new LogSaver(_context);
		
		Log.e(TAG, "create");
		
		_logSaver.save();

		contentsManager = new ContentsManager(_context);
		contentsManager.run();
		WaitForContents();


		//handlestart.post(startToWork);

		//showContent();

	}




	private int currentIndex;



	final Handler handlestart = new Handler();	

	final Runnable startToWork = new Runnable(){
		public void run(){
			//Toast.makeText(RunnableTestActivity.this, "Este es un hilo en background", Toast.LENGTH_SHORT).show();

			Log.e("Desktop", "startToWork");

			boolean flagstart = true;


			while (flagstart){

				//Log.e("Position", "wait for contents");

				if ((contentsManager._contentsPlanner.contentsVisor.size() > 0)){

					flagstart = false;
				}
				else{

					try {
						Thread.sleep(1000*60);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}
	};


	public void WaitForContents(){
		
		try{

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {

				_logSaver.save();
				
				while (contentsManager._contentsPlanner.contentsVisor.size() < 1){

					Log.e("show contents", "wait for contentList");

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						e.printStackTrace();
					}
				}

				_logSaver.save();
				
				Log.e("show contents", "ready");

				_logSaver.save();
				handler.post(showContentsH);

			}
		}); 
		th.start();
		
		}catch(Exception e){
			e.printStackTrace();
			_logSaver.save();
		}





	}

	final Handler handler = new Handler(); 
	final Runnable showContentsH = new Runnable() {
		@Override
		public void run() {

			Log.e("aqui", "aquiiiiiiiiiiiiii");

			boolean flag = true;

			showContent();



		}
	};



	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){

		try{
			
		
		super.onActivityResult(requestCode, resultCode, data);

		Log.e("Position", "onActivityResult");


		_logSaver.save();

		//showContent();
		//handler.post(showContentsH);
		WaitForContents();
	}catch(Exception e){
		e.printStackTrace();
		_logSaver.save();
	}

	}



	@SuppressWarnings("null")
	public void showContent(){


		try{

		boolean flag = true;

		this.currentIndex = 0;

		//while (flag){

		

		if (this.currentIndex >= contentsManager._contentsPlanner.contentsVisor.size())
			this.currentIndex = 0;
		
		Log.e("Position", "showContent()");

		//if (contentsManager._contentsPlanner.contentsVisor.get(this.currentIndex).isReady()){

			Visor content = contentsManager._contentsPlanner.contentsVisor.get(this.currentIndex);

			Log.e("Class", content.getClass().toString());

			//if (content.isReady()){

				flag = false;

				String current_classString = content.getClass().toString();

				if (current_classString.contains("Video")){

					showVideo(content);
				}
				if (current_classString.contains("LivingCities")){

					Log.e("Main", "showLivingCities");

					showLivingCities(content);
				}

				if (current_classString.contains("Weather")){

					showWeather(content);
				}

				if (current_classString.contains("Rss")){

					showRssReader(content);
				}

				if (current_classString.contains("Ads")){

					showAds(content);
				}

				if (current_classString.contains("Youtube")){

					showYoutube(content);
				}





			//}
		/*
			else{
				
				Log.e("Position", "showContent()2");

				if (contentsManager._contentsPlanner.contentsVisor.get(0).isError()){

					Log.e("Activity main","Borrar file erroneo");
					contentsManager._contentsPlanner.contentsVisor.remove(0);
				}
				else{

					this.currentIndex++;
				}

			}*/


		//}
		}catch(Exception e){
			e.printStackTrace();
			_logSaver.save();
		}


		//}

	}

	private void showVideo(Visor content) {
		// TODO Auto-generated method stub


		try{

			final VideoTV video = (VideoTV) content;

			contentsManager._contentsPlanner.contentsVisor.remove(this.currentIndex);

			//new Thread(new Runnable() {
			//public void run() {

			video.Play();

			Log.e("ZunzuneandoActivity", "Error opening VideoTV");

			try{

				startActivityForResult(video.lVideoIntent, 1);

			}catch(Exception e){

				Log.e("ZunzuneandoActivity", "Error opening VideoTV1");
				try {
					//this.finalize();
					WaitForContents();
				} catch (Throwable ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}


			}


		}


		catch(Exception e){

			Log.e("ZunzuneandoActivity", "Error opening VideoTV2");

		}



	}

	private void showLivingCities(Visor content) {
		// TODO Auto-generated method stub


		try{

			final LivingCities city = (LivingCities) content;


			contentsManager._contentsPlanner.contentsVisor.remove(this.currentIndex);

			//new Thread(new Runnable() {
			//public void run() {

			city.Play();

			try{

				startActivityForResult(city.lVideoIntent, 1);

			}catch(Exception e){
				
				_logSaver.save();

				Log.e("ZunzuneandoActivity", "Error opening LivingCities1");
				try {
					//this.finalize();
					WaitForContents();
				} catch (Throwable ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
					WaitForContents();
				}


			}
			
			_logSaver.save();


		}
		
		


		catch(Exception e){

			_logSaver.save();
			Log.e("ZunzuneandoActivity", "Error opening LivingCities2");
			WaitForContents();

		}

	}

	private void showRssReader(Visor content) {
		// TODO Auto-generated method stub


		try{

			final Rss rss = (Rss) content;

			contentsManager._contentsPlanner.contentsVisor.remove(this.currentIndex);


			//new Thread(new Runnable() {
			//public void run() {

			rss.Play();



			Log.e("ZunzuneandoActivity", "Error opening Rss");

			try{

				startActivityForResult(rss.lVideoIntent, 1);

			}catch(Exception e){

				Log.e("ZunzuneandoActivity", "Error opening Rss1");
				try {
					//this.finalize();
					WaitForContents();
				} catch (Throwable ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}


			}

		}


		catch(Exception e){

			Log.e("ZunzuneandoActivity", "Error opening Rss2");

		}



	}

	private void showWeather(Visor content) {
		// TODO Auto-generated method stub


		try{

			final zumzum.app.zunzuneando.visor.weather.Weather weather = (zumzum.app.zunzuneando.visor.weather.Weather) content;

			contentsManager._contentsPlanner.contentsVisor.remove(this.currentIndex);

			//new Thread(new Runnable() {
			//public void run() {

			weather.Play();



			Log.e("ZunzuneandoActivity", "Error opening weather");

			try{

				startActivityForResult(weather.lVideoIntent, 1);

			}catch(Exception e){

				Log.e("ZunzuneandoActivity", "Error opening weather1");
				try {
					//this.finalize();
					WaitForContents();
				} catch (Throwable ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}


			}

		}


		catch(Exception e){

			Log.e("ZunzuneandoActivity", "Error opening Weather2");

		}



	}

	private void showAds(Visor content) {
		// TODO Auto-generated method stub

		try{

			final Ads ad = (Ads) content;

			contentsManager._contentsPlanner.contentsVisor.remove(this.currentIndex);

			//new Thread(new Runnable() {
			//public void run() {

			ad.Play();

			Log.e("ZunzuneandoActivity", "Error opening ad");

			try{

				startActivityForResult(ad.lVideoIntent, 1);

			}catch(Exception e){

				Log.e("ZunzuneandoActivity", "Error opening ad1");
				try {
					//this.finalize();
					WaitForContents();
				} catch (Throwable ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}


			}

		}


		catch(Exception e){

			Log.e("ZunzuneandoActivity", "Error opening ad2");

		}

	}

	private void showYoutube(Visor content) {
		// TODO Auto-generated method stub

		try{

			final Youtube video = (Youtube) content;

			contentsManager._contentsPlanner.contentsVisor.remove(this.currentIndex);

			video.Play();

			Log.e("ZunzuneandoActivity", "Error opening weather");

			try{

				startActivityForResult(video.lVideoIntent, 1);

			}catch(Exception e){

				Log.e("ZunzuneandoActivity", "Error opening weather1");
				try {
					//this.finalize();
					WaitForContents();
				} catch (Throwable ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}


			}

		}


		catch(Exception e){

			Log.e("ZunzuneandoActivity", "Error opening weather2");

		}


	}


	private void CopyReadAssets(String filename) {
		AssetManager assetManager = getAssets();

		InputStream in = null;
		OutputStream out = null;

		Log.e("fd", getFilesDir().toString());

		File file = new File(getFilesDir(), filename);
		try {
			in = assetManager.open(filename);
			out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
	
	
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first

	    Log.e(TAG, "onResume");

	    _logSaver.save();
	}
	
	@Override
	public void onStop() {
	    super.onStop();  // Always call the superclass method first

	    Log.e(TAG, "onStop");

	    _logSaver.save();
	}


}
