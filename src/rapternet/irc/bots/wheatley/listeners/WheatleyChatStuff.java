/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

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
        
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            
//            if (cmdSplit[0].equalsIgnoreCase("hack")){
//                if((event.getUser().getNick().equals(Global.botOwner)||event.getChannel().isOwner(event.getUser()))&&event.getUser().isVerified()){
//                    event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(cmdSplit[1]),"Wheatley has killed you in his attempt to plug your brain into a computer");
//                }
//                else{
//                    event.getChannel().send().kick(event.getUser(),"Wheatley has killed you in an attempt to counter hack your brain");
//                }
//            }
//            
//            if (cmdSplit[0].equalsIgnoreCase("smash")){
//                if((event.getUser().getNick().equals(Global.botOwner)||event.getChannel().isOwner(event.getUser()))&&event.getUser().isVerified()){//||event.getUser().getNick().equals("fluke42")
//                    event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(cmdSplit[1]),"Aristotle vs MASHY-SPIKE-PLATE");
//                }
//                else{
//                    event.getChannel().send().kick(event.getUser(),"MASHY-SPIKE-PLATE smashed you into goop");
//                }
//            }
            
//            if (cmdSplit[0].equalsIgnoreCase("old"))
//                event.getBot().sendIRC().notice(event.getUser().getNick(),"you so funny, me ruv u rong time");
            
            if (cmdSplit[0].equalsIgnoreCase("Wheatley"))
                event.getBot().sendIRC().message(event.getChannel().getName(),"My command list --> http://bit.ly/QWAKdE");
            else if (cmdSplit[0].equalsIgnoreCase("flip"))
                event.getBot().sendIRC().message(event.getChannel().getName(),"(╯°□°）╯︵ ┻━┻");
            else if (cmdSplit[0].equalsIgnoreCase("unflip"))
                event.getBot().sendIRC().message(event.getChannel().getName(),"┬─┬ノ( º _ ºノ)");
            else if (cmdSplit[0].equalsIgnoreCase("carrots"))
                event.getBot().sendIRC().message(event.getChannel().getName(),"🥕");
            else if (cmdSplit[0].equalsIgnoreCase("🥕"))
                event.getBot().sendIRC().message(event.getChannel().getName(),"Carrots");
        }
        
        if (message.equalsIgnoreCase("Oh. Hi."))//||message.equalsIgnoreCase("potato?")
            event.getBot().sendIRC().message(event.getChannel().getName(),"Oh. Hi. So. How are you holding up? BECAUSE I'M A POTATO.");
        
        if (message.equalsIgnoreCase("THIS STATEMENT IS FALSE"))
            event.getBot().sendIRC().message(event.getChannel().getName(),"Um. 'True'. I'll go 'true'.");
        
        if (Pattern.matches(Global.mainNick+",?\\s+(youre|you're|you\\s+are)\\s+a?\\s*moron.*",message))
            event.getBot().sendIRC().message(event.getChannel().getName(),"I AM NOT A MORON");
        
        // see also spaceCMD, which is the ! form of the command
        if (message.equalsIgnoreCase("SPACE")) {
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