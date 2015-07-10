package Utils;

import Wheatley.Global;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.pircbotx.User;
//import org.royaldev.royalbot.auth.Auth;
//import org.royaldev.royalbot.auth.AuthResponse;
//import org.royaldev.royalbot.commands.ChannelCommand;
//import org.royaldev.royalbot.commands.IRCCommand;
//import org.royaldev.royalbot.configuration.ConfigurationSection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Utility class for commonly-used methods to be used in the bot. All of the methods in this class throw a
 * {@link java.lang.NullPointerException} if any argument is null.
 */
public class BotUtils {
    
//    private final static ObjectMapper om = new ObjectMapper();
    
    private BotUtils() {} // this is a thing that should never be done
    
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
     * Pastes something to Hastebin.
     *
     * @param paste String to paste
     * @return Hastebin URL or null if error encountered
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static String pastebin(String paste) {
        System.out.println("Start trying to pastebin");
        
        notNull(paste, "paste was null");
        System.out.println("not null");
        
        CloseableHttpClient hc = HttpClients.createDefault();
        HttpPost hp = new HttpPost("http://hastebin.com/documents");
        
        System.out.println("http made");
        
        try {
            System.out.println("http set entity");
            hp.setEntity(new StringEntity(paste, "UTF-8"));
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
        HttpResponse hr;
        try {
            System.out.println("http execute");
            hr = hc.execute(hp);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        HttpEntity he = hr.getEntity();
        System.out.println("http got entity");
        if (he == null) return null;
        String json;
        
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(he.getContent()))) {
                json = br.readLine();
                System.out.println(json);
            } finally {
                hc.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        JSONObject jn;
        try {
            jn = (JSONObject) new JSONTokener(json).nextValue();
            json = jn.getString("key");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        
        return json.isEmpty() ? "faioledasdf" : "http://hastebin.com/" + json;
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
        System.out.println("entering pastebin link maker");
        notNull(t, "");
        String pastebin = BotUtils.pastebin(BotUtils.getStackTrace(t));
        System.out.println("Should have pastebin link");
        if (pastebin != null) {
            System.out.println("performing pastebin link shortener");
            pastebin += ".txt";
            String url = null;
            try {
                url = BotUtils.shortenURL(pastebin);
            } catch (Exception ignored) {
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
    
    /**
     * Gets a string to send a user if help is requested for a command.
     *
     * @param ic Command to get help for
     * @return String to send user
     * @throws java.lang.NullPointerException If any argument is null
     */
//    public static String getHelpString(IRCCommand ic) {
//        notNull(ic, "ic was null");
//        return ic.getName() + " / Description: " + ic.getDescription() + " / Usage: " + ic.getUsage().replaceAll("(?i)<command>", ic.getName()) + " / Aliases: " + Arrays.toString(ic.getAliases()) + " / Type: " + ic.getCommandType().getDescription();
//    }
    
    /**
     * Creates a channel-specific command based on JSON.
     *
     * @param commandJson JSON of command
     * @param channel     Channel for command
     * @return ChannelCommand
     * @throws RuntimeException               If there is any error
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static void createChannelCommand(String commandJson, final String channel) throws RuntimeException {
        notNull(commandJson, "commandJson was null");
        notNull(channel, "channel was null");
        final JSONObject jn;
        try {
            jn = (JSONObject) new JSONTokener(commandJson).nextValue();
        } catch (Exception ex) {
            String paste = BotUtils.linkToStackTrace(ex);
            throw new RuntimeException("An error occurred reading that!" + ((paste != null) ? " (" + paste + ")" : ""));
        }
        String name = "";
        String desc = "";
        String usage = "";
        String auth = "";
        String script = "";
        List<String> aliases = new ArrayList<>();
        
        try {
            name = jn.getString("name").trim();
            desc = jn.getString("description").trim();
            usage = jn.getString("usage").trim();
            auth = jn.getString("auth").trim();
            script = jn.getString("script").trim();
            aliases = new ArrayList<>();
            for (String alias : jn.getString("aliases").trim().split(",")) {
                alias = alias.trim();
                if (alias.isEmpty()) continue;
                aliases.add(alias + ":" + channel);
            }
            
        } catch (Exception ex) {
            
        }
        if (name.isEmpty() || desc.isEmpty() || usage.isEmpty() || auth.isEmpty() || script.isEmpty()) {
            throw new RuntimeException("Invalid JSON.");
        }
//        final IRCCommand.AuthLevel al;
        try {
//            al = IRCCommand.AuthLevel.valueOf(auth.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid auth level.");
        }
//        return new ChannelCommand() {
//            @Override
//            public String getBaseName() {
//                return name;
//            }
//
//            @Override
//            public String getChannel() {
//                return channel;
//            }
//
//            @Override
//            public String getJavaScript() {
//                return script;
//            }
//
//            @Override
//            public String getUsage() {
//                return usage;
//            }
//
//            @Override
//            public String getDescription() {
//                return desc;
//            }
//
//            @Override
//            public String[] getAliases() {
//                return aliases.toArray(new String[aliases.size()]);
//            }
//
//            @Override
//            public AuthLevel getAuthLevel() {
//                return al;
//            }
//        };
    }
    
    /**
     * Checks if a hostmask matches a pattern. This replaces "*" with ".+" prior to checking, and it does use regex, as
     * one would assume.
     *
     * @param hostmask     Hostmask of a user (regex, * = .+)
     * @param checkAgainst Hostmask pattern to check against
     * @return true if hostmask matches checkAgainst, false if otherwise
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static boolean doesHostmaskMatch(String hostmask, String checkAgainst) {
        notNull(hostmask, "hostmask was null");
        notNull(checkAgainst, "checkAgainst was null");
        checkAgainst = checkAgainst.replace("*", ".+");
        return hostmask.matches(checkAgainst);
    }
    
    /**
     * Checks to see if a hostmask is ignored by the bot.
     *
     * @param hostmask Hostmask to check
     * @param ignores  List of hostmasks to check against
     * @return true if hostmask is ignored, false if not
     * @throws java.lang.NullPointerException If any argument is null
     */
//    public static boolean isIgnored(String hostmask, List<String> ignores) {
//        notNull(hostmask, "hostmask was null");
//        notNull(ignores, "ignores was null");
//        for (String ignore : ignores) {
//            if (ignore.equals(hostmask)) return true;
//            if (doesHostmaskMatch(hostmask, ignore)) return true;
//        }
//        return false;
//    }
    
    /**
     * Checks to see if a hostmask is ignored by the bot.
     *
     * @param hostmask Hostmask to check
     * @return true if hostmask is ignored, false if not
     * @throws java.lang.NullPointerException If any argument is null
     */
//    public static boolean isIgnored(String hostmask) {
//        notNull(hostmask, "hostmask was null");
//        return isIgnored(hostmask, RoyalBot.getInstance().getConfig().getIgnores());
//    }
    
    /**
     * Temporary workaround for PircBotX not returning the right value for {@link org.pircbotx.User#getHostmask()}.
     *
     * @param user User to get hostmask of
     * @return Real hostmask
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static String generateHostmask(User user) {
        notNull(user, "user was null");
        return user.getNick() + "!" + user.getLogin() + "@" + user.getHostmask();
    }
    
    /**
     * Checks to see if a User is authorized in a channel or is a superadmin.
     *
     * @param u    User to check
     * @param chan Channel of user
     * @return true if authorized or superadmin, false if not
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static boolean isAuthorized(User u, String chan) {
        notNull(u, "u was null");
        notNull(chan, "chan was null");
//        AuthResponse ar = Auth.checkAuth(u);
        boolean loggedIn = u.isVerified();
        boolean isChanOp = u.getChannelsOpIn().contains(u.getBot().getUserChannelDao().getChannel(chan));
        boolean isSuperAdmin = Global.botOwner.equalsIgnoreCase(u.getNick());
        return (loggedIn && isChanOp) || isSuperAdmin;
    }
    
    /**
     * Flips a string upside-down in accordance to the table from the configuration.
     *
     * @param toFlip String to flip
     * @return Flipped string
     * @throws java.lang.NullPointerException If any argument is null
     */
//    public static String flip(String toFlip) {
//        notNull(toFlip, "toFlip was null");
//        final ConfigurationSection flips = RoyalBot.getInstance().getConfig().getFlipTable();
//        final StringBuilder sb = new StringBuilder();
//        for (char c : toFlip.toCharArray()) {
//            final String ch = String.valueOf(c);
//            sb.append(flips.getString(ch, ch));
//        }
//        return sb.toString();
//    }
    
    /**
     * Gets an array of all indices of a string.
     *
     * @param string Search string
     * @param of     Delimiter
     * @return Array of ints
     * @throws java.lang.NullPointerException If any argument is null
     */
    public static int[] indicesOf(String string, String of) {
        notNull(string, "string was null");
        notNull(of, "of was null");
        List<Integer> indices = new ArrayList<>();
        for (int i = string.indexOf(of); i >= 0; i = string.indexOf(of, i + 1)) indices.add(i);
        return ArrayUtils.toPrimitive(indices.toArray(new Integer[indices.size()]));
    }
    
    /**
     * Returns a string of less than or equal to the maximum length provided. If the provided string is more than the
     * maximum length, the rest of the contents will be removed.
     *
     * @param text      Text to truncate
     * @param maxLength Maximum length to allow; must be 0+
     * @return Truncated string
     * @throws java.lang.IllegalArgumentException If maxLength is less than zero
     * @throws java.lang.NullPointerException     If any argument is null
     */
    public static String truncate(String text, int maxLength) {
        notNull(text, "text was null");
        if (maxLength < 0) throw new IllegalArgumentException("maxLength was less than zero");
        return text.length() < maxLength ? text : text.substring(0, maxLength);
    }
    
}
