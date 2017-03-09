/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects.Shorten;

import Utils.BotUtils;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Utilities
 *    BotUtils
 * - Linked Classes
 *    N/A
 * 
 * Object:
 *      IsGd
 * - Object that implements the link shortner interface, giving access to Is.Gd
 *   as a link shortening service
 * 
 */
public class IsGd extends ShortenerInterface {
    
    @Override
    public String shorten(String url) {
        try {
            return BotUtils.shortenURL(url);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    public boolean isShortIdentifier(String id) {
        return id.equalsIgnoreCase("i");
    }
    
    @Override
    public String getName() {
        return "is.gd";
    }
    
    @Override
    public String getInfo() {
        return (Colors.BOLD + "ID: " + Colors.NORMAL + "i " + Colors.BOLD + " Shortener: " + Colors.NORMAL + "is.gd");
    }
}
