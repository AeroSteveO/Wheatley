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
public class CustomKick implements KickInterface{
    private final String command;
    private final String message;
    private final ArrayList<String> allowedUsers;
    private final ArrayList<String> blockedFromChannels;
    private final String failure;
    
    public CustomKick(String command, String message) {
        this(command, message, null, null, null);
    }
    
    public CustomKick(String command, String message, String failure, ArrayList<String> allowedUsers, ArrayList<String> blockedChannels) {
        this.command = command;
        this.message = message;
        this.allowedUsers = allowedUsers;
        this.blockedFromChannels = blockedChannels;
        this.failure = failure;
    }

    
    public String getCommand() {
        return command;
    }
    public String getMessage() {
        return message;
    }
    public ArrayList<String> getAllowedUsers() {
        return allowedUsers;
    }
    public ArrayList<String> getBlockedChannels() {
        return blockedFromChannels;
    }
    public String getFailureMessage() {
        return failure;
    }
}
