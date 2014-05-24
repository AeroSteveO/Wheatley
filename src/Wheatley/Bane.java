/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *      Idea for a random command similar to !Xzibit
 */
public class Bane extends ListenerAdapter{
    public void onMessage(MessageEvent event) {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.equalsIgnoreCase("!bane"))
            event.getBot().sendIRC().message(event.getChannel().getName(),"Ah you think darkness is your ally? You merely adopted the dark. I was born in it, molded by it. I didn't see the light until I was already a man, by then it was nothing to me but blinding!");
        
        if (message.toLowerCase().startsWith("!bane ")){
            String[] baneterm = message.split(" ");
            if (baneterm.length==2){
                ArrayList<String> baneparts = new ArrayList<String>();
                baneparts.add("Ah you think ");
                baneparts.add(" is your ally? You merely adopted the ");
                baneparts.add(". I was born in it, molded by it. I didn't see the light until I was already a man, by then it was nothing to me but blinding!");
                event.getBot().sendIRC().message(event.getChannel().getName(),baneparts.get(0)+baneterm[1]+baneparts.get(1)+baneterm[1]+baneparts.get(2));
            }
            else if (baneterm.length==3){
                ArrayList<String> baneparts = new ArrayList<String>();
                baneparts.add("Ah you think ");
                baneparts.add(" is your ally? You merely adopted the ");
                baneparts.add(". I was born in it, molded by it. I didn't see the ");
                baneparts.add(" until I was already a man, by then it was nothing to me but blinding!");
                event.getBot().sendIRC().message(event.getChannel().getName(),baneparts.get(0)+baneterm[1]+baneparts.get(1)+baneterm[1]+baneparts.get(2)+baneterm[2]+baneparts.get(3));
            }
            else
                event.getBot().sendIRC().message(event.getChannel().getName(),"Bane input should be '!bane [term1] [term2]'");
        }
    }  
}
