/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
 *          pulls up the last url seen in the current channel
 *      !secondlasturl
 *          pulls up the second last url seen in the current channel
 *
 */
public class SRSBSNS extends ListenerAdapter {
    List<String> UrlHistory = new ArrayList<>();
    
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
                Global.Channels.getChan(currentChan).secondLastUrl = Global.Channels.getChan(currentChan).lastUrl;
                Global.Channels.getChan(currentChan).lastUrl = item;
            } catch (MalformedURLException e) {
                // If there was an URL that was not it!...
                //System.out.print( item + " " );
            }
            if (message.equalsIgnoreCase("!lasturl")){
                if (!Global.Channels.getChan(currentChan).lastUrl.equals(""))
                    event.getBot().sendIRC().action(currentChan,"Last URL: "+Global.Channels.getChan(currentChan).lastUrl);
                else
                    event.getBot().sendIRC().action(currentChan,"No previous URL found");
            }
            if (message.equalsIgnoreCase("!secondlasturl")){
                if (!Global.Channels.getChan(currentChan).secondLastUrl.equals(""))
                    event.getBot().sendIRC().action(currentChan,"Second to last URL: "+Global.Channels.getChan(currentChan).secondLastUrl);
                else
                    event.getBot().sendIRC().action(currentChan,"Currently less than 2 URLs found");
            }
        }
    }
}
