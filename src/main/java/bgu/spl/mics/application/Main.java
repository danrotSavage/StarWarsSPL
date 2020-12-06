package main.java.bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import main.java.bgu.spl.mics.application.passiveObjects.Attack;
import main.java.bgu.spl.mics.application.passiveObjects.Diary;
import main.java.bgu.spl.mics.application.passiveObjects.Ewok;
import main.java.bgu.spl.mics.application.passiveObjects.Ewoks;
import main.java.bgu.spl.mics.application.services.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws IOException {
		final String filepath="C:\\Users\\guyne\\IdeaProjects\\StarWarsSPL\\input.json";
		final String filepathOut="C:\\Users\\guyne\\IdeaProjects\\StarWarsSPL\\output.json";

		Gson gson =new Gson();
		GSON_EXAMPLE_1 dan = gson.fromJson(new FileReader(filepath), GSON_EXAMPLE_1.class);

		/*List<Integer> attackList=new LinkedList<>();
		attackList.add(2);
		attackList.add(4);
		Attack a = new Attack(attackList,500);
		Attack b = new Attack(attackList,500);
		Attack[] attackArray = {a,b};
*/
		C3POMicroservice c3 = new C3POMicroservice();
		LeiaMicroservice lea = new LeiaMicroservice(dan.attacks);
		HanSoloMicroservice han = new HanSoloMicroservice();
		R2D2Microservice r2 = new R2D2Microservice(dan.R2D2);
		LandoMicroservice lando = new LandoMicroservice(dan.Lando);
		Ewoks.getEwoks(dan.Ewoks);

		long startTime=System.currentTimeMillis();
		Thread one = new Thread(lea);
		Thread two = new Thread(c3);
		Thread three = new Thread(han);
		Thread four = new Thread(r2);
		Thread five = new Thread(lando);

		one.start();
		two.start();
		three.start();
		four.start();
		five.start();


		try {
			one.join();
			two.join();
			three.join();
			four.join();
			five.join();
		}
		catch (Exception ex){}



		Diary d=Diary.getDiary();
		System.out.println(Diary.getDiary().toString());

		System.out.println(d.getC3POFinish()-d.getHanSoloFinish());
		System.out.println(d.getLeiaTerminate()-Math.max(d.getC3POFinish(),d.getHanSoloFinish()));
		OutputJsonClass outJson = new OutputJsonClass();

		outJson.totalAttacks=d.getTotalAttacks();
		outJson.C3POFinish=d.getC3POFinish()-startTime;
		outJson.HanSoloFinish=d.getHanSoloFinish()-startTime;
		outJson.R2D2Deactivate=d.getR2D2Deactivate()-startTime;
		outJson.LeiaTerminate=d.getLeiaTerminate()-startTime;
		outJson.C3POTerminate=d.getC3POTerminate()-startTime;
		outJson.HanSoloTerminate=d.getHanSoloTerminate()-startTime;
		outJson.R2D2Terminate=d.getR2D2Terminate()-startTime;
		outJson.LandoTerminate=d.getLandoTerminate()-startTime;
		FileWriter fw = new FileWriter(filepathOut);
		gson.toJson(outJson,fw);
		fw.close();

	}
}

