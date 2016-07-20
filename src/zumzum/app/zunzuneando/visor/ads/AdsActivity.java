package zumzum.app.zunzuneando.visor.ads;



import java.io.File;
import java.io.IOException;

import zumzum.app.zunzuneando.R;
import zumzum.app.zunzuneando.visor.contents.ContentAd;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;


public class AdsActivity extends Activity {
    private static final String TAG = "AdsActivity";
	/** Called when the activity is first created. */
	
	private final long startTimeLong = 5*1000;
	private final long startTimeShort = 15*1000;

	private final long interval = 1000;
	public MalibuCountDownTimer countDownTimer;

	public boolean statusLong = true;
	
	ViewAnimator viewAnimator ;
	
	ImageView imgFront;
	ImageView imgBack;
	ImageView imgBackground;
	
	//private ContentAd advertising;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adsimg);
        
        viewAnimator = (ViewAnimator)this.findViewById(R.id.viewFlipper);
        
        imgFront = (ImageView)this.findViewById(R.id.imageFront); 
    	imgBack = (ImageView)this.findViewById(R.id.imageBack); 
    	imgBackground= (ImageView)this.findViewById(R.id.imageBackground); 
    	
    	//advertising = (ContentAd) getIntent().getExtras().getSerializable("Advertising");
    	
    	String background = (String) getIntent().getExtras().getSerializable("background");
    	
    	Log.e(TAG, background);
    	
    	String logo = (String) getIntent().getExtras().getSerializable("logo");
    	String offert = (String) getIntent().getExtras().getSerializable("offert");
    	
    	imgFront.setBackgroundDrawable(new BitmapDrawable(getBitmap(logo)));
    	imgBack.setBackgroundDrawable(new BitmapDrawable(getBitmap(offert)));
    	imgBackground.setBackgroundDrawable(new BitmapDrawable(getBitmap(background)));
    	
    	long duration = (Long) getIntent().getExtras().getSerializable("Duration"); //arreglar
    	
        countDownTimer = new MalibuCountDownTimer(startTimeLong, interval);
		countDownTimer.start();
        
		
		
        /**
         * Bind a click listener to initiate the flip transitions
         */
        viewAnimator.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) { 
				// This is all you need to do to 3D flip
				AnimationFactory.flipTransition(viewAnimator, FlipDirection.LEFT_RIGHT);
			}
        	
        });
        
        
        
    }
    
	public static Bitmap getBitmap(String path)
	{
		
		Bitmap result = null;
		
		result =  getScaleImage(BitmapFactory.decodeFile(path), 1250);
		
		return result;
	}
	
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
	  matrix.postScale((float) scale, (float) scale);

	  // Create a new bitmap and convert it to a format understood

	                // by the
	  // ImageView
	  Bitmap scaledBitmap = Bitmap.
	                     createBitmap(bitmap, 0, 0, width, height,
	                     matrix, true);
	  
	  // Apply the scaled bitmap
	 
	  return scaledBitmap;

	 }
		
		
    
	// CountDownTimer class
	public class MalibuCountDownTimer extends CountDownTimer
	{


		public MalibuCountDownTimer(long startTime, long interval)
		{
			super(startTime, interval);
		}

		@Override
		public void onFinish()
		{
			Log.e("TAG", "Time's up!");
			if (statusLong){

				Log.e("TAG", "statusLong");

				cancel();
				statusLong = false;
				
				AnimationFactory.flipTransition(viewAnimator, FlipDirection.LEFT_RIGHT);

				countDownTimer = new MalibuCountDownTimer(startTimeShort, 1000);
				countDownTimer.start();
				

			}
			else{

				Log.e("TAG", "statusShort");

				cancel();
				statusLong = true;
				
				AnimationFactory.flipTransition(viewAnimator, FlipDirection.LEFT_RIGHT);

				//countDownTimer = new MalibuCountDownTimer(startTimeLong, 1000);
				//countDownTimer.start();
				finish();
			}

			

		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			//text.setText("Time remain:" + millisUntilFinished);
			//timeElapsed = startTime - millisUntilFinished;
			//timeElapsedView.setText("Time Elapsed: " + String.valueOf(timeElapsed));
		}
	}
}