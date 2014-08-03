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
 * original bot functions by Blarghedy
 * Who's lazy and doesn't run his bot much
 * Original Bot output can be seen below
 * 
 * Activate Command with:
 *      !Blarghlebot
 *          Responds with a random action to the user who gave the command
 */
public class BlarghleRandom extends ListenerAdapter {
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel())) {
            if (message.equalsIgnoreCase("!blarghlebot")){
                String a = new String();
                //  a=" " + adverb() +" "+ verb() +" "+ event.getUser().getNick() + "'s " + adjective() + " " + bodypart();
                switch((int) (Math.random()*3+1)) {
                    case 1:
                        a= adverb() +" "+ verb() +" "+ event.getUser().getNick() + "'s " + adjective() + " " + bodypart();
                        break;
                    case 2:
                        a= adverb() + " " + "rips off" + " " + event.getUser().getNick()+"'s " + bodypart()+ " and "+adverb()+ " beats them with it";
                        break;
                    case 3:
                        a= adverb() + " " + "shoves" + " " + event.getUser().getNick()+"'s " + bodypart() + " through their "+adjective()+ " " + bodypart();
                        break;
                }
                event.getBot().sendIRC().action(event.getChannel().getName(), a);
            }
        }
    }
    private static String adjective() {
        List<String> a = new ArrayList<>();
        a.add("delectable");
        a.add("scrumptious");
        a.add("gross");
        a.add("fartful");
        a.add("engorged");
        a.add("throbbing");
        a.add("fishy-smelling");
        a.add("throbbing");
        a.add("sexy");
        a.add("ugly");
        a.add("delicious");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String bodypart() {
        List<String> a = new ArrayList<>();
        a.add("arm");
        a.add("manhorn");
        a.add("fingers");
        a.add("toes");
        a.add("right nostril");
        a.add("left nostril");
        a.add("hand");
        a.add("foot");
        a.add("elbow");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String adverb() {
        List<String> a = new ArrayList<>();
        a.add("lovingly");
        a.add("carefully");
        a.add("forcefully");
        a.add("hatefully");
        a.add("angrily");
        a.add("roughly");
        a.add("ferociously");
        a.add("hopefully");
        a.add("randily");
        a.add("sadly");
        a.add("emotionally");
        a.add("intricately");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String verb() {
        List<String> a = new ArrayList<>();
        a.add("flatulates on");
        a.add("noms on");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String verbset2() {
        List<String> a = new ArrayList<>();
        a.add("shoves");
        a.add("rips off");
        //      a.add("beats");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
}

//       COMMAND SENTENCE SET 1
// Mostly Added, minus the different sentence structures
//* BlarghleBot lovingly flatulates on jason89s's delectable hand
//* BlarghleBot hatefully noms on fluke42's gross toes
//* BlarghleBot roughly noms on jnick's fartful manhorn
//* BlarghleBot ferociously flatulates on Steve-O's throbbing arm
//* BlarghleBot randily flatulates on Steve-O's fishy-smelling arm
//* BlarghleBot sadly flatulates on Vanilla's throbbing foot
//* BlarghleBot emotionally noms on Steve-O's sexy toes
//* BlarghleBot forcefully noms on Steve-O's ugly toes
//* BlarghleBot lovingly flatulates on Pankeiko's scrumptious hand

//       COMMAND SENTENCE SET 2
//* BlarghleBot intricately rips off Steve-O's nose and emotionally beats them with it.
//* BlarghleBot carefully shoves Steve-O's left nostril through their scrumptious right nostril
//* BlarghleBot hopefully rips off Steve-O's arm and hopefully beats them with it.
//* BlarghleBot intricately shoves Steve-O's fingers through their gross left nostril
//* BlarghleBot hatefully shoves Steve-O's left nostril through their delicious elbow
//* BlarghleBot angrily shoves Steve-O's eyes through their engorged toes