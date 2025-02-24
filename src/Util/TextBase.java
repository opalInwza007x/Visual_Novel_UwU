package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TextBase {
	private ArrayList<String[]> texts = new ArrayList<>();
	
	public TextBase() {}
	
	public TextBase(String url) {
		try (BufferedReader reader = new BufferedReader(new FileReader(url))){
			String line;
			
			while((line = reader.readLine()) != null) {
				line = line.replace("\\n", "\n");
				String[] splitTexts = line.split("\\$", 4);
				
				System.out.println(splitTexts.length);
				texts.add(splitTexts);
			}
		}catch (Exception err) {
			System.out.println("Error reading file: " + err.getMessage());
		}
		System.out.println(texts.size());
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
