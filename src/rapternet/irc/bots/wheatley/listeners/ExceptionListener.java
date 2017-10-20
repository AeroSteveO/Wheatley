/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapternet.irc.bots.wheatley.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import static org.apache.commons.lang3.Validate.notNull;
import org.jpaste.pastebin.Pastebin;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ExceptionEvent;
import org.pircbotx.hooks.events.ListenerExceptionEvent;

/**
 *
 * @author Stephen
 */
public class ExceptionListener extends ListenerAdapter {

  @Override
  public void onException(ExceptionEvent event) throws Exception {
    onException(event.getException());
  }

  @Override
  public void onListenerException(ListenerExceptionEvent event) throws Exception {
    onException(event.getException());
  }
  static String pasteBinKey = "";

  public ExceptionListener() {
    if (!Global.settings.contains("pastebin-api")) {
      Global.settings.create("pastebin-api", "AddHere");
      System.out.println("ERROR: NO Pastebin API KEY");
    }
    pasteBinKey = Global.settings.get("pastebin-api");

  }

  public void onException(Throwable thrwbl) {
    if (thrwbl instanceof IrcException || thrwbl instanceof IOException) {
      thrwbl.printStackTrace();
    } else {
      try {
        if (Global.bot.isConnected()) {
          Global.bot.sendIRC().message("#dtella2.0", formatPastebinPost(thrwbl));
        }
        thrwbl.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static String formatPastebinPost(Throwable t) {

    String stackURL = linkToStackTrace(t);
    String response = "Exception! " + t.getClass().getSimpleName() + ": " + t.getMessage();
    if (stackURL != null) {
      response += (" (" + stackURL + ")");
    }

    return response;
  }

  public static String linkToStackTrace(Throwable t) {
    //noinspection ThrowableResultOfMethodCallIgnored
    notNull(t, "");
    final StringWriter sw = new StringWriter();
    t.printStackTrace(new PrintWriter(sw));

    String pastebin = pastebin(sw.toString());
    if (pastebin != null) {
      String url = null;
      try {
        url = shortenURL(pastebin);
      } catch (Exception ignored) {
        ignored.printStackTrace();
      }
      if (url != null) {
        return url;
      }
    }
    return null;
  }
  private static final String key = "3e7246e70c";

  public static String shortenURL(String url) throws IOException {
    try {
      String json = readUrl("http://dtl.la/yourls-api.php?signature=" + key + "&action=shorturl&url=" + url + "&format=json");
      JSONObject defObject = (JSONObject) new JSONTokener(json).nextValue();
      String link;
      if (!defObject.isNull("shorturl")) {

        link = defObject.getString("shorturl");
        if (defObject.getString("status").equalsIgnoreCase("error")) {
          return null;
        } else {
          return link;
        }
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String readUrl(String urlString) throws Exception {
    BufferedReader reader = null;
    try {
      URL url = new URL(urlString);
      reader = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder buffer = new StringBuilder();
      int read;
      char[] chars = new char[1024];
      while ((read = reader.read(chars)) != -1) {
        buffer.append(chars, 0, read);
      }
      return buffer.toString();
    } catch (IOException ex) {
      ex.printStackTrace();
      return (null);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  public static String pastebin(String paste) {
    try {
      return Pastebin.pastePaste(pasteBinKey, paste, Global.mainNick + " EXCEPTION").toString();
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static String getContent(String url) throws IOException {
    notNull(url, "url was null");
    final URL u = new URL(url);
    final BufferedReader br = new BufferedReader(new InputStreamReader(u.openConnection().getInputStream()));
    final StringBuilder sb = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line).append("\n");
    }
    return sb.substring(0, sb.length() - 1); // remove last newline
  }

}
