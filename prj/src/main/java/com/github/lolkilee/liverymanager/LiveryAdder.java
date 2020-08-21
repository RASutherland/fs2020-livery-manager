package com.github.lolkilee.liverymanager;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class LiveryAdder {

    private final String parentPath;

    public LiveryAdder(String parentPath) {
        this.parentPath = parentPath;
    }

    //Main method to update the files
    //TODO: remove old code which sets the config, and use the config set by clink in public livery repository
    public static void updateFiles(String livery_name, String texture_folder, String airline_name, String planeRegistration,
                                   String icao_airline_id, int ai_texture, String simObjectsPath)
            throws IOException {

        //Code here

    }

    //Updates the layout.json
    public static void updateLayout(String parentPath, String textureFolder){
        try {
            File layoutFile = new File(parentPath + "layout.json");
            System.out.println("Layout file path: " + parentPath + "layout.json");
            FileReader jsonReader = new FileReader(layoutFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement jsonContent = gson.fromJson(jsonReader, JsonElement.class);
            JsonObject jsonObjectArray = jsonContent.getAsJsonObject();
            JsonArray jsonArray = jsonObjectArray.get("content").getAsJsonArray();
            File[] textureFiles = new File(parentPath + "SimObjects/Airplanes/Asobo_A320_NEO/TEXTURE." + textureFolder + "/").listFiles();
            int n = 0;

            for (File f : textureFiles) {
                n++;
                JsonObject jObject = new JsonObject();
                String path = "SimObjects/Airplanes/Asobo_A320_NEO/TEXTURE." + textureFolder + "/" + f.getName();
                jObject.addProperty("path", path);
                jObject.addProperty("size", f.length());
                jObject.addProperty("date", 132398508758630094L);
                jsonArray.add(jObject);
            }

            jsonObjectArray = new JsonObject();
            jsonObjectArray.add("content", jsonArray);
            jsonContent = jsonObjectArray;
            FileWriter fw = new FileWriter(layoutFile);
            gson.toJson(jsonContent, fw);
            fw.close();
            jsonReader.close();

            System.out.println("[log]: added " + n + " lines to layout.json");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
