/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Objects.Kicks;

import java.util.ArrayList;

/**
 *
 * @author Stephen
 */
public class SmashKick implements KickInterface {
    
    @Override
    public String toString() {
        return("Some text about the command that will never be used");
    }
        
    @Override
    public String getCommand() {
        return "smash";
    }

    @Override
    public String getMessage() {
        return "Aristotle vs MASHY-SPIKE-PLATE";
    }
    
    @Override
    public String getFailureMessage() {
        return "MASHY-SPIKE-PLATE smashed you into goop";
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
