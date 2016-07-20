package zumzum.app.zunzuneando.visor.contents;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class ContentRSS extends Content {
	
	
	private String _color;
	public List<String> _rss;


	public ContentRSS(String source, String theme){
		
		super(source, theme);
		
		_color = "#d25dce";
		
		_rss = new ArrayList<String>();
				
		
	}
	
	public void setContents(){
		
		//si tenemps coneccion a internet 
		//mandamos a actualizar el fichero de datos
		
		//updateFromDB();
		
		//una vez actualizado o si no hay coneccion
		
		//loadContentFromFile();
		
	}
	
	public void updateFromDB(){
		
		//si tenemps coneccion a internet 
		//mandamos a actualizar el fichero de datos
		
		
		//loadContentFromFile()
	}

	
	public void loadContentFromFile(){
		
		
		
	}
	

	public String getColor() {
		return _color;
	}


	public void setColor(String color) {
		this._color = color;
	}


}
