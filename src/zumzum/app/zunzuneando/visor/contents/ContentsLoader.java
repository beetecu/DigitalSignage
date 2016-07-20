package zumzum.app.zunzuneando.visor.contents;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class ContentsLoader{

	private static final String TAG = "ContentsLoader";

	Context _context;

	List<ContentTV> tvContents; 

	ListOfContentLivingCity cityContents = null;

	ListOfContentRSS rssContents = null;;

	List<ContentAd> adsContents;

	ContentWeather contentWeather;

	ContentScreen contentScreen;

	List<ContentYoutube> contentYoutube;	

	private boolean _isReady;

	public String httpPath = "http://192.168.1.128:8000/Anuncios/default/call/json/";

	public String screenId = "1";


	public ContentsLoader(Context context) {

		_context = context;

		tvContents = new ArrayList<ContentTV>();
		
		adsContents = new ArrayList<ContentAd>(); 

		contentYoutube = new ArrayList<ContentYoutube>(); 

		_isReady = false;


		String json = "";
		String path = "";
		File folder = new File(Environment.getExternalStorageDirectory()+ "/BarrioTV/data");

		// init camera

		path = folder.getPath() + "/cameras.json";

		json = httpPath + "cameras";

		cityContents = new ListOfContentLivingCity(path, json, _context); 
		
		//InitCamera(path, json);

		//init rss

		//path = folder.getPath() + "/rss.json";

		//json = httpPath + "category_rss?screen=" + screenId;

		//InitRSS(path, json);
		//rssContents = new ListOfContentRSS(path, json, _context); 


	}




	public boolean isReady() {

		boolean result = true;

		if (this.rssContents != null){

			if (!this.rssContents._isReady)
				result = false;
		}
		
		if ((this.cityContents != null) && (result)){

			if (!this.cityContents._isReady)
				result = false;
		}
		
		Log.e(TAG, "is ready ask");
		
		setReady(result);

		return _isReady;
	}


	public void setReady(boolean isReady) {
		this._isReady = isReady;
	}


	private Bitmap getImageFromAssert(String name){

		Bitmap b = null;
		try {
			b = BitmapFactory.decodeStream(_context.getAssets().open(name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b;
	}

	private InputStream doHttpGet(String urlString) throws IOException {

		InputStream inputStream = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.setUseCaches(true);
			Object content = httpConn.getContent();
			if (content instanceof Bitmap) {
				Bitmap bitmap = (Bitmap) content;
			}

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting" + ex);
		}
		return inputStream;
	}

	public String inputStreamToString(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

}