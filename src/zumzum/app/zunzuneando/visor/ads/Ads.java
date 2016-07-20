package zumzum.app.zunzuneando.visor.ads;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

import zumzum.app.zunzuneando.util.VideoDownloader;
import zumzum.app.zunzuneando.util.YouTubeUtility;
import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.contents.ContentAd;
import zumzum.app.zunzuneando.visor.contents.ContentTV;
import zumzum.app.zunzuneando.visor.livingCities.GetStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Ads extends Visor {

	String TAG = "Ads";

	private boolean isStop;

	private static boolean isFree;

	public ContentAd ad;

	private String file2show;

	VideoDownloader video2show;

	static final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ssZ");

	private static final long serialVersionUID = 0L;

	public Ads(Context context) {
		// TODO Auto-generated constructor stub
		super(context);

		_context = context;
		this.setReady(false);

		long duration = 50 * 1000;

		this.setDuration(duration);

		isStop = false;

		isFree = true;

		Log.e(TAG, "Create");

	}

	public void Prepare() {

	}

	public void Prepare(ContentAd ad) {

		this.ad = ad;

		if (ad.getSource().contains("dropbox")) {

			// this.file2show = ad.getSource();
			String source = ad.getSource();
			String filename = "";

			// download video
			filename = Environment.getExternalStorageDirectory()
					+ "/BarrioTV/Ads/test.flv";

			file2show = filename;

			VideoDownloader downloadThread = new VideoDownloader(source,
					filename, downloadedInfo);
			Log.e("Pos", "aqui1");
			downloadThread.start();
			Log.e("Pos", "aqui2");
			this.setReady(false);

		} else {

			this.file2show = ad.getSource();
			this.setReady(true);
		}

	}

	@Override
	public void Play() {

		if (ad.getSource().contains("http")) {

			if (ad.getSource().contains("youtube")) {

				// TODO Auto-generated method stub

				lVideoIntent = new Intent(
						null,
						Uri.parse("ytv://" + getYoutubeID(this.file2show)),
						_context,
						zumzum.app.zunzuneando.visor.ads.OpenYouTubePlayerActivity.class);
				// lVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				lVideoIntent.putExtra("title", "this.getTitle()");
				lVideoIntent.putExtra("description", "this.getInfo()");
				lVideoIntent.putExtra("Duration", "this.getDuration()");
			} 
			if (ad.getSource().contains("html")) {
				// TODO Auto-generated method stub
				lVideoIntent = new Intent(_context,
						zumzum.app.zunzuneando.visor.ads.WebShowActivity.class);
				// lVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// lVideoIntent.putExtra("filename", this.filesready.get(0));
				Bundle bundle = new Bundle();
				bundle.putSerializable("Filename", this.file2show);
				bundle.putSerializable("Duration", this.getDuration());

				this.lVideoIntent.putExtras(bundle);
			}
		} else {

			Log.e(TAG, "show ad static");
			lVideoIntent = new Intent(_context,
					zumzum.app.zunzuneando.visor.ads.AdsActivity.class);

			Bundle bundle = new Bundle();
			//bundle.putSerializable("Advertising", this.ad);

			bundle.putSerializable("background", this.ad.getBackground());
			bundle.putSerializable("logo", this.ad.getLogo());
			bundle.putSerializable("offert", this.ad.getImage());
			bundle.putSerializable("Duration", this.getDuration());
			
			this.lVideoIntent.putExtras(bundle);

		}

	}

	private String getYoutubeID(String file2show2) {
		// TODO Auto-generated method stub

		String videoid = file2show2.substring(file2show2.indexOf("v=") + 2);

		Log.e("videoId", videoid);

		return videoid;
	}

	Handler downloadedInfo = new Handler() {
		public void handleMessage(Message msg) {

			Log.e(TAG, "video downloaded return");

			boolean status = (Boolean) msg.obj;
			// if (status){
			setReady(true);
			// }

		};
	};

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

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}

}
