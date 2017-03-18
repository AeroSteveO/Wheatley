/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package Wheatley;

import Objects.MapArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 *
 * Requirements: - APIs N/A - Custom Objects MapArray - Linked Classes Global
 *
 * Activate Commands with: s/replaceThis/replaceWithThis Replaces the text
 * replaceThis with the text replaceWithThis, using a log of previous messages
 * !bf !bf [nick] Reverses the word order in the previous line said, or the
 * previous line said by the input nickname !bff !bff [nick] Reverses all the
 * letters in the previous line said, or the previous line said by the input
 * nickname !bfff !bfff [nick] Reverses all the words, and all the letters in
 * all the words, of the previous line said, or the previous line said by the
 * input nickname
 *
 */
public class Swapper extends ListenerAdapter {
//    Map<String,ArrayList<ArrayList<String>>> log = Collections.synchronizedMap(new TreeMap<String,ArrayList<ArrayList<String>>>());

  public static MapArray logger = new MapArray(100);

  @Override
  public void onMessage(MessageEvent event) {

    String message = Colors.removeFormattingAndColors(event.getMessage());
    String channel = event.getChannel().getName();
    addToLog(channel, new ArrayList(Arrays.asList("<" + event.getUser().getNick() + ">", event.getMessage())));

    // PROCESS s// commands and sed type commands
    if (message.toLowerCase().startsWith("sw/") || ((message.toLowerCase().startsWith("s/") || message.toLowerCase().startsWith("sed/")) && ((event.getBot().getUserChannelDao().containsUser("BlarghleBot") && !event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel())) || !event.getBot().getUserChannelDao().containsUser("BlarghleBot")))) {
//            if(logger.isEmpty(channel)) {
//                event.getBot().sendIRC().message(event.getChannel().getName(), "Swap Log Empty");
//                return;
//            }
//            if (message.endsWith("/"))
//                message+="";

      ArrayList<ArrayList<String>> logCopy = logger.getArray(channel);
      if (logCopy == null || logCopy.isEmpty()) { // if log is empty, no output is available
        event.getBot().sendIRC().notice(event.getUser().getNick(), "s// log empty");
        return;
      }
      String[] findNreplace = Colors.removeFormattingAndColors(message).split("/");

      if (findNreplace.length != 3) { // input of "s/anything/" will have a blank string added to the end, string.split does not do this.
        findNreplace = new String[]{findNreplace[0], findNreplace[1], ""};
      }

      int i = logCopy.size() - 2;

      ArrayList<String> reply = findReplace(i, findNreplace, logCopy);

      if (reply.size() == 2 && !reply.get(1).equalsIgnoreCase("")) {
        event.getBot().sendIRC().message(event.getChannel().getName(), reply.get(0) + " " + reply.get(1).substring(0, Math.min(reply.get(1).length(), 400)));
        addToLog(channel, reply);
      }
    }
    /*
        [Thread-0] INFO org.pircbotx.InputParser - :BODpc!BODpc@dtl-2f162k.com PRIVMSG #dtella :sw/\/
java.util.regex.PatternSyntaxException: Unexpected internal error near index 1
\
 ^
        at java.util.regex.Pattern.error(Pattern.java:1924)
        at java.util.regex.Pattern.compile(Pattern.java:1671)
        at java.util.regex.Pattern.<init>(Pattern.java:1337)
        at java.util.regex.Pattern.compile(Pattern.java:1022)
        at Wheatley.Swapper.findReplace(Swapper.java:184)
        at Wheatley.Swapper.onMessage(Swapper.java:72)
        at org.pircbotx.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:63)
        at org.pircbotx.hooks.managers.ThreadedListenerManager$1.call(ThreadedListenerManager.java:119)
        at org.pircbotx.hooks.managers.ThreadedListenerManager$1.call(ThreadedListenerManager.java:115)
        at java.util.concurrent.FutureTask.run(FutureTask.java:262)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
        at java.lang.Thread.run(Thread.java:745)
     */
    if (message.startsWith(Global.commandPrefix) && !message.matches("([ ]{0,}" + Global.commandPrefix + "{1,}[ ]{0,}){1,}")) {

      String command = message.split(Global.commandPrefix)[1];
      String[] cmdSplit = command.split(" ");

      if (cmdSplit[0].toLowerCase().matches("b[f]{1,}")) {
        if (cmdSplit.length == 2) {
          String nick = cmdSplit[1];
          ArrayList<ArrayList<String>> logCopy = logger.getArray(channel);

          int i = logCopy.size() - 2;
          boolean found = false;
          String line = new String();
          if (logCopy == null || logCopy.isEmpty()) {
            event.getBot().sendIRC().notice(event.getUser().getNick(), "!BFF log empty");
            return;
          }
          while (!found && i >= 0) {
            if (logCopy.get(i).get(0).replaceAll("(<|>)", "").equalsIgnoreCase(nick)) {
              found = true;
              nick = logCopy.get(i).get(0);
              line = logCopy.get(i).get(1);
            }
            i--;
          }
          if (!found) {
            event.getBot().sendIRC().notice(event.getUser().getNick(), "!BF nick not found in log");
          } else if (cmdSplit[0].equalsIgnoreCase("bf")) {
            addToLog(channel, new ArrayList(Arrays.asList(nick, reverseWords(line))));
            event.getBot().sendIRC().message(event.getChannel().getName(), nick + " " + reverseWords(line));
          } else if (cmdSplit[0].equalsIgnoreCase("bff")) {
            addToLog(channel, new ArrayList(Arrays.asList(nick, reverseAllLetters(line))));
            event.getBot().sendIRC().message(event.getChannel().getName(), nick + " " + reverseAllLetters(line));
          } else {
            addToLog(channel, new ArrayList(Arrays.asList(nick, reverseLettersAndWords(line))));
            event.getBot().sendIRC().message(event.getChannel().getName(), nick + " " + reverseLettersAndWords(line));
          }
        } else if (cmdSplit.length > 2) {
          event.getBot().sendIRC().notice(event.getUser().getNick(), "!BF may take a nickname as input, or no input at all");
        } else {
          ArrayList<ArrayList<String>> logCopy = logger.getArray(channel);

          if (logCopy == null || logCopy.isEmpty() || logCopy.size() < 2) {
            event.getBot().sendIRC().notice(event.getUser().getNick(), "!BF log empty");
            return;
          }

          String nick = logCopy.get(logCopy.size() - 2).get(0);
          String line = logCopy.get(logCopy.size() - 2).get(1);

          if (cmdSplit[0].equalsIgnoreCase("bf")) {
            addToLog(channel, new ArrayList(Arrays.asList(nick, reverseWords(line))));
            event.getBot().sendIRC().message(event.getChannel().getName(), nick + " " + reverseWords(line));
          } else if (cmdSplit[0].equalsIgnoreCase("bff")) {
            addToLog(channel, new ArrayList(Arrays.asList(nick, reverseAllLetters(line))));
            event.getBot().sendIRC().message(event.getChannel().getName(), nick + " " + reverseAllLetters(line));
          } else {
            addToLog(channel, new ArrayList(Arrays.asList(nick, reverseLettersAndWords(line))));
            event.getBot().sendIRC().message(event.getChannel().getName(), nick + " " + reverseLettersAndWords(line));
          }
        }
      }
    }

  }

  private String reverseWords(String line) {
    String[] words = line.split(" ");
    String newLine = "";
    for (int c = words.length - 1; c >= 0; c--) {
      newLine += words[c] + " ";
    }
    return newLine;
  }

  private String reverseAllLetters(String line) {
    String reverse = "";
    int length = line.length();

    for (int i = length - 1; i >= 0; i--) {
      reverse = reverse + line.charAt(i);
    }

    return reverse;
  }

  private String reverseLettersAndWords(String line) {
    String reversedLine = reverseWords(line);
    String[] words = reversedLine.split(" ");
    String newLine = "";

    for (int i = 0; i < words.length; i++) {
      words[i] = reverseAllLetters(words[i]);
    }

    for (int c = words.length - 1; c >= 0; c--) {
      newLine += words[c] + " ";
    }
    return newLine;
  }

  /**
   * Searches through the log until it finds a match to the regex. If the match
   * is in the form of another swap styled string, then continue looking back
   * through the log with the new regex from the swap string.
   * 
   * @param i Number of items left in the log file that have yet to be looked at.
   * @param findNreplace Array containing the swap data {s, input, output}
   * @param logCopy the log to search through
   * @return List containing the output response, {Nick, Message}.
   */
  private ArrayList<String> findReplace(int i, String[] findNreplace, ArrayList<ArrayList<String>> logCopy) {
    ArrayList<String> reply = new ArrayList<>();
    Boolean found = false;
    Pattern findThis = Pattern.compile(findNreplace[1]);

    while (i >= 0 && !found) {
      try {
        if (findThis.matcher(logCopy.get(i).get(1)).find()) {

          // DO THE SWAPPING
          reply = new ArrayList(Arrays.asList(logCopy.get(i).get(0), logCopy.get(i).get(1).replaceAll(findNreplace[1], findNreplace[2])));

          // if the string is another swapper string, need to work on it,
          // and continue recursively through the log.
          if (reply.get(1).startsWith("sw/") || reply.get(1).startsWith("s/") || reply.get(1).startsWith("sed/")) {
            i--;
            
            findNreplace = reply.get(1).split("/"); // split up the swapper string
            if (findNreplace.length != 3) { // if the length isn't 3, add on an empty string to make the swapper remove words
              findNreplace = new String[]{findNreplace[0], findNreplace[1], ""};
            }
            // Found another swapper string, continue our recursive track
            // through the log.
            reply = findReplace(i, findNreplace, logCopy);
          }

          found = true;
        }
        i--;
      } catch (Exception ex) {
        // Don't care about the exceptions, just don't want to break from them.
      }
    }
    for (int j = 0; j < reply.size(); j++) {
      reply.set(j, reply.get(j).trim().replaceAll(" +", " "));
    }
    return (reply);
  }

  @Override
  public void onAction(ActionEvent event) {
    addToLog(event.getChannel().getName(), new ArrayList(Arrays.asList("* " + event.getUser().getNick(), event.getAction())));
  }

  @Override
  public void onKick(KickEvent event) {
    addToLog(event.getChannel().getName(), new ArrayList(Arrays.asList("* " + event.getUserHostmask().getNick(), "has kicked " + event.getRecipient().getNick() + " from " + event.getChannel().getName() + " (" + event.getReason() + ")")));
  }

//    @Override
//    public void onNotice(NoticeEvent event) {
//        addToLog(event.getChannel().getName(), new ArrayList(Arrays.asList("-"+event.getUser().getNick()+"-",event.getNotice())));
//    }
  private void addToLog(String channel, ArrayList<String> message) {
    logger.addToLog(channel, message);
  }
}
