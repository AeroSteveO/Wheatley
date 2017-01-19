/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Stephen
 */
public abstract class SettingsBase {

    File file = new File("doNotSave");
    Map<String, Object> settings = Collections.synchronizedMap(new TreeMap<String, Object>());

    public SettingsBase(){
        
    }
    
    public SettingsBase(File file) {
        this.file = file;
        try {
            this.loadFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public SettingsBase(String filename) {
        this.file = new File(filename);
        try {
            this.loadFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return settings.isEmpty();
    }

    public ArrayList<String> getKeyList() {
        Set keys = settings.keySet();
        ArrayList<String> keyList = new ArrayList();
        Iterator i = keys.iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            keyList.add(key);
        }
        return keyList;
    }
    
    public void save() {
        try {
            if (!file.getName().equalsIgnoreCase("doNotSave")) {
                JSONObject genJSON = new JSONObject(settings);

                String json = genJSON.toString(2);
                try {

                    file.createNewFile(); // We're just replacing the old file, not modifying it

                    FileWriter fileWritter = new FileWriter(file.getName());
                    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                    bufferWritter.write(json);
                    bufferWritter.close();
                    System.out.println("FILE SAVED");
                } catch (IOException e) {
                    System.out.println(file.getName() + " HAS NOT BEEN SAVED");
                    e.printStackTrace();
                }
            } else {
                System.out.println("FILE HAS NOT BEEN SAVED, NO FILENAME INPUT");
            }
        } catch (Exception ex) {
            System.out.println("FILE SAVE HAS FAILED");
            ex.printStackTrace();
        }
    }

    public boolean loadFile() throws IOException, JSONException {

        try {
            String json = loadText(); // Should only have one line of text

            if (json != null && !json.equals("")) {
                JSONObject object = (JSONObject) new JSONTokener(json).nextValue();

                settings = jsonToMap(object);

            } else {
                System.out.println(file.getName() + " IS EMPTY");
                return false;
            }
        } catch (Exception e) {
            System.out.println(file.getName() + " HAS NOT BEEN LOADED");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setFileName(String filename) {
        this.file = new File(filename);
        try {
            if (!this.file.exists()) {
                this.file.createNewFile(); // We're just replacing the old file, not modifying it
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    protected String loadText() throws FileNotFoundException, IOException {
        //if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
            return null;
        }

        try {
            Scanner wordfile = new Scanner(file);
            String wordls = "";
            while (wordfile.hasNext()) {
                wordls = wordls + (wordfile.nextLine());
            }
            wordfile.close();
            return (wordls);
        } catch (FileNotFoundException ex) {
            System.out.println("TEXT LOADER FAILED");
            ex.printStackTrace();
            return null;
        }
    }

    protected static Map jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        } else {
            System.out.println("JSON EMPTY");
        }
        return retMap;
    }

    private static Map toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}