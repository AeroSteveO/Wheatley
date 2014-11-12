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
    
    private final List<Throttle> throttling = Collections.synchronizedList( new ArrayList<Throttle>());
    
    public Throttle getThrottleByType(String get){
        
//        List throttleSyn = Collections.synchronizedList(throttling);
        int idx = -1;
        
        synchronized(throttling){
            
            for(int i = 0; i < throttling.size(); i++) {
                if (((Throttle) throttling.get(i)).getType().equalsIgnoreCase(get)) {
                    idx = i;
                    break;
                }
            }
            if (idx==-1){
                throttling.add(new Throttle(get));
                idx = throttling.size();
            }
        }
        return ((Throttle) throttling.get(idx));
    }
    
    public boolean containsType(String type){
        
//        List throttleSyn = Collections.synchronizedList(throttling);
        
        synchronized(throttling){
            
            for(int i = 0; i < throttling.size(); i++) {
                if (((Throttle)throttling.get(i)).getType().equalsIgnoreCase(type)) {
                    return(true);
                }
            }
        }
        return(false);
    }
    
    public ArrayList<String> getAllThrottleTypes(){
        
        ArrayList<String> typesContained = new ArrayList<>();
//        List throttleSyn = Collections.synchronizedList(throttling);
        
        synchronized(throttling){
            
            for(int i = 0; i < throttling.size(); i++) {
                if (!typesContained.contains(((Throttle)throttling.get(i)).getType())) {
                    typesContained.add(((Throttle)throttling.get(i)).getType());
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
        
//        List throttleSyn = Collections.synchronizedList(throttling);
        
        synchronized(throttling){
            
            for(int i = 0; i < throttling.size(); i++) {
                
                if (((Throttle) throttling.get(i)).getType().equalsIgnoreCase(type)) {
                    
                    throttling.remove(i);
                    i--;
                }
            }
        }
    }
    
    public void removeDupes(){
        
        ArrayList<String> typesContained = new ArrayList<>();
        
//        List throttleSyn = Collections.synchronizedList(throttling);
        synchronized(throttling){
            
            for(int i = 0; i < throttling.size(); i++) {
                if (!typesContained.contains(((Throttle)throttling.get(i)).getType())) {
                    typesContained.add(((Throttle)throttling.get(i)).getType());
                }
                else if (typesContained.contains(((Throttle)throttling.get(i)).getType())){
                    throttling.remove(i);
                    i--;
                }
            }
        }
    }
}