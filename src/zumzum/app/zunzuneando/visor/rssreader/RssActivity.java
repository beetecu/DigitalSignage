package zumzum.app.zunzuneando.visor.rssreader;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import zumzum.app.zunzuneando.R;
import zumzum.app.zunzuneando.util.LogSaver;
import zumzum.app.zunzuneando.util.SystemUiHider;

import fr.missingfeature.CoverFlowView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class RssActivity extends Activity implements
CoverFlowView.Listener {
	private static final String TAG = "CoverFlowActivity";
	private CoverFlowView mCoverflow;
	private Bitmap[] mReflectedBitmaps;
	private boolean mCoverflowCleared = false;
	
	Reminder _reminder;
	int index = 0;
	
	public static int NUMBER_OF_IMAGES;
	
	private long duration;

	private final long interval = 1000;
	public MalibuCountDownTimer countDownTimer;

	LogSaver _logSaver;
	private Context _context;
	

	 /*
	  * Scaling down the image
	  */
	 public static Bitmap getScaleImage(Bitmap bitmap, int boundBoxInDp) {
	  
	  // Get current dimensions
	  int width = bitmap.getWidth();
	  int height = bitmap.getHeight();

	  // Determine how much to scale: the dimension requiring
	                // less scaling is.
	  // closer to the its side. This way the image always 
	                // stays inside your.
	  // bounding box AND either x/y axis touches it.
	  float xScale = ((float) boundBoxInDp) / width;
	  float yScale = ((float) boundBoxInDp) / height;
	  float scale = (xScale <= yScale) ? xScale : yScale;

	  // Create a matrix for the scaling and add the scaling data
	  Matrix matrix = new Matrix();
	  matrix.postScale((float) 0.50, (float) 0.35);

	  // Create a new bitmap and convert it to a format understood

	                // by the
	  // ImageView
	  Bitmap scaledBitmap = Bitmap.
	                     createBitmap(bitmap, 0, 0, width, height,
	                     matrix, true);
	  
	  // Apply the scaled bitmap
	 
	  return scaledBitmap;

	 }


	 
	/**
	 * Get an array of Bitmaps for our sample images
	 * 
	 * @param c
	 * @return
	 * @throws IOException
	 */
	public static Bitmap[] getBitmaps(Context c) throws IOException {
		
		//String root = Environment.getExternalStorageDirectory()+ "/BarrioTV/RSS/";
		//String path =  root + Term;  
		String path =  Term;  
		
		//String path = Environment.getExternalStorageDirectory().toString()+ "/saved_images";
		Log.e("Files", "Path: " + path);
		File f = new File(path);        
		File file[] = f.listFiles();
		//Log.e("Files", "Size: "+ file.length);
		NUMBER_OF_IMAGES = file.length;
		Bitmap[] result = new Bitmap[file.length];
		for (int i=0; i < file.length; i++)
		{
		    //Log.e("File", "FileName:" + file[i].getName());
		    String imagePath = path + "/" + file[i].getName();
			result[i] = getScaleImage(BitmapFactory.decodeFile(imagePath), 350);
		}
		
		

		return result;
	}
	
	private static String Term;

	
	private int mode; //0 is intro, 1 no intro

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rss);
		
		_context = this.getBaseContext();
		_logSaver = new LogSaver(_context);
		

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		
		final VideoView mVideoView = (VideoView) findViewById(R.id.videoView1);
		
		final String filename = (String) getIntent().getExtras().getSerializable("Filename");
		
		
		

		 //int id = getResources().getIdentifier(videoName, "raw", this.getPackageName());

		 //final String path = "android.resource://" + this.getPackageName() + "/" + id;
		
		
    	
		
		String introfile = getFilesDir() + "/" + "noticias.mp4";

		 mVideoView.setVideoPath(introfile);
		 
		 mVideoView.setVideoURI(Uri.parse(introfile));
		 
		 
		//final View lRelLayoutplayer = (RelativeLayout) findViewById(R.id.screen);;
		//lRelLayoutplayer.setVisibility(View.GONE);
		 
	     //mVideoView.requestFocus();
	     
	     mode = 0;

	  // Find the coverflow
	  			mCoverflow = (CoverFlowView) findViewById(R.id.coverflow);
	  			
	  			Term = (String) getIntent().getExtras().getSerializable("Term");
	  			
	  			duration = (Long) getIntent().getExtras().getSerializable("Duration");

	  			// Get the bitmaps
	  			Bitmap[] bitmaps = null;
	  			
	  			
	  			try {
	  				bitmaps = getBitmaps(this);
	  			} catch (IOException e) {
	  				Log.e(TAG, "Could not load bitmaps", e);
	  			}
	  			
	  			

	  			_logSaver.save();
	  			
	  			// Listen to the coverflow
	  			mCoverflow.setListener(this);

	  			// Fill in images
	  			for (int i = 0; bitmaps != null && i < bitmaps.length; i++) {
	  				mCoverflow.setBitmapForIndex(bitmaps[i], i);
	  			}
	  			
	  			mCoverflow.setNumberOfImages(bitmaps.length);
	  			
	  			Log.e(TAG, "setBitmapForIndex + 1");

	  			_logSaver.save();

	  			// Cache the reflected bitmaps
	  			mReflectedBitmaps = mCoverflow.getReflectedBitmaps();
	  			
	  			mVideoView.setVisibility(View.VISIBLE);
	  	    	
	  	    	mCoverflow.setVisibility(View.GONE);
	  	    	
	  	    	mVideoView.start();
		 
		 mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			    @Override
			    public void onCompletion (MediaPlayer mp) {
			        // your code to clean up and finish the activity...
			    	
			    	mVideoView.setVisibility(View.GONE);
			    
			    	
			    	mCoverflow.setVisibility(View.VISIBLE);
			    	
			    	_reminder = null;
					
					index = 0;
					
					mCoverflow.changeImageView(index, newItem);
					
					//lRelLayoutplayer.setVisibility(View.VISIBLE);
			    	
			    	countDownTimer = new MalibuCountDownTimer(duration, interval);
					countDownTimer.start();
					
	
			    }
			});
		 
		

			
			
			


	

		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		//delayedHide(100);
	}

	
	
	@Override
	protected void onResume() {

		// If we cleared the coverflow in onPause, resurrect it
		if (mCoverflowCleared) {
			for (int i = 0; i < mReflectedBitmaps.length; i++)
				mCoverflow.setReflectedBitmapForIndex(mReflectedBitmaps[i], i);
			mCoverflow.setNumberOfImages(mReflectedBitmaps.length);
		}
		mCoverflowCleared = false;
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Clear the coverflow to save memory
		mCoverflow.clear();
		mCoverflowCleared = true;
	}

	public Bitmap defaultBitmap() {
		try {
			return BitmapFactory.decodeStream(getAssets().open(
					"images/default.png"));
		} catch (IOException e) {
			Log.e(TAG, "Unable to get default image", e);
		}
		return null;
	}



	public void onSelectionChanged(CoverFlowView coverFlow, int index) {
		Log.d(TAG, String.format("Selection did change: %d", index));
	}

	public void onSelectionChanging(CoverFlowView coverFlow, int index) {
		Log.d(TAG, String.format("Selection is changing: %d", index));
	}

	public void onSelectionClicked(CoverFlowView coverFlow, int index) {
		Log.d(TAG, String.format("Selection clicked: %d", index));
	}

	public void onSelectionLongClicked(CoverFlowView coverFlow, int index) {
		Log.d(TAG, String.format("Selection long clicked: %d", index));
	}


	private Handler newItem = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			//Log.e("Message", "new item");
			_reminder = new Reminder(9*1000, newRemider);

		}
	};


	private Handler newRemider = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			//Log.e("Message", "changeImageView");
			Random generator = new Random();
			int n = 1;
			n = generator.nextInt(NUMBER_OF_IMAGES);
			index= index + 1;
			
			_logSaver.save();
			//index = n;
			mCoverflow.changeImageView(index, newItem);

		}
	};
	
	public Timer timer;
	public class Reminder {
		
		Handler _handler;
		

		public Reminder(int seconds, Handler handler) {
			timer = new Timer();
			timer.schedule(new RemindTask(), seconds);
			_handler = handler;
			
		}

		class RemindTask extends TimerTask {
			public void run() {
				//Log.e("tag", "Time's uppppppppppppppppppppppppppppp!%n");
				//startDownloading(_cam, _callbackToDrow);
				//mCoverflow.changeImageView(index, newItem);
				
				timer.cancel();
				//timer = new Timer();
				//timer.schedule(new RemindTask(), 15);
				_reminder = null;
				Message mssg = new Message();
				mssg.obj = true;
				//msg.obj = result;
				_handler.sendMessage(mssg);

			}
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
			//Log.e("TAG", "Time's up!");


				//Log.e("TAG", "statusShort");

				timer.cancel();
				
				cancel();
				
				//mCoverflow=null;
				mReflectedBitmaps = null;
				
				_logSaver.save();
				
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
