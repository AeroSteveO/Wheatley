/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects.Shorten;

import Utils.TextUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.pircbotx.Colors;


/**
 *
 * @author Stephen
 */
public class DtellaShorten extends ShortenerInterface {
    private final String USER_AGENT = "Mozilla/5.0";
    private final String key = "3e7246e70c";
    @Override
    public String shorten(String url) {
        try {
            String json= TextUtils.readUrl("http://dtl.la/yourls-api.php?signature="+key+"&action=shorturl&url="+url+"&format=json");
            JSONObject defObject = (JSONObject) new JSONTokener(json).nextValue();
            String link = null;
            if (!defObject.isNull("shorturl")) {
                
                link = defObject.getString("shorturl");
                
//                JSONObject data = defObject.getJSONObject("data");
//                JSONArray link_lookup = data.getJSONArray("link_lookup");
                
                if (defObject.getString("status").equalsIgnoreCase("error")) {
                    return null;
                }
                else {
                    return link;
                }
            }
            else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getName() {
        return "Dtl.la";
    }
    /*
    <burg> [16:55:40] user Steve-O 
<burg> [16:55:46] pass HAISTEVE-O!
    
    */
    @Override
    public boolean isShortIdentifier(String id) {
        return id.equalsIgnoreCase("d");
    }
    
    @Override
    public String getInfo() {
        return (Colors.BOLD + "ID: " + Colors.NORMAL + "d " + Colors.BOLD + " Shortener: " + Colors.NORMAL + "Dtl.la");
    }
}
