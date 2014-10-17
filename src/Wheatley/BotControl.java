/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.ChannelStore;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author Steve-O
 * Part code from RoyalBot -- http://www.royalcraft.org/royaldev/royalbot
 * Rest of the code is Wheatley Original
 *
 * Activate Commands with:
 *      Wheatley, join #[channel]
 *          Makes the bot join the given channel
 *      Wheatley, part #[channel]
 *          Makes the bot part the given channel
 *      Wheatley, leave
 *          Makes the bot part the channel its currently in
 *      Wheatley, fix yourself
 *          Ghosts/Recovers nick, rejoins channels it was disconnected from
 *      Wheatley, whats your IP
 *          Gives the current external IP address of the bot
 *      Wheatley, shutdown
 *          Shuts down the bot
 *
 */
public class BotControl extends ListenerAdapter{
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException, Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.equalsIgnoreCase("!flush")&&(event.getUser().getNick().equals(Global.botOwner)&&event.getUser().isVerified())){
            
            Global.channels.removeDupes();
            Blarghlebot.poop = "null";
            BadWords.badwords = null;
        }
        
        if (message.equalsIgnoreCase(Global.mainNick+", fix yourself")
                &&((event.getUser().getNick().equalsIgnoreCase(Global.botOwner)
                ||event.getUser().getNick().equalsIgnoreCase("theDoctor"))&&event.getUser().isVerified())){
            
            event.getBot().sendIRC().message("NickServ", "ghost " + Global.mainNick + " " + Global.nickPass);  //ghost is a depricated command, if it doesn't work, the next command should work
            event.getBot().sendIRC().message("NickServ", "recover " + Global.mainNick + " " + Global.nickPass);//sends both commands, NS can yell about one and do the other
            
            Thread.sleep(5000); // wait between killing the ghost to changing nick and registering
            event.getBot().sendIRC().changeNick(Global.mainNick);
            event.getBot().sendIRC().message("NickServ", "identify " + Global.nickPass);
            Global.channels.removeDupes();
            for (int i=0;i<Global.channels.size();i++){
                event.getBot().sendIRC().joinChannel(Global.channels.get(i).toString());
            }
        }
        
        if (message.equalsIgnoreCase(Global.mainNick+", please shutdown")
                ||message.equalsIgnoreCase("!shutdown")
                ||message.equalsIgnoreCase(Global.mainNick+", shutdown")) {
            
            if (event.getUser().getNick().equals(Global.botOwner)&&event.getUser().isVerified()){
                Global.reconnect = false;
                event.getBot().sendIRC().message(event.getChannel().getName(), "Let go! Let go! I'm still connected. I can pull myself in. I can still fix this!");
                Random generator = new Random();
                int i = generator.nextInt(2);
                if (i == 0) {
                    event.getBot().sendIRC().quitServer("Accidentally went to space");
                } else {
                    event.getBot().sendIRC().quitServer("Squished by GLaDOS");
                }
                System.exit(0);
            }
            else
                event.getChannel().send().kick(event.getUser(), "PART 5! BOOBY TRAP THE STALEMATE BUTTON!"); // kick people for trying to kill the bot
        }
        
        // command the bot to join channels
        if ((message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", join ")
                ||message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", please join "))
                &&((event.getUser().getNick().equals(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("theDoctor"))&&event.getUser().isVerified())){ //message.toLowerCase().startsWith("!join ")
            
            String[] chan = message.split("#");
            if (message.toLowerCase().contains("#")){
                event.getBot().sendIRC().message(event.getChannel().getName(),"Joining #" + chan[1]);
                event.getBot().sendIRC().joinChannel("#" + chan[1]);
                Global.channels.add(new ChannelStore("#"+chan[1]));
            }
            else
                event.getBot().sendIRC().message(event.getChannel().getName(),chan[chan.length-1] + " is not a channel");
        }
        
        // command the bot to part a different channel from where you are
        if ((message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", leave")
                ||message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", please leave"))){//message.toLowerCase().startsWith("!part")
            
            if (message.toLowerCase().contains("#")&&((event.getUser().getNick().equals(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("theDoctor"))&&event.getUser().isVerified())) {
                
                String[] chan = message.split("#");
                Channel c = event.getBot().getUserChannelDao().getChannel("#"+chan[1]);
                if (!event.getBot().getUserBot().getChannels().contains(c)) {
                    event.respond("Not in that channel!");
                }
                else {
                    c.send().part();
                    event.respond("Left #" + chan[1] + ".");
                    Global.channels.remove(Global.channels.getChanIdx("#"+chan[1]));
                }
            } // command the bot to part the current channel that the command was sent from
            else if ((event.getChannel().isOwner(event.getUser())||event.getUser().getNick().equals(Global.botOwner))&&(message.endsWith("leave"))){//||message.equalsIgnoreCase("!part"))){
                
                event.getChannel().send().part("Goodbye");
                Global.channels.remove(Global.channels.getChanIdx(event.getChannel().getName().toString()));
            }
        }
        if((message.equalsIgnoreCase(Global.mainNick+", whats your ip")||message.equalsIgnoreCase("!ip"))
                &&(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified())){
            
            String ipInfo = readUrl("http://ipinfo.io/json");
            JSONParser parser = new JSONParser();
            try{
                JSONObject ipJSON = (JSONObject) parser.parse(ipInfo);
                String ipAddress = (String) ipJSON.get("ip");
                event.getBot().sendIRC().notice(event.getUser().getNick(), "Current IP Address is: "+ipAddress);
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                event.getBot().sendIRC().notice(event.getUser().getNick(), "ERROR PARSING IP ADDRESS");
            }
        }
    }
    
    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws InterruptedException{
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.equalsIgnoreCase(Global.mainNick+", fix yourself")
                &&((event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("theDoctor"))&&event.getUser().isVerified())){
            
            event.getBot().sendIRC().message("NickServ", "ghost " + Global.mainNick + " " + Global.nickPass);  //ghost is a depricated command, if it doesn't work, the next command should work
            event.getBot().sendIRC().message("NickServ", "recover " + Global.mainNick + " " + Global.nickPass);//sends both commands, NS can yell about one and do the other
            
            Thread.sleep(5000); // wait between killing the ghost to changing nick and registering
            event.getBot().sendIRC().changeNick(Global.mainNick);
            event.getBot().sendIRC().message("NickServ", "identify " + Global.nickPass);
            Global.channels.removeDupes();
            for (int i=0;i<Global.channels.size();i++){
                event.getBot().sendIRC().joinChannel(Global.channels.get(i).toString());
            }
        }
    }
    //converts URL to string, primarily used to string-ify json text
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
//    public void onEvent(Event event) throws Exception {
//        MessageEvent newEvent = null;
//        if (event instanceof MessageEvent) {
//            if (!Permissions.getAccess((MessageEvent) event)) {
//                return;
//            }
//        } else if (event instanceof PrivateMessageEvent) {
//            newEvent = new MessageEvent(event.getBot(), (Channel) event.getBot().getUserBot().getChannels().toArray()[0], ((PrivateMessageEvent) event).getUser(), ((PrivateMessageEvent) event).getMessage());
//            if (!Permissions.getAccess((MessageEvent) newEvent)) {
//                return;
//            }
//        }