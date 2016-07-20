/*
 * Copyright (C) 2010 A. Horn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zumzum.app.zunzuneando.visor.rssreader;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * HTTP client to retrieve and parse RSS 2.0 feeds. Callers must call
 * {@link RSSReader#close()} to release all resources.
 * 
 * @author Mr Horn
 */
public class RSSReader implements java.io.Closeable {

  protected static final String TAG = "RSSReader";

/**
   * Thread-safe {@link HttpClient} implementation.
   */
  private final HttpClient httpclient;

  /**
   * Thread-safe RSS parser SPI.
   */
  private final RSSParserSPI parser;

  /**
   * Instantiate a thread-safe HTTP client to retrieve RSS feeds. The injected
   * {@link HttpClient} implementation must be thread-safe.
   * 
   * @param httpclient thread-safe HTTP client implementation
   * @param parser thread-safe RSS parser SPI implementation
   */
  public RSSReader(HttpClient httpclient, RSSParserSPI parser) {
    this.httpclient = httpclient;
    this.parser = parser;
  }

  /**
   * Instantiate a thread-safe HTTP client to retrieve RSS feeds. The injected
   * {@link HttpClient} implementation must be thread-safe. Internal memory
   * consumption and load performance can be tweaked with {@link RSSConfig}.
   * 
   * @param httpclient thread-safe HTTP client implementation
   * @param config RSS configuration
   */
  public RSSReader(HttpClient httpclient, RSSConfig config) {
    this(httpclient, new RSSParser(config));
  }

  /**
   * Instantiate a thread-safe HTTP client to retrieve and parse RSS feeds.
   * Internal memory consumption and load performance can be tweaked with
   * {@link RSSConfig}.
   */
  public RSSReader(RSSConfig config) {
    this(new DefaultHttpClient(), new RSSParser(config));
  }

  /**
   * Instantiate a thread-safe HTTP client to retrieve and parse RSS feeds.
   * Default RSS configuration capacity values are used.
   */
  public RSSReader() {
    this(new DefaultHttpClient(), new RSSParser(new RSSConfig()));
  }

  /**
   * Send HTTP GET request and parse the XML response to construct an in-memory
   * representation of an RSS 2.0 feed.
   * 
   * @param uri RSS 2.0 feed URI
   * @return in-memory representation of downloaded RSS feed
   * @throws RSSReaderException if RSS feed could not be retrieved because of
   *           HTTP error
   * @throws RSSFault if an unrecoverable IO error has occurred
   */
  public void load(final String uri, final Handler feedLoded) {
    final HttpGet httpget = new HttpGet(uri);

    
    
    new Thread(new Runnable() {
        private InputStream feedStream;

		public void run() {
            try {
            	//InputStream feedStream = null;
            	
            	
                RSSFeed feed = null;
                
                // Send GET request to URI
                final HttpResponse response = httpclient.execute(httpget);

                // Check if server response is valid
                final StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                  
                }

                // Extract content stream from HTTP response
                HttpEntity entity = response.getEntity();
                feedStream = entity.getContent();
                
                
                Log.e("TAG", "feed = parser");

                feed = parser.parse(feedStream);
                
                Log.e("TAG", "feed = parserrrr");
                
                Message mssg = new Message();
				mssg.obj = feed;
				//msg.obj = result;
				feedLoded.sendMessage(mssg);

                if (feed.getLink() == null) {
                  //feed.setLink(android.net.Uri.parse(uri));
                }

                
              } catch (ClientProtocolException e) {
                throw new RSSFault(e);
              } catch (IOException e) {
                throw new RSSFault(e);
              } catch (Exception e) {
                  //throw new RSSFault(e);
                  
              } finally {
                Resources.closeQuietly(feedStream);
              }
        }
    }).start();
    

  }

  /**
   * Release all HTTP client resources.
   */
  public void close() {
    httpclient.getConnectionManager().shutdown();
  }

}

