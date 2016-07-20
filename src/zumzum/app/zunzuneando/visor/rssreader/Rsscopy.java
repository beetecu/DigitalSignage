package zumzum.app.zunzuneando.visor.rssreader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import zumzum.app.zunzuneando.R;
import zumzum.app.zunzuneando.visor.Visor;
import zumzum.app.zunzuneando.visor.contents.ContentRSS;

public class Rsscopy extends Visor{

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


	//private RSS currentRss;

	protected boolean _readyforNext;

	private ContentRSS _rssContent;


	public Rsscopy(Context context) {
		super(context);
		// TODO Auto-generated constructor stub



		this.setDuration(40*1000); // duracion en milisegundos


	}

	@Override
	public void Prepare() {
		// TODO Auto-generated method stub


		_ready = false;
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


		//new Thread(new Runnable() { 
			//public void run(){

				Update();


			//}
		//}).start();




	}

	public void Update(){
		
		new Thread(new Runnable() { 
		public void run(){

		_ready = false;

		next = false;

		index = 0;



		String root = Environment.getExternalStorageDirectory()+ "/BarrioTV/RSS/";
		myDir = new File(root + getRssContent().getTheme());  

		Log.e("TEMA", getRssContent().getTheme());

		deleteDirectory(myDir); 

		try {

			for (int i = 0; i < getRssContent()._rss.size(); i++){

				_readyforNext = false;

				reader = new RSSReader();

				//Log.e("rss to load url: ", getRssContent()._rss.get(i));

				String uri = getRssContent()._rss.get(i);
				
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
								Log.e("WAIT", " wait for Next");
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



				//wait to finish
				

			}
			
			_ready = true;
			
			/*
			boolean flag = true;
			_readyforNext = false;
			while (flag){

				if (_readyforNext){

					Log.e("NExt", "FIN");

					flag = false;
					
					_ready = true;

				}
				else{
					try {
						Log.e("WAIT", " wait for Next");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			 */

			//if  (_readyforNext)
				//_ready = true;




		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		}
	  }).start();

	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return _ready;
	}

	@Override
	public void Play() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub




		// lVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		lVideoIntent = new Intent(_context, zumzum.app.zunzuneando.visor.rssreader.RssActivity.class);


		Bundle bundle = new Bundle();
		bundle.putSerializable("Term", this._rssContent.getTheme());
		bundle.putSerializable("Duration", this.getDuration());

		this.lVideoIntent.putExtras(bundle);

		// context.startActivity(lVideoIntent);
		// context.startActivityForResult(lVideoIntent,1);

	}

	@Override
	public void Stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub

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
						
						//Log.e(TAG, "feed enclosure: "+feed.getItems().get(index).getTitle());
						
												
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
						
						Log.e(TAG, "url: "+url);
						
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
			int color = Color.parseColor(this._rssContent.getColor());
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
					
					
					files[i].delete();
				}
			}
		}
		return( path.delete() );
	}

	public class RSS{

		private String _rssAddress;
		private String _rssColor;

		public RSS(String rssAddress, String rssColor){

			_rssAddress = rssAddress;
			_rssColor = rssColor;
		}

		public void setRssAdress(String rssAddress){
			_rssAddress = rssAddress;
		}
		public void setRssColor(String rssColor){
			_rssColor = rssColor;
		}
		public String getRssAdress(){
			return _rssAddress;
		}
		public String getRssColor(){
			return _rssColor;
		}
	}



	public ContentRSS getRssContent() {
		return _rssContent;
	}

	public void setRssContent(ContentRSS _rssContent) {
		this._rssContent = _rssContent;
	}

	@Override
	public boolean isError() {
		// TODO Auto-generated method stub
		return false;
	}

}
