/**
 * 
 */
package org.dimigo.billboard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * <pre>
 * org.dimigo.billboard
 *   |_ Rank
 *
 * 1. 개요 :
 * 2. 작성일 : 2017. 6. 23.
 * </pre>
 *
 * @author      : 공경배
 * @version     : 1.0
 */
public class Music{
	
	private IntegerProperty rank;
	private StringProperty rank_lastweek;
	private StringProperty song;
	private StringProperty artist;
	private StringProperty imageURL;
	
	/**
	 * @param rank
	 * @param rank_lastweek
	 * @param song
	 * @param artist
	 * @param imageURL
	 */
	
	public Music(IntegerProperty rank, StringProperty rank_lastweek, 
			StringProperty song, StringProperty artist, StringProperty imageURL) {
		super();
		this.rank = rank;
		this.rank_lastweek = rank_lastweek;
		this.song = song;
		this.artist = artist;
		this.imageURL = imageURL;
	}
	
	public IntegerProperty rankProperty(){
		return rank;
	}
	
	public StringProperty rank_lastweekProperty(){
		return rank_lastweek;
	}
	public StringProperty songProperty(){
		return song;
	}
	public StringProperty artistProperty(){
		return artist;
	}
	public StringProperty imageURLProperty(){
		return imageURL;
	}
	
}
