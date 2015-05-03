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
 *
 * Requirements:
 * - APIs
 *    JSON (AOSP JSON parser)
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 * 
 * 
 * 
 */

public class Definitions {
    private Map<String,Map<String,String>> definitionData = Collections.synchronizedMap(new TreeMap<String,Map<String,String>>());
    private File file = new File("doNotSave");
    
    public Definitions (File file){
        this.file = file;
        try{
            this.loadFile();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public String getRandomWord(){
        ArrayList<String> words = getDefList();
        return (words.get((int) (Math.random()*words.size()-1)));
    }
    
    public ArrayList<String> getDefList(){
        Set<String> defs = definitionData.keySet();
        ArrayList<String> defList = new ArrayList<>();
        
        Iterator<String> defIterator = defs.iterator();
        while(defIterator.hasNext()){
            String word = defIterator.next();
            defList.add(definitionData.get(word.toLowerCase()).get("word"));
        }
        return defList;
    }
    public boolean deleteDef(String word){
        definitionData.remove(word.toLowerCase());
        save();
        return !definitionData.containsKey(word.toLowerCase());
    }
    
    public Definitions (String filename){
        this.file = new File(filename);
        try{
            this.loadFile();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public boolean createDef(String word, String def, String user, String time){
        if(!definitionData.containsKey(word.toLowerCase())){
            TreeMap<String,String> newDef = new TreeMap<>();
            newDef.put("word", word);
            newDef.put("originator",user);
            newDef.put("definition",def);
            newDef.put("time",time);
            
            definitionData.put(word.toLowerCase(), newDef);
            save();
            return true;
        }
        else
            return false;
    }
    
    public boolean containsDef(String word){
        return definitionData.containsKey(word.toLowerCase());
    }
    
    public Map<String,String> getDefMap(String word){
        if(definitionData.containsKey(word.toLowerCase())){
            return definitionData.get(word.toLowerCase());
        }
        else
            return null;
    }
    
    public String getOriginator(String word){
        if(definitionData.containsKey(word.toLowerCase())){
            return definitionData.get(word.toLowerCase()).get("originator");
        }
        else
            return null;
    }
    
    public String getTimeOfDef(String word){
        if(definitionData.containsKey(word.toLowerCase())){
            return definitionData.get(word.toLowerCase()).get("time");
            //Global.getTimestamp(Long.toString(rs.getLong("TIME")*1000))
            //event.getTimestamp() / 1000
        }
        else
            return null;
    }
    
    public String getDefOfWord(String word){
        if(definitionData.containsKey(word.toLowerCase())){
            return definitionData.get(word.toLowerCase()).get("definition");
        }
        else
            return null;
        
    }
    
    public String getWordWithCase(String word){
        if(definitionData.containsKey(word.toLowerCase())){
            return (String) definitionData.get(word.toLowerCase()).get("word");
        }
        else
            return null;
    }
    
    public void save() {
        try{
            if (!file.getName().equalsIgnoreCase("doNotSave")){
                JSONObject writeJSON = new JSONObject(definitionData);
                String json = writeJSON.toString(2);
                try{
                    
                    file.createNewFile(); // We're just replacing the old file, not modifying it
                    
                    //true = append file
                    FileWriter fileWritter = new FileWriter(file.getName());
                    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                    bufferWritter.write(json);
                    bufferWritter.close();
                    System.out.println("FILE SAVED");
                }catch(IOException e){
                    System.out.println(file.getName()+" HAS NOT BEEN SAVED");
                    e.printStackTrace();
                }
            }
            else
                System.out.println("FILE HAS NOT BEEN SAVED, NO FILENAME INPUT");
        }
        catch (Exception ex){
            System.out.println("FILE SAVE HAS FAILED");
            ex.printStackTrace();
        }
    }
    
    public boolean loadFile() throws IOException, JSONException{
        
        
        try{
            String json = loadText(); // Should only have one line of text
            
            if (json!=null&&!json.equals("")){
                JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
                
                definitionData = (Map) jsonToMap(object);
                
            }
            else{
                System.out.println(file.getName()+" IS EMPTY");
            }
        }catch(Exception e){
            System.out.println(file.getName()+" HAS NOT BEEN LOADED");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    
    public void setFileName(String filename){
        this.file =new File(filename);
    }
    
    public void setFile(File file){
        this.file = file;
    }
    
    private String loadText() throws FileNotFoundException, IOException{
        //if file doesnt exists, then create it
        if(!file.exists()){
            file.createNewFile();
            return null;
        }
        
        try{
            Scanner wordfile = new Scanner(file);
            String wordls = "";
            while (wordfile.hasNext()){
                wordls= wordls+(wordfile.nextLine());
            }
            wordfile.close();
            return (wordls);
        } catch (FileNotFoundException ex) {
            System.out.println("TEXT LOADER FAILED");
            ex.printStackTrace();
            return null;
        }
    }
    
    private static Map jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();
        
        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        else
            System.out.println("JSON EMPTY");
        return retMap;
    }
    
    private static Map toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        
        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }
    
    private static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            else
                System.out.println("DANGIT");
            list.add(value);
        }
        return list;
    }
}
