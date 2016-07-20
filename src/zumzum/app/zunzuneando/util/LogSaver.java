package zumzum.app.zunzuneando.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class LogSaver {
	static final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ssZ");
	private static final Executor EX = Executors.newSingleThreadExecutor();

	private Context mContext;


	final File file;
	final File path;
	
	private static final String SEPARATOR = System.getProperty("line.separator");


	public LogSaver(Context context) {
		mContext = context;

		path = new File(Environment.getExternalStorageDirectory()+ "/BarrioTV/log");
		file = new File(path + "/logcat.txt");

	}

	public void save() {
		
		try {
		    //Executes the command.
		    Process process = Runtime.getRuntime().exec(
		        "/system/bin/logcat -d *:E");

		    BufferedReader reader = new BufferedReader(
		        new InputStreamReader(process
		        .getInputStream()));

		    String line;
		    StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append(SEPARATOR);
            }

            
		    String lastLine = sb.toString();

		    reader.close();

		    //Waits for the command to finish.
		    process.waitFor();
		    
		    

		    if(lastLine != null){
		       // Log.e("Last log line : ",  lastLine);
		    
		    
		    
		    BufferedWriter bw = null;
            try {
                    file.createNewFile();
                    bw = new BufferedWriter(new FileWriter(file), 1024);
                    bw.write(lastLine);
            } catch (IOException e) {
                    Log.e("alogcat", "error saving log", e);
            } finally {
                    if (bw != null) {
                            try {
                                    bw.close();
                            } catch (IOException e) {
                                    Log.e("alogcat", "error closing log", e);
                            }
                    }
            }
            }
		} catch (IOException e) {
		    throw new RuntimeException(e);
		} catch (InterruptedException e) {
		    throw new RuntimeException(e);
		}catch (Exception e) {
		    throw new RuntimeException(e);
		}

	}
		
	

}
