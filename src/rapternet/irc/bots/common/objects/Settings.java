/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.common.objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
 * Object:
 *      Settings
 * - Object that extends the generic settings object and adds the necessary functions
 *   and ability to handle throttling
 *
 * Methods:
 *     *contains  - Returns true if the input key or channel/key pair exists in the map
 *     *create    - Creates the input key/value or key/channel/value pair
 *     *get       - Gets the value for the input key or key/channel
 *     *getChannelList - Returns an ArrayList of channels that exist in the settings file
 *     *loadFile       - Parses the file string into a map
 *      loadText       - Loads an input file to string
 *     *save           - Saves the settings to file
 *     *setFileName    - Sets the file name to be used for saving the settings
 *      toList         - Converts JSON list to list
 *      toMap          - Converts JSONObject to map
 *      jsonToMap      - Recursively uses toList and toMap to convert JSON objects
 *                       to maps/lists
 *
 * Note: Only commands marked with a * are available for use outside the object
 *
 * Useful Resources
 * - http://developer.android.com/reference/org/json/package-summary.html
 * - http://developer.android.com/reference/java/util/Map.html
 *
 * Version: 0.6.0
 *
 */
public class Settings extends SettingsBase {
    Map <String, Object> channelSettings = Collections.synchronizedMap(new TreeMap<String, Object>( ));
    
    public Settings(String filename){
        this(new File(filename));
    }
    
    public Settings(File file){
        this.file = file;
        try {
            this.loadFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Settings(){
        super();
    }
    
    public void create(String key, String value){
        key=key.toLowerCase();
        if (!settings.containsKey(key))
            settings.put(key,value);
        save();
    }
    
    public Set<String> keySet(){
        Set<String> genSet = settings.keySet();
        Set<String> chanSet = channelSettings.keySet();
        Set<String> allSet = new HashSet<>();
        
        Iterator<String> keyIterator = chanSet.iterator();
        
        while (keyIterator.hasNext()){
            String key = keyIterator.next();
            allSet.addAll(((Map)channelSettings.get(key)).keySet());
            
        }
        
        allSet.addAll(genSet);
//        System.out.println("combined size: "+allSet.size());
        return allSet;
    }
    
  /**
   * Determines if the key is contained within the settings file
   * 
   * @param key The key to search for
   * @return TRUE if the key exists in settings, false otherwise.
   */
  @Override
    public boolean contains(String key){
        key=key.toLowerCase();
        return(settings.containsKey(key));
    }
    
  /**
   * Determines if the key is contained within the specified channels settings
   * 
   * @param key The key to search for in the channel specific settings
   * @param channel The channel to search in for the key
   * @return TRUE if the key exists for the channel in the settings file, false
   *         otherwise
   */
  public boolean contains(String key, String channel){
        key=key.toLowerCase();
        channel = channel.toLowerCase();
        if (!channelSettings.containsKey(channel))
            create("NA","NA",channel);
        return(((Map)channelSettings.get(channel)).containsKey(key));
    }
    
    public String get(List<String> tree){
//        System.out.println(tree + " is the contains tree");
        if (this.contains(tree)){
            if (tree.isEmpty()){
                throw new UnsupportedOperationException("Input tree cannot be of ZERO size");
            }
            else if (tree.size()==1){
                if(tree.get(0).startsWith("#"))
                    throw new UnsupportedOperationException("Channels cannot contain any values, only more maps");
                else
                    return (settings.get(tree.get(0)).toString());
            }
            else{
                Map<String,Object> tempMap = new TreeMap<>();
                boolean continueSearch = true;
                
                if (tree.get(0).startsWith("#")){
                    // We gotta check channel settings, not general
                    if (channelSettings.containsKey(tree.get(0))){
                        
                        Iterator treeIterator = tree.iterator();
                        tempMap.putAll(channelSettings);
                        String key = new String();
                        while (treeIterator.hasNext() && continueSearch){
                            
                            key = ((String) treeIterator.next()).toLowerCase();
                            
                            if (tempMap.containsKey(key)){
                                if (tempMap.get(key) instanceof Map){
                                    tempMap = (Map) tempMap.get(key);
                                }
//                                else
//                                    System.out.println(tempMap.get(key) + " WTF?");
                            }
                            else{
                                continueSearch = false;
                                System.out.println(key);
                            }
                        }
                        return tempMap.get(key).toString();
                    }
                    else
                        return null;
                }
                else{
                    if (settings.containsKey(tree.get(0))){
                        Iterator treeIterator = tree.iterator();
                        tempMap.putAll(settings);
                        String key = new String();
                        while (treeIterator.hasNext() && continueSearch){
                            
                            key = ((String) treeIterator.next()).toLowerCase();
                            
                            if (tempMap.containsKey(key)){
                                if (tempMap.get(key) instanceof Map){
                                    tempMap = (Map) tempMap.get(key);
                                }
                                else
                                    System.out.println(tempMap.get(key) + " WTF?");
                                
                            }
                            else{
                                continueSearch = false;
                                System.out.println(key);
                            }
                            
                        }
                        return tempMap.get(key).toString();
                        
                    }
                    else
                        return null;
                }
            }
        }
        else{
            return null;
        }
    }
    
    public boolean contains(List<String> tree){
//        System.out.println(tree + " is the contains tree");
        if (tree.isEmpty()){
            throw new UnsupportedOperationException("Input tree cannot be of ZERO size");
        }
        else if (tree.size()==1){
            if(tree.get(0).startsWith("#"))
                return (channelSettings.containsKey(tree.get(0)));
            else
                return (settings.containsKey(tree.get(0)));
        }
        else{
            Map<String,Object> tempMap = new TreeMap<>();
            boolean continueSearch = true;
            
            if (tree.get(0).startsWith("#")){
                // We gotta check channel settings, not general
                if (channelSettings.containsKey(tree.get(0))){
                    
                    Iterator treeIterator = tree.iterator();
                    tempMap.putAll(channelSettings);
                    
                    while (treeIterator.hasNext() && continueSearch){
                        
                        String key = ((String) treeIterator.next()).toLowerCase();
                        
                        if (tempMap.containsKey(key)){
                            if (tempMap.get(key) instanceof Map){
                                tempMap = (Map) tempMap.get(key);
                            }
                            else
                                System.out.println(tempMap.get(key) + " WTF?");
                        }
                        else{
                            continueSearch = false;
                            System.out.println(key);
                        }
                    }
                    return continueSearch;
                }
                else
                    return false;
            }
            else{
                if (settings.containsKey(tree.get(0))){
                    Iterator treeIterator = tree.iterator();
                    tempMap.putAll(settings);
                    
                    while (treeIterator.hasNext() && continueSearch){
                        
                        String key = ((String) treeIterator.next()).toLowerCase();
                        
                        if (tempMap.containsKey(key)){
                            if (tempMap.get(key) instanceof Map){
                                tempMap = (Map) tempMap.get(key);
                            }
                            else
                                System.out.println(tempMap.get(key) + " WTF?");
                            
                        }
                        else{
                            continueSearch = false;
                            System.out.println(key);
                        }
                        
                    }
                    return continueSearch;
                    
                }
                else
                    return false;
            }
        }
    }
    
    public void create(List<String> tree){
        if (tree == null || tree.isEmpty()) {
            throw new UnsupportedOperationException("TREE MUST CONTAIN AT LEAST 2 VALUES, A KEY AND A VALUE");
        }

        if (!contains(tree.subList(0, tree.size()-1))){
            
            
            if (tree.size()==1){
                throw new UnsupportedOperationException("TREE MUST CONTAIN AT LEAST 2 VALUES, A KEY AND A VALUE");
            }
            else if (tree.size()==2){
                if (tree.get(0).startsWith("#")){
                    throw new UnsupportedOperationException("CHANNELS CANNOT HAVE VALUES ASSOCIATED WITH THEM, ONLY KEY/VALUE PAIRS");
                }
                else{
                    settings.put(tree.get(0).toLowerCase(), tree.get(1));
                }
            }
            else{
                Map<String, Object> tmpMap = new TreeMap<>();
                Map<String, Object> newMap = new TreeMap<>();
                
                tmpMap.put(tree.get(tree.size()-2).toLowerCase(), tree.get(tree.size()-1));
                
                for (int i=tree.size()-3;i>=0;i--){
                    newMap.put(tree.get(i).toLowerCase(), tmpMap);
                    
                    tmpMap = new TreeMap<>();
                    
                    tmpMap.putAll(newMap);
                }
                if (tree.get(0).startsWith("#")){
                    this.channelSettings.putAll((Map) newMap);
                }
                else{
                    this.settings.putAll((Map) newMap);
                }
            }
            save();
        }
    }
    
    
    public void create (String key, String value, String channel) {
        key=key.toLowerCase();
        channel = channel.toLowerCase();
        if (key.equals("NA") && value.equals("NA")){
            if (channel.startsWith("#")&&channel.split(" ").length==1){
                if (!channelSettings.containsKey(channel)){
                    Map<String, String> newSetting = new TreeMap<>();
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
                if(!((Map)channelSettings.get(channels.get(i))).containsKey(key)){
                    ((Map)channelSettings.get(channels.get(i))).put(key, value);
//                    System.out.println("SETTING "+key+" WAS ADDED TO "+channels.get(i));
                }
//                System.out.println(channelSettings.get(channels.get(i)).get(key));
            }
            save();
        }
        else if (channel.startsWith("#")&&channel.split(" ").length==1){
            if (channelSettings.containsKey(channel)){
                if(!((Map)channelSettings.get(channel)).containsKey(key))
                    ((Map)channelSettings.get(channel)).put(key, value);
            }
            else{
                Map<String, String> newSetting = new TreeMap<>();
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
                if (((Map)channelSettings.get(channels.get(i))).containsKey(key)){
                    ((Map)channelSettings.get(channels.get(i))).put(key, value);
//                    System.out.println(channelSettings.get(channels.get(i)).get(key));
                }
            }
            save();
            return true;
        }
        else if (channel.startsWith("#")&&channel.split(" ").length==1){
            
            if (channelSettings.containsKey(channel)){
                if (((Map)channelSettings.get(channel)).containsKey(key)){
                    ((Map)channelSettings.get(channel)).put(key, value);
//                    System.out.println(channelSettings.get(channel).get(key));
                    save();
                    return true;
                }
            }
            else if (settings.containsKey(key)){
                settings.put(key,value);
                save();
                return true;
            }
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
    
    public boolean set(String key, String value) {
        if (settings.containsKey(key.toLowerCase())){
            settings.put(key.toLowerCase(),value);
            save();
            return true;
        }
        return false;
    }
    
    public String get(String key){
        key=key.toLowerCase();
        
        if(settings.containsKey(key))
            return settings.get(key).toString();
        else
            throw new UnsupportedOperationException("KEY MISSING");
    }

  /**
   * Gets the ArrayList String located at the input key
   * 
   * @param key The used to identify the array
   * @return ArrayList String located at the input key
   */
  public ArrayList<String> getArray(String key){
      key=key.toLowerCase();
      if (!key.endsWith("list"))
        key += "list";
      if(settings.containsKey(key))
        return (ArrayList) settings.get(key);
      else
        throw new UnsupportedOperationException("KEY MISSING");
    }
    
    public void setArray(String key, ArrayList<String> value){
      key=key.toLowerCase();
      if (!key.endsWith("list"))
        key += "list";
      
      if(settings.containsKey(key))
        settings.put(key,value);
      else
        throw new UnsupportedOperationException("KEY MISSING");
    }
    
    public void create(String key, ArrayList<String> value){
        key=key.toLowerCase();
        if (!key.endsWith("list"))
            key += "list";
        
        if (!settings.containsKey(key))
            settings.put(key,value);
    }

    public String get(String key, String channel){
        key=key.toLowerCase();
        channel = channel.toLowerCase();
        if (channelSettings.containsKey(channel)){
            if(((Map)channelSettings.get(channel)).containsKey(key))
                return ((Map)channelSettings.get(channel)).get(key).toString();
            else
                throw new UnsupportedOperationException("KEY MISSING");
        }
        else
            throw new UnsupportedOperationException("CHANNEL MISSING");
    }
    
    public ArrayList<String> getChannelList(){
        
        ArrayList<String> channels = new ArrayList<>();
        Collection chanSet = channelSettings.values();
        Iterator<Map.Entry<String, Object>> chanIterator = channelSettings.entrySet().iterator();
        
        while(chanIterator.hasNext()){
            Map.Entry channelEntry = chanIterator.next();
            channels.add(channelEntry.getKey().toString());
        }
        return(channels);
    }
    
    @Override
    public void save() {
        try{
            if (!this.file.getName().equalsIgnoreCase("doNotSave")){
                JSONObject writeJSON = new JSONObject();
                JSONObject genJSON = new JSONObject(settings);
                JSONObject chanJSON = new JSONObject();
                
                writeJSON.put("generalSettings", genJSON);
                
//                Collection chanSet = channelSettings.values();
                Iterator<Map.Entry<String, Object>> chanIterator = channelSettings.entrySet().iterator();
                
                while(chanIterator.hasNext()){
                    Map.Entry channelEntry = chanIterator.next();
                    
                    JSONObject channelSettingJSON = new JSONObject();
                    Iterator<Map.Entry<String, String>> settingIterator = ((Map)channelSettings.get(channelEntry.getKey().toString())).entrySet().iterator();
                    
                    while(settingIterator.hasNext()){
                        Map.Entry settingEntry = settingIterator.next();
                        channelSettingJSON.put(settingEntry.getKey().toString(), settingEntry.getValue().toString());
                    }
                    
                    chanJSON.put(channelEntry.getKey().toString(), channelSettingJSON);
                }
                
                writeJSON.put("channelSettings", chanJSON);
                
                String json = writeJSON.toString(2);
                try{
                    file.createNewFile(); // We're just replacing the old file, not modifying it
                    
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
    @Override
    public boolean loadFile() throws IOException, JSONException{
        
        
        try{
            String json = loadText(); // Should only have one line of text
            
            if (json!=null&&!json.equals("")){
                JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
                
                Map convertedJSON = jsonToMap(object);
                
                settings =(Map) convertedJSON.get("generalSettings");
                channelSettings =(Map) convertedJSON.get("channelSettings");
                
                
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
}
