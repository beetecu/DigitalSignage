package zumzum.app.zunzuneando.visor.contents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zumzum.app.zunzuneando.visor.livingCities.GetStream;
import zumzum.app.zunzuneando.visor.rssreader.RSSReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ListOfContentLivingCity {
	
	protected static final String TAG = "ListOfContentLivingCity";

	List<ContentLivingCity> _cityContents;
	
	private String _file2save;
	private String _url2load;
	
	public boolean _isReady;
	
	Context _context;
	
	String _root;
	
	
	private boolean _readyforNext;
	private boolean stop;
	
	public List<Integer> _indexCityReady;
	public List<String> _filesReady;
	
	public String _currentFile;
	
	public int _currentIndex;
	

	
	static final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");

	private  String EMERGENCY_FILE = null;
	
	
	public ListOfContentLivingCity(String file2save, String url2load, Context context){

		_isReady = false;
		
		_context = context;
		
		_root = Environment.getExternalStorageDirectory()+ "/BarrioTV/Living/";
		
		_cityContents = new ArrayList<ContentLivingCity>(); 
		
		_file2save = file2save;
		
		_url2load = url2load;
	
		stop = false;
		
		_currentIndex = 0;
	    _indexCityReady = new ArrayList<Integer>();
		_filesReady = new ArrayList<String>();
		_currentFile = null;
		
		CopyReadAssets("rtmpdump");
		
		
		 EMERGENCY_FILE = _context.getFilesDir() + "/" + "cities.mp4";
		
		loadWebcamSources(_file2save, _url2load);
		
	}
	
	public void UpdateContent(){
		
		
		// wait for load
		
		loadWebcamSources(_file2save, _url2load);
		
		
		
	}
	

	public void loadWebcamSources(final String file2save, final String url2load){

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {


				Log.e("Pos1", "Pos1");

				//String url = "http://192.168.1.130:8000/Anuncios/default/call/json/category_rss?screen=1";

				try {
					String Content = inputStreamToString(doHttpGet(url2load));

					//Log.e("Pos2", Content);

					JSONObject jsonResponse;

					jsonResponse = new JSONObject(Content);

					Log.e("Pos3", "Pos3");

					FileWriter file = new FileWriter(file2save);
					file.write(jsonResponse.toString());
					file.flush();
					file.close();

					Log.e("Pos4", "Pos4");

					//loadJsonRss(file2save);
					//loadJsonCamera(file2save);
					//loadJsonAds(file2save);
					//loadJsonVideo(file2save);



				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				loadJsonCamera(file2save);

			}
		}); 
		th.start();

	}

	public void loadJsonCamera(final String file2load){

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {


				Log.e("Pos5", "Pos5");

				File yourFile = new File(file2load);
				FileInputStream stream;
				try {
					stream = new FileInputStream(yourFile);
					String jsonStr = null;
					try {
						FileChannel fc = stream.getChannel();
						MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

						jsonStr = Charset.defaultCharset().decode(bb).toString();

						//Log.e("jsonStr", jsonStr);


						JSONObject jsonObj = new JSONObject(jsonStr);

						// Getting data JSON Array nodes
						JSONArray data  = jsonObj.getJSONArray("cameras");


						//Log.e("nodos", Integer.toString(data.length()));

						//Log.e("quien soy", data.optString(0, "f_place"));

						ContentLivingCity contentL;

						Log.e(TAG,"ContentCity");

						
						// looping through All nodes
						for (int i = data.length()-1; i >= 0; i--) {

							JSONObject c = data.getJSONObject(i);


							if (c.getString("f_address_string").contains("rtmp")){

								contentL = new ContentLivingCity(c.getString("f_address_string"), "");

								//Log.e("Pos6", "Pos");



								Log.e("f_place", c.getString("f_place"));
								//Log.e("f_WOEID", c.getString("f_WOEID"));
								//Log.e("f_source", c.getString("f_source"));
								//Log.e("f_time_in", c.getString("f_time_in"));
								//Log.e("f_time_out", c.getString("f_time_out"));
								//Log.e("f_address_string", c.getString("f_address_string"));

								contentL.setAddress(c.getString("f_address_string"));

								contentL.setSource(c.getString("f_source"));
								contentL.setWoeid(c.getString("f_WOEID"));
								contentL.setPlace(c.getString("f_place"));
								contentL.setTime_in(c.getString("f_time_in"));
								contentL.setTime_out(c.getString("f_time_out"));


								_cityContents.add(contentL);

								//String rss_country = c.getString("rss_country");
								//String f_rss_source = c.getString("f_rss_source");
								//String duration = c.getString("duration");
								//use >  int id = c.getInt("duration"); if you want get an int


								// tmp hashmap for single node
								//HashMap<String, String> parsedData = new HashMap<String, String>();

								// adding each child node to HashMap key => value
								//parsedData.put("rss_country", rss_country);
								//parsedData.put("f_rss_source", f_rss_source);
								//parsedData.put("duration", duration);

								//Log.e("rss_country",rss_country);
								//Log.e("f_rss_source",f_rss_source);


								// do what do you want on your interface
							}
						}	
						


					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
						try {
							stream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



				
				_isReady = true;
				download();
			}
			
		}); 
		th.start();



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


	////////////////////////////////////////////////////////////////////////////////////
	///rtmpdump
	private void CopyReadAssets(String filename) {
		AssetManager assetManager =  _context.getAssets();

		InputStream in = null;
		OutputStream out = null;

		//Log.e("fd",  _context.getFilesDir().toString());

		File file = new File( _context.getFilesDir(), filename);
		try {

			in = assetManager.open("rtmpdump");

			out =  _context.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Runtime.getRuntime().exec("/system/bin/chmod 777 "+ filename,null, _context.getFilesDir());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
	
   public void download(){
	   
		new Thread(new Runnable() { 
			

			

			public void run(){

				try{
					
					

					//for each categorie
					_currentIndex = 0;
					while(!stop){
					//for(int i=5;i<6;i++){
						
						
							
							
							try{
								
								if (_currentIndex >= _cityContents.size())
									_currentIndex = 0;
							
								_readyforNext = false;
								
								String path = Environment.getExternalStorageDirectory()+ "/BarrioTV/Living" + "/video" + LOG_FILE_FORMAT.format(new Date()) + String.valueOf(_currentIndex) +".flv";

								String source = _cityContents.get(_currentIndex).getAddress();
									
								_currentFile = path;
								
								new Thread(new GetStream(downloadedInfo, source,
										_context.getFilesDir(), new File(path))).start();
								
								
								_currentIndex++;
								
								boolean flag = true;
								while (flag){

									if (_readyforNext){

										Log.e("NExt", "ready for Next");

										flag = false;

									}
									else{
										try {
											//Log.e("WAIT", " wait for Next");
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						}

					
					 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}).start();





	}
   
	Handler downloadedInfo = new Handler() {
		public void handleMessage(Message msg) {


			try{
				
			
			Log.e(TAG, "video dwnloaded return");

			
			
			
			File myfile = new File(_currentFile);
		

			Log.e(TAG, myfile.getName() + String.valueOf(myfile.length()));
			
			
			boolean status = (Boolean) msg.obj;
			if ((status) && (myfile.length() > 1000)){

				_filesReady.add(_currentFile);
				_indexCityReady.add(_currentIndex);
			   
				
				Log.e(TAG, "video downloaded OK");
				
			}
			else{
				
				//myfile.delete();
				
				//filesource2process.remove(0);
				Log.e(TAG, "video downloaded ERROR: " + _currentFile);
				
				
			}

			_readyforNext = true;
			
			} catch (Exception e) {
				Log.e(TAG, "error downloadedInfo: " + e.getMessage(), e);
				_readyforNext = true;
				
			}


		};
	};


	//mejorar
	public String getSource2Show(){
		
		String result = null;
		
		int index = 0;
		
		if (_filesReady.size() > 0){
			
			result = _filesReady.get(index);
			_filesReady.remove(index);
			this._indexCityReady.remove(index);
		}
		
		return result;
		
		
	}
	
	public String getEmergencyFile(){
		
		
		return EMERGENCY_FILE;
		
		
	}
	
	


}
