package zumzum.app.zunzuneando.visor.ads;


import java.util.Timer;
import java.util.TimerTask;

import zumzum.app.zunzuneando.R;
import zumzum.app.zunzuneando.util.PlaylistId;
import zumzum.app.zunzuneando.util.VideoId;
import zumzum.app.zunzuneando.util.YouTubeId;
import zumzum.app.zunzuneando.util.YouTubeUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.LinearLayout.LayoutParams;

/**
 * <p>Activity that will play a video from YouTube.  A specific video or the latest video in a YouTube playlist 
 * can be specified in the intent used to invoke this activity.  The data of the intent can be set to a 
 * specific video by using an Intent data URL of the form:</p>
 * 
 * <pre>
 *     ytv://videoid
 * </pre>  
 *    
 * <p>where <pre>videoid</pre> is the ID of the YouTube video to be played.</p>
 * 
 * <p>If the user wishes to play the latest video in a YouTube playlist, the Intent data URL should be of the 
 * form:</p>
 * 
 * <pre>
 *     ytpl://playlistid
 * </pre>
 * 
 * <p>where <pre>playlistid</pre> is the ID of the YouTube playlist from which the latest video is to be played.</p>
 * 
 * <p>Code used to invoke this intent should look something like the following:</p>
 * 
 * <pre>
 *      Intent lVideoIntent = new Intent(null, Uri.parse("ytpl://"+YOUTUBE_PLAYLIST_ID), this, OpenYouTubePlayerActivity.class);
 *              startActivity(lVideoIntent);
 * </pre>
 * 
 * <p>There are several messages that are displayed to the user during various phases of the video load process.  If 
 * you wish to supply text other than the default english messages (e.g., internationalization, etc.), you can pass 
 * the text to be used via the Intent's extended data.  The messages that can be customized include:
 * 
 * <ul>
 *   <li>com.keyes.video.msg.init        - activity is initializing.</li>
 *   <li>com.keyes.video.msg.detect      - detecting the bandwidth available to download video.</li>
 *   <li>com.keyes.video.msg.playlist    - getting latest video from playlist.</li>
 *   <li>com.keyes.video.msg.token       - retrieving token from YouTube.</li>
 *   <li>com.keyes.video.msg.loband      - buffering low-bandwidth.</li>
 *   <li>com.keyes.video.msg.hiband      - buffering hi-bandwidth.</li>
 *   <li>com.keyes.video.msg.error.title - dialog title displayed if anything goes wrong.</li>
 *   <li>com.keyes.video.msg.error.msg   - message displayed if anything goes wrong.</li>
 * </ul>
 * 
 * <p>For example:</p>
 * 
 * <pre>
 *      Intent lVideoIntent = new Intent(null, Uri.parse("ytpl://"+YOUTUBE_PLAYLIST_ID), this, OpenYouTubePlayerActivity.class);
 *      lVideoIntent.putExtra("com.keyes.video.msg.init", getString("str_video_intro"));
 *      lVideoIntent.putExtra("com.keyes.video.msg.detect", getString("str_video_detecting_bandwidth"));
 *      ...
 *      startActivity(lVideoIntent);
 * </pre>
 * 
 * @author David Keyes
 *
 */
public class OpenYouTubePlayerActivity extends Activity {
        
        public static final String SCHEME_YOUTUBE_VIDEO = "ytv";
        public static final String SCHEME_YOUTUBE_PLAYLIST = "ytpl";
        
        static final String YOUTUBE_VIDEO_INFORMATION_URL = "http://www.youtube.com/get_video_info?&video_id=";
        static final String YOUTUBE_PLAYLIST_ATOM_FEED_URL = "http://gdata.youtube.com/feeds/api/playlists/";
        
        protected ProgressBar mProgressBar;
        protected TextView    mProgressMessage;
        protected VideoView   mVideoView;
        
     
        /** Background task on which all of the interaction with YouTube is done */
        protected QueryYouTubeTask mQueryYouTubeTask;
        
        protected String mVideoId = null;
        
    	public static boolean isfinished = false;
        
    	Intent intent;

 

    	
        
        @Override
        protected void onCreate(Bundle pSavedInstanceState) {
                super.onCreate(pSavedInstanceState);
                
         //Bundle parameters = getIntent().getExtras();
         //if(parameters != null && parameters.containsKey("layout"))
                   //setContentView(parameters.getInt("layout"));
                //else
                   //setContentView(R.layout.defaultLayout);
                
                setContentView(R.layout.adsyoutube);
     
                
        		
        		intent = new Intent();
        		intent.putExtra("msg", "OK");
        		setResult(RESULT_OK, intent);

        		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        				WindowManager.LayoutParams.FLAG_FULLSCREEN);

        		// create the layout of the view
        		//setupView();
        		
        		//final TextView mTextView;
        		//mTextView = (TextView) findViewById(R.id.fullscreen_content);
        		//mTextView.setSelected(true);
        		//mTextView.setVisibility(View.GONE);
        		
        		
        		
        		mVideoView = (VideoView)  findViewById(R.id.videoView1);
        		
        		Log.e("YoutubeId", this.getIntent().getData().toString());
            
            // set the flag to keep the screen ON so that the video can play without the screen being turned off
        //getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                //mProgressBar.bringToFront();
                //mProgressBar.setVisibility(View.VISIBLE);
                //mProgressMessage.setText(mMsgInit);
                
                // extract the playlist or video id from the intent that started this video
        
        
                
                Uri lVideoIdUri = this.getIntent().getData();
                
                if(lVideoIdUri == null){
                        Log.i(this.getClass().getSimpleName(), "No video ID was specified in the intent.  Closing video activity.");
                        finish();
                }
                String lVideoSchemeStr = lVideoIdUri.getScheme();
                String lVideoIdStr     = lVideoIdUri.getEncodedSchemeSpecificPart();
                if(lVideoIdStr == null){
                        Log.i(this.getClass().getSimpleName(), "No video ID was specified in the intent.  Closing video activity.");
                        finish();
                }
                if(lVideoIdStr.startsWith("//")){
                        if(lVideoIdStr.length() > 2){
                                lVideoIdStr = lVideoIdStr.substring(2);
                        } else {
                                Log.i(this.getClass().getSimpleName(), "No video ID was specified in the intent.  Closing video activity.");
                                finish();
                        }
                }

                ///////////////////
                // extract either a video id or a playlist id, depending on the uri scheme
                YouTubeId lYouTubeId = null;
                if(lVideoSchemeStr != null && lVideoSchemeStr.equalsIgnoreCase(SCHEME_YOUTUBE_PLAYLIST)){
                        lYouTubeId = new PlaylistId(lVideoIdStr);
                }
                
                else if(lVideoSchemeStr != null && lVideoSchemeStr.equalsIgnoreCase(SCHEME_YOUTUBE_VIDEO)){
                        lYouTubeId = new VideoId(lVideoIdStr);
                }

                if(lYouTubeId == null){
                        Log.i(this.getClass().getSimpleName(), "Unable to extract video ID from the intent.  Closing video activity.");
                        finish();
                }
                
                mQueryYouTubeTask = (QueryYouTubeTask) new QueryYouTubeTask().execute(lYouTubeId);
                
                
        }
        
      

        /**
         * Determine the messages to display during video load and initialization. 
         */
       

        /**
         * Create the view in which the video will be rendered.
         */
 

        @Override
        protected void onDestroy() {
                super.onDestroy();
                
                YouTubeUtility.markVideoAsViewed(this, mVideoId);
                
                if(mQueryYouTubeTask != null){
                        mQueryYouTubeTask.cancel(true);
                }
                
                if(mVideoView != null){
                        mVideoView.stopPlayback();
                }
                
            // clear the flag that keeps the screen ON 
                getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                this.mQueryYouTubeTask = null;
                this.mVideoView = null;
                
        }
        
        public void updateProgress(String pProgressMsg){
                try {
                        mProgressMessage.setText(pProgressMsg);
                } catch(Exception e) {
                        Log.e(this.getClass().getSimpleName(), "Error updating video status!", e);
                }
        }
        
        private class ProgressUpdateInfo {
        
                public String mMsg;
        
                public ProgressUpdateInfo(String pMsg){
                        mMsg = pMsg;
                }
        }
        
        /**
         * Task to figure out details by calling out to YouTube GData API.  We only use public methods that
         * don't require authentication.
         * 
         */
    	private class QueryYouTubeTask extends
		AsyncTask<YouTubeId, ProgressUpdateInfo, Uri> {

	private boolean mShowedError = false;

	@Override
	protected Uri doInBackground(YouTubeId... pParams) {
		String lUriStr = null;
		String lYouTubeFmtQuality = "17"; // 3gpp medium quality, which
											// should be fast enough to view
											// over EDGE connection
		String lYouTubeVideoId = null;

		if (isCancelled())
			return null;

		try {

			// publishProgress(new ProgressUpdateInfo(mMsgDetect));

			WifiManager lWifiManager = (WifiManager) OpenYouTubePlayerActivity.this
					.getSystemService(Context.WIFI_SERVICE);
			TelephonyManager lTelephonyManager = (TelephonyManager) OpenYouTubePlayerActivity.this
					.getSystemService(Context.TELEPHONY_SERVICE);

			// //////////////////////////
			// if we have a fast connection (wifi or 3g), then we'll get a
			// high quality YouTube video
			if ((lWifiManager.isWifiEnabled()
					&& lWifiManager.getConnectionInfo() != null && lWifiManager
					.getConnectionInfo().getIpAddress() != 0)
					|| ((lTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS
							||

							/*
							 * icky... using literals to make backwards
							 * compatible with 1.5 and 1.6
							 */
							lTelephonyManager.getNetworkType() == 9 /* HSUPA */
							|| lTelephonyManager.getNetworkType() == 10 /* HSPA */
							|| lTelephonyManager.getNetworkType() == 8 /* HSDPA */
							|| lTelephonyManager.getNetworkType() == 5 /* EVDO_0 */|| lTelephonyManager
							.getNetworkType() == 6 /* EVDO A */)

					&& lTelephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)) {
				lYouTubeFmtQuality = "18";
			}

			// /////////////////////////////////
			// if the intent is to show a playlist, get the latest video id
			// from the playlist, otherwise the video
			// id was explicitly declared.
			if (pParams[0] instanceof PlaylistId) {
				//publishProgress(new ProgressUpdateInfo(mMsgPlaylist));
				lYouTubeVideoId = YouTubeUtility
						.queryLatestPlaylistVideo((PlaylistId) pParams[0]);
			}

			else if (pParams[0] instanceof VideoId) {
				lYouTubeVideoId = pParams[0].getId();
			}

			mVideoId = lYouTubeVideoId;

			//publishProgress(new ProgressUpdateInfo(mMsgToken));

			if (isCancelled())
				return null;

			// //////////////////////////////////
			// calculate the actual URL of the video, encoded with proper
			// YouTube token
			lUriStr = YouTubeUtility.calculateYouTubeUrl(
					lYouTubeFmtQuality, true, lYouTubeVideoId);

			if (isCancelled())
				return null;

			if (lYouTubeFmtQuality.equals("17")) {
				//publishProgress(new ProgressUpdateInfo(mMsgLowBand));
			} else {
				//publishProgress(new ProgressUpdateInfo(mMsgHiBand));
			}

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(),
					"Error occurred while retrieving information from YouTube.",
					e);
		}

		if (lUriStr != null) {
			return Uri.parse(lUriStr);
		} else {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Uri pResult) {
		super.onPostExecute(pResult);

		try {
			if (isCancelled())
				return;

			if (pResult == null) {
				throw new RuntimeException("Invalid NULL Url.");
			}

			//Log.e("video2show", pResult.toString());
			mVideoView.setVideoURI(pResult);

	
			
			if (isCancelled())
				return;

			mVideoView.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {

					Log.e("estoy aqui", "onError");
					OpenYouTubePlayerActivity.this.finish();

					// videoError = true;

					// Your code goes here
					return true;

				}
			});

			mVideoView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e("LOG_TAG", "click");
				}
			});

			// TODO: add listeners for finish of video
			mVideoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer pMp) {
					if (isCancelled())
						return;
					isfinished = true;
					OpenYouTubePlayerActivity.this.finish();
				}

			});

			if (isCancelled())
				return;

			final MediaController lMediaController = new MediaController(
					OpenYouTubePlayerActivity.this);
			 mVideoView.setMediaController(lMediaController);
			 //mVideoView.setMediaController(null);
			 lMediaController.hide();

			// mVideoView.setKeepScreenOn(true);
			mVideoView
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer pMp) {
							if (isCancelled())
								return;
							//OpenYouTubePlayerActivity.this.lRelLayoutpre
								//	.setVisibility(View.GONE);
							//OpenYouTubePlayerActivity.this.lRelLayoutplayer
							//		.setVisibility(View.VISIBLE);
							
						}

					});

			if (isCancelled())
				return;

			mVideoView.requestFocus();
			mVideoView.start();
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Error playing video!",
					e);

			// if(!mShowedError){
			// showErrorAlert();
			// }
			OpenYouTubePlayerActivity.this.finish();
		}
	}
    	}

        
        @Override
        protected void onPause() {
        
        	Log.e("pos","onPause");
        	super.onPause();
        }
        


        
        @Override
        protected void onStart() {
                super.onStart();
        }

        @Override
        protected void onStop() {
                super.onStop();
        }
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
            	
            	intent.putExtra("msg", "Menu");     
            	setResult(RESULT_OK, intent);
            	OpenYouTubePlayerActivity.this.finish();
            }
            if (keyCode == KeyEvent.KEYCODE_BACK) {
            	intent.putExtra("msg", "Menu");     
            	setResult(RESULT_OK, intent);
            	OpenYouTubePlayerActivity.this.finish();
            }
            
            if (keyCode == KeyEvent.KEYCODE_HOME) {
            	intent.putExtra("msg", "Menu");     
            	setResult(RESULT_OK, intent);
            	OpenYouTubePlayerActivity.this.finish();
            }

            return true;
        }
        
   

}
