package rapternet.irc.bots.common.utils;

import rapternet.irc.bots.wheatley.listeners.Global;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;

import static org.apache.commons.lang3.Validate.notNull;
import org.jpaste.pastebin.Pastebin;

/**
 * Utility class for commonly-used methods to be used in the bot. All of the methods in this class throw a
 * {@link java.lang.NullPointerException} if any argument is null.
 */
public class BotUtils {
    
    public static String getClassName(Object obj) {
        return obj.getClass().getSimpleName();
    }
    
    /**
     * Gets the appropriate string to send to a user if an exception is encountered.
     *
     * @param t Exception to format
     * @return Message to send user; never null
     * @throws java.lang.NullPointerException If any argument is null
     */
    private static String formatException(Throwable t) {
        notNull(t, "t was null");
        return "Exception! " + t.getClass().getSimpleName() + ": " + t.getMessage();
    }
    
    /**
     * Converts a Throwable's stack trace into a String.
     *
     * @param t Throwable
     * @return Stack trace as string
     * @throws java.lang.NullPointerException If any argument is null
     */
    private static String getStackTrace(Throwable t) {
        final StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
    /**
     * Pastes something to Pastebin.
     * https://github.com/BrianBB/jPastebin
     * @param paste String to paste
     * @return Pastebin URL or null if error encountered
     * @throws java.lang.NullPointerException If any argument is null
     */
    private static String pastebin(String paste) {
        String pasteBinKey = Global.settings.get("pastebin-api");
        try {
            return Pastebin.pastePaste(pasteBinKey, paste, Global.mainNick + " EXCEPTION").toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String formatPastebinPost(Throwable t) {
        String pastebin = pastebin(getStackTrace(t));
        String stackURL = null;

        String response = formatException(t);
        if (pastebin != null)
                response += (" (" + pastebin + ")");
        
        return response;
    }
    
    /**
     * Gets the contents of an external URL.
     *
     * @param url URL to get contents of
     * @return Contents
     * @throws IOException
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static String getContent(String url) throws IOException {
        notNull(url, "url was null");
        final URL u = new URL(url);
        final BufferedReader br = new BufferedReader(new InputStreamReader(u.openConnection().getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line).append("\n");
        return sb.substring(0, sb.length() - 1); // remove last newline
    }
}
