 /**
  *
  *
  *
  */
package Wheatley;

import Objects.ServerReconnector;
import Objects.PastebinExceptionHandler;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.Configuration.*;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import java.io.File;
import java.util.Arrays;
import org.pircbotx.Colors;
import java.util.ArrayList;
import org.pircbotx.hooks.managers.BackgroundListenerManager;
import uno2.UnoBot;

/**
 *
 *  With Functions from
 *      theTardis   -- by theDoctor
 *      Matrapter   -- by Steve-O
 *      Blarghlebot -- by Blarghedy
 *      Bellagio    -- by http://casinobot.codeplex.com/
 *      RoyalBot    -- by http://www.msclemens.com/royaldev/royalbot
 *      SrsBsns     -- by saigon
 *      LilWayne    -- by 
 *      Hermes      -- by aaahhh
 *      Poopsock    -- by khwain
 *      meatpod     -- by 
 *      unoBot      -- by https://github.com/mjsalerno/UnoBot
 *
 *
 * @author Sten
 * often by siphoning code from other bots by tangd, and Vanilla, and theDoctor
 * or by converting code from other bots to Wheatley/pircbotx2.0
 * and by mirroring or re-making commands for theTardis/Wheatley
 *
 */
public class WheatleyMain extends ListenerAdapter {
    
    
    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
// in case something should be done here
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (!message.startsWith(Global.commandPrefix)&&!message.toLowerCase().startsWith(Global.mainNick.toLowerCase())){
            event.respond("I am a bot, I cannot respond to private messages");
        }
    }
    @Override
    // Rejoin on Kick
    public void onKick(KickEvent event) throws Exception {
        if (event.getRecipient().getNick().equals(event.getBot().getNick())) {
            event.getBot().sendIRC().joinChannel(event.getChannel().getName());
        }
    }
    
    @Override
    // Set mode +B for Bots
    public void onConnect(ConnectEvent event) throws Exception {
        event.getBot().sendRaw().rawLine("mode " + event.getBot().getNick() + " +B");
    }
    
    @Override
    // Joins channels it has been invited to
    public void onInvite(InviteEvent event) {
        
        if (!Global.settings.contains(Arrays.asList(event.getChannel(),"acceptinvites"))){
            Global.settings.create("acceptinvites","true",event.getChannel());
        }
//        System.out.println(Boolean.parseBoolean(Global.settings.get("acceptinvites",event.getChannel())));
        if (Boolean.parseBoolean(Global.settings.get("acceptinvites",event.getChannel()))){
            event.getBot().sendIRC().joinChannel(event.getChannel());
            Global.channels.add(event.getChannel().toLowerCase()); //think this will work
        }
    }
    
    @Override
    public void onNickAlreadyInUse(NickAlreadyInUseEvent event) {
        event.getBot().sendIRC().message("NickServ", "ghost " + Global.mainNick + " " + Global.nickPass);  //ghost is a depricated command, if it doesn't work, the next command should work
        event.getBot().sendIRC().message("NickServ", "recover " + Global.mainNick + " " + Global.nickPass);//sends both commands, NS can yell about one and do the other
        event.getBot().sendIRC().changeNick(Global.mainNick);
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args) {
//        Global.addCommands(Global.commandList, CMD.class);
        //Setup this bot
        try{
            File fXmlFile = new File("Settings.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Element baseElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("basicsettings").item(0);
            int test = Integer.parseInt(baseElement.getElementsByTagName("test").item(0).getTextContent());
            Element eElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("server").item(test);
            Global.mainNick = baseElement.getElementsByTagName("nick").item(0).getTextContent();
            Global.nickPass = baseElement.getElementsByTagName("nickservpass").item(0).getTextContent();
            Global.botOwner = baseElement.getElementsByTagName("botowner").item(0).getTextContent();
            Global.serverPort = eElement.getElementsByTagName("port").item(0).getTextContent();
            Global.phrasePrefix = Global.mainNick+", ";
            Global.mainServer = eElement.getElementsByTagName("address").item(0).getTextContent();
            
            BackgroundListenerManager BackgroundListener = new BackgroundListenerManager();
            BackgroundListener.setExceptionHandler(new PastebinExceptionHandler());
            
            ArrayList<ServerEntry> servers = new ArrayList<>();
            ServerEntry entry = new ServerEntry(Global.mainServer, Integer.parseInt(Global.serverPort));
            
            //   Configuration configuration;
            Configuration.Builder configuration = new Configuration.Builder()
                    .setName(Global.mainNick)
                    .setRealName(Global.mainNick)
                    .setLogin(baseElement.getElementsByTagName("login").item(0).getTextContent()) //login part of hostmask, eg name:login@host
                    .setNickservPassword(Global.nickPass)
                    .setAutoNickChange(true) //Automatically change nick when the current one is in use
                    .setCapEnabled(true)     //Enable CAP features
                    .setAutoReconnect(true)
                    .setMaxLineLength(425)
                    .setListenerManager(BackgroundListener)//Allow for logger background listener
                    .addListener(new WheatleyMain())       //This main class's listener
                    .addListener(new Blarghlebot())        //Trollbot Listener
                    .addListener(new Swapper())
                    .addListener(new GameOmgword())        //omgword game listener
                    .addListener(new GameReverse())        //reverse the word game
                    .addListener(new GameHangman())        //omgword game listener
//                    .addListener(new GameBomb())           //bomb game listener
                    .addListener(new GameMasterMind())     //mastermind game listener
                    .addListener(new GameGuessTheNumber()) //guess the number game listener
                    .addListener(new UnoBot())
                    .addListener(new GameListener())
//                    .addListener(new GameLuckyLotto())
                    .addListener(new GameHighLow())
                    .addListener(new GameBlackjack())
                    .addListener(new GameSlots())
                    .addListener(new GameAltReverse())     //alternate reverse game listener
                    .addListener(new WheatleyChatStuff())  //general portal wheatley chat stuff
                    .addListener(new MatrapterChat())
                    .addListener(new DefListener2())
                    .addListener(new AutodlText())
//                    .addListener(new Fuckingweather())
//                    .addListener(new KickBanWatcher())
                    .addListener(new BotControl())
//                    .addListener(new Ping())
                    .addListener(new BlarghleRandom())
//                    .addListener(new Weather())
//                    .addListener(new TvSchedule())
//                    .addListener(new MetaCritic())
//                    .addListener(new Recommendations())
//                    .addListener(new Urban())
                    .addListener(new CommandListener())
                    .addListener(new IdleRPG())
//                    .addListener(new MovieRatings())
                    .addListener(new BadWords())
//                    .addListener(new MarkovInterface())
                    .addListener(new SRSBSNS())              // contains lasturl and secondlasturl
                    .addListener(new UpdateFiles())          // updates text files via irc
                    .addListener(new RandChan())             // generates random 4chan image links
                    .addServer(entry);
            
            BackgroundListener.addListener(new Logger(),true); //Add logger background listener
            BackgroundListener.addListener(new MarkovInterface(), true);
            
            for (int i=0;i<eElement.getElementsByTagName("channel").getLength();i++){ //Add channels from XML and load into channels Object
                configuration.addAutoJoinChannel(eElement.getElementsByTagName("channel").item(i).getTextContent());
                Global.channels.add((eElement.getElementsByTagName("channel").item(i).getTextContent().toLowerCase()));
                Global.settings.create("NA", "NA", eElement.getElementsByTagName("channel").item(i).getTextContent());
                Global.throttle.create("NA", "NA", eElement.getElementsByTagName("channel").item(i).getTextContent());
            }
            Configuration config = configuration.buildConfiguration();
            
            try {
                Global.bot = new PircBotX(config);
                Global.bot.getConfiguration().getListenerManager().setExceptionHandler(new PastebinExceptionHandler());
                ServerReconnector parallel = new ServerReconnector(Global.bot);
                Thread t = new Thread(parallel);
                parallel.giveT(t);
                t.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Failed to start bot");
            }
            }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}