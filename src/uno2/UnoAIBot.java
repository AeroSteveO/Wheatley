package uno2;

import Wheatley.Global;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NoticeEvent;

/*
* To change this template, choose Tools | Templates and open the template in
* the editor.
*/

/**
 *
 * @author roofis0
 */
public class UnoAIBot extends ListenerAdapter {
    
    private String[] botOps;
    private boolean justDrew;
    private Deck savedDeck = null;
    private Player savedMe;
    private String savedChannel;
    private PircBotX bot;
    
    public UnoAIBot(PircBotX bot) {
        this.bot = bot;
        justDrew = false;
    }
    
    public void setBotOps(String[] botOps) {
        this.botOps = botOps;
    }
    
    private boolean isBotOp(String nick) {
        for (String i : botOps) {
            if (i.equalsIgnoreCase(nick)) {
                return true;
            }
        }
        return false;
    }
    public void playAI(String channel, Player me, Deck deck) {
        Card card = null;
        if (UnoAI.hasPlayable(me, deck)) {
            card = UnoAI.getPlayable(me, deck);
        } else {
            justDrew = true;
            bot.sendIRC().message(channel, "!draw");
            savedDeck = deck;
            savedMe = me;
            savedChannel = channel;
        }
        
        if (!justDrew && card.color.equals(Card.Color.WILD)) {
            bot.sendIRC().message(channel, Global.commandPrefix + "play " + card.face.toString() + " " + UnoAI.colorMostOf(me, deck).toString());
            System.out.println("SHOULD HAVE SENT MESSAGE");
        } else if (!justDrew) {
            bot.sendIRC().message(channel, Global.commandPrefix + "play " + card.color.toString() + " " + card.face.toString());
            System.out.println("SHOULD HAVE SENT MESSAGE");
        }
        
    }
    
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String sender = event.getUser().getNick();
        String channel = event.getChannel().getName();
        String[] Tokens = Colors.removeFormattingAndColors(event.getMessage()).split(" ");
        
        //NICK
        if (Tokens[0].equalsIgnoreCase(Global.commandPrefix + "nickai") && this.isBotOp(sender)) {
            event.getBot().sendIRC().changeNick(Tokens[1]);
        } //HELP
        //JOINC
        else if (Tokens[0].equalsIgnoreCase(Global.commandPrefix + "joincai") && this.isBotOp(sender)) {
            event.getBot().sendIRC().joinChannel(Tokens[1]);
        } //QUIT
        else if (Tokens[0].equalsIgnoreCase(Global.commandPrefix + "quit") && this.isBotOp(sender)) {
            event.getBot().sendIRC().quitServer();
        } //UNO
        else if (Tokens[0].equalsIgnoreCase(Global.commandPrefix + "uno")) {
            event.getBot().sendIRC().message(channel, Global.commandPrefix + "join");
        }
        else if (Tokens[0].equalsIgnoreCase(Global.commandPrefix + "unoAIhelp")){
            event.getBot().sendIRC().notice(sender, Global.commandPrefix + "nickai ----- Tells the bot to change his nick.");
            event.getBot().sendIRC().notice(sender, Global.commandPrefix + "joincai ---- Tells the bot to join a channel.");
            event.getBot().sendIRC().notice(sender, Global.commandPrefix + "uno ----- Tells the bot send " + Global.commandPrefix + "join to join the uno game.");
            event.getBot().sendIRC().notice(sender, Global.commandPrefix + "quit ----- Tells the bot to dissconnect from the entire server.");
        }
    }
    
    @Override
    public void onKick(KickEvent event) throws Exception {
        String recipientNick = event.getRecipient().getNick();
        if (recipientNick.equals(event.getBot().getNick())) {
            event.getBot().sendIRC().joinChannel( event.getChannel().getName() );
        }
    }
    
    
    
    @Override
    public void onNotice(NoticeEvent event) throws Exception {
        String notice = event.getNotice();
        if (justDrew && notice.contains("drew")) {
            Card drawnCard = null;
            String[] split = notice.split(" ");
            drawnCard = UnoEngine.stringToCard(split[3] + " " + split[4]);
            justDrew = false;
            
            if (savedMe.isCardPlayable(drawnCard, savedDeck)) {
                if (drawnCard.color.equals(Card.Color.WILD)) {
                    event.getBot().sendIRC().message(savedChannel, Global.commandPrefix + "play " + drawnCard.face.toString() + " " + UnoAI.colorMostOf(savedMe, savedDeck).toString());
                } else {
                    event.getBot().sendIRC().message(savedChannel, Global.commandPrefix + "play " + drawnCard.color.toString() + " " + drawnCard.face.toString());
                }
            } else {
                event.getBot().sendIRC().message(savedChannel, Global.commandPrefix + "pass");
            }
            
        }
        
        if(justDrew && notice.contains("attacked")) {
            Card card = null;
            justDrew = false;
            if (UnoAI.hasPlayable(savedMe, savedDeck)) {
                card = UnoAI.getPlayable(savedMe, savedDeck);
            } else {
                event.getBot().sendIRC().message(savedChannel, Global.commandPrefix + "pass");
            }
            if (!justDrew && card.color.equals(Card.Color.WILD)) {
                event.getBot().sendIRC().message(savedChannel, Global.commandPrefix + "play " + card.face.toString() + " " + UnoAI.colorMostOf(savedMe, savedDeck).toString());
            } else if (!justDrew) {
                event.getBot().sendIRC().message(savedChannel, Global.commandPrefix + "play " + card.color.toString() + " " + card.face.toString());
            }
        }
    }
}
