package zumzum.app.zunzuneando.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;



import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class YouTubeDownloader  implements Runnable{

    public static String newline = System.getProperty("line.separator");
    private static final String scheme = "http";
    private static final String host = "www.youtube.com";
    private static final Pattern commaPattern = Pattern.compile(",");
    private static final char[] ILLEGAL_FILENAME_CHARACTERS = { '/', '\n',
                    '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"',
                    ':' };

    private static final String ENCODING =  "UTF-8";
    private static final String USER_AGENT =  "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13";
    private static final String EXTENSION = "mp4";
	private static final String TAG = "YoutubeDownloader";
	
	private Handler outputMsg;
	private String youtubeInfo;
	private OutputStream output;
    public YouTubeDownloader(String youtubeId, OutputStream output, Handler outputMsg) {
    	
    	this.outputMsg =  outputMsg;
    	this.output = output;
    	
    	String info = null;
    	this.youtubeInfo = getSpecFromYouTubeVideoID(youtubeId);
    	
    	
    	
    }
   
   
    
    private String getSpecFromYouTubeVideoID(String id) {
    	        String spec = null;
    	        try {
    	            String infoUri = "http://www.youtube.com/get_video_info?&video_id=" + id;
    	            URL infoUrl = new URL(infoUri);
    	            URLConnection urlConnection = infoUrl.openConnection();
    	            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    	            try {
    	                StringBuilder sb = new StringBuilder();
    	                String line;
    	                while ((line = br.readLine()) != null)
    	                    sb.append(line);
    	                android.net.Uri fakeUri = android.net.Uri.parse("fake:/fake?" + sb);
    	                String streamMap = fakeUri.getQueryParameter("url_encoded_fmt_stream_map");
    	                if (streamMap == null)
    	                    return null;
    	                String[] streams = streamMap.split(",");
    	                for (int i = 0; i < streams.length; i++) {
    	                    fakeUri = android.net.Uri.parse("fake:/fake?" + streams[i]);
    	                    String url = fakeUri.getQueryParameter("url");
    	                    String type = fakeUri.getQueryParameter("type");
    	                    if (type != null && url != null &&
    	                        (type.startsWith("video/mp4") || type.startsWith("video/webm"))) {
    	                        spec = url;
    	                    }
    	                }
    	            } finally {
    	                br.close();
    	            }
    	        } catch (Exception e) {
    	            Log.e("VideoPlayer", "exception", e);
    	        }
    	        return spec;
    	    }
    
   
    

    private void download(String downloadUrl, OutputStream output) throws IOException{
            HttpGet httpget = new HttpGet(downloadUrl);
            httpget.setHeader("User-Agent", USER_AGENT);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpget);
            
            Log.e(TAG, " download1");
           
            HttpEntity entity = response.getEntity();
            Log.e(TAG, " download2");
            
            if (entity != null && response.getStatusLine().getStatusCode() == 200) {
            	 
                    InputStream instream = entity.getContent();
                    Log.e(TAG, " download3");
                    try {
                            byte[] buffer = new byte[2048];
                            int count = -1;
                            while ((count = instream.read(buffer)) != -1) {
                                    output.write(buffer, 0, count);
                                    Log.e(TAG, " download4");
                            }
                            output.flush();
                    } finally {
                            output.close();
                    }
            } //else {
                    
           // }
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		HttpGet httpget = new HttpGet(this.youtubeInfo);
        httpget.setHeader("User-Agent", USER_AGENT);
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
		
		response = httpclient.execute(httpget);
		
        
        Log.e(TAG, " download1");
       
        HttpEntity entity = response.getEntity();
        Log.e(TAG, " download2");
        
        if (entity != null && response.getStatusLine().getStatusCode() == 200) {
        	 
                InputStream instream = entity.getContent();
                Log.e(TAG, " download3");
                try {
                        byte[] buffer = new byte[2048];
                        int count = -1;
                        while ((count = instream.read(buffer)) != -1) {
                                this.output.write(buffer, 0, count);
                                Log.e(TAG, " download4");
                        }
                        send(true);
                        this.output.flush();
                } finally {
                        output.close();
                }
        }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			send(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			send(false);
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
						
		this.outputMsg.sendMessage(mesg);

	}
   
    
    
    
}


