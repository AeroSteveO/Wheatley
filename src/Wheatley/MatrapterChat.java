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
 *
 * Activate Commands With
 *      !Bane
 *      !Bane [word 1] [word 2]
 *      !Bane [word 1] [word 2] [word 3]
 *      !Bane [word 1] [word 2] [word 3] [word 4]
 *          Sends banes speech either as he said it, or replacing "darkness", "dark", "light" and "blinding" with the given words
 *      !Penis
 *          Responds with ascii penis
 *      !Botd
 *      !Boobs
 *      !Melons
 *          Responds with random ascii boobs
 *      !Butt
 *          Responds with a PM with ascii art of a butt
 *      !Meatlab
 *          Responds with saying
 *      !Matlab
 *          Responds with matrab
 *      !Meatpod
 *      Meatpod
 *          Responds with link to meatspin
 */
public class MatrapterChat extends ListenerAdapter {
    
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("matrapter")).contains(event.getChannel())) {
            String currentChan = event.getChannel().getName();
            String message = Colors.removeFormattingAndColors(event.getMessage());
            
            if (message.equalsIgnoreCase("!penis"))
                event.getBot().sendIRC().message(currentChan, "8==D");
            
            if (message.equalsIgnoreCase("!vagina"))
                event.getBot().sendIRC().message(currentChan, "({'})");
            
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
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"This bot doesn't contain 100% of Matrapter's original capability");
            }
            
            //Meatpod Functions
            if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("meatpod")).contains(event.getChannel())) {
                if (message.equalsIgnoreCase("!meatpod")||message.equalsIgnoreCase("meatpod"))
                    event.getBot().sendIRC().message(currentChan, "http://meatspin.cc");
                if (message.equalsIgnoreCase("fuck")){
                    int i = (int) (Math.random()*500+1);
                    if (i == 250)
                        event.getBot().sendIRC().message(currentChan, "a duck");
                    if (i == 500)
                        event.getBot().sendIRC().message(currentChan, "a dog in the ass");
                    else
                        event.getBot().sendIRC().message(currentChan, "you");
                }
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
                else if (baneterm.length==4){
                    ArrayList<String> baneparts = new ArrayList<String>();
                    baneparts.add("Ah you think ");
                    baneparts.add(" is your ally? You merely adopted the ");
                    baneparts.add(". I was born in it, molded by it. I didn't see the ");
                    baneparts.add(" until I was already a man, by then it was nothing to me but blinding!");
                    event.getBot().sendIRC().message(currentChan,baneparts.get(0)+baneterm[1]+baneparts.get(1)+baneterm[2]+baneparts.get(2)+baneterm[3]+baneparts.get(3));
                }
                else if (baneterm.length==5){
                    ArrayList<String> baneparts = new ArrayList<String>();
                    baneparts.add("Ah you think ");
                    baneparts.add(" is your ally? You merely adopted the ");
                    baneparts.add(". I was born in it, molded by it. I didn't see the ");
                    baneparts.add(" until I was already a man, by then it was nothing to me but ");
                    event.getBot().sendIRC().message(currentChan,baneparts.get(0)+baneterm[1]+baneparts.get(1)+baneterm[2]+baneparts.get(2)+baneterm[3]+baneparts.get(3)+baneterm[4]+"!");
                }
                else
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Bane input should be '!bane [term1] [term2]'");
            }
            if (message.equalsIgnoreCase("!butt")){
                ArrayList<String> butt = new ArrayList();
                butt.add("     .         .");
                butt.add("     |         |");
                butt.add("     j    :    l");
                butt.add("    /           \\ ");
                butt.add("   /             \\");
                butt.add("  Y       .       Y");
                butt.add("  |       |       |");
                butt.add("  l \"----~Y~----\" !");
                butt.add("   \\      |      /");
                butt.add("    Y     |     Y");
                butt.add("    |     I     |");
                for (int i=0;i<butt.size();i++)
                    event.getBot().sendIRC().message(event.getUser().getNick(),butt.get(i));
            }
            
            if (message.equalsIgnoreCase("!flag")||message.equalsIgnoreCase("!america")
                    ||message.equalsIgnoreCase("!merica")||message.equalsIgnoreCase("!merika")){
                ArrayList<String> a = getFlag();
                for (int i=0;i<a.size();i++)
                    event.getBot().sendIRC().message(event.getUser().getNick(),a.get(i));
            }
            
            if (message.toLowerCase().startsWith("give ")&&(message.toLowerCase().split(" ",3)[2].toLowerCase().equalsIgnoreCase("some freedom"))){
                
                String user = message.split(" ")[1];
                if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser(user))) {

                    ArrayList<String> a = getFlag();
                    event.getBot().sendIRC().notice(event.getUser().getNick(),user+" has been PMed the American flag");
                    
                    for (int i=0;i<a.size();i++)
                        event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser(user).getNick(),a.get(i));
                }
                else if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"tell "+Colors.NORMAL+"user not in channel");
                }
            }
        }
    }
    
    public ArrayList<String> getFlag() {
        ArrayList<String> a = new ArrayList();
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.BLUE)+"* * * * * * * * * * "+Colors2.getColors(Colors2.RED,Colors2.RED)+"OOOOOOOOOOOOOOOOOOOOOOOOO"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.BLUE)+" * * * * * * * * *  "+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.LIGHT_GRAY)+":::::::::::::::::::::::::"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.BLUE)+"* * * * * * * * * * "+Colors2.getColors(Colors2.RED,Colors2.RED)+"OOOOOOOOOOOOOOOOOOOOOOOOO"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.BLUE)+" * * * * * * * * *  "+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.LIGHT_GRAY)+":::::::::::::::::::::::::"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.BLUE)+"* * * * * * * * * * "+Colors2.getColors(Colors2.RED,Colors2.RED)+"OOOOOOOOOOOOOOOOOOOOOOOOO"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.BLUE)+" * * * * * * * * *  "+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.LIGHT_GRAY)+"::::::::::::::::::::;::::"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.BLUE)+"* * * * * * * * * * "+Colors2.getColors(Colors2.RED,Colors2.RED)+"OOOOOOOOOOOOOOOOOOOOOOOOO"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.LIGHT_GRAY)+":::::::::::::::::::::::::::::::::::::::::::::"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.RED,Colors2.RED)+"OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.LIGHT_GRAY)+":::::::::::::::::::::::::::::::::::::::::::::"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.RED,Colors2.RED)+"OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.LIGHT_GRAY,Colors2.LIGHT_GRAY)+":::::::::::::::::::::::::::::::::::::::::::::"+Colors.NORMAL+"|");
        a.add("|"+Colors2.getColors(Colors2.RED,Colors2.RED)+"OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+Colors.NORMAL+"|");
        return a;
    }
}