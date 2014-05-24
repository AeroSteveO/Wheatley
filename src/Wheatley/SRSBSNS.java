/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import static Wheatley.TextModification.getColorList;
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
        
        // separete input by spaces ( URLs don't have spaces )
        String [] parts = message.split("\\s");
        // Attempt to convert each item into an URL.
        for( String item : parts ) try {  
            URL url = new URL(item);
            // If possible then replace with anchor...
            UrlHistory.add(item);
          //  event.getBot().sendIRC().action(event.getChannel().getName(),UrlHistory.get(UrlHistory.size()-1));
            if (UrlHistory.size()>2)
                UrlHistory.remove(0);
        } catch (MalformedURLException e) {
            // If there was an URL that was not it!...
            //System.out.print( item + " " );
        }
        if (message.equalsIgnoreCase("!lasturl")){
            if (UrlHistory.size()>0)
                event.getBot().sendIRC().action(event.getChannel().getName(),"Last URL: "+UrlHistory.get(UrlHistory.size()-1));
            else
                event.getBot().sendIRC().action(event.getChannel().getName(),"No previous URL found");
        }
        if (message.equalsIgnoreCase("!secondlasturl")){
            if (UrlHistory.size()>1)
                event.getBot().sendIRC().action(event.getChannel().getName(),"Second to last URL: "+UrlHistory.get(UrlHistory.size()-2));
            else
                event.getBot().sendIRC().action(event.getChannel().getName(),"Currently less than 2 URLs found");
        }
    }
}
