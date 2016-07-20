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

public class CopyOfLivingCities extends Visor{



	String TAG = "LivingCities";

	private boolean isStop;

	private static boolean isFree;

	public ContentLivingCity currentFile;
	
	

	private List<String> file2process;
	private List<String> filesource2process;
	private List<ContentLivingCity> filesready;

	private List<ContentLivingCity> sources2process;

	private List<Boolean> errorList;

	static final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");
	
	int index;

	public CopyOfLivingCities(Context context) {
		// TODO Auto-generated constructor stub
		super(context);

		_context = context;
		this.setReady(false);

		CopyReadAssets("rtmpdump");


		long duration = 100*1000;

		this.setDuration(duration);


		isStop = false;

		isFree = true;

		Log.e("Video","Create");

		file2process = new ArrayList<String>();
		filesource2process = new ArrayList<String>();

		filesready = new ArrayList<ContentLivingCity>();

		sources2process = new ArrayList<ContentLivingCity>();

		errorList = new ArrayList<Boolean>();

		
		index = 0;
		//recordVideo();

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
	////////////////////////////////////////////////////////////////////////

	public void Prepare(){

		//String path = Environment.getExternalStorageDirectory()+ "/BarrioTV/Living" + "/video" + LOG_FILE_FORMAT.format(new Date()) + String.valueOf(file2process.size()) +".flv";

		//file2process.add(path);


	}

	public void Prepare(ContentLivingCity source){


		//Log.e(TAG,  "Prepare: " + source.getAddress());

		
		String path = Environment.getExternalStorageDirectory()+ "/BarrioTV/Living" + "/video" + LOG_FILE_FORMAT.format(new Date()) + String.valueOf(index) +".flv";

		index ++;
		
		//Log.e(TAG,  "Prepare path" + path);

		if (source.getAddress().contains("-r")){

			file2process.add(path);
			
			filesource2process.add(path);

			sources2process.add(source);

			//errorList.add(false);
			
			Log.e(TAG, "IT IS RTMP");


		}

		else{



			Log.e(TAG, "NO RTMP");

		}	

		if ((file2process.size()>0) && (isFree))

			startDownloading();

	}


	final Timer timer = new Timer();


	public void recordVideo(){
		
		try{

		//for (int i = 1; i <= 5; i++)  {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				Log.e(TAG, "recordVideo");

				if ((file2process.size()>0) &&(isFree)){

					Log.e(TAG, "video ready to download");



					Log.e("LivingCities files en cola: ", String.valueOf(file2process.size()));

					String filename = file2process.get(0);
					file2process.remove(0);
					currentFile = sources2process.get(0);


					isFree = false;
					//flag = false;

					Log.e(TAG,"startDownloadinggggggggggggggggggggggggggggg");

					//String source = "http://212.20.40.37:1234/udp/233.7.70.47:5000";


					String source = sources2process.get(0).getAddress();
					sources2process.remove(0);

					//currentFile.setSource(filename);//OJO

					new Thread(new GetStream(downloadedInfo, source,
							_context.getFilesDir(), new File(filename))).start();


				}
			}
		};

		if (!isStop){

			timer.schedule(task, 0, 5000); 

		}
		} catch (Exception e) {
			Log.e(TAG, "error recordVideo: " + e.getMessage(), e);
			startDownloading();
			
		}

	}





	Handler downloadedInfo = new Handler() {
		public void handleMessage(Message msg) {


			try{
				
			
			Log.e(TAG, "video dwnloaded return");

			isFree = true;
			
			filesready.add(currentFile);
			
			File myfile = new File(filesource2process.get(0));
		

			Log.e(TAG, myfile.getName() + String.valueOf(myfile.length()));
			
			
			boolean status = (Boolean) msg.obj;
			if ((status) && (myfile.length() > 1000)){

				
				
				Log.e(TAG, "video downloaded OK");
				errorList.add(false);
			}
			else{
				
				//myfile.delete();
				
				//filesource2process.remove(0);
				Log.e(TAG, "video downloaded ERROR: " + filesource2process.get(0));
				
				errorList.add(true);
			}

			startDownloading();
			
			} catch (Exception e) {
				Log.e(TAG, "error downloadedInfo: " + e.getMessage(), e);
				startDownloading();
				
			}


		};
	};



	public boolean isReady() {


		boolean result = false;

		if (filesready.size()>0) 
			
			result = true;

		//Log.e(TAG, String.valueOf(result));
		return result;
	}

	public void startDownloading(){
		
		try{

		//Log.e(TAG, "recordVideo");

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {

				if ((file2process.size()>0) &&(isFree)){

					Log.e(TAG, "video ready to download");



					Log.e("LivingCities files en cola: ", String.valueOf(file2process.size()));

					String filename = file2process.get(0);
					file2process.remove(0);
					currentFile = sources2process.get(0);


					isFree = false;
					//flag = false;

					Log.e(TAG,"startDownloadinggggggggggggggggggggggggggggg");

					//String source = "http://212.20.40.37:1234/udp/233.7.70.47:5000";


					String source = sources2process.get(0).getAddress();
					sources2process.remove(0);

					//currentFile.setSource(filename);//OJO

					new Thread(new GetStream(downloadedInfo, source,
							_context.getFilesDir(), new File(filename))).start();


				}
			}

		}); 
		th.start();
		} catch (Exception e) {
			Log.e(TAG, "error startDownloading: " + e.getMessage(), e);
			
		}
	}




	public void doStop() {
		// TODO Auto-generated method stub

		//_systemStatus.doStop();

	}

	@Override
	public void Play() {
		
		try{
		
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		lVideoIntent = new Intent(
				_context,
				zumzum.app.zunzuneando.visor.livingCities.LivingCitiesActivity.class);
		// lVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//lVideoIntent.putExtra("filename", this.filesready.get(0));
		Bundle bundle = new Bundle();
		bundle.putSerializable("Filename", this.filesource2process.get(0));
		bundle.putSerializable("Duration", this.getDuration());


		this.lVideoIntent.putExtras(bundle);

		this.filesready.remove(0);
		this.filesource2process.remove(0);
		this.errorList.remove(0);

		// context.startActivity(lVideoIntent);
		// context.startActivityForResult(lVideoIntent,1);
		
	} catch (Exception e) {
		Log.e(TAG, "error: " + e.getMessage(), e);
		
	}

	}

	@Override
	public void Stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub

	}

	//@Override
	public boolean isError() {
		// TODO Auto-generated method stub

		boolean result = false;
		
		Log.e(TAG, "CHECK error");
		

		if (errorList.get(0)){
			
			Log.e(TAG, "eliminando fichero de la lista por erroneo" + filesource2process.get(0) );
			
			result = true;
			errorList.remove(0);
			this.filesready.remove(0);
			this.filesource2process.remove(0);
		}

		return result;


	}


}
