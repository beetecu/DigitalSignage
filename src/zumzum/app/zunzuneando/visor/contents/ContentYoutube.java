package zumzum.app.zunzuneando.visor.contents;

import java.util.ArrayList;
import java.util.List;

import zumzum.app.zunzuneando.visor.youtube.Library;
import zumzum.app.zunzuneando.visor.youtube.Video;


import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ContentYoutube extends Content {
	
	Library lib;
	public List<String> videos;


	public ContentYoutube(String source, String theme){
		
		super(source, theme);
		
		videos = new ArrayList<String>();	
		
	}
	
	


	

}
