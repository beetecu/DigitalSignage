package zumzum.app.zunzuneando.visor.youtube;

import java.io.Serializable;


/**
 * This is a representation of a users video off YouTube
 * @author paul.blundell
 */
public class Video implements Serializable {
	// The title of the video
	private String title;
	// A link to the video on youtube
	private String videoId;
	// A link to a still image of the youtube video
	private String thumbUrl;
	
	public Video(String title, String videoID, String thumbUrl) {
		super();
		this.title = title;
		this.videoId = videoID;
		this.thumbUrl = thumbUrl;
	}

	/**
	 * @return the title of the video
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * @return the url to this video on youtube
	 */
	public String getVideoId() {
		return this.videoId;
	}

	/**
	 * @return the thumbUrl of a still image representation of this video
	 */
	public String getThumbUrl() {
		return thumbUrl;
	}
}