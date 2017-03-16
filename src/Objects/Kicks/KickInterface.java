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

  /**
   * Returns the string that triggers the kick
   * @return command term
   */
  public String getCommand();

  /**
   * Message to use in the kick
   * @return kick message
   */
  public String getMessage();

  /**
   * List of the users who are given access to use the kick
   * @return List of usernames
   */
  public ArrayList<String> getAllowedUsers();

  /**
   * List of channels that the kick is either whitelisted or blacklisted in
   * @return List of channels
   */
  public ArrayList<String> getChannelList();

  /**
   * TRUE if the channel list is a whitelist, FALSE if the channel list is a
   * blacklist
   * @return TRUE or FALSE
   */
  public boolean isChannelListWhitelist();

  /**
   * Message to use in the kick upon a user trying to use it
   * without being given access to it
   * @return string failure message
   */
  public String getFailureMessage();
}
