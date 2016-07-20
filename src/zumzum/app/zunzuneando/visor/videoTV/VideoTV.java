package zumzum.app.zunzuneando.visor.videoTV;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.contents.ContentTV;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class VideoTV extends Visor{

	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;


	String TAG = "VideoTV";

	private boolean isStop;

	private static boolean isFree;

	public static ContentTV currentFile;



	static final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ssZ");

	public VideoTV(Context context) {
		// TODO Auto-generated constructor stub
		super(context);

		_context = context;
		this.setReady(false);


		long duration = 90*1000;

		this.setDuration(duration);


		isStop = false;

		isFree = true;

		Log.e(TAG,"Create");


		//recordVideo();

	}

	public void Prepare(){



	}

	public void Prepare(ContentTV source){


		if (!source.getSource().contains(" ")){

			if ((source.getSource().contains("http")) || 
				(source.getSource().contains("rtsp")) || 
				(source.getSource().contains("rtmp")) || 
				(source.getSource().contains("mms"))){
				
				currentFile = source;

				//comprobar que esta online
				//filesready.add(source);

			}

		}

		else{
			
			String path = Environment.getExternalStorageDirectory()+ "/BarrioTV/Video" + "/video" + LOG_FILE_FORMAT.format(new Date()) + ".flv";

			//file2process.add(path);

			//sources2process.add(source);
		}	

	}


	final Timer timer = new Timer();


	
	public boolean isReady() {

		
			return true;

	}



	public void doStop() {
		// TODO Auto-generated method stub

		//_systemStatus.doStop();

	}

	@Override
	public void Play() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		lVideoIntent = new Intent(
				_context,
				zumzum.app.zunzuneando.visor.videoTV.MediaPlayerVideo.class);
		// lVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//lVideoIntent.putExtra("filename", this.filesready.get(0));
		Bundle bundle = new Bundle();
		bundle.putSerializable("Filename", currentFile.getSource());
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

	@Override
	public boolean isError() {
		// TODO Auto-generated method stub
		return false;
	}


}
