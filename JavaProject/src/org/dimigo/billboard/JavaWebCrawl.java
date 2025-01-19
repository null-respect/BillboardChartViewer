/**
 * 
 */
package org.dimigo.billboard;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 * <pre>
 * org.dimigo.billboard
 *   |_ JavaWebCrawl
 *
 * 1. 개요 :
 * 2. 작성일 : 2017. 6. 23.
 * </pre>
 *
 * @author      : 공경배
 * @version     : 1.0
 */
public class JavaWebCrawl{

	public static List<Music> webCrawl(String date) throws Exception {

		// Jsoup 라이브러리를 이용해, Billboard 페이지의 코드를 받아옴
		Document doc = Jsoup.connect("http://www.billboard.com/charts/hot-100"+date).get();

		Elements song = doc.select(".chart-row__song");
		List<String> listSong = song.eachText();
		
		
		Elements artist = doc.select(".chart-row__artist");
		List<String> listArtist = artist.eachText();
		
		Elements rank = doc.select("span.chart-row__last-week");
		List<String> listRankLast = rank.eachText();
		
		Elements image = doc.select(".chart-row__image");	
		String strImage = (String.valueOf(image));
		String[] s2 = strImage.split("http://");
		// 원하는 부분을 얻기위해 String으로 바꾼후, split으로 쪼갬
		List<String> listImageURL = new ArrayList<String>();

		// 아티스트의 이미지 파싱
		for(int i=1; i<s2.length; i++) {
				String[] temp = s2[i].split(".jpg");
				listImageURL.add(temp[0]);
		}
		
		// 이미지가 없는 부분 null추가
		int p=0;
		for(int i=1; i<s2.length; i++) {
			String[] temp = s2[i].split("div class");
			if(temp.length>2) {
				for(int j=0; j<temp.length-2; j++) {
					p++;
					listImageURL.add(i+p-1, null);
				}
				
			}
		}
		//처음 부분 순위가 image가 없을경우
		String[] firstNull = s2[0].split("row");
		for(int i=0; i<firstNull.length-2; i++) {
			listImageURL.add(i, null);
		}
		
		// 마지막 순위가 image가 없을 경우
		for(int i=listImageURL.size(); i<100; i++){
			listImageURL.add(i, null);
		}

		// 지난 주 순위를 추출
		List<String> listRankLast2 = new ArrayList<>();
		for(String s : listRankLast) {
			listRankLast2.add(s.substring(11));
		}
		
		// 파싱한 데이터를 list에 추가
		List<Music> result = new ArrayList<Music>();

		for(int i=0; i<100; i++) {
			result.add(new Music(new SimpleIntegerProperty(i+1), 
					new SimpleStringProperty(listRankLast2.get(i)), 
					new SimpleStringProperty(listSong.get(i)), 
					new SimpleStringProperty(listArtist.get(i)),
					new SimpleStringProperty(listImageURL.get(i))));
			
		}
		return result;
		
	}
}
