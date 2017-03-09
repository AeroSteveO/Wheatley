/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects.Shorten;

import Utils.TextUtils;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 * 
 * Requirements:
 * - APIs
 *    AOSP JSON
 * - Custom Objects
 *    N/A
 * - Utilities
 *    TextUtils
 * - Linked Classes
 *    N/A
 * 
 * Object:
 *      Bitly
 * - Object that implements the link shortner interface, giving access to Bit.Ly
 *   as a link shortening service
 * 
 */
public class Bitly extends ShortenerInterface {
    String bitlyKey = "165beb4666880ab9f846c5f81702eded052afae8";
    
    @Override
    public String shorten (String url) {
        String json;
        try {
            String link = ("https://api-ssl.bitly.com/v3/link/lookup?url=" + URLEncoder.encode(url, "UTF-8") + "&access_token=" + bitlyKey);
            json = TextUtils.readUrl(link);
            JSONObject defObject = (JSONObject) new JSONTokener(json).nextValue();
            
            if (!defObject.isNull("data")) {
                JSONObject data = defObject.getJSONObject("data");
                JSONArray link_lookup = data.getJSONArray("link_lookup");
                
                if (link_lookup.getJSONObject(0).has("error")) {
                    return null;
                }
                else
                    return link_lookup.getJSONObject(0).getString("aggregate_link");
            }
            else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getName() {
        return "Bit.ly";
    }
    
    @Override
    public boolean isShortIdentifier(String id) {
        return id.equalsIgnoreCase("b");
    }
    
    @Override
    public String getInfo() {
        return (Colors.BOLD + "ID: " + Colors.NORMAL + "b " + Colors.BOLD + " Shortener: " + Colors.NORMAL + "Bit.ly");
    }
}
