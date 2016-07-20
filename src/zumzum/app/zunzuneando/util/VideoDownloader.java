package zumzum.app.zunzuneando.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;



import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class VideoDownloader extends Thread implements Runnable {
	private static final int BUFFERMAX = 1000;
	private static final String TAG = "VideoDownloader";
	private long contentLength = -1;
	private long downloadingProgress = 0;
	private String embedUrlStr;
	private File tempFile;
	
	private Handler _report;

	

	public VideoDownloader(String embedUrlStr, String pathToFile, Handler report) {
		//super("DOWNLOADING_STREAM");
		Log.e(TAG,"video downloader create");
		//this.embedUrlStr = embedUrlStr;
		this.embedUrlStr = "https://dl.dropboxusercontent.com/s/ea5b8rw08qh5f80/boracay2.flv?dl=1";		
		this.tempFile = new File(pathToFile);
		this._report = report;
		
		
	}
	
	

	@Override
	public void run() {
		try {
			 Log.e(TAG,"aqui1");
			this.startDownloadItem();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "", e);
		}
	}

	/**
	 * 3rd
	 */
	private void startDownloadItem() {
		
		
		Log.e(TAG, "start Downloading");

		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(
					new URL(this.embedUrlStr).openStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		saveInputStreamToFile(bis);
	}

	/**
	 * 4th
	 */
	private void saveInputStreamToFile(BufferedInputStream bin) {
		BufferedOutputStream bos = null;
		int count = 0;
		try {
			Log.e(TAG,
					"INSIDE saveInputStreamToFile");
			this.tempFile.createNewFile();
			bos = new BufferedOutputStream(
					new FileOutputStream(this.tempFile, false));
			byte[] buffer = new byte[1024*8];
			int byteRead = 0;

			Log.e(TAG,this.embedUrlStr);
			
			URL url = new URL(this.embedUrlStr);
			URLConnection conection = url.openConnection();
			conection.connect();
			conection.setConnectTimeout(30000);

			contentLength = conection.getContentLength();
			
			while ((byteRead = bin.read(buffer)) != -1
					&& !Thread.currentThread().isInterrupted()) {
				bos.write(buffer, 0, byteRead);
				bos.flush();
				count++;
				downloadingProgress += byteRead;
				
				Log.e("PosVideo","downloading");
				
				

			}
			if (Thread.currentThread().isInterrupted()) {
				closeStreams(bin, bos);
				//new File(filePath).delete();
				return;
			}
			Log.e("DOWNLOADING ENDS", "DOWNLOADING ENDS");
			//closeStreams(bin, bos);
			downloadingProgress = 100L;
			
			
			
		} catch (Exception e) {
			Log.e(TAG, "" + e.toString());
		} finally {
			Log.e("DOWNLOADING CLOSE", "DOWNLOADING CLOSE");
			closeStreams(bin, bos);
		}
		
		//report
		Message mesg = new Message();
		mesg.obj = false; 
		
		
		if (count < 1000) {
			mesg.obj = true;
		}
	
		
		_report.sendMessage(mesg);
	}

	private void closeStreams(BufferedInputStream bis, BufferedOutputStream bos) {
		if (bos != null) {
			try {
				bos.flush();
				bos.close();
			} catch (IOException e) {
				Log.e(TAG, "",
						e);
			}
		}
		if (bis != null)
			try {
				bis.close();
			} catch (IOException e) {
				Log.e(TAG, "", e);
			}
	}

	public synchronized long getDownloadingProgress() {
		return this.downloadingProgress;
	}

	public long getContentLength() {
		return this.contentLength;
	}

	
}
