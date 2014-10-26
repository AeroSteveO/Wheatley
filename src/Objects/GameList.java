/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 * Replaces the GameArray vector
 * 
 * Object:
 *      GameList
 * - Object that contains game data and prevents multiple instances of single 
 *   games to occur in one channel
 *
 * Methods:
 *     *contains                   - Returns true if the current input game/channel 
 *                                   combination is already in the array
 *     *add                        - Adds the input game/channel combo to the array
 *     *remove                     - Removes the input game/channel combo from the array
 *     *getCurrentGameDescriptions - Gives a nice text output of the game/channel
 *                                   combinations in the array
 *      getGameIDX                 - Returns the game/chanel combinations index 
 *                                   value of its position in the array
 *     *isGameActive               - Returns true if the game is currently active, 
 *                                   returns false if the game is currently inactive, 
 *                                   if the game is inactive it adds it to the array 
 *                                   so subsequent searches show it as active
 * 
 * Note: Only commands marked with a * are available for use outside the object
 */

public class GameList {
    String[] info;
    ArrayList<String[]> games = new ArrayList<>();
    
    public boolean contains(String channel, String game){
        
        List gameSyn = Collections.synchronizedList(games);
        
        synchronized(gameSyn){
            Iterator<String[]> i = gameSyn.iterator();
            while (i.hasNext()){
                String[] tmp = i.next();
                if (tmp[0].equalsIgnoreCase(channel)&&tmp[1].equalsIgnoreCase(game)){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public void add(String channel, String game){
        games.add(new String[] {channel,game});
    }
    
    public ArrayList<String> getCurrentGameDescriptions(){
        List gameSyn = Collections.synchronizedList(games);
        ArrayList<String> descriptions = new ArrayList<>();
        synchronized(gameSyn){
            Iterator<String[]> i = gameSyn.iterator();
            while (i.hasNext()){
                String[] tmp = i.next();
                descriptions.add(Colors.BOLD+"Game: "+Colors.NORMAL+tmp[1]+Colors.BOLD+" Channel: "+Colors.NORMAL+tmp[0]);
            }
        }
        
        return(descriptions);
    }
    
    private int getGameIDX(String channel, String game){
        
        int idx = 0;
        List gameSyn = Collections.synchronizedList(games);
        
        synchronized(gameSyn){
            
            Iterator<String[]> i = gameSyn.iterator();
            
            while (i.hasNext()){
                
                String[] tmp = i.next();
                
                if (tmp[0].equalsIgnoreCase(channel)&&tmp[1].equalsIgnoreCase(game)){
                    return idx;
                }
                idx++;
            }
        }
        return -1;
    }
    
    public void remove(String channel, String game){
        int idx = getGameIDX(channel, game);
        if (idx>=0)
            games.remove(idx);
        else
            System.out.println("GAME NOT FOUND IN ARRAY");
    }
    
    public boolean isGameActive(String currentChan, String GameType) {
        boolean isActive=false;
        if (games.isEmpty()){
            add(currentChan, GameType);
        }
        else{
            isActive = contains(currentChan,GameType);
            if (!isActive){
                add(currentChan,GameType);
            }
        }
        return(isActive);
    }
}
