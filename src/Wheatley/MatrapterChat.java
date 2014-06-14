/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 */
public class MatrapterChat extends ListenerAdapter {
    
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String currentChan = event.getChannel().getName();
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.equalsIgnoreCase("!penis"))
            event.getBot().sendIRC().message(currentChan, "8==D");
        if (message.equalsIgnoreCase("!boobs")||message.equalsIgnoreCase("!botd")||message.equalsIgnoreCase("!melons")){
            List<String> a = new ArrayList<>();
            a.add("( . ) ( . )");
            a.add("(oYo)");
            a.add("(.Y.)");
            a.add("（。 ㅅ  。）");
            a.add("（@Y@）");
            a.add("（•_ㅅ_•）");
            a.add("( × Y × )");
            event.getBot().sendIRC().message(currentChan,a.get((int) (Math.random()*a.size()-1)) );
        }
        if (message.equalsIgnoreCase("!meatlab"))
            event.getBot().sendIRC().message(currentChan, "u so funny, me ruv u rong time");
        if (message.toLowerCase().startsWith("!matlab")){
            if (message.split(" ").length==1)
                event.getBot().sendIRC().message(currentChan, "MATRABBB");
            else
                event.respond("This bot doesn't contain 100% of Matrapter's original capability yet");
        }
        
        //Meatpod Functions
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("meatpod")).contains(event.getChannel())) {
            if (message.equalsIgnoreCase("!meatpod")||message.equalsIgnoreCase("meatpod")||message.equalsIgnoreCase("meatpod?"))
                event.getBot().sendIRC().message(currentChan, "http://meatspin.cc");
            if (message.equalsIgnoreCase("fuck"))
                event.getBot().sendIRC().message(currentChan, "you");
        }
        
        //Bane
        //Idea for a random command similar to !Xzibit
        if (message.equalsIgnoreCase("!bane"))
            event.getBot().sendIRC().message(currentChan,"Ah you think darkness is your ally? You merely adopted the dark. I was born in it, molded by it. I didn't see the light until I was already a man, by then it was nothing to me but blinding!");
        
        if (message.toLowerCase().startsWith("!bane ")){
            String[] baneterm = message.split(" ");
            if (baneterm.length==2){
                ArrayList<String> baneparts = new ArrayList<String>();
                baneparts.add("Ah you think ");
                baneparts.add(" is your ally? You merely adopted the ");
                baneparts.add(". I was born in it, molded by it. I didn't see the light until I was already a man, by then it was nothing to me but blinding!");
                event.getBot().sendIRC().message(currentChan,baneparts.get(0)+baneterm[1]+baneparts.get(1)+baneterm[1]+baneparts.get(2));
            }
            else if (baneterm.length==3){
                ArrayList<String> baneparts = new ArrayList<String>();
                baneparts.add("Ah you think ");
                baneparts.add(" is your ally? You merely adopted the ");
                baneparts.add(". I was born in it, molded by it. I didn't see the ");
                baneparts.add(" until I was already a man, by then it was nothing to me but blinding!");
                event.getBot().sendIRC().message(currentChan,baneparts.get(0)+baneterm[1]+baneparts.get(1)+baneterm[1]+baneparts.get(2)+baneterm[2]+baneparts.get(3));
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"Bane input should be '!bane [term1] [term2]'");
        }
    }
}