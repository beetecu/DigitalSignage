package zumzum.app.zunzuneando.visor.contents;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

public class ContentsManager implements Runnable{

	private static final String TAG = "ContentsManager";

	Context _context;	
	
	static public ContentsLoader _contentsLoader;

	public ContentsPlanner _contentsPlanner;
	
	private final long interval = 800;
	
	
	
	public ContentsManager(Context context) {
		
		Log.e(TAG, "ContentsManager");

		_context = context;
		
		_contentsLoader = new ContentsLoader(_context);	
		
		_contentsPlanner = new ContentsPlanner(_context); 
		
	
		
	}

	@Override
	public void run() {
		
		// wait for contents loaded
		
		
		//_contentsLoader.update();
		
		//_contentsPlanner.start();
		
		//keepUpdatedContents();
		
		
	}
	
	
}

