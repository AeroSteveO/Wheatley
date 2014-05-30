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
 */
public class SRSBSNS extends ListenerAdapter {
    List<String> UrlHistory = new ArrayList<>();
    
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("srsbsns")).contains(event.getChannel())) {
            // separete input by spaces ( URLs don't have spaces )
            String [] parts = message.split("\\s");
            // Attempt to convert each item into an URL.
            for( String item : parts ) try {
                URL url = new URL(item);
                // If possible then replace with anchor...
                Global.Channels.get(getChanIdx(event.getChannel().getName().toString())).secondLastUrl = Global.Channels.get(getChanIdx(event.getChannel().getName().toString())).lastUrl;
                Global.Channels.get(getChanIdx(event.getChannel().getName().toString())).lastUrl = item;
//                event.getBot().sendIRC().action(event.getChannel().getName(),item);
//                if (UrlHistory.size()>2)
//                    UrlHistory.remove(0);
            } catch (MalformedURLException e) {
                // If there was an URL that was not it!...
                //System.out.print( item + " " );
            }
            if (message.equalsIgnoreCase("!lasturl")){
                if (!Global.Channels.get(getChanIdx(event.getChannel().getName().toString())).lastUrl.equals(""))
                    event.getBot().sendIRC().action(event.getChannel().getName(),"Last URL: "+Global.Channels.get(getChanIdx(event.getChannel().getName().toString())).lastUrl);
                else
                    event.getBot().sendIRC().action(event.getChannel().getName(),"No previous URL found");
            }
            if (message.equalsIgnoreCase("!secondlasturl")){
                if (!Global.Channels.get(getChanIdx(event.getChannel().getName().toString())).secondLastUrl.equals(""))
                    event.getBot().sendIRC().action(event.getChannel().getName(),"Second to last URL: "+Global.Channels.get(getChanIdx(event.getChannel().getName().toString())).secondLastUrl);
                else
                    event.getBot().sendIRC().action(event.getChannel().getName(),"Currently less than 2 URLs found");
            }
        }
    }
    public int getChanIdx(String toCheck){
        int idx = -1;
        for(int i = 0; i < Global.Channels.size(); i++) {
            if (Global.Channels.get(i).name.equalsIgnoreCase(toCheck)) {
                idx = i;
                break;
            }
        }
        return (idx);
    }
}
