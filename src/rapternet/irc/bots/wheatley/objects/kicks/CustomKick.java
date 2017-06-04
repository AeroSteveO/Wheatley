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
public class CustomKick implements KickInterface{
    private final String command;
    private final String message;
    private final ArrayList<String> allowedUsers;
    private final ArrayList<String> blockedFromChannels;
    private final String failure;
    private final boolean isWhitelist; // True if the channel list is a whitelist, false if its a blacklist
    
    public CustomKick(String command, String message) {
        this(command, message, null, null, null, false);
    }
    
    public CustomKick(String command, String message, String failure, ArrayList<String> allowedUsers, ArrayList<String> blockedChannels, boolean isChannelListWhitelist) {
        this.command = command;
        this.message = message;
        this.allowedUsers = allowedUsers;
        this.blockedFromChannels = blockedChannels;
        this.failure = failure;
        this.isWhitelist = isChannelListWhitelist;
    }

    
    @Override
    public String getCommand() {
        return command;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    @Override
    public ArrayList<String> getAllowedUsers() {
        return allowedUsers;
    }
    
    @Override
    public ArrayList<String> getChannelList() {
        return blockedFromChannels;
    }
    
    @Override
    public String getFailureMessage() {
        return failure;
    }

    @Override
    public boolean isChannelListWhitelist() {
        return isWhitelist;
    }
    
}
