/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Wheatley;

import Objects.BanLog;
import Objects.BanLog.BanList;
import com.google.common.collect.ImmutableList;
import org.pircbotx.Colors;
import org.pircbotx.ReplyConstants;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ModeEvent;
import org.pircbotx.hooks.events.RemoveChannelBanEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.SetChannelBanEvent;

/**
 *
 * @author Steve-O
 *
 * ban followed by kick
 * ban without kick
 *
 * local active list of bans
 * -pulls banlist on join
 * -monitors ban removal and addition in chan
 * -modifies and maintains active banlist from this
 *
 * kick without ban
 */
class KickBanWatcher extends ListenerAdapter {
    BanList userBans = new BanList();// = new BanList();
    
    
    @Override
    public void onMessage(MessageEvent event){
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.equalsIgnoreCase("!list bans")){
            if (userBans.isEmpty()){
                event.getBot().sendIRC().message(event.getChannel().getName(),"Banlist is empty");
            }
            else {
                for (int i=0;i<userBans.size();i++){
                    event.getBot().sendIRC().message(event.getChannel().getName(),userBans.get(i).getFormattedResponse());
                }
            }
        }
        if(message.equalsIgnoreCase("!test colors")){
            event.respond(Colors2.getRandomColorAndBackground()+"COOL STUFF"+Colors.NORMAL+Colors2.getRandomColor()+" IS COOL");
        }
    }
    
    @Override
    public void onJoin(JoinEvent event){
        event.getBot().sendRaw().rawLine("mode "+event.getChannel().getName()+" +b");
        int start = ReplyConstants.RPL_BANLIST;
        System.out.println(start);
        int end = ReplyConstants.RPL_ENDOFBANLIST;
        System.out.println(end);
    }
    
    @Override
    public void onKick(KickEvent event) {
        String hostmask = event.getUser().getHostmask();
        String user = event.getRecipient().getNick();
        String kicker = event.getUser().getNick();
        String reason = Colors.removeFormattingAndColors(event.getReason());
        String time = Global.getTimestamp(event);
        event.getBot().sendIRC().message(event.getChannel().getName(), time);//time.toString());
    }
    
    @Override
    public void onSetChannelBan(SetChannelBanEvent event) {
        String hostmask = event.getUser().getHostmask();
        String banner = event.getUser().getNick();
        String channel = event.getChannel().getName();
        String time = Global.getTimestamp(event);
        userBans.safeAdd(new BanLog(hostmask, channel, banner,time));
        event.getBot().sendIRC().message(channel,"Ban added: "+userBans.getBan(hostmask, channel).getSimpleInfo());
        System.out.println("BAN SET");
    }
    
    @Override
    public void onRemoveChannelBan(RemoveChannelBanEvent event) {
        String hostmask = event.getUser().getHostmask();
        String banner = event.getUser().getNick();
        String channel = event.getChannel().getName();
        String time = Global.getTimestamp(event);
        event.getBot().sendIRC().message(channel,"Ban removed: "+userBans.getBan(hostmask, channel).getSimpleInfo());
        userBans.safeRemove(hostmask, channel);
        System.out.println("BAN REMOVED");
    }
    
    @Override
    public void onMode(ModeEvent event){
        System.out.println("MODE CHANGED");
    }
    
    @Override
    public void onServerResponse(ServerResponseEvent event) {
        try{
            if (event.getRawLine().contains(Integer.toString(ReplyConstants.RPL_BANLIST))){
                ImmutableList ban = (event.getParsedResponse());
                System.out.println(event.getParsedResponse());
//                String[] message = event.getRawLine().split(" ");
                String channel = (String) ban.get(1);
                String hostmask =(String) ban.get(2);
                String banner = (String) ban.get(3);
                String time = Global.getTimestamp(event);
                userBans.safeAdd(new BanLog(hostmask, channel, banner, time));
            }
            else if (event.getRawLine().contains("MODE")){
                System.out.println("MODE CHANGE");
            }
//            System.out.println(event.getRawLine());
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
//(`id`,`length`,`banned`,`kicked`,`time`,`kicker`,`user`,`reason`,`host`) 