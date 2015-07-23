/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import Utils.BotUtils;
import Wheatley.Global;
import java.io.IOException;
import java.net.UnknownHostException;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.managers.ManagerExceptionHandler;

/**
 *
 * @author Stephen
 */
public class PastebinExceptionHandler implements ManagerExceptionHandler {
    
    @Override
    public void onException(Listener ll, Event event, Throwable thrwbl) {
        if (thrwbl instanceof IrcException || thrwbl instanceof IOException) {
            thrwbl.printStackTrace();
        }
        else {
            try {
                if (Global.bot.isConnected()) {
                    event.getBot().sendIRC().message("#rapterverse", BotUtils.formatPastebinPost(thrwbl));
                }
                thrwbl.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
