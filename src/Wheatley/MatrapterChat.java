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
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.equalsIgnoreCase("!penis"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "8==D");
        if (message.equalsIgnoreCase("!boobs")||message.equalsIgnoreCase("!botd")){
            List<String> a = new ArrayList<>();
            a.add("( . ) ( . )");
            a.add("(oYo)");
            a.add("(.Y.)");
            a.add("（。 ㅅ  。）");
            a.add("（@ㅅ@）");
            a.add("（•_ㅅ_•）");
            event.getBot().sendIRC().message(event.getChannel().getName(),a.get((int) (Math.random()*a.size()-1)) );
        }
        if (message.equalsIgnoreCase("!meatlab"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "u so funny, me ruv u rong time");
        if (message.equalsIgnoreCase("!matlab"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "MATRABBB");
        
        //Meatpod Functions
        if (message.equalsIgnoreCase("!meatpod")||message.equalsIgnoreCase("meatpod")||message.equalsIgnoreCase("meatpod?"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "http://meatspin.cc");
        if (message.equalsIgnoreCase("fuck"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "you");
    }
}
