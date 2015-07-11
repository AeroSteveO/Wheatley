package Utils;

import Wheatley.Global;
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
    
    /**
     * Gets the appropriate string to send to a user if an exception is encountered.
     *
     * @param t Exception to format
     * @return Message to send user; never null
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static String formatException(Throwable t) {
        //noinspection ThrowableResultOfMethodCallIgnored
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
    public static String getStackTrace(Throwable t) {
        //noinspection ThrowableResultOfMethodCallIgnored
        notNull(t, "t was null");
        final StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
    /**
     * Pastes something to Pastebin.
     *
     * @param paste String to paste
     * @return Pastebin URL or null if error encountered
     * @throws java.lang.NullPointerException If any argument is null
     */
    static String pasteBinKey = "45c22303a7f1c7da1e9ec812f559c65b";
    public static String pastebin(String paste) {
        
        try {
            return Pastebin.pastePaste(pasteBinKey, paste, Global.mainNick + " EXCEPTION").toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String formatPastebinPost(Throwable t) {
        
        String stackURL = linkToStackTrace(t);
        String response = formatException(t);
        if (stackURL != null)
                response += (" (" + stackURL + ")");
        
        return response;
    }
    
    /**
     * Convenience method to get a stack trace from an Exception, send it to Hastebin, and shorten the resulting link
     * with is.gd.
     * <br/>
     * <strong>Note:</strong> If <em>any</em> errors occur, this will simply return null, and you will get no feedback
     * of the error.
     *
     * @param t Throwable to do this with
     * @return Shortened link to the stack trace or null
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static String linkToStackTrace(Throwable t) {
        //noinspection ThrowableResultOfMethodCallIgnored
        notNull(t, "");
        String pastebin = BotUtils.pastebin(BotUtils.getStackTrace(t));
        if (pastebin != null) {
            String url = null;
            try {
                url = BotUtils.shortenURL(pastebin);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            if (url != null) return url;
        }
        return null;
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
    
    /**
     * Shortens a URL with is.gd.
     *
     * @param url URL to shorten
     * @return Shortened URL
     * @throws IOException                    If an exception occurs encoding or shortening
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static String shortenURL(String url) throws IOException {
        notNull(url, "url was null");
        final URL shorten = new URL("http://is.gd/create.php?format=simple&url=" + URLEncoder.encode(url, "UTF-8"));
        System.out.println(shorten.toString());
        System.out.println(getContent(shorten.toString()));
        return getContent(shorten.toString());
    }
}