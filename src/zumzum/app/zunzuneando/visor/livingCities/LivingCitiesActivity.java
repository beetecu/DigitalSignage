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

package zumzum.app.zunzuneando.visor.livingCities;

import zumzum.app.zunzuneando.R;
import zumzum.app.zunzuneando.util.LogSaver;
import zumzum.app.zunzuneando.visor.videoTV.MediaPlayerVideo.MalibuCountDownTimer;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

public class LivingCitiesActivity extends Activity implements
OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private static final String TAG = "LivingCitiesActivity";
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

	public boolean isIntro;
	private Context _context;
	
	LogSaver _logSaver;

	//public RelativeLayout lRelLayoutplayer;

	/**
	 * 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.city);

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


		_logSaver = new LogSaver(_context);
		
		Log.e(TAG, "file2show: " + filename);

		_logSaver.save();
		//type = (Integer) getIntent().getExtras().getSerializable("media");

		//playVideo();

		/*

		new Thread(new Runnable() {
			public void run() {

				String introfile = getFilesDir() + "/" + "cities.mp4";

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



		//Animation traslate = AnimationUtils.loadAnimation(_context, R.anim.traslate_x);
		//traslate.setRepeatCount(Animation.INFINITE);


		//splash.startAnimation(traslate);

		//startVideoPlayback();



		//showContent();
		//handler.post(showContentsH);
		//WaitForContents();

	}

	private void playVideo() {
		
		try {
		
		doCleanUp();
		

			//Log.e(TAG, "PlayVideo");

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

					
					countDownTimer.cancel();
					
					Log.e(TAG,"onError");
					
					_logSaver.save();
					
					finish();

					return true;
				}};
				
				_logSaver.save();


		} catch (Exception e) {
			
			Log.e(TAG, "error: " + e.getMessage(), e);
			_logSaver.save();
			countDownTimer.cancel();
			finish();
		}
	}

	
	public void onCompletion(MediaPlayer arg0) {
		
		try{
			
		
		Log.e(TAG, "onCompletion called");
		_logSaver.save();
		finish();
		countDownTimer.cancel();
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			countDownTimer.cancel();
			finish();
		}

		//startVideoPlayback();
		//mMediaPlayer.seekTo(0);
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

		try{


			// Log.e(TAG, "onVideoSizeChanged called");
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

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			countDownTimer.cancel();
			finish();
		}
	}

	public void onPrepared(MediaPlayer mediaplayer) {

		try{
			Log.e(TAG, "onPrepared called");
			mIsVideoReadyToBePlayed = true;
			_logSaver.save();
			if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
				startVideoPlayback();
			}
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			countDownTimer.cancel();
			finish();
		}
	}

	public void onError(){

		finish();
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		try{
			//Log.e(TAG, "surfaceChanged called");
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			countDownTimer.cancel();
			finish();
		}

	}

	
	public void surfaceCreated(SurfaceHolder holder) {

		try{

			//Log.d(TAG, "surfaceCreated called");
			//playVideo(extras.getInt(MEDIA));
			playVideo();
			final ImageView splash = (ImageView) findViewById(R.id.imageTraslate);

			Animation animation = new TranslateAnimation(1800, -850,0, 0);
			animation.setRepeatCount(Animation.INFINITE);
			animation.setDuration(20000);
			animation.setFillAfter(true);
			splash.startAnimation(animation);
			splash.setVisibility(0);

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			countDownTimer.cancel();
			finish();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		
		try {
		
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
		
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			//countDownTimer.cancel();
			//finish();
		}
		
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

		try{


			Log.e(TAG, "startVideoPlayback");
			holder.setFixedSize(mVideoWidth, mVideoHeight);
			mMediaPlayer.start();

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			countDownTimer.cancel();
			finish();
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

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		
	}

}


