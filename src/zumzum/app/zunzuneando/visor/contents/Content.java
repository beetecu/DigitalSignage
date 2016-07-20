package zumzum.app.zunzuneando.visor.contents;

import java.util.ArrayList;
import java.util.List;

public abstract class Content {
	
	private String _source;
	
	private String _theme; //fashion, live
	
	private List<String> _time_in;
    
    private List<String> _time_out;

	public Content(String source, String theme){
		
		this.setSource(source);
		this.setTheme(theme);
		_time_in  = new ArrayList<String>();
		_time_out = new ArrayList<String>();
		
	}

	public String getSource() {
		return _source;
	}

	public void setSource(String _source) {
		this._source = _source;
	}

	public String getTheme() {
		return _theme;
	}

	public void setTheme(String _theme) {
		this._theme = _theme;
	}


}
