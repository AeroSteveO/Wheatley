/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Stephen
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Throttle
 * - Linked Classes
 *    N/A
 * 
 * 
 * 
 */
public class ThrottleArray {
    
    ArrayList<Throttle> throttling = new ArrayList<>();
    
    public Throttle getThrottleByType(String get){
        
        List throttleSyn = Collections.synchronizedList(throttling);
        int idx = -1;
        
        synchronized(throttleSyn){
            
            for(int i = 0; i < throttleSyn.size(); i++) {
                if (((Throttle) throttleSyn.get(i)).getType().equalsIgnoreCase(get)) {
                    idx = i;
                    break;
                }
            }
            if (idx==-1){
                throttleSyn.add(new Throttle(get));
                idx = throttleSyn.size();
            }
        }
        return ((Throttle) throttling.get(idx));
    }
    
    public boolean containsType(String type){
        
        List throttleSyn = Collections.synchronizedList(throttling);
        
        synchronized(throttleSyn){
            
            for(int i = 0; i < throttleSyn.size(); i++) {
                if (((Throttle)throttleSyn.get(i)).getType().equalsIgnoreCase(type)) {
                    return(true);
                }
            }
        }
        return(false);
    }
    
    public ArrayList<String> getAllThrottleTypes(){
        
        ArrayList<String> typesContained = new ArrayList<>();
        List throttleSyn = Collections.synchronizedList(throttling);
        
        synchronized(throttleSyn){
            
            for(int i = 0; i < throttling.size(); i++) {
                if (!typesContained.contains(((Throttle)throttleSyn.get(i)).getType())) {
                    typesContained.add(((Throttle)throttleSyn.get(i)).getType());
                }
            }
        }
        return(typesContained);
    }
    
    public void add(String type){
        if (!containsType(type)){
            throttling.add(new Throttle(type));
        }
    }
    
    public void remove(String type){
        
        List throttleSyn = Collections.synchronizedList(throttling);
        
        synchronized(throttleSyn){
            
            for(int i = 0; i < throttleSyn.size(); i++) {
                
                if (((Throttle) throttleSyn.get(i)).getType().equalsIgnoreCase(type)) {
                    
                    throttling.remove(i);
                    i--;
                }
            }
        }
    }
    
    public void removeDupes(){
        
        ArrayList<String> typesContained = new ArrayList<>();
        
        List throttleSyn = Collections.synchronizedList(throttling);
        synchronized(throttleSyn){
            
            for(int i = 0; i < throttleSyn.size(); i++) {
                if (!typesContained.contains(((Throttle)throttleSyn.get(i)).getType())) {
                    typesContained.add(((Throttle)throttleSyn.get(i)).getType());
                }
                else if (typesContained.contains(((Throttle)throttleSyn.get(i)).getType())){
                    throttling.remove(i);
                    i--;
                }
            }
        }
    }
}