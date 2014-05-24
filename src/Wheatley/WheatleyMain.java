 /**
  *
  *
  *
  */
package Wheatley;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.TLSCapHandler;
import org.pircbotx.dcc.ReceiveChat;
import org.pircbotx.Configuration.*;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

/**
 *
 *
 * Wheatley, the derp bot
 *  With Functions from
 *      Blarghlebot -- by Blarghedy
 *      Matrapter   -- by Steve-O
 *      Bellagio    -- by http://casinobot.codeplex.com/
 *      theTardis   -- by theDoctor
 *      RoyalBot    -- by http://www.msclemens.com/royaldev/royalbot
 *
 * @author Steve-O
 * often by siphoning code from other bots by tangd, and Vanilla, and theDoctor
 * or by converting code from other bots to Wheatley/pircbotx2.0
 *
 */
public class WheatleyMain extends ListenerAdapter {
    
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        //      String message = Colors.removeFormattingAndColors(event.getMessage());
        
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
        event.getBot().sendRaw().rawLine("mode " + event.getBot().getNick() + " +B"); // Register this as a Bot
    }
    @Override
    // Something from the example script that has continued to stay in my bots code
    public void onIncomingChatRequest(IncomingChatRequestEvent event) throws Exception {
        //Accept the incoming chat request. If it fails it will throw an exception
        ReceiveChat chat = event.accept();
        //Read lines from the server
        String line;
        while ((line = chat.readLine()) != null)
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
    
    @SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args) {
        //Setup this bot
        try{
            File fXmlFile = new File("Settings.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            //     doc.getDocumentElement().normalize();
            //  }
            //   catch (Exception ex) {
            //		ex.printStackTrace();
            //  }
            
            // Get basic settings for Wheatley and which server to log onto
            //Document doc = dBuilder.parse(fXmlFile);
            //NodeList basesettings = doc.getElementsByTagName("basicsettings");
            //Node basenode = basesettings.item(0);
            //Element baseElement = (Element) basenode;
            Element baseElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("basicsettings").item(0);
            //Element ListenerElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("listener").item(0);
            int test = Integer.parseInt(baseElement.getElementsByTagName("test").item(0).getTextContent());
            
            // Get server settings from XML file for the server defined by the above commands
            //NodeList nList = doc.getElementsByTagName("server");
            //       int test = 0;
            //Node nNode = nList.item(test);
            //Element eElement = (Element) nNode;
            Element eElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("server").item(test);
            Global.MainNick = baseElement.getElementsByTagName("nick").item(0).getTextContent();
            Global.NickPass = baseElement.getElementsByTagName("nickservpass").item(0).getTextContent();
            Global.BotOwner = baseElement.getElementsByTagName("botowner").item(0).getTextContent();
            
            
            
            //  ArrayList<Listener> BotListeners = new ArrayList();
            //  BotListeners.add(new Blarghlebot());
            //  Class listenerclass = Class.forName("org.pircbotx.impl.Blarghlebot");
            //  BotListeners.add(new listenerclass());
            
            
            //   Configuration configuration;
            Configuration.Builder configuration = new Configuration.Builder()
                    .setName(Global.MainNick) //Set the nick of the bot. CHANGE IN YOUR CODE
                    .setLogin(baseElement.getElementsByTagName("login").item(0).getTextContent()) //login part of hostmask, eg name:login@host
                    .setNickservPassword(Global.NickPass)
                    .setAutoNickChange(true) //Automatically change nick when the current one is in use
                    .setCapEnabled(true) //Enable CAP features
                    .setAutoReconnect(true)
                    .setMaxLineLength(425)
//                    .addCapHandler(new TLSCapHandler(new UtilSSLSocketFactory().trustAllCertificates(), true))
                    .addListener(new WheatleyMain())    //This main class's listener
                    .addListener(new Blarghlebot()) //Trollbot Listener
                    .addListener(new GameOmgword())        //omgword game listener
                    .addListener(new GameReverse())        //reverse the word game
                    .addListener(new GameHangman())     //omgword game listener
                    .addListener(new GameBomb()) 
                    .addListener(new Why())         // gives a random reason as to 'why?'
                    .addListener(new WheatleyChatStuff())  //general portal wheatley chat stuff
                    .addListener(new MatrapterChat())
                    .addListener(new AutodlText())
                    .addListener(new Ignite())
                    .addListener(new Laser())
                    .addListener(new FactSphereFacts())
                    .addListener(new Shakespeare())
                    .addListener(new BotControl())
                    .addListener(new Ping())
                    .addListener(new CaveJohnson())
                    .addListener(new BlarghleRandom())
                    .addListener(new BadWords())
//                    .addListener(new GameBackbone())
                    .addListener(new TextModification())
                    .addListener(new SRSBSNS())              // contains lasturl and secondlasturl
                    .addListener(new UpdateFiles())          // updates text files via irc
                    .addListener(new Bane())                 // Banes qq speech
//                    .addListener(new SimplePing())
                    .setServerHostname(eElement.getElementsByTagName("address").item(0).getTextContent());
            //  for (int i=0;i<baseElement.getElementsByTagName("listener").getLength();i++)
            //     configuration.addListener("new "baseElement.getElementsByTagName("listener").item(i).getTextContent()+"()")
            //             .buildConfiguration();
            
            
            
            
            for (int i=0;i<eElement.getElementsByTagName("channel").getLength();i++)
            {
                configuration.addAutoJoinChannel(eElement.getElementsByTagName("channel").item(i).getTextContent());
                Global.Channels.add(eElement.getElementsByTagName("channel").item(i).getTextContent());
                //.buildConfiguration();
            }
            Configuration config = configuration.buildConfiguration();
            Global.bot = new PircBotX(config);
            //bot.connect throws various exceptions for failures
            PircBotX bot = new PircBotX(config);
            bot.startBot();
        } //In your code you should catch and handle each exception seperately,
        //but here we just lump them all together for simplicity
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}