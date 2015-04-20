/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Utils;

import Wheatley.Global;

/**
 *
 * @author Stephen
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    Global
 *
 * Methods:
 *     *areGamesBlocked - Checks the global bot settings to see if games are blocked in the input channel
 *
 * Note: Only commands marked with a * are available for use outside the object 
 * 
 */
public class GameUtils {
    public static boolean areGamesBlocked(String channel) {
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
    }
}
