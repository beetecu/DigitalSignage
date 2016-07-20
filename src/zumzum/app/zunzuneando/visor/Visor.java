/**
 * 
 */
package zumzum.app.zunzuneando.visor;

import android.content.Context;
import android.content.Intent;

/**
 * @author beetecu
 *
 */
public abstract class Visor {

	public Context _context;
	
	public Intent lVideoIntent;
	
	protected boolean _ready;
	
	private long _duration;
	
	
	
	
	
	public Visor(Context context) {
		// TODO Auto-generated constructor stub
		_context = context;
		
		setDuration(0);
	
	}
	
	public abstract void Prepare();
	
	public abstract boolean isReady();
	
	public abstract void Play();
	
	public abstract void Stop();
	
	public abstract void Pause();
	
	public abstract boolean isError();

	public long getDuration() {
		return _duration;
	}

	public void setDuration(long duration) {
		this._duration = duration;
	}

	public void setReady(boolean ready) {
		this._ready = ready;
	}
	
	public boolean getIsReady(){
		return this._ready;
	}

	
}
