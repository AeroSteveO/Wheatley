/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Objects.Shorten;

import Utils.BotUtils;
import java.io.IOException;

/**
 *
 * @author Stephen
 */
public class IsGd implements ShortenerInterface {
    
    @Override
    public String shorten(String url) {
        try {
        return BotUtils.shortenURL(url);
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public boolean isShortIdentifier(String id) {
        return id.equalsIgnoreCase("i");
    }
    public String getShortID() {
        return "i";
    }
}
