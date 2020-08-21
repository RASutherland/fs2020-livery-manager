package com.github.lolkilee.liverymanager;

import com.google.gson.*;

import java.io.*;
import java.net.URLDecoder;
import java.util.Scanner;

public class Main {

    /*Input variables list:

    %livery_name%,
    %texture_folder%,
    %airline_name%,
    %plane_registration%,
    %is_airtraffic%,
    %icao_airline_id%

     */

    public static void main(String[] args){
        runScript();
    }

    public static void runScript() {
        String livery_name;
        String texture_folder;
        String airline_name;
        String planeRegistration;
        String icao_airline_id = "";
        boolean set_icao_code = false;
        boolean is_airtraffic = false;

        System.out.println("==============================================" +
                "\nLolkilee's livery script | version 1.0"
                + "\nPack should be installed in the megapack base folder!"
                + "\nCurrently only works with the a320 neo"
                + "\n==============================================\n");

        Scanner scanner = new Scanner(System.in);
        System.out.println("The script will now ask for multiple variables to add the livery to the megapack," +
                "\nPlease make sure your already have all the files required by the livery installed!");

        System.out.println("\n===========Livery name=========="
                + "\nPurely cosmetic, sets the name of livery in the game, e.g. when added a \"delta\" livery, the name should be delta"
                + "\nenter livery name: ");
        livery_name = scanner.nextLine();
        System.out.println("Set the livery name to: " + livery_name + "\n");

        System.out.println("\n===========Texture folder========"
                + "\nImportant! this should match the folder within the megapack without the TEXTURE part"
                + "\nExample: if the texture folder is TEXTURE.DELTA, this should be DELTA"
                + "\nenter texture folder: ");
        texture_folder = scanner.nextLine();
        System.out.println("Set the texture folder to: " + texture_folder + "\n");

        System.out.println("\n==========Airline name========="
                + "\nName of the airline, purely cosmetical"
                + "\nenter airline name: ");
        airline_name = scanner.nextLine();
        System.out.println("Set the airline name to: " + airline_name + "\n");

        System.out.println("\n==========Airplane registration========="
                + "\nSet the standard registration of the livery (e.g PH-BXW, C-XFCD, etc.), purely cosmetical"
                + "\nenter aircraft registration: ");
        planeRegistration = scanner.nextLine();
        System.out.println("Set the registration to: " + planeRegistration + "\n");

        boolean hasAnsweredCorrectly = false;
        int ai_texture = 0;
        System.out.println("\nUse the texture for ai (parked & traffic) aircraft?"
                + "\nWarning not recommended can seriously affect game performance!"
                + "\nYes [y] or no [n]: ");
        while(!hasAnsweredCorrectly) {
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                hasAnsweredCorrectly = true;
                is_airtraffic = true;
            } else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
                hasAnsweredCorrectly = true;
                is_airtraffic = false;
            } else {
                System.out.println("\nAnswered incorrectly! please answer with either yes or no");
                hasAnsweredCorrectly = false;
            }
        }
        if(is_airtraffic) ai_texture = 1;
        else ai_texture = 0;
        System.out.println("\nSet texture for ai to: " + ai_texture);

        if(is_airtraffic) {
            hasAnsweredCorrectly = false;
            System.out.println("\nWant to set the icao code for the livery (so flying traffic use the correct livery if possible)" +
                    "\nYes [y] or no [n]: ");
            while(!hasAnsweredCorrectly) {
                String input = scanner.nextLine();
                if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                    hasAnsweredCorrectly = true;
                    set_icao_code = true;
                } else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
                    hasAnsweredCorrectly = true;
                    set_icao_code = false;
                } else {
                    System.out.println("\nAnswered incorrectly! please answer with either yes or no");
                    hasAnsweredCorrectly = false;
                }
            }

            if(set_icao_code) {
                System.out.println("\nPlease enter the 3 letter icao code for the airline (Delta = DAL)");
                icao_airline_id = scanner.nextLine();
                System.out.println("\nSet the icao code to: " + icao_airline_id);
            }
        }

        System.out.println("\n==================Summary=================" +
                "\nLivery name: " + livery_name +
                "\nTexture folder: " + texture_folder +
                "\nAirline name: " + airline_name +
                "\nUsed as ai traffic: " + boolToString(is_airtraffic) +
                "\nIcao code (if not set should by empty): " + icao_airline_id +
                "\n==========================================");

        boolean wantsToContinue = false;
        hasAnsweredCorrectly = false;
        System.out.println("Want to continue?" +
                "\nYes [y] or no [n]: ");
        while(!hasAnsweredCorrectly) {
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                hasAnsweredCorrectly = true;
                wantsToContinue = true;
            } else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
                hasAnsweredCorrectly = true;
                wantsToContinue = false;
            } else {
                System.out.println("\nAnswered incorrectly! please answer with either yes or no");
                hasAnsweredCorrectly = false;
            }
        }

        scanner.close();

        if(wantsToContinue) try{
            updateFiles(livery_name, texture_folder, airline_name, planeRegistration, icao_airline_id, ai_texture);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void updateFiles(String livery_name, String texture_folder, String airline_name, String planeRegistration,
                                   String icao_airline_id, int ai_texture)
            throws IOException {
        System.out.println("\nAdding the configuration to the files!" +
                "\nProgress log:");
        String s = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String pathUnfiltered = URLDecoder.decode(s, "UTF-8");
        String path = pathUnfiltered.substring(0, pathUnfiltered.indexOf("livery-manager"));
        System.out.println("[log]: System path: " + path);
        String simobjectPath = path + "SimObjects/Airplanes/Asobo_A320_NEO/";

        File configFile = new File(simobjectPath + "aircraft.cfg");
        Scanner configReader = new Scanner(configFile);
        int n = -1;
        while(configReader.hasNextLine()) {
            String line = configReader.nextLine();
            if(line.contains("FLTSIM"))
                n++; //count how many variations are already present
        }
        configReader.close();
        System.out.println("[log]: Livery number: " + n);

        FileWriter fileWriter = new FileWriter(configFile, true);
        for(String line : Presets.aircraftCFGLines) {

            if (!line.equalsIgnoreCase("icao_airline = \"%icao_airline_id%\"")) {
                if (line.contains("%")) {
                    if (line.contains("%livery_name%")) line = line.replace("%livery_name%", livery_name);
                    else if (line.contains("%n%")) line = line.replace("%n%", Integer.toString(n));
                    else if (line.contains("%texture_folder%")) line = line.replace("%texture_folder%", texture_folder);
                    else if (line.contains("%airline_name%")) line = line.replace("%airline_name%", airline_name);
                    else if (line.contains("%plane_registration%")) line = line.replace("%plane_registration%", planeRegistration);
                    else if (line.contains("%is_airtraffic%")) line = line.replace("%is_airtraffic%", Integer.toString(ai_texture));
                }
                line += "\n";
                fileWriter.append(line);
                System.out.println("[log]: appended line: " + line);
            } else {
                if(!icao_airline_id.equalsIgnoreCase("")) {
                    line = line.replace("%icao_airline_id%", icao_airline_id);
                    line += "\n";
                    fileWriter.append(line);
                    System.out.println("[log]: appended line: " + line);
                }
            }
        }
        fileWriter.close();
        System.out.println("[log]: Appended to the aircraft.cfg file");
        System.out.println("[log]: Updating layout.json");
        updateLayout(path, texture_folder);
    }

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

    public static String boolToString(boolean b) {
        if(b) return "true";
        else return "false";
    }
}
