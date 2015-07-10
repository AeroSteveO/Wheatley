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

/**
 *
 * @author Stephen
 */
public class Bitly implements ShortenerInterface {
    String bitlyKey = "165beb4666880ab9f846c5f81702eded052afae8";
    
    @Override
    public String shorten (String url) {
        String shortenedURL = new String();
        try {
            String link = ("https://api-ssl.bitly.com/v3/link/lookup?url=" + URLEncoder.encode(url, "UTF-8") + "&access_token=" + bitlyKey);
            String json = TextUtils.readUrl(link);
            
            JSONObject defObject = (JSONObject) new JSONTokener(json).nextValue();
            JSONObject data = defObject.getJSONObject("data");
            JSONArray link_lookup = data.getJSONArray("link_lookup");
            
            shortenedURL = link_lookup.getJSONObject(0).getString("aggregate_link");
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        
        return shortenedURL;
    }
    
    @Override
    public boolean isShortIdentifier(String id) {
        return id.equalsIgnoreCase("b");
    }
    public String getShortID() {
        return "b";
    }
}
