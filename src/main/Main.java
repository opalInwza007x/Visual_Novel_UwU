package main;

import java.net.URL;

import Util.TextBase;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
//    private String[][] dialog = {
//    		{"", "น้องมีฮัก ฮักมีหน่อย อย่าให้น้องคอย... คอยอยู่ทุกมื่อ    \nLet's Go!!!", ""},
//    		{"", "มองมาตั้งนาน ส่องเบิ่งตั้งแต่เมื่อวานแล้ะ อ้ายมาแต่ใส เป็นจังได๋ถึงเป็นตาฮักจังซี่ล่ะ", ""},
//    		{"", "ใจมันตึ๊กตึ๊ก มันคึกมันคักก็มันแพ้ นี่ไม่ได้การและ ต้องเริ่มปฏิบัติการให้รู้ตัวซะบ่", ""},
//    		{"", "รีบเลย...    \nฟ่าวเลย...    \nบ่มีเวลามาเล่นตาเล่นหู จับไมค์เอื้อนเพลง ให้อ้ายได้รู้...", ""},
//    };
    
    private TextBase storyTexts = new TextBase("src/resources/texts/main.txt");
    
    private int currentTextIndex = 0; 
    private Timeline timeline;
    private MediaPlayer effecttalking;

    @Override
    public void start(Stage primaryStage) {
        showMenuScene(primaryStage);
    }

    /*** Scene Management ***/
    private void showMenuScene(Stage primaryStage) {
        AnchorPane menuRoot = new AnchorPane();
        
        // Start Button
        Button startButton = createButton("Start Game", "rgba(0, 255, 0, 0.7)", 20);
        startButton.setOnAction(e -> showLoadingScene(primaryStage));
        AnchorPane.setTopAnchor(startButton, 250.0);
        AnchorPane.setLeftAnchor(startButton, 130.0);
        menuRoot.getChildren().add(startButton);
        
        // Anime Image
        ImageView animeImageView = createImageView("/resources/animeGirl.jpeg", 450, 648);
        AnchorPane.setRightAnchor(animeImageView, 0.0);
        menuRoot.getChildren().add(animeImageView);

        primaryStage.setScene(new Scene(menuRoot, 968, 648, Color.BLACK));
        primaryStage.setTitle("Visual Novel - Menu");
        primaryStage.show();
    }

    private void showLoadingScene(Stage primaryStage) {
        StackPane loadingRoot = new StackPane();
        loadingRoot.setStyle("-fx-background-color: black;");

        Text loadingText = new Text("Loading...");
        loadingText.setStyle("-fx-font-size: 40px; -fx-fill: white; -fx-font-family: 'Courier New';");
        loadingRoot.getChildren().add(loadingText);

        Timeline loadingTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> loadingText.setOpacity(loadingText.getOpacity() == 1 ? 0 : 1))
        );
        loadingTimeline.setCycleCount(Timeline.INDEFINITE);
        loadingTimeline.play();

        primaryStage.setScene(new Scene(loadingRoot, 968, 648));
        new Timeline(new KeyFrame(Duration.seconds(2), e -> showGameScene(primaryStage))).play();
    }

    private void showGameScene(Stage primaryStage) {
        VBox root = new VBox(10);
        ImageView background = createImageView("/resources/background/classroomTest.jpg", 968, 486);
        TextArea textBox = createTextArea();
        Button nextButton = createButton("Next", "rgba(255, 0, 0, 0.7)", 16);
        
        nextButton.setOnAction(event -> handleNextText(primaryStage, textBox));
        StackPane textBoxWithButton = new StackPane(textBox, nextButton);
        StackPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);

        timeline = createTimeline(textBox);
        timeline.play();

        root.getChildren().addAll(background, textBoxWithButton);
        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
    }

    /*** Helper Functions ***/
    private ImageView createImageView(String path, double width, double height) {
        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private Button createButton(String text, String color, int fontSize) {
        Button button = new Button(text);
        button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: %dpx;", color, fontSize));
        return button;
    }

    private TextArea createTextArea() {
        TextArea textBox = new TextArea();
        textBox.setEditable(false);
        textBox.setWrapText(true);
        textBox.setPadding(new Insets(10));
        textBox.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/font/Prompt-ExtraLight.ttf"), 18));
        textBox.setPrefHeight(162);
        return textBox;
    }

    private void handleNextText(Stage primaryStage, TextArea textBox) {
    	if(isRunning(textBox)) {
    		return;
    	}
    	
    	if (currentTextIndex < storyTexts.getStoryTexts().size() - 1) {
            currentTextIndex++;
            textBox.clear();
            timeline.stop();
            timeline = createTimeline(textBox);
            timeline.play();
        } 
        else {
        	showLoadingScene(primaryStage);
            new Timeline(new KeyFrame(Duration.seconds(2), e -> new Chapter1().startChapter(primaryStage))).play();
        }
    }

    private Timeline createTimeline(TextArea textBox) {
        String currentText = storyTexts.getStoryTexts().get(currentTextIndex)[2];  // ข้อความที่จะแสดง
        Timeline timeline = new Timeline();
        
        for (int i = 0; i < currentText.length(); i++) {
            final int index = i;  // ต้องใช้ตัวแปร final สำหรับ Lambda
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(33 * (i + 1)), e -> {
                textBox.appendText(String.valueOf(currentText.charAt(index)));
                playTalkingSound();
            }));
        }

        return timeline;  // ไม่ต้อง setCycleCount เพราะจะหยุดเองเมื่อครบข้อความ
    }
    
    private boolean isRunning(TextArea textBox) {
    	if (timeline.getStatus() == Animation.Status.RUNNING) {
    		timeline.stop();
    		
    		String currentText = storyTexts.getStoryTexts().get(currentTextIndex)[2];
    		
    		textBox.setText(currentText);
    		return true;
    	}
    	return false;
    }
    
    private void playTalkingSound() {
    	String effectPath = "/resources/sound/talking.mp3";
    	
    	URL talkingURL = getClass().getResource(effectPath);
    	if (talkingURL != null) {
    		effecttalking = new MediaPlayer(new Media(talkingURL.toExternalForm()));
    		effecttalking.setVolume(0.1);
    		effecttalking.play();
        } else {
            System.out.println("Error: Effect sound file talking.mp3 not found!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}