/**
 * 
 */
package org.dimigo.billboard;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * <pre>
 * org.dimigo.billboard
 *   |_ BillboardController
 *
 * 1. 개요 :
 * 2. 작성일 : 2017. 6. 23.
 * </pre>
 *
 * @author      : 공경배
 * @version     : 1.0
 */
public class BillboardController implements Initializable{
	
	@FXML DatePicker date;
	
	@FXML TableView<Music> musicTableView;
	@FXML TableColumn<Music, Integer> rankColumn;
	@FXML TableColumn<Music, String> rank_lastweekColumn;
	@FXML TableColumn<Music, String> songColumn;
	@FXML TableColumn<Music, String> artistColumn;
	@FXML TableColumn<Music, String> artistImageURLColumn;
	
	@FXML Label lbSongInfo;
	@FXML Label lbRank;
	@FXML Label lbRankLastWeek;
	@FXML ImageView artistImage;
	
	@FXML Label lbArtist;
	@FXML Label lbSongName;
	@FXML Button youtubeSearch;
	@FXML WebView webView;
    private WebEngine engine;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = webView.getEngine();
        
    	// 차트 가져오기
        List<Music> list = null;
		try {
			list = JavaWebCrawl.webCrawl("/");
		} catch (Exception e) {
			e.printStackTrace();
		}
        ObservableList<Music> data = FXCollections.observableArrayList(list);
        rankColumn.setCellValueFactory(cellData ->  cellData.getValue().rankProperty().asObject());
        rank_lastweekColumn.setCellValueFactory(cellData -> cellData.getValue().rank_lastweekProperty());
        songColumn.setCellValueFactory(cellData -> cellData.getValue().songProperty());
        artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
        
        musicTableView.setItems(data);
        
    }   
    
    @FXML
    public void click(MouseEvent event) {
    	Music music = musicTableView.getSelectionModel().getSelectedItem();
    	
    	if(music==null) return;  // tableView의 header부분을 선택한 경우, 에러 방지 
    	
    	// 선택한 곡의 정보를 출력
    	lbSongInfo.setText("곡 정보");
    	lbRank.setText(String.valueOf(music.rankProperty().getValue()));
    	lbRankLastWeek.setText("last week : " + music.rank_lastweekProperty().getValue());
    	lbSongName.setText(music.songProperty().getValue());
    	
    	lbArtist.setText(music.artistProperty().getValue());
    	
    	
    	if(music.imageURLProperty().getValue()!=null) {
	    	Image image = new Image("http://"+ music.imageURLProperty().getValue() + ".jpg");
	    	artistImage.setImage(image);
    	}
    	else {
    		artistImage.setImage(null); 
    	}
    }
    
    @FXML
    public void handleDate(ActionEvent event) {
    	    	
    	// 빌보드에서 검색할 수 있는 날짜의 이전 날짜를 선택한 경우 메시지 출력
    	LocalDate localDate = date.getValue();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    	String formattedString = localDate.format(formatter);
    	int dateCheck = Integer.valueOf(formattedString);
    	
    	if(dateCheck<19580816) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("에러 메시지");
    		alert.setHeaderText("날짜를 다시 선택해주세요.");
    		alert.setContentText("1958년 8월 16일 이전은 기록이 없습니다.");

    		alert.showAndWait();
    		return;
    	}
    	Date todayDate = new Date();
    	DateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String formattedString2 = df.format(todayDate);
    	int todayInt= Integer.valueOf(formattedString2);
    	
    	// 오늘보다 미래를 선택한 경우 메시지 출력
    	if(dateCheck>todayInt) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("에러 메시지");
    		alert.setHeaderText("날짜를 다시 선택해주세요.");
    		alert.setContentText("미래는 예측할 수 없습니다.");

    		alert.showAndWait();
    		return;
    	}
    	Calendar cal= Calendar.getInstance ();

        cal.set(Calendar.YEAR, date.getValue().getYear());
        cal.set(Calendar.MONTH, date.getValue().getMonthValue()-1);
        cal.set(Calendar.DATE, date.getValue().getDayOfMonth());
        
        // 빌보드 차트는 토요일기준으로 변하므로, 날짜를 토요일로 바꿈
            for(int i=cal.get(Calendar.DAY_OF_WEEK); i!=7; ) {
            	i--;
            	if(i<=0) i=7;
        		cal.add(cal.DATE, -1);
            }
            
            Date s = cal.getTime();
            
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            String to = transFormat.format(s);
            
            // 선택한 날짜의 차트를 가져옴
            List<Music> list = null;
    		try {
    			list = JavaWebCrawl.webCrawl("/" + to);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            ObservableList<Music> data = FXCollections.observableArrayList(list);
            
            rankColumn.setCellValueFactory(cellData ->  cellData.getValue().rankProperty().asObject());
            rank_lastweekColumn.setCellValueFactory(cellData -> cellData.getValue().rank_lastweekProperty());
            songColumn.setCellValueFactory(cellData -> cellData.getValue().songProperty());
            artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
            
            musicTableView.setItems(data);
    }
    
    @FXML
	public void handleYoutubeAction(ActionEvent event){
    	// webView에 유튜브 검색 결과를 불러옴
    	// 아티스트명 + 곡명으로 검색
		Music music = musicTableView.getSelectionModel().getSelectedItem();
        
		engine.load("https://www.youtube.com/results?search_query=" 
        		+ music.artistProperty().getValue() + " "
        		+ music.songProperty().getValue());
    }
    
    public void handleCloseAction(ActionEvent event){
		Platform.exit();
	}
  
	
}
