package main.java.bgu.spl.mics.application;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class jsonReader {

    public static JsonInput getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, JsonInput.class);
        } catch (IOException ex) {
        }
        return null;
    }

}
