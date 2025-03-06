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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Util.Chapter;
import Util.TextBase;

public class Chapter2 extends Chapter {
    private ImageView friendImage;
    private ImageView cashenImage;
    private StackPane stackPane;

    @Override
    public void startChapter(Stage primaryStage) {
        playBackgroundMusic("/resources/sound/bgChap1.mp3");
        loadSoundEffect(Arrays.asList("whoosh", "pop", "wow"));
        setStoryTexts("src/resources/texts/Chapter1.txt");

        ImageView background = setupBackground("/resources/background/classroomTest.jpg");
        TextFlow textBox = createTextFlow();
        Button nextButton = createNextButton(primaryStage, textBox);

        friendImage = createSpeakerImage("เพื่อน");
        cashenImage = createSpeakerImage("คเชน");
        updateSpeakerVisibility();

        // Stack text box background and text
        StackPane textBoxStack = createTextBoxStack(textBox);
        StackPane textBoxWithButton = createTextBoxWithButton(textBoxStack, nextButton);

        // Speaker images container
        HBox speakerPane = new HBox(80, friendImage, cashenImage);
        speakerPane.setAlignment(Pos.BOTTOM_CENTER);

        stackPane = new StackPane(background, speakerPane);
        timeline = createTimeline(textBox);
        timeline.play();

        // Setup root directly
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(stackPane, textBoxWithButton);

        // Setup scene directly
        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
        primaryStage.setTitle("Visual Novel - Chapter 1");
    }
    
    @Override
    public ImageView createSpeakerImage(String speaker) {
        String imagePath;
        int width, height;

        switch (speaker) {
            case "คเชน":
                imagePath = "/resources/cashen/cashen_normal.png";
                width = 200;
                height = 300;
                break;
            case "เพื่อน":
                imagePath = "/resources/friend/friend_normal.png";
                width = 260;
                height = 300;
                break;
            case "อาริสา":
                imagePath = "/resources/arisa/arisa_normal.png";
                width = 220;
                height = 310;
                break;
            default:
                System.out.println("Unknown speaker: " + speaker);
                return new ImageView(); // TODO Unknown speaker
        }

        ImageView img = createImageView(imagePath, width, height);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.1);
        img.setEffect(dropShadow);

        return img;
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

        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setImage(new Image(getClass().getResource(getImagePath("คเชน", emotion)).toExternalForm()));
        } else {
            friendImage.setImage(new Image(getClass().getResource(getImagePath("เพื่อน", emotion)).toExternalForm()));
        }
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
    public void setStoryTexts(String url) {
        storyTexts = new TextBase(url);
    }

	@Override
	protected void goToNextChapter(Stage primaryStage) {
		// TODO Auto-generated method stub
		
	}
}