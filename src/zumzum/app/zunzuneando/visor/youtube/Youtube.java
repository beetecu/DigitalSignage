package zumzum.app.zunzuneando.visor.youtube;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.contents.ContentTV;
import zumzum.app.zunzuneando.visor.contents.ContentYoutube;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Youtube extends Visor{

	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;


	String TAG = "Youtube";

	private boolean isStop;

	private static boolean isFree;

	public static String video2show;



	public Youtube(Context context) {
		// TODO Auto-generated constructor stub
		super(context);

		_context = context;
		this.setReady(false);


		long duration = 30*1000;

		this.setDuration(duration);

	}

	public void Prepare(){



	}

	public void Prepare(ContentYoutube source){


		//return a random video from list
		
		//Random rnd = new Random();
		//int randno = rnd.nextInt(source.videos.size()-1);
		//Log.e("randno", String.valueOf(randno));
		
        video2show = source.videos.get(0);
        Log.e("video2show", video2show);
        
		
	}





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
		
		String cmd = "";
		if (video2show.contains("playlist")){
			
			cmd = "ytpl://"+ getYoutubeID(video2show);
			
		}
		else{
			
			cmd = "ytv://"+ getYoutubeID(video2show);
			
		}
		
		lVideoIntent = new Intent(null, Uri.parse(cmd), 
				_context, 
				zumzum.app.zunzuneando.visor.youtube.YouTubePlayerActivity.class);
		//lVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		lVideoIntent.putExtra("title", "this.getTitle()" );
		lVideoIntent.putExtra("description", "this.getInfo()");	
		lVideoIntent.putExtra("Duration",this.getDuration());

	}
	
	private String getYoutubeID(String file2show2) {
		// TODO Auto-generated method stub
		
		String videoid= file2show2.substring(file2show2.indexOf("=")+1);
		
		Log.e("videoId", videoid);
		
		return videoid;
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
