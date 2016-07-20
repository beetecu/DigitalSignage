package zumzum.app.zunzuneando.visor.contents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zumzum.app.zunzuneando.R;
import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.rssreader.RSSFeed;
import zumzum.app.zunzuneando.visor.rssreader.RSSReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.TextView;

public class ListOfContentRSS{
	
	protected static final String TAG = "RSS";
	RSSReader reader;
	RSSFeed feed;


	private ImageView itemImage;
	private TextView  title;
	private TextView  itemTitle;
	private TextView  itemDescription;
	private TextView  itemDate;
	private TextView  itemExtra;	
	private View targetView;

	int index;

	boolean next;
	private File myDir;
	private String image_name;
	
	
	public boolean _isReady;


	//private RSS currentRss;

	protected boolean _readyforNext;

	
	public List<ContentRSS> _rssContents;
	

	
	private int _catGeneral;
	
	private String _file2save;
	private String _url2load;
	
	String _root; 

	private ContentRSS currentContent;
	
	Context _context;

	
	public ListOfContentRSS(String file2save, String url2load, Context context){
		
		_isReady = false;
		
		_context = context;
		
		_root = Environment.getExternalStorageDirectory()+ "/BarrioTV/RSS/";
		
		_rssContents = new ArrayList<ContentRSS>(); 
		
		_file2save = file2save;
		
		_url2load = url2load;
	
		//newsImage = new ArrayList<Bitmap>();

		//newsImage = null;


		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.newspage, null);

		itemImage = (ImageView) v.findViewById(R.id.iImage);
		title = (TextView) v.findViewById(R.id.title);
		itemTitle = (TextView) v.findViewById(R.id.iTitle);
		itemDescription = (TextView) v.findViewById(R.id.iDescription);
		itemDate  = (TextView) v.findViewById(R.id.fecha);
		itemExtra  = (TextView) v.findViewById(R.id.infoExtra);

		targetView = v.findViewById(R.id.relview);

		
		
		setCatGeneral(0);
		
		loadRssSources(_file2save, _url2load);
		
	}
	
	public void UpdateContent(){
		
		
		// wait for load
		
		loadRssSources(_file2save, _url2load);
		
		
		
	}
	
	

	public void loadRssSources(final String file2save, final String url2load){

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {


				//Log.e("Pos1", "Pos1");
				
				//String url = "http://192.168.1.130:8000/Anuncios/default/call/json/category_rss?screen=1";

				try {
					String Content = inputStreamToString(doHttpGet(url2load));
					
					//Log.e("Pos2", Content);

					JSONObject jsonResponse;

					jsonResponse = new JSONObject(Content);

					//Log.e("Pos3", "Pos3");
					
					FileWriter file = new FileWriter(file2save);
					file.write(jsonResponse.toString());
					file.flush();
					file.close();
					
					//Log.e("Pos4", "Pos4");
					
					//loadJsonRss(file2save);
					//loadJsonCamera(file2save);
					//loadJsonAds(file2save);
					//loadJsonVideo(file2save);
					


				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				loadJsonRss(file2save);

			}
		}); 
		th.start();

	}
		
	
	private void loadJsonRss(final String file2load){

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {


				//Log.e("Pos5", "loadJsonRss1");
				
				File yourFile = new File(file2load);
	            FileInputStream stream;
				try {
					stream = new FileInputStream(yourFile);
					String jsonStr = null;
		            try {
		                FileChannel fc = stream.getChannel();
		                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

		                jsonStr = Charset.defaultCharset().decode(bb).toString();
		                
		                //Log.e("jsonStr", jsonStr);
		                
		                
		                JSONObject jsonObj = new JSONObject(jsonStr);

		                // Getting data JSON Array nodes
		                JSONArray data  = jsonObj.getJSONArray("rss");
		                
		                
		                //Log.e("nodos", Integer.toString(data.length()));
		                
		                //Log.e("quien soy", data.optString(0, "rss_country"));
		                
		                
		                ContentRSS contentRSS;
		    			
		    				
		    			List<String> duplicateList = (List<String>) Arrays.asList("Android" , "Android", "iOS", "Windows mobile");

		    			LinkedHashSet<String> listToSet = new LinkedHashSet<String>(duplicateList);
		    		     
		    	        //Creating Arraylist without duplicate values
		    	        List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);

		    			//1 create cathegories list
		    			List<String> cat = new ArrayList<String>();
		    			
						for (int i = 0; i < data.length(); i++) {
		    			//for (int i = 0; i < 1; i++) {	
		                	
		                	//Log.e("Pos6", "Pos");
		                	
		                    JSONObject c = data.getJSONObject(i);
		                    
		                    if (c.getJSONObject("t_categories").getString("f_categorie").contains("General"))
		                    	setCatGeneral(i);
		                    
		                    cat.add( c.getJSONObject("t_categories").getString("f_categorie"));
		                    
		                   
		                  }
		    			
		                //2 remove duplicates items
						LinkedHashSet<String> catRedset = new LinkedHashSet<String>(cat);
		    		     
		    	        //Creating Arraylist without duplicate values
		    	        List<String> catWithoutDuplicates = new ArrayList<String>(catRedset);
						
		    			//3 for each cathegory
		    	        for(int j = 0; j < catWithoutDuplicates.size(); j++){
		    	        //for (int j = 2; j < 3; j++) {	
		    	        	
		    	        	String tempCat = catWithoutDuplicates.get(j);
		    	        	
		    	        	//Log.e("categotie", tempCat);
		    	        	
		    	        
		    	        	contentRSS = new ContentRSS("", tempCat);
		    	        	
		    	        	
		    	        	for (int i = 0; i < data.length(); i++) {
			                	
			                	//Log.e("Pos6", "Pos");
			                	
			                    JSONObject c = data.getJSONObject(i);
			                    
			                    String temptempCat = c.getJSONObject("t_categories").getString("f_categorie");
			                    
			                    // find all items of this cathegory
			                    
			                    if (temptempCat.contains(tempCat)){
			                    	
			                    	//Log.e("rss_country", c.getJSONObject("t_categories_rss").getString("rss_country"));
				                    //Log.e("rss_city", c.getJSONObject("t_categories_rss").getString("rss_city"));
			                    	
			                    	if (c.getJSONObject("t_categories").getString("f_categorie_color") != null)
			                    		
			                    		contentRSS.setColor(c.getJSONObject("t_categories").getString("f_categorie_color"));
			                    	else
			                    		contentRSS.setColor("#fb022b");
			                    	
			                    	
			                    	
			                    	
			                    	contentRSS._rss.add(c.getJSONObject("t_categories_rss").getString("f_rss_source"));
			                    	
			                    	//contentRSS._rss.add("http://estaticos.elmundo.es/elmundo/rss/portada.xml");
			                    	
			                    	//Log.e("rss", c.getJSONObject("t_categories_rss").getString("f_rss_source"));
			                    	
			                    }
			                    
			                    
			                    
			                   
			                  }

			    			
			    			_rssContents.add(contentRSS);
			    			
			    			
		    	        	
		    	        }
		    	        
		    			  
		             
		              } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		              finally {
		                try {
							stream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		              }
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
				

				Log.e(TAG, "END loading rss sources");
				updateRssFeeds();
			}
			
		}); 
		th.start();



	}


	public void updateRssFeeds(){

		new Thread(new Runnable() { 
			

			public void run(){

				try{
					
					_isReady = false;

					//for each categorie
					for(int i=0;i< _rssContents.size();i++){
					//for(int i=5;i<6;i++){
						
						
						myDir = new File(_root + _rssContents.get(i).getTheme());  

						Log.e("TEMA", _rssContents.get(i).getTheme());
						
						currentContent = _rssContents.get(i);

						deleteDirectory(myDir); 

						//for each feed sourse
						for(int j=0;j< _rssContents.get(i)._rss.size();j++){

							//load feeds
							_readyforNext = false;

							reader = new RSSReader();

							//Log.e("rss to load url: ", _rssContents.get(i)._rss.get(j));

							String uri = _rssContents.get(i)._rss.get(j);
							
							try{
							
								reader.load(uri, feedLoded);
								boolean flag = true;
								while (flag){

									if (_readyforNext){

										Log.e("NExt", "ready for Next");

										flag = false;

									}
									else{
										try {
											//Log.e("WAIT", " wait for Next");
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						}

					}
					 _isReady = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}).start();





	}

	public ContentRSS getRssFeeds(int index){
		
		if (index >= _rssContents.size())
			index=0;
		
		return _rssContents.get(index);
	}
	
	
	public ContentRSS getRssFeeds(String cat){
		
		int index=-1;
		
		int i = 0;
		
		boolean flag = true;
		
		while(flag){
			
			if (_rssContents.get(i).getTheme().contains(cat)){
				
				if ( _rssContents.get(i)._rss.size() > 0){
				
					flag = false;
					index = i;
				}
			}
			
			if (i>=_rssContents.size()){
				
				flag = true;
			}
			
			i++;
			
		}
		
		
		// si no existe de esa categoria se toma de la caregoría genérica
		if (index == -1){
			index = getCatGeneral();
			
		}
		
		
		return _rssContents.get(index);
	}
	
	



	public int getCatGeneral() {
		return _catGeneral;
	}


	public void setCatGeneral(int catGeneral) {
		this._catGeneral = catGeneral;
	}
	
	private InputStream doHttpGet(String urlString) throws IOException {

		InputStream inputStream = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.setUseCaches(true);
			Object content = httpConn.getContent();
			if (content instanceof Bitmap) {
				Bitmap bitmap = (Bitmap) content;
			}

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting" + ex);
		}
		return inputStream;
	}

	public String inputStreamToString(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
	


	public static boolean deleteDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					System.currentTimeMillis();
					
					if (System.currentTimeMillis() - files[i].lastModified() > 5*24*60*60) {
						files[i].delete();
					}
					
					
				}
			}
		}
		return( true );
	}
	
	
	private Handler feedLoded = new Handler() {


		@Override
		public void handleMessage( Message msg) {
			feed = (RSSFeed) msg.obj;
			
			Log.e(TAG, String.valueOf(feed.getItems().size()));
			
			new Thread(new Runnable() { 
				public void run(){


					int cant_items = 20;
					if (feed.getItems().size() < cant_items)
						cant_items = feed.getItems().size();

					for (int i = 0; i < feed.getItems().size(); i++){
						
						Log.e(TAG, String.valueOf(i));

						index = i;
						next = false;
						
						boolean url_enc = false;
						
						boolean url_media = false;
						
						//Log.e(TAG, "feed enclosure: "+feed.getItems().get(index).getDescription());
						
												
						try{
							
							//Log.e(TAG, "feed enclosure1: "+feed.getItems().get(index).getEnclosure().getUrl().toString());

						
						if (feed.getItems().get(index).getEnclosure() != null)
								
							url_enc = feed.getItems().get(index).getEnclosure().getUrl().toString().contains(".jpg");
						
						}catch(Exception e)
						{
							Log.e(TAG, "FEED multimedia error getEnclosure");
						}
						
						try{

						if ( feed.getItems().get(index).getThumbnails().size() >0)
						 
							url_media = feed.getItems().get(index).getThumbnails().get(0).getUrl().toString().contains(".jpg");

						}catch(Exception e)
						{
							Log.e(TAG, "FEED multimedia error getThumbnails()");
						}
						String url = null;
						
						if (url_enc)
							url = feed.getItems().get(index).getEnclosure().getUrl().toString();
						
						if (url_media)
							url = feed.getItems().get(index).getThumbnails().get(0).getUrl().toString();
						
						//Log.e(TAG, "url: "+url);
						
						if (url != null){ 
							//Log.e("image_url", (feed.getItems().get(index).getThumbnails().get(0).getUrl().toString()));
							//itemImage.setImageBitmap(getBitmapFromURL(feed.getItems().get(index).getThumbnails().get(0).getUrl().toString()));

							//String temp_name = feed.getItems().get(index).getThumbnails().get(0).getUrl().toString();
							image_name = url.substring(url.lastIndexOf("/"), url.lastIndexOf("."));

							getBitmapFromURL(url, imagefeedLoded);

							//wait to finish
							boolean flag = true;
							while (flag){

								if (next){

									//Log.e("NExt", "Next");

									flag = false;

								}
								else{
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}

						}

					}
					_readyforNext = true;
					
				}
				
				
			}).start();

		}

	};


	private Handler imagefeedLoded = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			//Log.e("image_url", "loaded image");

			Bitmap b = (Bitmap) msg.obj;
			itemImage.setImageBitmap(b);

			buidImage(b);


		}
	};

	private void buidImage(Bitmap bitmap){

		//Log.e("Description", feed.getDescription());

		//Log.e("title_new", feed.getItems().get(index).getTitle());

		try{

			//Log.e(TAG, "pos1");
			//itemImage.setImageBitmap(feed.getItems().get(0).getThumbnails().get(0).)
			title.setText(" " + feed.getTitle());
			
			//Log.e(TAG, this._rssContent.getColor());
			int color = Color.parseColor(currentContent.getColor());
			title.setBackgroundColor(color);

			//Log.e(TAG, "pos3");
			
			Spanned sTitle = Html.fromHtml(feed.getItems().get(index).getTitle());
			itemTitle.setText(sTitle);


			//Log.e(TAG, "pos4");

			int end = feed.getItems().get(index).getDescription().indexOf(".");
			String des_short=feed.getItems().get(index).getDescription().substring(0, end+1);

			itemImage.setImageBitmap(bitmap);

			Spanned sDescription = Html.fromHtml(des_short);
			itemDescription.setText(sDescription );

			itemDate.setText("Publicado: "+feed.getItems().get(index).getPubDate().toLocaleString());
			itemExtra.setText(feed.getDescription());

			DisplayMetrics dm = _context.getApplicationContext().getResources().getDisplayMetrics(); 
			targetView.measure(MeasureSpec.makeMeasureSpec(dm.heightPixels, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(dm.widthPixels, MeasureSpec.EXACTLY));
			targetView.layout(0, 0, targetView.getMeasuredWidth(), targetView.getMeasuredHeight());
			//targetView.setGravity(Gravity.CENTER);
			Bitmap b = Bitmap.createBitmap(targetView.getMeasuredWidth(),
					targetView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			targetView.draw(c);

			//Log.e(TAG, "pos5");
			
			SaveImage(b);
			next = true;

			//Log.e(TAG, "pos6");

			//newsImage = new ArrayList<Bitmap>();
			//newsImage.add(b);

		}catch(Exception e){

			Log.e(TAG, "EROR buildImage");
			e.printStackTrace();
		}


	}
	
	private void SaveImage(final Bitmap finalBitmap) {

		new Thread(new Runnable() { 
			public void run(){

				myDir.mkdirs();
				Random generator = new Random();
				int n = 10000;
				n = generator.nextInt(n);
				String fname = image_name +".png";
				File file = new File (myDir, fname);
				//if (file.exists ()) file.delete (); 
				try {
					FileOutputStream out = new FileOutputStream(file);
					finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
					out.flush();
					out.close();

				} catch (Exception e) {
					e.printStackTrace();
				}


			}
		}).start();


	}

	
	public void getBitmapFromURL(final String src, final Handler imageLoded) {

		new Thread(new Runnable() {
			private InputStream feedStream;

			public void run() {
				try {


					Bitmap myBitmap = null;

					URL url = new URL(src);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					myBitmap = BitmapFactory.decodeStream(input);

					buidImage(myBitmap);

					//Message mssg = new Message();
					//mssg.obj = myBitmap;
					//msg.obj = result;
					//imageLoded.sendMessage(mssg);



				} catch (IOException e) {
					e.printStackTrace();
					//return null;
				}
			};
		}).start();

	}


	public String getSource2Show(String cat){
		
		//myDir = new File(_root + cat); 
		
		cat = _root + cat;
		
		myDir = new File(cat); 
		
		boolean flag = false;
		
		if( myDir.exists() ) {
			File[] files = myDir.listFiles();
			if (files.length > 1){
				
				flag = true;
				
			}
			
		}
		
		if (!flag){
			cat = _root + "General";
		}
		
		
		return cat;
		
	}
	
	public String getSource2Show(int index){
		
		//myDir = new File(_root + cat); 
		
		String cat = _root +  this._rssContents.get(index).getTheme();
		
		boolean flag = false;
		
		myDir = new File(cat); 
		
		if( myDir.exists() ) {
			File[] files = myDir.listFiles();
			if (files.length > 1){
				
				flag = true;
				
			}
			
		}
		
		if (!flag){
			cat = _root + "General";
		}
		
		
		return cat;
		
	}	

}
