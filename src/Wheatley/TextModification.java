/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class TextModification extends ListenerAdapter{
    List<String> ColorList = getColorList();
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.toLowerCase().startsWith("!colortext")){
            event.getBot().sendIRC().message(event.getChannel().getName(), "this text is "+ Colors.BLUE + Colors.WHITE +",02blue on a white background");
            event.getBot().sendIRC().message(event.getChannel().getName(), "this text is "+ Colors.WHITE + Colors.BLUE +",02white on a blue background");
        }
        
    }
    public static List<String> getColorList() {
        List<String> a = new ArrayList<>();
        a.add(Colors.BLACK.toString());
        a.add(Colors.RED.toString());
        a.add(Colors.YELLOW.toString());
        a.add(Colors.GREEN.toString());
        a.add(Colors.BROWN.toString());
        a.add(Colors.BLUE.toString());
        a.add(Colors.CYAN.toString());
        a.add(Colors.MAGENTA.toString());
        a.add(Colors.DARK_BLUE.toString());
        a.add(Colors.WHITE.toString());
        a.add(Colors.DARK_GRAY.toString());
        a.add(Colors.LIGHT_GRAY.toString());
        a.add(Colors.DARK_GREEN.toString());
        a.add(Colors.OLIVE.toString());
        a.add(Colors.PURPLE.toString());
        a.add(Colors.TEAL.toString());

        
        a.add(Colors.NORMAL.toString());
        a.add(Colors.BOLD.toString());
        a.add(Colors.UNDERLINE.toString());
        a.add(Colors.REVERSE.toString());
        return (a);
    }
}
