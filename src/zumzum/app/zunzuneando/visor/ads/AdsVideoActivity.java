/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zumzum.app.zunzuneando.visor.ads;

import zumzum.app.zunzuneando.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

public class AdsVideoActivity extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private String path;
	private Bundle extras;
	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private String filename;
	private Long duration;
	private int type;
	
	private final long interval = 1000;
	public MalibuCountDownTimer countDownTimer;
	
	public Context _context;


	/**
	 * 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.adsvideo);
		
		_context = this.getBaseContext();
		
		mPreview = (SurfaceView) findViewById(R.id.surface);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888); 
		//extras = getIntent().getExtras();
		filename = (String) getIntent().getExtras().getSerializable("Filename");
		
		duration = (Long) getIntent().getExtras().getSerializable("Duration");
		
		countDownTimer = new MalibuCountDownTimer(duration, interval);
		countDownTimer.start();
		
		//type = (Integer) getIntent().getExtras().getSerializable("media");
		
		//playVideo();
		
		/*
		
		new Thread(new Runnable() {
			public void run() {
				
				String introfile = getFilesDir() + "/" + "reportajes.mp4";
				
				Intent lVideoIntent = new Intent(
						_context,
						zumzum.app.zunzuneando.PlayerIntro.class);
				// lVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//lVideoIntent.putExtra("filename", this.filesready.get(0));
				Bundle bundle = new Bundle();
				bundle.putSerializable("Filename", introfile);
				lVideoIntent.putExtras(bundle);

				startActivityForResult(lVideoIntent, 1);


			}
		}).start();

		
		 */

	}
	
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){

		super.onActivityResult(requestCode, resultCode, data);

		Log.e("MediaPlayerVideo", "onActivityResult");
		
		
		/*
		final ImageView splash = (ImageView) findViewById(R.id.imageTraslate);
		
		Animation animation = new TranslateAnimation(1800, -1800,0, 0);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setDuration(20000);
		animation.setFillAfter(true);
		splash.startAnimation(animation);
		splash.setVisibility(0);
		*/
		
		//Animation traslate = AnimationUtils.loadAnimation(_context, R.anim.traslate_x);
		//traslate.setRepeatCount(Animation.INFINITE);
		
		
		//splash.startAnimation(traslate);
		
		//startVideoPlayback();



		//showContent();
		//handler.post(showContentsH);
		//WaitForContents();

	}

	private void playVideo() {
		doCleanUp();
		try {

			Log.e(TAG, "PlayVideo");

			// Create a new media player and set the listeners
			mMediaPlayer = new MediaPlayer(this);
			
			Log.e("video2show",filename);
			//Log.e("vioed", "http://46.249.213.87/iPhone/broadcast/fashiontv-tablet.3gp/fashiontv-tablet.3gp-mr398k.m3u8");
			
			mMediaPlayer.setDataSource(filename);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepare();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			mMediaPlayer.getMetadata();
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			
			MediaPlayer.OnErrorListener myVideoViewErrorListener
			 = new MediaPlayer.OnErrorListener(){

			  @Override
			  public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
			   
				  finish();
				  
				  return true;
			  }};
			

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		Log.d(TAG, "onBufferingUpdate percent:" + percent);

	}

	public void onCompletion(MediaPlayer arg0) {
		Log.d(TAG, "onCompletion called");
		finish();
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}
	
	public void onError(){
		finish();
		
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		Log.d(TAG, "surfaceChanged called");

	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed called");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
		//playVideo(extras.getInt(MEDIA));
		playVideo();

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
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


