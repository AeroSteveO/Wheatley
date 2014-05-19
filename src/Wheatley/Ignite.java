/*
* BURN    Provides quick ways to light any input on fire.
*      BURN, by itself, burns 'it'.
*      BURN(it) burns the provided object.
* Please embellish or modify this function to suit your own tastes.
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
 * original bot = Matrapter
 * matlab based IRC bot written by Steve-O
 *
 */
public class Ignite extends ListenerAdapter {
    @Override
    public void onMessage(MessageEvent event) {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith("!ignite")){
            String it;
            String[] check = message.split(" ");
            if (check.length!=2){
                it = "it";
            }
            else {
                it = check[1];
            }
            String chat = simple_front()+ " " + it + " " + simple_end();
            event.getBot().sendIRC().message(event.getChannel().getName(), chat.toUpperCase());
        }
    }
    public static String simple_front() {
        List<String> a = new ArrayList<>();
        a.add("burn");
        a.add("bake");
        a.add("broil");
        a.add("roast");
        a.add("vaporize");
        a.add("sublimate");
        a.add("toast");
        a.add("melt");
        a.add("brand");
        a.add("incinerate");
        a.add("carbonize");
        a.add("flay");
        a.add("attack");
        a.add("constipate");
        a.add("gasify");
        a.add("atomize");
        a.add("bombard");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    public static String simple_end() {
        List<String> a = new ArrayList<>();
        a.add("with nukes");
        a.add("with gasoline");
        a.add("with dicks");
        a.add("with diesel fuel");
        a.add("with babies");
        a.add("with the broken dreams of engineers");
        a.add("with plasma");
        a.add("with the Incinerate! plasmid from Ryan Industries");
        a.add("with the Devil's kiss vigor by Fink Co.");
        a.add("with thermite");
        a.add("in a toaster");
        a.add("in an oven like momma's cookies");
        a.add("with fire");
        a.add("in a bonfire");
        a.add("in the nether");
        a.add("in a blaze of failure");
        a.add("with the unused art degrees of baristas");
        a.add("with napalm");
        a.add("with exploding barrels");
        a.add("using a chemical thrower");
        a.add("like you mean it");
        a.add("like a pyromaniac");
        a.add("in the center of a star");
        a.add("in the corona of a star");
        a.add("in the heart of a volcano");
        a.add("in the exhaust of the space shuttle");
        a.add("with lasers");
        a.add("with ejected plasma from the corona of the sun");
        a.add("with a carebearstare");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
}