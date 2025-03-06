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

public class Chapter5 extends Chapter{
	private ImageView fatherInLawImage;
	private ImageView motherInLawImage;
	private ImageView strangeManImage;
    private ImageView cashenImage;
    private StackPane stackPane;
    private String nameSpeakerLeft = null ; 
    private String nameSpeakerRight = null ;
    private ImageView leftImage  ; 
    private ImageView rightImage ;
	@Override
	protected void startChapter(Stage primaryStage) {
		playBackgroundMusic("/resources/sound/bgChap1.mp3");
        loadSoundEffect(Arrays.asList("whoosh", "pop", "wow"));
        setStoryTexts("src/resources/texts/Chapter5.txt");

        ImageView background = setupBackground("/resources/background/classroomTest.jpg");
        TextFlow textBox = createTextFlow();
        Button nextButton = createNextButton(primaryStage, textBox);

        fatherInLawImage = createSpeakerImage("พ่อตา");
        motherInLawImage = createSpeakerImage("แม่ยาย");
        cashenImage = createSpeakerImage("คเชน");
        strangeManImage = createSpeakerImage("ชายแปลกหน้า");
        //initialize speaker & listener
        nameSpeakerLeft = "คเชน";
        nameSpeakerRight = "ชายแปลกหน้า";
        leftImage = createSpeakerImage("คเชน");
        rightImage = createSpeakerImage("ชายแปลกหน้า");
        
        updateSpeakerVisibility();

        // Stack text box background and text
        StackPane textBoxStack = createTextBoxStack(textBox);
        StackPane textBoxWithButton = createTextBoxWithButton(textBoxStack, nextButton);

        // Speaker images container
        HBox speakerPane = new HBox(80 , leftImage,rightImage);
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
        primaryStage.setTitle("Visual Novel - Chapter 5");
		
	}
	protected boolean isSpeaker( String object ) {
		if( object.equals( storyTexts.getStoryTexts().get(currentTextIndex)[1] )) {
			return true;
		}else {
			return false ;
		}
	}

	@Override
	protected void updateCharacterImages() {
		String emotion = storyTexts.getStoryTexts().get(currentTextIndex)[1];
//		if( !Speaker.equals(currentSpeaker)) {
//			this.currentListener = this.currentSpeaker;   
//			this.currentSpeaker = Speaker;
//		}
		
       
        //setleftImage
        if (nameSpeakerLeft.equals("คเชน")) {
        	leftImage.setImage(new Image(getClass().getResource(getImagePath("คเชน", emotion)).toExternalForm()));
        } else if( nameSpeakerLeft.equals("พ่อตา")){
        	leftImage.setImage(new Image(getClass().getResource(getImagePath("พ่อตา", emotion)).toExternalForm()));
        }else if( nameSpeakerLeft.equals("แม่ยาย")) {
        	leftImage.setImage(new Image(getClass().getResource(getImagePath("แม่ยาย", emotion)).toExternalForm()));
        }
        //setRightImage
        if (nameSpeakerRight.equals("คเชน")) {
        	rightImage.setImage(new Image(getClass().getResource(getImagePath("คเชน", emotion)).toExternalForm()));
        } else if( nameSpeakerLeft.equals("พ่อตา")){
        	rightImage.setImage(new Image(getClass().getResource(getImagePath("พ่อตา", emotion)).toExternalForm()));
        }else if( nameSpeakerLeft.equals("แม่ยาย")) {
        	rightImage.setImage(new Image(getClass().getResource(getImagePath("แม่ยาย", emotion)).toExternalForm()));
        }
        
        
		
	}

	@Override
	protected ImageView createSpeakerImage(String speaker) {
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
	            case "พ่อตา":
	                imagePath = "/resources/fatherInLaw/fatherInLaw_normal.png";
	                width = 220;
	                height = 310;
	                break;
	            case "แม่ยาย":
	                imagePath = "/resources/motherInLaw/motherInLaw_normal.png";
	                width = 220;
	                height = 310;
	                break;
	            case "ชายแปลกหน้า":
	                imagePath = "/resources/strangeMan/strangeMan_normal.png";
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
	protected void updateSpeakerVisibility() {
		
	        
	        // Update speaker visibility without animations
	        if ( isSpeaker(nameSpeakerLeft)) {
	            leftImage.setOpacity(1.0);
	            rightImage.setOpacity(0.6);
	        } else {
	        	leftImage.setOpacity(0.6);
	        	rightImage.setOpacity(1.0);
	        }
		
	}

	@Override
	protected void createAnswerBoxFor2(Stage primaryStage, TextFlow textBox) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setStoryTexts(String url) {
		storyTexts = new TextBase(url);
		
	}

	@Override
	protected void goToNextChapter(Stage primaryStage) {
//		backgroundMusic.stop();
//    	
//    	Chapter6 chapter2 = new Chapter2();
//        chapter2.startChapter(primaryStage);
		
	}

}
