/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Stephen
 */
public class MapArray {
    Map<String,ArrayList<ArrayList<String>>> log = Collections.synchronizedMap(new TreeMap<String,ArrayList<ArrayList<String>>>());
    private final int size;
    
    public MapArray(int size) {
        this.size = size;
    }
    
    public ArrayList<ArrayList<String>> getArray(String channel) {
        synchronized(log) {
            if (log.containsKey(channel)) {
                ArrayList<ArrayList<String>> newArray = new ArrayList<ArrayList<String>>();
                newArray.addAll(log.get(channel));
                return newArray;
            }
            else {
                return null;
            }
        }
    }
    
    public void addToLog(String channel, String message) {
        synchronized(log) {
            ArrayList<String> array = new ArrayList<String>();
            array.add(message);
            addToLog(channel, array);
        }
    }
    
    public void addToLog(String channel, ArrayList<String> message) {
        synchronized(log) {
            channel = channel.toLowerCase();
            if (!log.containsKey(channel)){
                ArrayList<ArrayList<String>> channelLog = new ArrayList<>();
                channelLog.add(message);
                log.put(channel, channelLog);
            }
            else{
                log.get(channel).add(message);
                
                if (log.get(channel).size() > size){
                    log.get(channel).remove(0);
                }
            }
        }
    }
}
