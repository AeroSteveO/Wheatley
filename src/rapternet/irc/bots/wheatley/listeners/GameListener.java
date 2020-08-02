/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import rapternet.irc.bots.wheatley.gamecommands.GameBombCMD;
import rapternet.irc.bots.wheatley.gamecommands.Lotto;
import rapternet.irc.bots.wheatley.gamecommands.MakeItRain;
import rapternet.irc.bots.wheatley.gamecommands.Cheat;
import rapternet.irc.bots.wheatley.gamecommands.MoneyCMD;
import rapternet.irc.bots.wheatley.gamecommands.Merge;
import rapternet.irc.bots.wheatley.gamecommands.Save;
import rapternet.irc.bots.wheatley.gamecommands.Flush;
import rapternet.irc.bots.wheatley.gamecommands.Give;
import rapternet.irc.bots.wheatley.objects.CommandGame;
import rapternet.irc.bots.wheatley.objects.GameList;
import rapternet.irc.bots.wheatley.objects.Score;
import rapternet.irc.bots.common.utils.GameUtils;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.UserListEvent;
import rapternet.irc.bots.wheatley.objects.Env;

/**
 *
 * @author Stephen
 */
public class GameListener extends ListenerAdapter{
    List<CommandGame> commandList = getCommandList();
    public static GameList activeGame = new GameList();    // To be implemented in games
    public static Score.ScoreArray scores = new Score.ScoreArray();
    String filename = Env.CONFIG_LOCATION + "gameScores.json";
    boolean loaded = startScores();
    
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (!GameUtils.areGamesBlocked(event.getChannel().getName())) {
            if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")) {
                String command = message.toLowerCase().split(Global.commandPrefix)[1];
                String[] cmdSplit = command.split(" ");
                for (int i=0;i<commandList.size();i++){
                    if (commandList.get(i).commandTerms().contains(cmdSplit[0])) {
                        commandList.get(i).processCommand(event);
                    }
                }
            }
        }
    }
    
    private List<CommandGame> getCommandList() {
        List<CommandGame> listOfCommands = new ArrayList<>();
        listOfCommands.add(new MoneyCMD());
        listOfCommands.add(new Lotto());
        listOfCommands.add(new Flush());
        listOfCommands.add(new Cheat());
        listOfCommands.add(new Give());
        listOfCommands.add(new MakeItRain());
        listOfCommands.add(new Merge());
        listOfCommands.add(new Save());
       // listOfCommands.add(new PurchaseTimeBomb());
        listOfCommands.add(new GameBombCMD());
        return(listOfCommands);
    }
    private boolean startScores(){
        boolean loaded;
        try{
            scores.setFilename(filename);
            loaded = scores.loadFromJSON();
        }
        catch (Exception ex){
            System.out.println("SCORES FAILED TO LOAD");
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public void onJoin(JoinEvent event){
        if (!GameUtils.areGamesBlocked(event.getChannel().getName())){
            if (!scores.containsUser(event.getUser().getNick())){
                scores.addUser(event.getUser().getNick());
                scores.saveToJSON();
            }
        }
    }
    
    @Override
    public void onUserList(UserListEvent event){
        
        if (!GameUtils.areGamesBlocked(event.getChannel().getName())){
            
            ImmutableSortedSet users = event.getUsers();
            
            Iterator<User> iterator = users.iterator();
            boolean modified = false;
            while(iterator.hasNext()) {
                User element = iterator.next();
                if (!scores.containsUser(element.getNick())){
                    scores.addUser(element.getNick());
                    System.out.println(element.getNick());
                    modified = true;
                }
            }
            if (modified)
                scores.saveToJSON();
        }
    }
}
