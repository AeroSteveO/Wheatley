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
public interface KickInterface {
    public String getCommand();
    public String getMessage();
    public ArrayList<String> getAllowedUsers();
    public ArrayList<String> getChannelList();
    public boolean isChannelListWhitelist();
    public String getFailureMessage();
    
}
