package zumzum.app.zunzuneando.visor.contents;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.ads.Ads;
import zumzum.app.zunzuneando.visor.livingCities.LivingCities;
import zumzum.app.zunzuneando.visor.rssreader.RSSReader;
import zumzum.app.zunzuneando.visor.rssreader.Rss;
import zumzum.app.zunzuneando.visor.videoTV.VideoTV;
import zumzum.app.zunzuneando.visor.weather.Weather;
import zumzum.app.zunzuneando.visor.youtube.Youtube;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ContentsPlanner {

	private static final String TAG = "ContentsPlanner";

	Context _context;

	public List<Visor> contentsVisor;

	//public List<AdsVideos> _adsList;

	//public ScreenInfo _screenInfo;

	private boolean isReady;

	VideoTV _videos;
	LivingCities _livingCities;
	Weather _weather;
	Rss _rss;
	
	Ads _advertising;
	
	Youtube _youtube;

	int indexTV;
	int indexL;
	int indexR;
	int indexA;
	int indexY;
	
	private boolean isInit;

	private ContentsLoader contents;

	public ContentsPlanner(Context context) {

		_context = context;

		contentsVisor = new ArrayList<Visor>();

		_videos = new VideoTV(_context);
		
		
		_advertising = new Ads(_context);


		_weather = new Weather(_context);

		indexTV = 0;
		indexL = 0;
		indexR = 0;
		indexA = 0;
		indexY = 0;
		
		isInit = true;
		
		keepUpdatedContents(); 

	}



	Handler update = new Handler() {
		public void handleMessage(Message msg) {


			updateContents2Show();


		};
	};



	public void updateContents2Show(){
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {


		try{

			isReady = false;

			long time_counter = 0;
			long time_max = 1*60*1000;
			
			
			if (isInit){
				//contents.cityContents.download();
				isInit = false;
			}
			
			
			
			while((time_counter < time_max)){
				
				//Log.e(TAG, "new youtube");
				
				//_youtube = new Youtube(_context);
				
				//Log.e(TAG, "indexY= "+ String.valueOf(indexY));
								
				//_youtube.Prepare(contents.contentYoutube.get(indexY));
								
				//contentsVisor.add(_youtube);
				
				//time_counter =  time_counter + _youtube.getDuration();
				
				//Log.e(TAG, "new Ads");
				
				//_advertising = new Ads(_context);
				
				//_advertising.Prepare(contents.adsContents.get(indexA));
				
				//contentsVisor.add(_advertising);
				
				//time_counter =  time_counter + _advertising.getDuration();
				
				
				
				//Log.e(TAG, "new rss");
				
				//_rss = new Rss(_context);

				//Log.e(TAG, contents.rssContents.get(indexR).getTheme());
				
				//_rss.setSource(contents.rssContents.getSource2Show("Belleza"));
				//_rss.setSource(contents.rssContents.getSource2Show(indexR));

				//_rss.Prepare();

				//contentsVisor.add(_rss);
				
				//time_counter =  time_counter + _rss.getDuration();
				
				
				Log.e(TAG, "new item city");
				
				_livingCities = new LivingCities(_context);
				
				
				String source = contents.cityContents.getSource2Show();
				
				
				if (source == null)
					source = contents.cityContents.getEmergencyFile();

				Log.e(TAG, source);
				_livingCities.setSource(source);
				
				
				_livingCities.Prepare();
				
				
				contentsVisor.add(_livingCities);
				
				
				time_counter =  time_counter + _livingCities.getDuration();
				
				
				//indexR++;

				//if (indexR >= contents.rssContents._rssContents.size()){
				//	indexR = 0;
				//}
				
				//if (_advertising.ad.getSource().length() <2){
					
					//contentsVisor.add(_advertising);
					
					//time_counter =  time_counter + _advertising.getDuration();
				//}
				
				
				

				/*
				Log.e(TAG, "new item video");

				_videos.Prepare(contents.tvContents.get(indexTV));
				contentsVisor.add(_videos);

				time_counter =  time_counter + _videos.getDuration();
				

				
				indexTV++;

				if (indexTV >= contents.tvContents.size()){
					indexTV = 0;
				}

				indexL++;

				
				indexR++;

				if (indexR >= contents.rssContents._rssContents.size()){
					indexR = 0;
				}
				
				indexA++;

				if (indexA >= contents.adsContents.size()){
					indexA = 0;
				}
				
				indexY++;

				if (indexY >= contents.contentYoutube.size()){
					indexY = 0;
				}*/
			}
			
			//_weather.setWeatherContent(contents.contentWeather);

			//_weather.Prepare();
			
			//contentsVisor.add(_weather);

			//time_counter =  time_counter + _weather.getDuration();
			

			isReady = true;



		}catch(Exception e){

			Log.e(TAG, e.getMessage());
		}
		
}
			
		}); 
		th.start();
	}




	// falta poner el stop
	Timer t;
	private Handler handler;
	private void keepUpdatedContents() {
		
		handler = new Handler();

		// Declare the timer
		t = new Timer();
		// Set the schedule function and rate
		t.scheduleAtFixedRate(new TimerTask() {
			
			
			
			@Override
			public void run() {
				
				Runnable runnable = new Runnable() {
		           

					@Override
		            public void run() {
		                handler.post(new Runnable() { // This thread runs in the UI
		                    @Override
		                    public void run() {
		                    	Log.e(TAG, "keepUpdatedContents " + String.valueOf(contentsVisor.size()));

		        				if ((contentsVisor.size() < 2) && (ContentsManager._contentsLoader.isReady())){

		        					
		        					setContents(ContentsManager._contentsLoader);

		        					updateContents2Show();

		        				}
		                    }
		                });
		            }
		        };
		        new Thread(runnable).start();
				
				
				



			}

		},
		// Set how long before to start calling the TimerTask (in milliseconds)
		0,
		// Set the amount of time between each execution (in
		// milliseconds)
		500 * 60 * 1);
	}






	public ContentsLoader getContents() {
		return contents;
	}

	public void setContents(ContentsLoader contents) {
		
		
		this.contents = contents;
		

		
		
	}

}