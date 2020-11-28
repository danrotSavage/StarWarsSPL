package main.java.bgu.spl.mics.application;

import com.google.gson.Gson;
import main.java.bgu.spl.mics.application.passiveObjects.Ewok;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		Gson gson=new Gson();
		String json = gson.toJson(new Ewok(2));
		System.out.println(json);
	}
}
