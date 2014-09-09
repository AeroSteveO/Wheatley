/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *      Original Bot: SRSBSNS by: i dunno who
 *
 * Activate Command with:
 *      !lasturl
 *          Pulls up the last url seen in the current channel
 *      !secondlasturl
 *          Pulls up the second last url seen in the current channel
 *      !christmas
 *          Outputs the current number of days till Christmas
 *      !summon [user]
 *          Sends a PM to the user stating that they have been summoned
 *      !tell [user] [statement]
 *          Sends the given statement to the user via PM
 *      !srsbsns
 *          wat
 *
 */
public class SRSBSNS extends ListenerAdapter {
    List<String> UrlHistory = new ArrayList<>();
    
//    !whodef (who defined), !whatis (definition), !explain (definition),
//    !rt (rotten tomatoes movie rating),
//    !imdb (imdb movie search), !metacritic (metacritic.com rating),
    
    
//    !lasturl (analyzes the last url posted), !summon (person), !christmas (countdown to Christmas)
//    !udict (urban dictionary), !randomdef (random definition), !srsbsns (responds: wat), 
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String currentChan = event.getChannel().getName();
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("srsbsns")).contains(event.getChannel())) {
            // separete input by spaces ( URLs don't have spaces )
            String [] parts = message.split("\\s");
            // Attempt to convert each item into an URL.
            for( String item : parts ) try {
                URL url = new URL(item);
                // If possible then replace with anchor...
                Global.Channels.getChan(currentChan).setSecondLastUrl(Global.Channels.getChan(currentChan).getLastUrl());
                Global.Channels.getChan(currentChan).setLastUrl(item);
            } catch (MalformedURLException e) { // If exception happens, then its not a URL
            }
            if (message.equalsIgnoreCase("!lasturl")){
                if (!Global.Channels.getChan(currentChan).getLastUrl().equals(""))
                    event.getBot().sendIRC().action(currentChan,"Last URL: "+Global.Channels.getChan(currentChan).getLastUrl());
                else
                    event.getBot().sendIRC().action(currentChan,"No previous URL found");
            }
            if (message.equalsIgnoreCase("!secondlasturl")){
                if (!Global.Channels.getChan(currentChan).getSecondLastUrl().equals(""))
                    event.getBot().sendIRC().action(currentChan,"Second to last URL: "+Global.Channels.getChan(currentChan).getSecondLastUrl());
                else
                    event.getBot().sendIRC().action(currentChan,"Currently less than 2 URLs found");
            }
            if (message.equalsIgnoreCase("!srsbsns"))
                event.getBot().sendIRC().action(currentChan,"wat");
            
            if(message.equalsIgnoreCase("!christmas")) {
                GregorianCalendar now = new GregorianCalendar();
                GregorianCalendar christmas = new GregorianCalendar();
                christmas.set(Calendar.MONTH, 11);
                christmas.set(Calendar.DAY_OF_MONTH, 25);
                if(christmas.before(now)) {
                    christmas.add(Calendar.YEAR, 1);
                }
                else if(christmas.equals(now)) {
                    event.respond("It's Christmas!");
                }
                christmas.set(Calendar.HOUR_OF_DAY, 0);
                christmas.set(Calendar.MINUTE, 0);
                christmas.set(Calendar.SECOND, 0);
                long diff = (christmas.getTimeInMillis() - now.getTimeInMillis())/1000;
                long days = (diff/86400);
                diff %= 86400;
                long hours = diff/3600;
                diff %= 3600;
                long minutes = diff/60;
                diff %=60;
                long seconds = diff;
                event.respond(String.format("There are %d days, %d hours, %d minutes, and %d seconds until Christmas", days, hours, minutes, seconds));
            }
            //String message = event.getMessage().trim();
            if(message.matches("!summon\\s+[^\\s]+")) {
                String target = message.split("\\s+")[1];
                if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser(target))) {
                    //If the user is in the same channel as the summon
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!summon "+Colors.NORMAL+target+" has been PMed");
                    event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser(target).getNick(), Colors.BOLD+"!summon "+Colors.NORMAL+"you have been summoned by "+event.getUser().getNick()+" from "+event.getChannel().getName());
                }
                else {
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!summon "+Colors.NORMAL+"user not in channel");
                }
            }
        }
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("Hermes")).contains(event.getChannel())) {
            if(message.toLowerCase().startsWith("!tell")&&message.split(" ").length>2) {
                String target = message.split("\\s+")[1];
                String tell = message.split(target)[1];
                if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser(target))) {
                    //If the user is in the same channel as the summon
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!tell "+Colors.NORMAL+target+" has been PMed");
                    event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser(target).getNick(), event.getUser().getNick() + " wants me to tell you: "+tell);
                }
                else {
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!tell "+Colors.NORMAL+"user not in channel");
                }
            }
        }
    }
}
