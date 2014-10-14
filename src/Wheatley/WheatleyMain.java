 /**
  *
  *
  *
  */
package Wheatley;

import Objects.ChannelStore;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.dcc.ReceiveChat;
import org.pircbotx.Configuration.*;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import java.io.File;
import org.pircbotx.hooks.managers.BackgroundListenerManager;

/**
 *
 *
 * Wheatley, the derp bot
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
 *
 * @author Steve-O
 * often by siphoning code from other bots by tangd, and Vanilla, and theDoctor
 * or by converting code from other bots to Wheatley/pircbotx2.0
 * and by mirroring or re-making commands for theTardis/Wheatley
 *
 */
public class WheatleyMain extends ListenerAdapter {
    
//    @Override
//    public void onMessage(final MessageEvent event) throws Exception {
//// in case something should be done here
//    }
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
        event.getBot().sendIRC().joinChannel(event.getChannel());
        Global.channels.add(new ChannelStore(event.getChannel())); //think this will work?
    }
    @Override
    // Something from the example script that has continued to stay in my bots code
    public void onIncomingChatRequest(IncomingChatRequestEvent event) throws Exception {
        //Accept the incoming chat request. If it fails it will throw an exception
        ReceiveChat chat = event.accept();
        //Read lines from the server
        String line;
        while ((line = chat.readLine()) != null) {
            if (line.equalsIgnoreCase("done")) {
                //Shut down the chat
                chat.close();
                break;
            } else {
                //Fun example
                int lineLength = line.length();
                chat.sendLine("Line '" + line + "' contains " + lineLength + " characters");
            }
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args) {
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
            Global.phrasePrefix = Global.mainNick+", ";
            BackgroundListenerManager BackgroundListener = new BackgroundListenerManager();
            
            //   Configuration configuration;
            Configuration.Builder configuration = new Configuration.Builder()
                    .setName(Global.mainNick)
                    .setLogin(baseElement.getElementsByTagName("login").item(0).getTextContent()) //login part of hostmask, eg name:login@host
                    .setNickservPassword(Global.nickPass)
                    .setAutoNickChange(true) //Automatically change nick when the current one is in use
                    .setCapEnabled(true)     //Enable CAP features
                    .setAutoReconnect(true)
                    .setMaxLineLength(425)
                    .setListenerManager(BackgroundListener)//Allow for logger background listener
                    .addListener(new WheatleyMain())       //This main class's listener
                    .addListener(new Blarghlebot())        //Trollbot Listener
                    .addListener(new GameOmgword())        //omgword game listener
                    .addListener(new GameReverse())        //reverse the word game
                    .addListener(new GameHangman())        //omgword game listener
                    .addListener(new GameBomb())           //bomb game listener
                    .addListener(new GameMasterMind())     //mastermind game listener
                    .addListener(new GameGuessTheNumber()) //guess the number game listener
                    .addListener(new GameControl())
                    .addListener(new GameSlots())
                    .addListener(new GameAltReverse())     //alternate reverse game listener
                    .addListener(new Why())                // gives a random reason as to 'why?'
                    .addListener(new WheatleyChatStuff())  //general portal wheatley chat stuff
                    .addListener(new MatrapterChat())
                    .addListener(new EnglishSayings())
                    .addListener(new Definitions())
                    .addListener(new AutodlText())
                    .addListener(new KickBanWatcher())
                    .addListener(new Ignite())
                    .addListener(new Laser())
                    .addListener(new FactSphereFacts())
                    .addListener(new Insult())
                    .addListener(new BotControl())
                    .addListener(new Ping())
                    .addListener(new CaveJohnson())
                    .addListener(new BlarghleRandom())
                    .addListener(new Weather())
                    .addListener(new Urban())
                    .addListener(new MovieRatings())
                    .addListener(new BadWords())
                    .addListener(new MarkovInterface())
                    .addListener(new TextModification())
                    .addListener(new SRSBSNS())              // contains lasturl and secondlasturl
                    .addListener(new UpdateFiles())          // updates text files via irc
                    .addListener(new RandChan())             // generates random 4chan image links
                    .setServerHostname(eElement.getElementsByTagName("address").item(0).getTextContent());
            
            BackgroundListener.addListener(new Logger(),true); //Add logger background listener
            
            for (int i=0;i<eElement.getElementsByTagName("channel").getLength();i++) //Add channels from XML and load into channels Object
            {
                configuration.addAutoJoinChannel(eElement.getElementsByTagName("channel").item(i).getTextContent());
                Global.channels.add(new ChannelStore(eElement.getElementsByTagName("channel").item(i).getTextContent()));
            }
            Configuration config = configuration.buildConfiguration();
            
            Global.bot = new PircBotX(config);
            try {
                Runner parallel = new Runner(Global.bot);
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