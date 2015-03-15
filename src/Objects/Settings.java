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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.JSONArray;

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
 * Object:
 *      Settings
 * - Object that extends the generic settings object and adds the necessary functions
 *   and ability to handle throttling
 *
 * Methods:
 *      contains  - Returns true if the input key or channel/key pair exists in the map
 *      create    - Creates the input key/value or key/channel/value pair
 *      get       - Gets the value for the input key or key/channel
 *      getChannelList - Returns an ArrayList of channels that exist in the settings file
 *      loadFile       - Parses the file string into a map
 *      loadText       - Loads an input file to string
 *      save           - Saves the settings to file
 *      setFileName    - Sets the file name to be used for saving the settings
 *     *toList         - Converts JSON list to list
 *     *toMap          - Converts JSONObject to map
 *     *jsonToMap      - Recursively uses toList and toMap to convert JSON objects
 *                       to maps/lists
 *
 * Note: Only commands marked with a * are available for use outside the object
 */
public class Settings {
//    Map<String, String> stuff = new TreeMap<String, String>();
    String filename = "doNotSave";
    Map <String, String> generalSettings = Collections.synchronizedMap(new TreeMap<String, String>());
    Map <String, Map<String, String>> channelSettings = Collections.synchronizedMap(new TreeMap<String, Map<String, String>>());
    
    public void create(String key, String value){
        if (!generalSettings.containsKey(key))
            generalSettings.put(key,value);
    }
    
    public Set<String> keySet(){
        Set<String> genSet = generalSettings.keySet();
        Set<String> chanSet = channelSettings.keySet();
        Set<String> allSet = new HashSet<>();
//        System.out.println("general settings size: "+genSet.size());
//        System.out.println("channel settings size: "+chanSet.size());

                Iterator<String> keyIterator = chanSet.iterator();
        
        while (keyIterator.hasNext()){
            String key = keyIterator.next();
            allSet.addAll(channelSettings.get(key).keySet());
            
        }
        
        allSet.addAll(genSet);
//        allSet.addAll(chanSet);
        System.out.println("combined size: "+allSet.size());
        return allSet;
    }
    
    public boolean contains(String key){
        return(generalSettings.containsKey(key));
    }
    public boolean contains(String key, String channel){
        if (!channelSettings.containsKey(channel))
            create("NA","NA",channel);
        return(channelSettings.get(channel).containsKey(key));
    }
    
    public boolean contains(List<String> tree){
        if (tree.size()==0){
            throw new UnsupportedOperationException("Input tree cannot be of ZERO size");
        }
        else if (tree.size()==1){
            return (generalSettings.containsKey(tree.get(0)));
        }
        else{
            if (generalSettings.containsKey(tree.get(0))){
                return contains(tree.remove(0));
            }
            else
                return false;
        }
    }
    public void create(List<String> tree){
        
    }
    
    
    public void create (String key, String value, String channel) {
        if (key=="NA"&&value=="NA"){
            if (channel.startsWith("#")&&channel.split(" ").length==1){
                if (!channelSettings.containsKey(channel)){
                    Map<String, String> newSetting = new TreeMap<String,String>();
                    channelSettings.put(channel,newSetting);
//                    System.out.println("CHANNEL ADDED TO SETTINGS: "+channel);
                    save();
                }
            }
            else{
                throw new UnsupportedOperationException("Input channel is neither an accepted wildcard nor a channel name");
            }
        }
        
        else if (channel.equalsIgnoreCase("ALL")){
            ArrayList<String> channels = getChannelList();
            for (int i=0;i<channels.size();i++){
                if(!channelSettings.get(channels.get(i)).containsKey(key)){
                    channelSettings.get(channels.get(i)).put(key, value);
//                    System.out.println("SETTING "+key+" WAS ADDED TO "+channels.get(i));
                }
//                System.out.println(channelSettings.get(channels.get(i)).get(key));
            }
            save();
        }
        else if (channel.startsWith("#")&&channel.split(" ").length==1){
            if (channelSettings.containsKey(channel)){
                if(!channelSettings.get(channel).containsKey(key))
                    channelSettings.get(channel).put(key, value);
            }
            else{
                Map<String, String> newSetting = new TreeMap<String,String>();
                newSetting.put(key, value);
                channelSettings.put(channel,newSetting);
            }
            save();
        }
        else
            throw new UnsupportedOperationException("Input channel is neither an accepted wildcard nor a channel name");
    }
    
    public boolean set(String key, String value, String channel) {
//        try{
            if (channel.equalsIgnoreCase("ALL")){
                ArrayList<String> channels = getChannelList();
                for (int i=0;i<channels.size();i++){
                    if (channelSettings.get(channels.get(i)).containsKey(key)){
                        channelSettings.get(channels.get(i)).put(key, value);
//                    System.out.println(channelSettings.get(channels.get(i)).get(key));
                    }
                }
                save();
                return true;
            }
            else if (channel.startsWith("#")&&channel.split(" ").length==1){
                
                if (channelSettings.containsKey(channel)){
                    if (channelSettings.get(channel).containsKey(key)){
                        channelSettings.get(channel).put(key, value);
//                    System.out.println(channelSettings.get(channel).get(key));
                        return true;
                    }
                }
                else if (generalSettings.containsKey(key)){
                    generalSettings.put(key,value);
                    return true;
                }
                save();
            }
            else
                throw new UnsupportedOperationException("Input channel is neither an accepted wildcard nor a channel name");
//        }
//        catch (Exception ex){
//            System.out.println("SET SETTING FAILED");
//            ex.printStackTrace();
//            
//            return false;
//        }
        return false;
    }
    
    public String get(String key){
        if(generalSettings.containsKey(key))
            return generalSettings.get(key).toString();
        else
            throw new UnsupportedOperationException("KEY MISSING");
    }
    public String get(String key, String channel){
        if (channelSettings.containsKey(channel)){
            if(channelSettings.get(channel).containsKey(key))
                return channelSettings.get(channel).get(key).toString();
            else
                throw new UnsupportedOperationException("KEY MISSING");
        }
        else
            throw new UnsupportedOperationException("CHANNEL MISSING");
    }
    public ArrayList<String> getChannelList(){
        
        ArrayList<String> channels = new ArrayList<>();
        Collection chanSet = channelSettings.values();
        Iterator<Map.Entry<String, Map<String, String>>> chanIterator = channelSettings.entrySet().iterator();
        
        while(chanIterator.hasNext()){
            Map.Entry channelEntry = chanIterator.next();
            channels.add(channelEntry.getKey().toString());
        }
        return(channels);
    }
    public void save() {
        try{
        if (!this.filename.equalsIgnoreCase("doNotSave")){
            JSONObject writeJSON = new JSONObject();
            JSONObject genJSON = new JSONObject(generalSettings);
            JSONObject chanJSON = new JSONObject();
            
            writeJSON.put("generalSettings", genJSON);
            
            Collection chanSet = channelSettings.values();
            Iterator<Map.Entry<String, Map<String, String>>> chanIterator = channelSettings.entrySet().iterator();
            
            while(chanIterator.hasNext()){
                Map.Entry channelEntry = chanIterator.next();
                
                JSONObject channelSettingJSON = new JSONObject();
                Iterator<Map.Entry<String, String>> settingIterator = channelSettings.get(channelEntry.getKey().toString()).entrySet().iterator();
                
                while(settingIterator.hasNext()){
                    Map.Entry settingEntry = settingIterator.next();
                    channelSettingJSON.put(settingEntry.getKey().toString(), settingEntry.getValue().toString());
                }
                
                chanJSON.put(channelEntry.getKey().toString(), channelSettingJSON);
            }
            
            writeJSON.put("channelSettings",chanJSON);
//            For channel in channels
//                    ....JSONObject settings = new JSONObject();
//                    ....settings.addValue(settingName, settingValue);//for each setting
//                    ....channelJson.addValue(channelName, settings);
            String json = writeJSON.toString(2);
            try{
                File file =new File(filename);
                
                //if file doesnt exists, then create it
//                if(!file.exists()){
                file.createNewFile(); // We're just replacing the old file, not modifying it
//                }
                
                //true = append file
                FileWriter fileWritter = new FileWriter(file.getName());
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write(json);
                bufferWritter.close();
                System.out.println("FILE SAVED");
            }catch(IOException e){
                System.out.println(filename+" HAS NOT BEEN SAVED");
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
//                System.out.println(object.toString());
//                    object.
//                Set keys = object.keySet();
                Map convertedJSON = jsonToMap(object);
//                Map convertedChan = jsonToMap(object.getJSONObject("channelSettings"));
//                System.out.println(convertedJSON);
                generalSettings =(Map) convertedJSON.get("generalSettings");
                channelSettings =(Map) convertedJSON.get("channelSettings");
                
                
//                    for (int i=0;i<keys.size();i++){
//                        generalSettings.put(keys., json)
//                    }
            }
//                    JSONObject scores = (JSONObject) parser.parse(jsonText);
//                    Set users = scores.keySet();
//                    Iterator<String> iterator = users.iterator();
//                    while(iterator.hasNext()) {
//                        String element = iterator.next();
//                        this.add(new Score(element, (int) (long) scores.get(element)));
//                    }
////                String user = users.toArray();
////                    for (int i=0;i<users.length;i++){
////                        this.add(new Score(users[i],(int) scores.get(users[i])));
////                        System.out.println(this.get(i).toString());
////                    }
//                }
            
            else{
                System.out.println(filename+" IS EMPTY");
            }
        }catch(Exception e){
            System.out.println(filename+" HAS NOT BEEN LOADED");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    
    public void setFileName(String filename){
        this.filename=filename;
    }
    
    private String loadText() throws FileNotFoundException, IOException{
        File file =new File(filename);
        //if file doesnt exists, then create it
        if(!file.exists()){
            file.createNewFile();
            return null;
        }
        
        try{
            Scanner wordfile = new Scanner(new File(filename));
            String wordls = "";
            while (wordfile.hasNext()){
                wordls= wordls+(wordfile.next());
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
//            else
//                System.out.println("AWW HELL, "+key+" failed");
            map.put(key, value);
//            System.out.println("KEY: "+key+" VALUE: "+value);
        }
        return map;
    }
    
    private static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
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
