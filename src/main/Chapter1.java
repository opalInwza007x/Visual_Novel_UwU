package main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
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
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        // Create a stylish background
        ImageView background = createImageView("/resources/background/classroomTest.jpg", 968, 486);
        DropShadow backgroundEffect = new DropShadow();
        backgroundEffect.setRadius(15);
        backgroundEffect.setSpread(0.1);
        background.setEffect(backgroundEffect);

        // Create a beautiful text box with gradient border
        textBox = new TextFlow();
        textBox.setPrefHeight(162);
        textBox.setPadding(new Insets(20, 30, 20, 30)); // Add padding to prevent text from being too close to edges

        // Create a stylish text box with rounded corners and gradient background
        Rectangle textBoxBg = new Rectangle(948, 142);
        textBoxBg.setArcWidth(30);
        textBoxBg.setArcHeight(30);

        // Create a radial gradient that's pink at all edges and fades to almost white in the center
        Stop[] stops = new Stop[] {
            new Stop(0, Color.rgb(255, 255, 255, 0.9)), // Almost white in the center
            new Stop(0.7, Color.rgb(255, 220, 230, 0.7)), // Very light pink as transition
            new Stop(1, Color.rgb(255, 105, 180, 0.9))  // Hot pink at the edges
        };

        // Use a radial gradient to have the pink color at all borders (top, bottom, left, right)
        RadialGradient gradient = new RadialGradient(0, 0, 0.5, 0.5, 0.7, true, CycleMethod.NO_CYCLE, stops);

        textBoxBg.setFill(gradient);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(255, 105, 180, 0.7));
        dropShadow.setRadius(15);
        dropShadow.setSpread(0.05);
        textBoxBg.setEffect(dropShadow);
        
        // Style the text flow
        textBox.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-text-fill: white;");

        // Create a stylish next button
        Button nextButton = createButton("Next", "linear-gradient(to bottom, #ff5e62, #ff9966)", 18);
        nextButton.setStyle(nextButton.getStyle() + 
                "; -fx-background-radius: 25; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);");

        // Create character sprites with enhanced visual effects
        friendImage = createSpeakerImage("เพื่อน");
        cashenImage = createSpeakerImage("คเชน");

        updateSpeakerVisibility();

        nextButton.setOnAction(event -> handleNextText(primaryStage, textBox));

        // Stack the text box background and the text flow
        StackPane textBoxStack = new StackPane(textBoxBg, textBox);
        textBoxStack.setPadding(new Insets(0, 10, 10, 10));
        
        // Create a container for the text box with the next button
        StackPane textBoxWithButton = new StackPane(textBoxStack, nextButton);
        StackPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(nextButton, new Insets(0, 30, 20, 0));

        // Create a stylish character panel without animations
        HBox speakerPane = new HBox(80, friendImage, cashenImage);
        speakerPane.setAlignment(Pos.BOTTOM_CENTER);

        stackPane = new StackPane(background, speakerPane);

        timeline = createTimeline(textBox);
        timeline.play();

        root.getChildren().addAll(stackPane, textBoxWithButton);

        // Create a beautiful scene
        Scene scene = new Scene(root, 968, 648, Color.BLACK);
        primaryStage.setScene(scene);
        
        // Add a title to the window
        primaryStage.setTitle("Visual Novel - Chapter 1");
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
    	Button answerButton1 = createButton(
	        storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.answer1Index], 
	        "rgba(0, 128, 255, 0.8)", 
	        18
	    );
	    Button answerButton2 = createButton(
	        storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.answer2Index], 
	        "rgba(0, 128, 255, 0.8)", 
	        18
	    );
		
		String buttonStyle = "-fx-background-radius: 30; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 15 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);";
        
        answerButton1.setStyle(answerButton1.getStyle() + buttonStyle);
        answerButton2.setStyle(answerButton2.getStyle() + buttonStyle);
        
		VBox answerBox = new VBox(10);
		
		// ใช้ VBox เพื่อวางปุ่มเรียงกันในแนวตั้ง
		// 10 คือ spacing ระหว่างปุ่ม
		answerBox.setAlignment(Pos.CENTER);
	    answerBox.getChildren().addAll(answerButton1, answerButton2);
	    answerBox.setPadding(new Insets(20));
	    answerBox.setSpacing(20);

		// วาง VBox ไว้ตรงกลาง StackPane
	    Rectangle choiceBoxBg = new Rectangle(300, 150);
        choiceBoxBg.setArcWidth(30);
        choiceBoxBg.setArcHeight(30);
        choiceBoxBg.setFill(Color.rgb(20, 20, 60, 0.8));
        
        // Add a glow effect to the choice box
        DropShadow choiceBoxShadow = new DropShadow();
        choiceBoxShadow.setColor(Color.CORNFLOWERBLUE);
        choiceBoxShadow.setRadius(20);
        choiceBoxShadow.setSpread(0.2);
        choiceBoxBg.setEffect(choiceBoxShadow);
        
        // Stack the choice box on top of its background
        StackPane choiceBoxStack = new StackPane(choiceBoxBg, answerBox);
        
		answerButton1.setOnAction(event -> {
			currentTextIndex += Integer
					.parseInt(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.quesion1Index]);

			if (textBox != null) {
				textBox.getChildren().clear();
			}

			handleNextText(primaryStage, textBox);
			stackPane.getChildren().remove(choiceBoxStack);
		});

		answerButton2.setOnAction(event -> {
			currentTextIndex += Integer
					.parseInt(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.quesion2Index]);

			if (textBox != null) {
				textBox.getChildren().clear();
			}

			handleNextText(primaryStage, textBox);
			stackPane.getChildren().remove(choiceBoxStack);
		});

        // Add the choice box to the main stack pane
        StackPane.setAlignment(choiceBoxStack, Pos.CENTER);
        stackPane.getChildren().add(choiceBoxStack);
	}

    @Override
    public void updateCharacterImages() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        String emotion = storyTexts.getStoryTexts().get(currentTextIndex)[0];

        // Update character images without transitions
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
        
        // Add a drop shadow effect to character sprites
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.1);
        img.setEffect(dropShadow);

        return img;
    }

    @Override
    public void updateSpeakerVisibility() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        
        // Update speaker visibility without animations
        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setOpacity(1.0);
            friendImage.setOpacity(0.6);
        } else {
            cashenImage.setOpacity(0.6);
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