package main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

import Util.TextBase;

public class Chapter1 {
	private TextBase storyTexts = new TextBase("src/resources/texts/Chapter1.txt");
	
	private int currentTextIndex = 0;
    private Timeline timeline;
    private ImageView friendImage;
    private ImageView cashenImage;	
    private MediaPlayer backgroundMusic;
    private MediaPlayer effectPlayer;
    private MediaPlayer effecttalking;
    private StackPane stackPane;
    private VBox root = new VBox(10);
    TextFlow textBox;
    
    public void startChapter(Stage primaryStage) {
    	playBackgroundMusic();
    	loadSoundEffect();
        
        root.setAlignment(Pos.CENTER);
        
       
        ImageView background = createImageView("/resources/classroomTest.jpg", 968, 486);

         textBox = new TextFlow();
        textBox.setPadding(new Insets(10));
        textBox.setPrefHeight(162);
        textBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10px;");

        Button nextButton = createButton("Next", "rgba(255, 0, 0, 0.7)", 16);

        friendImage = createSpeakerImage("เพื่อน");
        cashenImage = createSpeakerImage("คเชน");

        updateSpeakerVisibility();

        nextButton.setOnAction(event -> handleNextText(primaryStage, textBox));
        
        StackPane textBoxWithButton = new StackPane(textBox, nextButton);
        StackPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);

        HBox speakerPane = new HBox(50, friendImage, cashenImage);
        speakerPane.setAlignment(Pos.BOTTOM_CENTER);

        stackPane = new StackPane(background, speakerPane);

        timeline = createTimeline(textBox);
        timeline.play();

        root.getChildren().addAll(stackPane, textBoxWithButton);

        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
    }
    
    private void playBackgroundMusic() {
        try {
            URL resource = getClass().getResource("/resources/bgChap1.mp3");
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                backgroundMusic = new MediaPlayer(media);
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // เล่นวนลูป
                backgroundMusic.setVolume(0.7); // ตั้งค่าความดัง (0.0 - 1.0)
                backgroundMusic.play();
            } else {
                System.out.println("Error: Background music file not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadSoundEffect() {
    	 try {
    	        URL whooshURL = getClass().getResource("/resources/whoosh.mp3");
    	        URL popURL = getClass().getResource("/resources/pop.mp3");
    	        URL wowURL = getClass().getResource("/resources/wow.mp3");

    	        if (whooshURL != null && popURL != null &&  wowURL != null) {
    	            effectPlayer = new MediaPlayer(new Media(whooshURL.toExternalForm()));
    	        } else {
    	            System.out.println("Error: Effect sound files not found!");
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    }
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

//    private TextArea createTextArea() {
//        TextArea textBox = new TextArea();
//        textBox.setEditable(false);
//        textBox.setWrapText(true);
//        textBox.setPadding(new Insets(10));
//        textBox.setFont(Font.font(18));
//        textBox.setPrefHeight(162);
//        return textBox;
//    }

  
    private void handleNextText(Stage primaryStage, TextFlow textBox) {
    	if(isRunning(textBox)) {
    		return;
    	}
    	
    	if (currentTextIndex < storyTexts.getStoryTexts().size() - 1) {
            currentTextIndex++;
            textBox.getChildren().clear();
            updateSpeakerVisibility();
            playEffectSound(storyTexts.getStoryTexts().get(currentTextIndex)[3]); // เล่นเสียงเอฟเฟกต์
         // อัปเดตรูปภาพของตัวละครที่กำลังพูด
            updateCharacterImages();
            if(storyTexts.getStoryTexts().get(currentTextIndex)[4].equals("ask2")) {
            	createAnswerBoxFor2(primaryStage,textBox);
            }
            timeline.stop();
            timeline = createTimeline(textBox);
            timeline.play();
        } else {
            showNextScene(primaryStage);
        }
    }
    private void createAnswerBoxFor2(Stage primaryStage, TextFlow textBox) {
        Button answerButton1 = createButton("Yes!", "rgba(0, 128, 255, 0.8)", 18);
        Button answerButton2 = createButton("No!", "rgba(0, 128, 255, 0.8)", 18);
        VBox answerBox = new VBox(10);
        answerButton1.setOnAction(event -> {
            currentTextIndex += Integer.parseInt(storyTexts.getStoryTexts().get(currentTextIndex)[4+1]);

            if (textBox != null) {
                textBox.getChildren().clear();
            }

            handleNextText(primaryStage, textBox);
            stackPane.getChildren().remove(answerBox);
        });

        answerButton2.setOnAction(event -> {
        	 currentTextIndex += Integer.parseInt(storyTexts.getStoryTexts().get(currentTextIndex)[4+2]);

            if (textBox != null) {
                textBox.getChildren().clear();
            }

            handleNextText(primaryStage, textBox);
            stackPane.getChildren().remove(answerBox);
        });

        // ใช้ VBox เพื่อวางปุ่มเรียงกันในแนวตั้ง
         // 10 คือ spacing ระหว่างปุ่ม
        answerBox.setAlignment(Pos.CENTER);
        answerBox.getChildren().addAll(answerButton1, answerButton2);

        // วาง VBox ไว้ตรงกลาง StackPane
        StackPane.setAlignment(answerBox, Pos.CENTER);
        stackPane.getChildren().add(answerBox);
    }

    private void updateCharacterImages() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        String emotion = storyTexts.getStoryTexts().get(currentTextIndex)[0];

        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setImage(new Image(getClass().getResource(getImagePath("คเชน", emotion)).toExternalForm()));
        } else {
            friendImage.setImage(new Image(getClass().getResource(getImagePath("เพื่อน", emotion)).toExternalForm()));
        }
    }
    private String getImagePath(String speaker, String emotion) {
        if (speaker.equals("คเชน")) {
            if (emotion.equals("normal")) return "/resources/cashen_normal.png";
            if (emotion.equals("smile")) return "/resources/cashen_smile.png";
        } else if (speaker.equals("เพื่อน")) {
            return "/resources/friend_normal.png";
        }
        return "/resources/default.png"; // กรณีผิดพลาด ให้ใช้ภาพ default
    }
    private void playEffectSound(String effect) {
        if (effectPlayer != null) {
            effectPlayer.stop(); // หยุดเสียงเก่าก่อนเล่นใหม่
        }
        String effectPath = "/resources/" + effect + ".mp3"; 
        
        URL effectURL = getClass().getResource(effectPath);

        if (effectURL != null) {
            effectPlayer = new MediaPlayer(new Media(effectURL.toExternalForm()));
            effectPlayer.play();
        } else {
            System.out.println("Error: Effect sound file " + effect + " not found!");
        }
    }
   
    private void playTalkingSound() {
    	String effectPath = "/resources/talking.mp3";
    	
    	URL talkingURL = getClass().getResource(effectPath);
    	if (talkingURL != null) {
    		effecttalking = new MediaPlayer(new Media(talkingURL.toExternalForm()));
    		effecttalking.setVolume(0.05);
    		effecttalking.play();
        } else {
            System.out.println("Error: Effect sound file talking.mp3 not found!");
        }
    }
    
    private ImageView createSpeakerImage(String speaker) {
        String imagePath = (speaker.equals("คเชน") ? "/resources/cashen_normal.png" : "/resources/friend_normal.png");
        ImageView  img;
        if( speaker.equals("เพื่อน")) {
        	img = createImageView(imagePath, 260, 300);
        }else {
        	img = createImageView(imagePath, 200, 300);
        }
        
        return img;
    }

    private void updateSpeakerVisibility() {
    	String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setOpacity(1.0);
            friendImage.setOpacity(0.8);
        } else {
            cashenImage.setOpacity(0.8);
            friendImage.setOpacity(1.0);
        }
    }

    private Timeline createTimeline(TextFlow textBox) {
    	String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
		String currentText = storyTexts.getStoryTexts().get(currentTextIndex)[2];

        Timeline timeline = new Timeline();
        textBox.getChildren().clear(); // เคลียร์ข้อความเก่าก่อนเริ่มใหม่

        // ทำให้ชื่อผู้พูดดูเด่น
        Text speakerText = new Text(currentSpeaker + " \n");
        
        speakerText.setFill(Color.RED); // เปลี่ยนสีเป็นแดงให้ดูเด่น
        speakerText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/Prompt-ExtraLight.ttf"), 20));
        // ข้อความที่พิมพ์ทีละตัว
        Text contentText = new Text();
        
        contentText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/Prompt-ExtraLight.ttf"), 18));
        textBox.getChildren().addAll(speakerText, contentText); // ใส่ลงใน TextFlow
        
        for (int i = 0; i < currentText.length(); i++) {
            final int index = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(33 * (i + 1)), e -> {
                contentText.setText(contentText.getText() + currentText.charAt(index)); // เพิ่มตัวอักษรทีละตัว
                playTalkingSound();
            }));
        }

        return timeline;
    }

    private void showNextScene(Stage primaryStage) {
    	 if (backgroundMusic != null) {
    	        backgroundMusic.stop(); // หยุดเสียงก่อนเปลี่ยนฉาก
    	    }
        StackPane nextSceneRoot = new StackPane();
        nextSceneRoot.setStyle("-fx-background-color: black;");
        primaryStage.setScene(new Scene(nextSceneRoot, 968, 648));
    }
    
    private boolean isRunning(TextFlow textBox) {
    	if (timeline.getStatus() == Animation.Status.RUNNING) {
    		timeline.stop();
    		
    		String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
    		String currentText = storyTexts.getStoryTexts().get(currentTextIndex)[2];
    		
    		textBox.getChildren().clear(); // เคลียร์ข้อความเก่าก่อนเริ่มใหม่

            // ทำให้ชื่อผู้พูดดูเด่น
            Text speakerText = new Text(currentSpeaker + " \n");
            speakerText.setFill(Color.RED);
            speakerText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/Prompt-ExtraLight.ttf"), 20));
            
            Text contentText = new Text(currentText);
            
            contentText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/Prompt-ExtraLight.ttf"), 18));
            textBox.getChildren().addAll(speakerText, contentText); // ใส่ลงใน TextFlow
    		return true;
    	}
    	return false;
    }
}