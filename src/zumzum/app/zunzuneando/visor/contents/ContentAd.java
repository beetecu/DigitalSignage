package zumzum.app.zunzuneando.visor.contents;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import zumzum.app.zunzuneando.util.YouTubeDownloader;
import zumzum.app.zunzuneando.visor.livingCities.GetStream;


import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ContentAd extends Content implements Serializable{
	
	private String _type_rec;
	

	private String _type;
	private String _title;
	private String _logo;
	private String _image;
	private String _back;
	private String _bussinessName;
	private String _discount;
	private double _priceBefore;
	private double _priceAfter;
	private String _gpsPos;
	
	
	private static final long serialVersionUID = -7060210544600464481L;


	protected static final String TAG = "ContentAd";   



	
	public ContentAd(String source, String theme){
		
		super(source, theme);
		
		if (source.contains("youtube")){
			
			//download video
            String videoId =  getYoutubeID(source);
            String outputFile = Environment.getExternalStorageDirectory()+ "/BarrioTV/Ads/" + videoId +".mp4";
            
            File temp = new File(outputFile);
            
            if (!temp.exists()){
            	
            	BufferedOutputStream bops;
                try {
                        bops = new BufferedOutputStream(new FileOutputStream(outputFile));
                        new Thread(new YouTubeDownloader(videoId, bops, downloadedInfo)).start();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } 
            }
            
            

		}
		
		
		
		
		
	}
	
	Handler downloadedInfo = new Handler() {
		public void handleMessage(Message msg) {


			Log.e(TAG, "video dwnloaded return");

			boolean status = (Boolean) msg.obj;
			if (status){

				
				Log.e(TAG, "video downloaded OK");
				
			}
			else{
				//errorList.set(0,true);
			}

			


		};
	};



	public String getType_rec() {
		return _type_rec;
	}



	public void setType_rec(String _type_rec) {
		this._type_rec = _type_rec;
	}



	public String getType() {
		return _type;
	}



	public void setType(String _type) {
		this._type = _type;
	}



	public String getTitle() {
		return _title;
	}



	public void setTitle(String _title) {
		this._title = _title;
	}



	public String getLogo() {
		return _logo;
	}



	public void setLogo(Bitmap logo, String image_name) {
		
		//save bitmap 
		String filename = image_name;
		
		String file_path = Environment.getExternalStorageDirectory()+ "/BarrioTV/Ads/" + filename;
		
		File imageF = new File(file_path);
		
		if (!imageF.exists())
			SaveImage(logo, imageF);
		
		this._logo = file_path;
	}



	public String getImage() {
		return _image;
	}



	public void setImage(Bitmap image, String image_name) {
		
		//save bitmap 
		String filename = image_name;
		
		String file_path = Environment.getExternalStorageDirectory()+ "/BarrioTV/Ads/" + filename;
		
		File imageF = new File(file_path);
		
		if (!imageF.exists())
			SaveImage(image, imageF);
		
		this._image = file_path;
	}



	public String getBussinessName() {
		return _bussinessName;
	}



	public void setBussinessName(String _bussinessName) {
		this._bussinessName = _bussinessName;
	}



	public String getDiscount() {
		return _discount;
	}



	public void setDiscount(String _discount) {
		this._discount = _discount;
	}



	public double getPriceBefore() {
		return _priceBefore;
	}



	public void setPriceBefore(double _priceBefore) {
		this._priceBefore = _priceBefore;
	}



	public double getPriceAfter() {
		return _priceAfter;
	}



	public void setPriceAfter(double _priceAfter) {
		this._priceAfter = _priceAfter;
	}



	public String getGpsPos() {
		return _gpsPos;
	}



	public void setGpsPos(String _gpsPos) {
		this._gpsPos = _gpsPos;
	}



	public String getBackground() {
		return _back;
	}



	public void setBackground(Bitmap back, String image_name) {
		
		//save bitmap 
		String filename = image_name;
		
		String file_path = Environment.getExternalStorageDirectory()+ "/BarrioTV/Ads/" + filename;
		
		File image = new File(file_path);
		
		if (!image.exists())
			SaveImage(back, image);
		
		this._back = file_path;
		
	}

	
	private void SaveImage(final Bitmap finalBitmap, final File file) {

		new Thread(new Runnable() { 
			public void run(){

				
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
	
	private String getYoutubeID(String file2show2) {
		// TODO Auto-generated method stub
		
		String videoid= file2show2.substring(file2show2.indexOf("=")+1);
		
		Log.e("videoId", videoid);
		
		return videoid;
	}
	


}
