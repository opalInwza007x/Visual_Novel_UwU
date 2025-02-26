package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class TextBase {
	private ArrayList<String[]> texts = new ArrayList<>();
	
	public final static int emotionIndex = 0;
	public final static int speakerIndex = 1;
	public final static int textIndex = 2;
	public final static int soundEffectIndex = 3;
	public final static int talkingSoungIndex = 4;
	public final static int readingStatusIndex = 5;
	public final static int quesion1Index = 6;
	public final static int answer1Index = 7;
	public final static int quesion2Index = 8;
	public final static int answer2Index = 9;
	public final static int maxTextIndex = 10;
	
	public TextBase() {}
	
	public TextBase(String url) {
		try (BufferedReader reader = new BufferedReader(new FileReader(url))){
			String line;
			
			while((line = reader.readLine()) != null) {
				line = line.replace("\\n", "\n");
				String[] splitTexts = line.split("\\$", maxTextIndex);
				
				texts.add(splitTexts);
			}
		}catch (Exception err) {
			System.out.println("Error reading file: " + err.getMessage());
		}
	}
	
	public TextBase(ArrayList<String[]> texts) {
		setTexts(texts);
	}
	
	public ArrayList<String[]> getStoryTexts() {
		return texts;
	}

	public void setTexts(ArrayList<String[]> texts) {
		this.texts = texts;
	}
}
