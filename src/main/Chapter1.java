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

import Util.Chapter;
import Util.TextBase;

public class Chapter1 extends Chapter {
	private ImageView friendImage;
	private ImageView cashenImage;
	private StackPane stackPane;
	private VBox root = new VBox(10);
	TextFlow textBox;

	@Override
	public void startChapter(Stage primaryStage) {
		playBackgroundMusic();
		loadSoundEffect();
		setStoryTexts("src/resources/texts/Chapter1.txt");
		root.setAlignment(Pos.CENTER);

		ImageView background = createImageView("/resources/background/classroomTest.jpg", 968, 486);

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

	@Override
	public void playBackgroundMusic() {
		try {
			URL resource = initialBackgroundMusic("/resources/sound/bgChap1.mp3"); 
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
	
	@Override
	public void loadSoundEffect() {
		try {
			URL whooshURL = getClass().getResource("/resources/sound/whoosh.mp3");
			URL popURL = getClass().getResource("/resources/sound/pop.mp3");
			URL wowURL = getClass().getResource("/resources/sound/wow.mp3");

			if (whooshURL != null && popURL != null && wowURL != null) {
				effectPlayer = new MediaPlayer(new Media(whooshURL.toExternalForm()));
			} else {
				System.out.println("Error: Effect sound files not found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void createAnswerBoxFor2(Stage primaryStage, TextFlow textBox) {
		Button answerButton1 = createButton("Yes!", "rgba(0, 128, 255, 0.8)", 18);
		Button answerButton2 = createButton("No!", "rgba(0, 128, 255, 0.8)", 18);
		VBox answerBox = new VBox(10);
		answerButton1.setOnAction(event -> {
			currentTextIndex += Integer.parseInt(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.quesion1Index]);

			if (textBox != null) {
				textBox.getChildren().clear();
			}

			handleNextText(primaryStage, textBox);
			stackPane.getChildren().remove(answerBox);
		});

		answerButton2.setOnAction(event -> {
			currentTextIndex += Integer.parseInt(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.quesion2Index]);

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

	
	@Override
	public void updateCharacterImages() {
		String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
		String emotion = storyTexts.getStoryTexts().get(currentTextIndex)[0];

		if (currentSpeaker.equals("คเชน")) {
			cashenImage.setImage(new Image(getClass().getResource(getImagePath("คเชน", emotion)).toExternalForm()));
		} else {
			friendImage.setImage(new Image(getClass().getResource(getImagePath("เพื่อน", emotion)).toExternalForm()));
		}
	}
	
	
	@Override
	public ImageView createSpeakerImage(String speaker) {
		String imagePath = (speaker.equals("คเชน") ? "/resources/cashen/cashen_normal.png"
				: "/resources/friend/friend_normal.png");
		ImageView img;
		if (speaker.equals("เพื่อน")) {
			img = createImageView(imagePath, 260, 300);
		} else {
			img = createImageView(imagePath, 200, 300);
		}

		return img;
	}

	
	@Override
	public void updateSpeakerVisibility() {
		String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
		if (currentSpeaker.equals("คเชน")) {
			cashenImage.setOpacity(1.0);
			friendImage.setOpacity(0.8);
		} else {
			cashenImage.setOpacity(0.8);
			friendImage.setOpacity(1.0);
		}
	}
	

	@Override
	public URL initialBackgroundMusic(String url) {
		return getClass().getResource(url);
	}
	
	@Override
	public void setStoryTexts(String url) {
		storyTexts = new TextBase(url);
	}
}