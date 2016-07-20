package zumzum.app.zunzuneando.visor.youtube;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


/**
 * This is the task that will ask YouTube for a list of videos for a specified user</br>
 * This class implements Runnable meaning it will be ran on its own Thread</br>
 * Because it runs on it's own thread we need to pass in an object that is notified when it has finished
 *
 * @author paul.blundell
 */
@SuppressLint({ "ParserError", "ParserError", "NewApi" })
public class GetYouTubePlaylistVideosTask implements Runnable {
	// A reference to retrieve the data when this task finishes
	public static final String LIBRARY = "Library";
	// A handler that will be notified when the task is finished
	private final Handler replyTo;
	// The user we are querying on YouTube for videos
	private String  channel;


	/**
	 * Don't forget to call run(); to start this task
	 * @param replyTo - the handler you want to receive the response when this task has finished
	 * @param username - the username of who on YouTube you are browsing
	 */
	public GetYouTubePlaylistVideosTask(Handler replyTo, String channels) {
		this.replyTo = replyTo;
		
		this.channel = channels;
		
		

	}

	//@SuppressLint({ "ParserError", "ParserError" })
	@Override
	public void run() {
		Bundle data = new Bundle();

		try {
			
			// Create a list to store are videos in
			List<Video> videos = new ArrayList<Video>();

			//Log.e("jsonString","GetYouTubePlaylistVideosTask");

			for(int j=0;j<1;j++)
			{



				// Get a httpclient to talk to the internet
				HttpClient client = new DefaultHttpClient();
				// Perform a GET request to YouTube for a JSON list of all the videos by a specific user
				HttpUriRequest request = new HttpGet("http://gdata.youtube.com/feeds/api/playlists/"+this.channel+"?v=2&alt=jsonc");
				// Get the response that YouTube sends back
				HttpResponse response = client.execute(request);
				// Convert this response into a readable string
				String jsonString = StreamUtils.convertToString(response.getEntity().getContent());

				//Log.e("jsonString",jsonString);

				try{
					// Create a JSON object that we can use from the String
					JSONObject json = new JSONObject(jsonString);


					// For further information about the syntax of this request and JSON-C
					// see the documentation on YouTube http://code.google.com/apis/youtube/2.0/developers_guide_jsonc.html

					// Get are search result items
					JSONArray jsonArray = json.getJSONObject("data").getJSONArray("items");

					//Log.e("numero de videos", String.valueOf(jsonArray.length()));

					//jsonArray = jsonArray.getJSONArray("videos");

					// Loop round our JSON list of videos creating Video objects to use within our app
					for (int i = 0; i < jsonArray.length(); i++) {


						

						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						

						JSONObject jsonVideo = jsonObject.optJSONObject("video");
						



						// The title of the video
						String title = jsonVideo.getString("title");
						try{
						String thumbUrl = jsonVideo.getJSONObject(
								"thumbnail").getString("sqDefault");
						//Log.e("URL", thumbUrl);
						
						if (!(TextUtils.isEmpty(thumbUrl))) {
						
							//Log.e("title", title);
							// The url link back to YouTube, this checks if it has a mobile url
							// if it doesnt it gets the standard url
							String url =  "";
							try {
								url = jsonVideo.getJSONObject("player").getString("mobile");
							} catch (JSONException ignore) {
								url = jsonVideo.getJSONObject("player").getString("default");
							}
							//Log.e("url", url);
							

							if (!(TextUtils.isEmpty(url))) {
								// A url to the thumbnail image of the video
								// We will use this later to get an image using
								// a Custom ImageView
								// Found here
								// http://b//Log.blundell-apps.com/imageview-with-loading-spinner/
								//String thumbUrl = jsonVideo.getJSONObject(
										//"thumbnail").getString("sqDefault");

								int ind = url.indexOf("v=");
								String videoIdd = url.substring(ind + 2,
										url.length());
								//Log.e("URL", videoIdd);

								// Create the video object and add it to our
								// list
								
									Log.e("title", title);
									videos.add(new Video(title, videoIdd, thumbUrl));
								
								
							}
						}
						} catch (JSONException e) {
							Log.e("Feckjkj", e.toString());
							//data.putSerializable(LIBRARY, null);
						}
					}
				} catch (JSONException e) {
					//////Log.e("Feck", e.toString());
					data.putSerializable(LIBRARY, null);
				}
			}
			Collections.shuffle(videos);
			Collections.shuffle(videos);
			Collections.shuffle(videos);

			// Create a library to hold our videos
			Library lib = new Library(videos);
			// Pack the Library into the bundle to send back to the Activity

			data.putSerializable(LIBRARY, lib);

			// Send the Bundle of data (our Library) back to the handler (our Activity)
			Message msg = Message.obtain();
			msg.setData(data);
			replyTo.sendMessage(msg);

			// We don't do any error catching, just nothing will happen if this task falls over
			// an idea would be to reply to the handler with a different message so your Activity can act accordingly
		} catch (ClientProtocolException e) {
			//Log.e("Feckk", e.toString());
			data.putSerializable(LIBRARY, null);
		} catch (IOException e) {
			//Log.e("Feckkk", e.toString());
			data.putSerializable(LIBRARY, null);
			Message msg = Message.obtain();
			msg.setData(data);
			replyTo.sendMessage(msg);
		}

	}

}