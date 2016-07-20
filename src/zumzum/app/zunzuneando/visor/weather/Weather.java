package zumzum.app.zunzuneando.visor.weather;



import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.contents.ContentWeather;
import zumzum.app.zunzuneando.visor.weather.YahooWeather.SEARCH_MODE;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Weather extends Visor implements YahooWeatherInfoListener {
	
	private static final String TAG = "Weather";

	public static WeatherInfo _weatherInfo;
	
	private YahooWeather mYahooWeather = YahooWeather.getInstance();

    
    private String lat;
	private String lon;

	public Weather(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		
		Log.e(TAG, "weather0");
		
		_weatherInfo = null;
		
		this._ready=false;
		
		//setParams();
		
		this.setDuration(40 * 1000);
		
	}

	@Override
	public void Prepare() {
		// TODO Auto-generated method stub
		
		//descarga la info actual del tiempo
		
		this._ready=false;
		
		searchByLatLon();
		
		
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return _ready;
	}

	@Override
	public void Play() {
		// TODO Auto-generated method stub
		
		this.lVideoIntent = new Intent(_context, zumzum.app.zunzuneando.visor.weather.WeatherActivity.class);
				
		Bundle bundle = new Bundle();
		bundle.putSerializable("Duration", this.getDuration());

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
	
	private void searchByLatLon(){
		mYahooWeather.setNeedDownloadIcons(true);
		mYahooWeather.setSearchMode(SEARCH_MODE.GPS);
		mYahooWeather.queryYahooWeatherByLatLon(_context.getApplicationContext(), this._contentWeather.getLat(), this._contentWeather.getLon(), this, weatherLoded);
		
	}

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
		
		//_weatherInfo = weatherInfo;
		this._ready=true;
		
	}
	
	
	
	private Handler weatherLoded = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Log.e("weather_url", "loaded weather");
			
			_weatherInfo = (WeatherInfo) msg.obj;
			
			_ready=true;
			
		


		}
	};

	private ContentWeather _contentWeather;

	@Override
	public boolean isError() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setWeatherContent(ContentWeather contentWeather) {
		// TODO Auto-generated method stub
		this._contentWeather =  contentWeather;
	}

}
