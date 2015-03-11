/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Utils;

import Wheatley.Global;
import org.json.JSONException;

/**
 *
 * @author Stephen
 */
public class GameUtils {
    public static boolean areGamesBlocked(String channel) {
//        try{
        if (Global.settings.contains("areGamesBlocked",channel)){
            return(Boolean.parseBoolean(Global.settings.get("areGamesBlocked",channel)));
        }
        else if (!Global.settings.contains("areGamesBlocked",channel)){
            Global.settings.create("areGamesBlocked","true",channel);
            return true;
        }
        else{
            return false;
        }
        
//        }
//        catch (Exception ex){
//            ex.printStackTrace();
//            return(false);
//        }
        
    }
}
