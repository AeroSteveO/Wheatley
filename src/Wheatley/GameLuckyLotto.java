/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Wheatley;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class GameLuckyLotto extends ListenerAdapter {
    
    @Override
    public void onMessage(MessageEvent event) {
        String message = Colors.removeFormattingAndColors(event.getMessage());
    
        
        
    }
}
