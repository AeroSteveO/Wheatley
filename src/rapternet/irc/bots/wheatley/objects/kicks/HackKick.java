/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rapternet.irc.bots.wheatley.objects.kicks;

import java.util.ArrayList;

/**
 *
 * @author Stephen
 */
public class HackKick implements KickInterface {
    
    @Override
    public String toString() {
        return("Some text about the command that will never be used");
    }
        
    @Override
    public String getCommand() {
        return "hack";
    }

    @Override
    public String getMessage() {
        return "Wheatley has killed you in his attempt to plug your brain into a computer";
    }
    
    @Override
    public String getFailureMessage() {
        return "Wheatley has killed you in an attempt to counter hack your brain";
    }

    @Override
    public ArrayList<String> getAllowedUsers() {
        return null;
    }

    @Override
    public ArrayList<String> getChannelList() {
        return null;
    }

    @Override
    public boolean isChannelListWhitelist() {
        return false;
    }
}
