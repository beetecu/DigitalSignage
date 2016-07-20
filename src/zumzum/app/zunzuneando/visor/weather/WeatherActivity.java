
package zumzum.app.zunzuneando.visor.weather;


import android.annotation.TargetApi;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import zumzum.app.zunzuneando.R;
import zumzum.app.zunzuneando.util.SystemUiHider;
import zumzum.app.zunzuneando.visor.weather.MyLog;
import zumzum.app.zunzuneando.visor.weather.WeatherInfo;
import zumzum.app.zunzuneando.visor.weather.WeatherActivity.MalibuCountDownTimer;
import zumzum.app.zunzuneando.visor.weather.WeatherInfo.ForecastInfo;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class WeatherActivity extends Activity {
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

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private ImageView iCurrentCond;
	private TextView  tCurrentTemp;
	private TextView tCondition;
	private TextView tCurrentCity;
	
	private TextView tday1;
	private TextView tday1Min;
	private TextView tday1Max;
	private ImageView iday1;

	private TextView tday2;
	private TextView tday2Min;
	private TextView tday2Max;
	private ImageView iday2;	
	
	private TextView tday3;
	private TextView tday3Min;
	private TextView tday3Max;
	private ImageView iday3;

	private TextView tday4;
	private TextView tday4Min;
	private TextView tday4Max;
	private ImageView iday4;	
	
	private long duration;

	private final long interval = 1000;
	public MalibuCountDownTimer countDownTimer;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.weather);
		
		

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		
		final VideoView mVideoView = (VideoView) findViewById(R.id.videoView1);
		mVideoView.setVisibility(View.VISIBLE);
		

		 //int id = getResources().getIdentifier(videoName, "raw", this.getPackageName());

		 //final String path = "android.resource://" + this.getPackageName() + "/" + id;
		
		final TextView mTextView;
		mTextView = (TextView) findViewById(R.id.fullscreen_content);
		mTextView.setSelected(true);
		mTextView.setVisibility(View.GONE);
		
		duration = (Long) getIntent().getExtras().getSerializable("Duration");
		
		String introfile = getFilesDir() + "/" + "eltiempo.mp4";

		 mVideoView.setVideoPath(introfile);
		 
		 mVideoView.setVideoURI(Uri.parse(introfile));
		 
		 final RelativeLayout lRelLayoutplayer = (RelativeLayout) findViewById(R.id.screen);
		 lRelLayoutplayer.setVisibility(View.GONE);
		 
	     //mVideoView.requestFocus();
	     mVideoView.start();
	     
	     WeatherInfo weatherInfo = Weather._weatherInfo;

	        MyLog.init(getApplicationContext());
	        
	        iCurrentCond = (ImageView) findViewById(R.id.icurrent);
	        tCurrentTemp = (TextView) findViewById(R.id.textCurrentTemp);
	        tCondition = (TextView) findViewById(R.id.tcondicion);
	        tCurrentCity = (TextView) findViewById(R.id.tcity);
	        
	    	
	    	tday1  = (TextView) findViewById(R.id.tday1);
	    	tday1Min  = (TextView) findViewById(R.id.tday1min);
	    	tday1Max  = (TextView) findViewById(R.id.tday1max);
	    	iday1 =  (ImageView) findViewById(R.id.iday1);
	        
	    	
	    	tday2  = (TextView) findViewById(R.id.tday2);
	    	tday2Min  = (TextView) findViewById(R.id.tday2min);
	    	tday2Max  = (TextView) findViewById(R.id.tday2max);
	    	iday2 =  (ImageView) findViewById(R.id.iday2);
	       
	    	tday3  = (TextView) findViewById(R.id.tday3);
	    	tday3Min  = (TextView) findViewById(R.id.tday3min);
	    	tday3Max  = (TextView) findViewById(R.id.tday3max);
	    	iday3 =  (ImageView) findViewById(R.id.iday3);
	        
	    	tday4  = (TextView) findViewById(R.id.tday4);
	    	tday4Min  = (TextView) findViewById(R.id.tday4min);
	    	tday4Max  = (TextView) findViewById(R.id.tday4max);
	    	iday4 =  (ImageView) findViewById(R.id.iday4);    
	    	
	     
	    	setWeatherInfo(weatherInfo);

		 
		 mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			    @Override
			    public void onCompletion (MediaPlayer mp) {
			        // your code to clean up and finish the activity...
			    	
			    	mVideoView.setVisibility(View.GONE);
			    	mTextView.setVisibility(View.VISIBLE);
			    	lRelLayoutplayer.setVisibility(View.VISIBLE);
		
	
			    }
			});


	

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		//findViewById(R.id.dummy_button).setOnTouchListener(
		//		mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	public void setWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
		
        if (weatherInfo != null) {
        	
        	Log.e("Pos", "gotWeatherInfo");
        	tCurrentCity.setText( weatherInfo.getLocationCity());
        	tCurrentTemp.setText((Integer.toString(weatherInfo.getCurrentTempF())) + "º");
        	tCondition.setText("  " + weatherInfo.getCurrentText());
			
			if (weatherInfo.getCurrentConditionIcon() != null) {
				iCurrentCond.setImageBitmap(weatherInfo.getCurrentConditionIcon());
			}
			
			ForecastInfo forecastInfo;
			//Forecast day1
			forecastInfo = weatherInfo.getForecastInfoList().get(0);
			tday1.setText(forecastInfo.getForecastDay());
	    	tday1Min.setText(Integer.toString(forecastInfo.getForecastTempLowF())+ "º");
	    	tday1Max.setText(Integer.toString(forecastInfo.getForecastTempHighF())+ "º");
	    	
	    	if (forecastInfo.getForecastConditionIcon() != null) {
	    		iday1.setImageBitmap(forecastInfo.getForecastConditionIcon());
			}

	    	
			//Forecast day2
			forecastInfo = weatherInfo.getForecastInfoList().get(1);
			tday2.setText(forecastInfo.getForecastDay());
	    	tday2Min.setText(Integer.toString(forecastInfo.getForecastTempLowF())+ "º");
	    	tday2Max.setText(Integer.toString(forecastInfo.getForecastTempHighF())+ "º");
	    	
	    	if (forecastInfo.getForecastConditionIcon() != null) {
	    		iday2.setImageBitmap(forecastInfo.getForecastConditionIcon());
			}	    	

			//Forecast day3
			forecastInfo = weatherInfo.getForecastInfoList().get(2);
			tday3.setText(forecastInfo.getForecastDay());
	    	tday3Min.setText(Integer.toString(forecastInfo.getForecastTempLowF())+ "º");
	    	tday3Max.setText(Integer.toString(forecastInfo.getForecastTempHighF())+ "º");
	    	
	    	if (forecastInfo.getForecastConditionIcon() != null) {
	    		iday3.setImageBitmap(forecastInfo.getForecastConditionIcon());
			}	    	

			//Forecast day4
			forecastInfo = weatherInfo.getForecastInfoList().get(3);
			tday4.setText(forecastInfo.getForecastDay());
	    	tday4Min.setText(Integer.toString(forecastInfo.getForecastTempLowF())+ "º");
	    	tday4Max.setText(Integer.toString(forecastInfo.getForecastTempHighF())+ "º");
	    	
	    	if (forecastInfo.getForecastConditionIcon() != null) {
	    		iday4.setImageBitmap(forecastInfo.getForecastConditionIcon());
			}	
	    	
	    	countDownTimer = new MalibuCountDownTimer(duration, interval);
			countDownTimer.start();
			
				    	
			    	
			/*
			for (int i = 0; i < YahooWeather.FORECAST_INFO_MAX_SIZE; i++) {
				final LinearLayout forecastInfoLayout = (LinearLayout) 
						getLayoutInflater().inflate(R.layout.forecastinfo, null);
				final TextView tvWeather = (TextView) forecastInfoLayout.findViewById(R.id.textview_forecast_info);
				final ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);
				tvWeather.setText("====== FORECAST " + (i+1) + " ======" + "\n" +
				                   "date: " + forecastInfo.getForecastDate() + "\n" +
				                   "weather: " + forecastInfo.getForecastText() + "\n" +
						           "low  temperature in ��C: " + forecastInfo.getForecastTempLowC() + "\n" +
				                   "high temperature in ��C: " + forecastInfo.getForecastTempHighC() + "\n" +
						           "low  temperature in ��F: " + forecastInfo.getForecastTempLowF() + "\n" +
				                   "high temperature in ��F: " + forecastInfo.getForecastTempHighF() + "\n"
						           );
				final ImageView ivForecast = (ImageView) forecastInfoLayout.findViewById(R.id.imageview_forecast_info);
				if (forecastInfo.getForecastConditionIcon() != null) {
					ivForecast.setImageBitmap(forecastInfo.getForecastConditionIcon());
				}
				mWeatherInfosLayout.addView(forecastInfoLayout);
			}*/
        } else {
        	//setNoResultLayout();
        }
	}


	
	// CountDownTimer class
	public class MalibuCountDownTimer extends CountDownTimer
	{


		public MalibuCountDownTimer(long duration, long interval)
		{
			super(duration, interval);
		}

		@Override
		public void onFinish()
		{
			Log.e("TAG", "Time's up!");


				Log.e("TAG", "statusShort");

				cancel();
				
				finish();
			

			

		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			//text.setText("Time remain:" + millisUntilFinished);
			//timeElapsed = duration - millisUntilFinished;
			//timeElapsedView.setText("Time Elapsed: " + String.valueOf(timeElapsed));
		}
	}

}



