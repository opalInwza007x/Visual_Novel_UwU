package main;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.util.Optional;

import Util.Chapter;
import Util.TextBase;

public class Chapter5 extends Chapter {
    private ImageView cashenImage;
    private ImageView father_motherInLawImage;

    @Override
    public void startChapter(Stage primaryStage) {
        stateSetup(primaryStage);
    }
    
    @Override
	protected void stateSetup(Stage primaryStage) {
    	playBackgroundMusic("/resources/sound/bgChap5.mp3");
        setStoryTexts("/resources/texts/Chapter5.txt");

        ImageView background = setupBackground("/resources/background/BackgroundChapter5.png");
        TextFlow textBox = createTextFlow();
        Button nextButton = createNextButton(primaryStage, textBox);

        father_motherInLawImage = createSpeakerImage("พ่อตา");
        cashenImage = createSpeakerImage("คเชน");
        updateSpeakerVisibility();

        // Stack text box background and text
        StackPane textBoxStack = createTextBoxStack(textBox);
        StackPane textBoxWithButton = createTextBoxWithButton(textBoxStack, nextButton);

        // Speaker images container
        HBox speakerPane = new HBox(80, father_motherInLawImage ,cashenImage);
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
        enterAnimation(root, textBox);
        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
        primaryStage.setTitle("Visual Novel - Chapter 5");
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
                imagePath = "/resources/arisa/Arisa_shy3_darkMarkMark.png";
                width = 220;
                height = 310;
                break;
            case "พ่อตา":
                imagePath = "/resources/fatherInLaw/father&motherInLaw_normal.png";
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
    public void updateCharacterImages() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        String emotion = storyTexts.getStoryTexts().get(currentTextIndex)[0];

        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setImage(new Image(getClass().getResource(getImagePath("คเชน", emotion)).toExternalForm()));
        } 
        else if(currentSpeaker.equals("พ่อตา") ||  currentSpeaker.equals("แม่ยาย") ) {
        	father_motherInLawImage.setImage(new Image(getClass().getResource(getImagePath("พ่อตา", emotion)).toExternalForm()));
        }
    }

    @Override
    public void updateSpeakerVisibility() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        String status = storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex];
        
        if (currentTextIndex == 0) {
        	father_motherInLawImage.setOpacity(0);
        }
        
        if (status.equals("event")) {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), father_motherInLawImage);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
        else if (status.equals("event2")) {
        	FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), father_motherInLawImage);
        	fadeOut.setFromValue(1.0);
        	fadeOut.setToValue(0.0);
        	fadeOut.play();
        }

        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setOpacity(1.0);
            if (father_motherInLawImage.getOpacity() > 0) {
            	father_motherInLawImage.setOpacity(0.6);
            }
        } 
        else if (currentSpeaker.equals("อาริสา") || currentSpeaker.equals("อาริสา (ร่าง 2)")) {
            cashenImage.setOpacity(0.6);
            if (father_motherInLawImage.getOpacity() > 0) {
            	father_motherInLawImage.setOpacity(1.0);
            }
        }
    }
    
    @Override
    public void handleNextText(Stage primaryStage, TextFlow textBox, int fromAnswerBox) {
        // If animation is running and user clicks Next, just show full text immediately
        if (fromAnswerBox == 0 && isRunning()) {
            timeline.stop();
            // Replace with direct text update without animation
            updateTextBoxInstantly(textBox);
            return;
        }

        // Advance text index
        if (fromAnswerBox == 0) {
            if (!"ask2".equals(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex])) {
                currentTextIndex++;
            }
            if (currentTextIndex < storyTexts.getStoryTexts().size() && ("event".equals(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex]) || "stopmusic".equals(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex]))) {
            	backgroundMusic.stop();
            }
            else if (!backgroundMusic.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            	backgroundMusic.play();
            }
        } 
        else {
            currentTextIndex += fromAnswerBox;
        }
        
        if (currentTextIndex < storyTexts.getStoryTexts().size()) {
            updateSpeakerVisibility();
            playEffectSound(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.soundEffectIndex]);
            updateCharacterImages();
            
            if ("ask2".equals(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex])) {
                createAnswerBoxFor2(primaryStage, textBox);
            }
            
            timeline.stop();
            timeline = createTimeline(textBox);
            timeline.play();
        } else {
            goToNextChapter(primaryStage);
        }
    }

    @Override
    public void setStoryTexts(String url) {
        storyTexts = new TextBase(url);
    }
    
    public void goToNextChapter(Stage primaryStage) {
    	// exitAnimation(primaryStage);
    	backgroundMusic.stop();
    	
    	Chapter6 chapter6 = new Chapter6();
        chapter6.startChapter(primaryStage);
    }
}