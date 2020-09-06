/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.objects.shorten;

import rapternet.irc.bots.common.utils.BotUtils;
import org.pircbotx.Colors;
import rapternet.irc.bots.common.utils.TextUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import static org.apache.commons.lang3.Validate.notNull;

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
            return shortenURL(url);
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * Shortens a URL with is.gd.
     *
     * @param url URL to shorten
     * @return Shortened URL
     * @throws IOException                    If an exception occurs encoding or shortening
     * @throws java.lang.NullPointerException If any argument is null
     */
    private static String shortenURL(String url) throws Exception {
        notNull(url, "url was null");
        final URL shorten = new URL("http://is.gd/create.php?format=simple&url=" + URLEncoder.encode(url, "UTF-8"));
        System.out.println(shorten.toString());
        System.out.println(TextUtils.readUrlUsingGet(shorten.toString()));
        return TextUtils.readUrlUsingGet(shorten.toString());
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
