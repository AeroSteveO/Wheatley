/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *   Functions based on the Valve Game Portal, and Portal 2
 *   Personality Spheres Currently Represented
 *      Wheatley
 *      Space Core
 *      Fact Core
 *   Other AI Present
 *      GLaDOS
 */
public class WheatleyChatStuff extends ListenerAdapter {
    
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.toLowerCase().startsWith("!hack")){
            if(event.getUser().getNick().equals(Global.BotOwner)||event.getChannel().isOwner(event.getUser())){
                String[] kill = message.split(" ");
                event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(kill[1]),"Wheatley has killed you in his attempt to plug your brain into a computer");
            }
            else{
                event.getChannel().send().kick(event.getUser(),"Wheatley has killed you in an attempt to counter hack your brain");
            }
        }
        if (message.toLowerCase().startsWith("!smash")){
            if(event.getUser().getNick().equals(Global.BotOwner)||event.getUser().getNick().equals("fluke42")||event.getChannel().isOwner(event.getUser())){
                String[] kill = message.split(" ");
                event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(kill[1]),"Aristotle vs MASHY-SPIKE-PLATE");
            }
            else{
                event.getChannel().send().kick(event.getUser(),"MASHY-SPIKE-PLATE smashed you into goop");
            }
        }
        if (message.toLowerCase().startsWith("!old "))
            event.getBot().sendIRC().notice(event.getUser().getNick(),"you so funny, me ruv u rong time");
        
        if (message.equalsIgnoreCase("!Wheatley"))
            event.getBot().sendIRC().message(event.getChannel().getName(),"My command list --> http://bit.ly/QWAKdE");
        
//        if (message.equalsIgnoreCase("!Wheatley?"))
//            event.getBot().sendIRC().message(event.getChannel().getName(),"Okay. What you're doing there is jumping. You just... you just jumped. But nevermind.");
        
        if (message.equalsIgnoreCase("Oh. Hi."))//||message.equalsIgnoreCase("potato?")
            event.getBot().sendIRC().message(event.getChannel().getName(),"Oh. Hi. So. How are you holding up? BECAUSE I'M A POTATO.");
        
        if (message.equalsIgnoreCase("THIS STATEMENT IS FALSE"))
            event.getBot().sendIRC().message(event.getChannel().getName(),"Um. 'True'. I'll go 'true'.");
        
        if (Pattern.matches(Global.MainNick+",?\\s+(youre|you're|you\\s+are)\\s+a?\\s*moron.*",message))
            event.getBot().sendIRC().message(event.getChannel().getName(),"I AM NOT A MORON");
        
//        if (message.equalsIgnoreCase("Wheatley?"))
//            event.respond("He's not just a regular moron. He's the product of the greatest minds of a generation working together with the express purpose of building the dumbest moron who ever lived. | My command list --> http://bit.ly/QWAKdE");
        
        if (message.equalsIgnoreCase("GLaDOS?"))
            event.respond("What a nasty piece of work she was, honestly. Like a proper maniac. You know who ended up, do you know who ended up taking her down in the end? You're not going to believe this. A human. I know! I know, I wouldn't have believe it either.");
        
        if ((message.equalsIgnoreCase("!space"))||(message.equalsIgnoreCase("SPACE"))) {
            switch((int) (Math.random()*3+1)) {
                case 1:
                    event.getBot().sendIRC().message(event.getChannel().getName(),"What's your favorite thing about space? Mine is space.");
                    break;
                case 2:
                    event.getBot().sendIRC().message(event.getChannel().getName(),"Ohmygodohmygodohmygod! I'm in space!");
                    break;
                case 3:
                    event.getBot().sendIRC().message(event.getChannel().getName(),"Oh oh oh. This is space! I'm in space!");
                    event.getBot().sendIRC().message(event.getChannel().getName(),"So much space. Need to see it all.");
                    event.getChannel().send().kick(event.getUser(), "Space Court. For people in space. Judge space sun presiding. Bam. Guilty. Of being in space.");
                    break;
            }
        }
    }
}