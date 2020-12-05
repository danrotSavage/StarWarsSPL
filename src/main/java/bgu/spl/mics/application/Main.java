package main.java.bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import main.java.bgu.spl.mics.application.passiveObjects.Ewok;

import java.nio.file.Paths;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		String userJson= "C:\\Users\\guyne\\IdeaProjects\\StarWarsSPL\\input.json";
		String m ={"attacks": [{"duration" : 1000, "serials" : [1,2]}, {"duration" : 1000, "serials" : [2,1]], "R2D2": 2000,"Lando": 2000};
		Gson input=new Gson();
		JsonInput j=input.fromJson(m,JsonInput.class);
		try {

			//input = jsonReader.getInputFromJson(userJson);
		}catch (Exception e){
			System.out.printf(e.toString());
		}
	}
}

