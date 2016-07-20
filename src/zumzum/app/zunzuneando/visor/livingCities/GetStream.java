package zumzum.app.zunzuneando.visor.livingCities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SyncFailedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This is the task that will ask YouTube for a list of videos for a specified
 * user</br> This class implements Runnable meaning it will be ran on its own
 * Thread</br> Because it runs on it's own thread we need to pass in an object
 * that is notified when it has finished
 * 
 * @author paul.blundell
 */
@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError",
	"ParserError", "ParserError" })
public class GetStream implements Runnable {
	// A reference to retrieve the data when this task finishes

	String ideo;
	private final Handler replyTo;
	public static final String STATUS = "";
	private static final int BUFFERMAX = 2000;
	Bundle data;
	Process process = null;
	Context context;
	static boolean isError = false;
	File filedir;
	static FileOutputStream outputfile;

	//InputStream in;
	byte[] buf;

	boolean isFirst = true;
	private static boolean stop = false;
	
	static boolean flagFile = true;

	/**
	 * Don't forget to call run(); to start this task
	 * 
	 * @param replyTo
	 *            - the handler you want to receive the response when this task
	 *            has finished
	 * @param username
	 *            - the username of who on YouTube you are browsing
	 */
	public GetStream(Handler replyTo, String bideo, File mfiledir, File output) {
		ideo = bideo;
		this.replyTo = replyTo;
		data = new Bundle();
		this.filedir = mfiledir;
		this.isError = false;

		isFirst = true;
		stop = false;
		
		flagFile = true;
		
		try {
			this.outputfile = new FileOutputStream(output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void stopThread() {

		if (flagFile){
			//frameLoaderThread.interrupt();

			boolean flag = true;

			stop = true;

			while(flag){


				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!flagFile)
					flag = false;

			}


			closeAll();

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("STOP","SSTTTTTOOPPPPPPPPPP");
			
			//frameLoaderThread.destroy();
		}
	}
	
	// @SuppressLint({ "ParserError", "ParserError" })
	@Override
	public void run() {

		data.putSerializable(STATUS, "Connecting..");

		Log.e("Pos", "Connecting.." + this.ideo);
		this.isError = false;
		try {
			process = Runtime.getRuntime().exec("./rtmpdump " + this.ideo,
					null, this.filedir);

			Log.e("Pos", "rtmpdump..OK");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.isError = true;
			Log.e("error", "rtmpdump");
			data.putSerializable(STATUS, "rtmperror");
			//Log.e("Pos", "Connecting error");
			send(false);
			process.destroy();
		}
		
		

		if (!this.isError) {
			
			

			// checking if the site is online

			buf = new byte[1024 * 32];
			int len;
			int totalBytesRead = 0;
			int totalKbRead = 0;
			//Log.e("fd", "reading");

			int cont = 0;

			ExecutorService executor = Executors.newFixedThreadPool(2);
			Integer readByte = 1;

			while (flagFile) {
				readByte = 1;
				

				try {
					while ((readByte > 0)) {

						Future<Integer> future = executor.submit(readTask);
						//Log.e("GetStream", "Pos1");
						try {
							readByte = future.get(50000, TimeUnit.MILLISECONDS);
							//Log.e("GetStream", "Pos2");
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							flagFile = false;
							readByte = 0;
							cont = cont + 1;
						} catch (ExecutionException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							flagFile = false;
							readByte = 0;
							cont = cont + 1;
						} catch (TimeoutException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							flagFile = false;
							readByte = 0;
							cont = cont + 1;
						}

						if (readByte > 0){

							//Log.e("GetStream", "Pos3");

							totalBytesRead = totalBytesRead + readByte;
							// incrementalBytesRead += numread;
							totalKbRead = totalBytesRead / (1024);

							outputfile.write(buf, 0, readByte);
							cont = 0;

							// mProgressBar.setProgress(totalKbRead/3000);

							String status = "#" + String.valueOf(totalKbRead);

							isFirst = false;

							data.putSerializable(STATUS, status);
							//send();
							//Log.e("fd", "readingggg");

							if (totalKbRead > BUFFERMAX) {

								process.destroy();
								closeAll();
								flagFile = false;

								status = "finish";
								Log.e("GetStream", "finish");

								data.putSerializable(STATUS, status);
								send(true);

								return;
							}
						}else{

							Log.e("GetStream", "Pos4");
						}


					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}



				cont++;
				Log.e("cont", Integer.toString(cont));
				if (cont >= 2) {

					Log.e("fd", "cont == 2");

					flagFile = false;
					//data.putSerializable(STATUS, "Reconecting");
					//send();
				} else {
					//flagFile = false;
					//cont=5;
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					flagFile = true;


				}
				Log.e("fd", "continue");

			}

			if (cont >= 2) {
				Log.e("Error", "read");
				isError = true;

				data.putSerializable(STATUS, "Channel is down");
				send(false);

				process.destroy();
				closeAll();
				flagFile = false;



				return;


			}

		} else {
			// finish();
			data.putSerializable(STATUS, "Channel is down");
			send(false);

			process.destroy();
			closeAll();


			return;
		}

		//Log.e("me voy", "me fui");

		// We don't do any error catching, just nothing will happen if this task
		// falls over
		// an idea would be to reply to the handler with a different message so
		// your Activity can act accordingly

	}


	// Read data with timeout
	Callable<Integer> readTask = new Callable<Integer>() {
		@Override
		public Integer call() throws Exception {
			//Log.e("GetStream", "Pos5");
			buf = new byte[1024 * 32];
			return  process.getInputStream().read(buf); 
		}
	};

	public static void closeAll() {

		try {
			outputfile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputfile.getFD().sync();
		} catch (SyncFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputfile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void send(boolean result) {
		// Send the Bundle of data (our Library) back to the handler (our
		// Activity)

		
		Log.e("SEND", "SEND MSG");
		//Message msg = Message.obtain();
		//msg.setData(data);
		//replyTo.sendMessage(msg);
		
		//report
		Message mesg = new Message();
		mesg.obj = result; 
						
		replyTo.sendMessage(mesg);

	}

}