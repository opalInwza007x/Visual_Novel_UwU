package Util;

import java.net.URL;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Chapter implements HaveBackgroundMusic, haveText {
	public TextBase storyTexts;
	protected int currentTextIndex = 0;
	protected Timeline timeline;
	protected MediaPlayer backgroundMusic;
	protected MediaPlayer effectPlayer;
	protected MediaPlayer effecttalking;

	public abstract void startChapter(Stage primaryStage);

	public abstract URL initialBackgroundMusic(String url);

	public abstract void loadSoundEffect();

	public abstract void updateCharacterImages();

	public abstract ImageView createSpeakerImage(String speaker);

	public abstract void updateSpeakerVisibility();

	public abstract void createAnswerBoxFor2(Stage primaryStage, TextFlow textBox);

	public abstract void setStoryTexts(String url);

	@Override
	public abstract void playBackgroundMusic();

	protected ImageView createImageView(String path, double width, double height) {
		ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		return imageView;
	}

	protected Button createButton(String text, String color, int fontSize) {
		Button button = new Button(text);
		button.setStyle(
				String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: %dpx;", color, fontSize));
		return button;
	}

	public void handleNextText(Stage primaryStage, TextFlow textBox) {
		if (isRunning()) {
			timeline.stop();

			String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.speakerIndex];
			String currentText = storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.textIndex];

			textBox.getChildren().clear(); // เคลียร์ข้อความเก่าก่อนเริ่มใหม่

			// ทำให้ชื่อผู้พูดดูเด่น
			Text speakerText = new Text(currentSpeaker + " \n");
			speakerText.setFill(Color.RED);
			speakerText.setFont(
					Font.loadFont(getClass().getResourceAsStream("/resources/font/Prompt-ExtraLight.ttf"), 20));

			Text contentText = new Text(currentText);

			contentText.setFont(
					Font.loadFont(getClass().getResourceAsStream("/resources/font/Prompt-ExtraLight.ttf"), 18));
			textBox.getChildren().addAll(speakerText, contentText); // ใส่ลงใน TextFlow
			return;
		}

		if (storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex].equals("ask2")) {
			return;
		}

		if (currentTextIndex < storyTexts.getStoryTexts().size() - 1) {
			currentTextIndex++;
			textBox.getChildren().clear();
			updateSpeakerVisibility();
			playEffectSound(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.soundEffectIndex]); // เล่นเสียงเอฟเฟกต์
			// อัปเดตรูปภาพของตัวละครที่กำลังพูด
			updateCharacterImages();
			if (storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex].equals("ask2")) {
				createAnswerBoxFor2(primaryStage, textBox);
			}

			timeline.stop();
			timeline = createTimeline(textBox);
			timeline.play();
		} else {
			showNextScene(primaryStage);
		}
	}

	protected String getImagePath(String speaker, String emotion) {
		if (speaker.equals("คเชน")) {
			if (emotion.equals("normal"))
				return "/resources/cashen/cashen_normal.png";
			if (emotion.equals("smile"))
				return "/resources/cashen/cashen_smile.png";
		} else if (speaker.equals("เพื่อน")) {
			return "/resources/friend/friend_normal.png";
		}
		return "/resources/default.png"; // กรณีผิดพลาด ให้ใช้ภาพ default
	}

	protected void playEffectSound(String effect) {
		if (effectPlayer != null) {
			effectPlayer.stop(); // หยุดเสียงเก่าก่อนเล่นใหม่
		}

		String effectPath = "/resources/sound/" + effect + ".mp3";

		URL effectURL = getClass().getResource(effectPath);

		if (effectURL != null) {
			effectPlayer = new MediaPlayer(new Media(effectURL.toExternalForm()));
			effectPlayer.play();
		} else {
			System.out.println("Error: Effect sound file " + effect + " not found!");
		}
	}

	public void playTalkingSound(String talking) {
		String effectPath = "/resources/sound/talking_" + talking + ".mp3";

		URL talkingURL = getClass().getResource(effectPath);
		if (talkingURL != null) {
			effecttalking = new MediaPlayer(new Media(talkingURL.toExternalForm()));
			effecttalking.setVolume(0.05);
			effecttalking.play();
		} else {
			System.out.println("Error: Effect sound file talking.mp3 not found!");
		}
	}

	public Timeline createTimeline(TextFlow textBox) {
		String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.speakerIndex];
		String currentText = storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.textIndex];

		Timeline timeline = new Timeline();
		textBox.getChildren().clear(); // เคลียร์ข้อความเก่าก่อนเริ่มใหม่

		// ทำให้ชื่อผู้พูดดูเด่น
		Text speakerText = new Text(currentSpeaker + " \n");

		speakerText.setFill(Color.RED); // เปลี่ยนสีเป็นแดงให้ดูเด่น
		speakerText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/font/Prompt-ExtraLight.ttf"), 20));
		// ข้อความที่พิมพ์ทีละตัว
		Text contentText = new Text();

		contentText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/font/Prompt-ExtraLight.ttf"), 18));
		textBox.getChildren().addAll(speakerText, contentText); // ใส่ลงใน TextFlow

		for (int i = 0; i < currentText.length(); i++) {
			final int index = i;
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(33 * (i + 1)), e -> {
				contentText.setText(contentText.getText() + currentText.charAt(index)); // เพิ่มตัวอักษรทีละตัว
				playTalkingSound(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.talkingSoungIndex]);
			}));
		}

		return timeline;
	}

	protected void showNextScene(Stage primaryStage) {
		if (backgroundMusic != null) {
			backgroundMusic.stop(); // หยุดเสียงก่อนเปลี่ยนฉาก
		}
		StackPane nextSceneRoot = new StackPane();
		nextSceneRoot.setStyle("-fx-background-color: black;");
		primaryStage.setScene(new Scene(nextSceneRoot, 968, 648));
	}

	public boolean isRunning() {
		if (timeline.getStatus() == Animation.Status.RUNNING) {
			return true;
		}
		return false;
	}
}
