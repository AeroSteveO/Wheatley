/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package rapternet.irc.bots.wheatley.objects;

import rapternet.irc.bots.wheatley.listeners.Global;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Object:
 *      CommandMetaData
 * - This object parses out a MessageEvent or PrivateMessageEvent into all the meta data
 *   including message, channel, channels from the message, user, and whether that user
 *   is verified as the bot owner, channel owner (from the event source) or channel
 *   owner from the channel in the message
 *
 * Methods:
 *      getCommands       - Sets the command and cmdSplit variables from the
 *                          message string
 *     *getChannel        - Returns the channel the event was sent from, if the
 *                          event was in a channel, else returns null
 *     *getCaller         - Returns the nickname of the user who sent the event
 *     *getCommand        - Gets the command string minus prefix
 *     *getCommandChannel - Returns the channel that was in the command, or the
 *                          channel the command was sent from
 *     *getCommandSplit   - Returns a String[] of the command data without the
 *                          prefix, and split by spaces
 *     *getEventChannel   - Returns the channel the event was sent from, or null
 *     *getEventType      - Returns either "privatemessage" or "channelmessage"
 *     *getMessage        - Returns the event message
 *     *isVerifiedChanOwner    - Returns true if the event user is verified and
 *                               is the owner of the channel in the command or
 *                               that the command was sent from
 *     *isVerifiedBotOwner     - Returns true if the event user is verified and
 *                               is the bot owner
 *     *isVerifiedChanBotOwner - Returns true if the event user is verified and
 *                               is either the bot owner, or the channel owner
 *                               of the channel the command was sent from, or
 *                               channel contained in the event
 *     *isVerified        - Returns true if the user is logged into the NickServ
 *                          account they're currently using
 *     *isUserInChannel   - Responds with a boolean if the input user is in the
 *                          input channel
 *
 * Note: Only commands marked with a * are available for use outside the object
 *
 * Useful Resources
 * - N/A
 *
 * Version: 0.5.5
 */
public class CommandMetaData {

  private final Event event;
  private String caller;
  private EventType eventType;
  private String channel = null; // This channel is only changed if the event is a message event
  private String refChan = null; // This channel is only changed if one is in the input string
  private boolean isVerified = false; // True if the User is Registered and Identified
  private boolean isBotOwner = false; // True if the User is using the Bot Owners Nick
  private boolean isChanOwner = false; // True if the User is the channel owner (+qo)
  private String message; // The message of the event
  private String command; // If the message starts with !, it is the word concantonated to the !
  private String[] cmdSplit; // Message split by spaces
  private boolean isSupportedEventType = true;

  /**
   * Constructs a metadata object out of the input event. Parses out information
   * from the event allowing easy access to it.
   *
   * @param event The event to analyze.
   * @param verify TRUE if the sender of the event should be verified as a
   * registered, logged in user, false if the sender does not need to be
   * verified.
   */
  public CommandMetaData(Event event, boolean verify) {
    this.event = event;
    if (verify) {
      processFullEvent();
    } else {
      processEventWithoutVerification();
    }
  }

  /**
   * Event types that can be processed by the generic commands and this metadata
   * object.
   */
  public enum EventType {
    MessageEvent, PrivateMessageEvent
  }

  /**
   * Process and parse metadata without verifying the event sender.
   */
  private void processEventWithoutVerification() {
    if (event instanceof MessageEvent) { // MESSAGE EVENT SPECIFIC PARSING
      eventType = EventType.MessageEvent;
      MessageEvent mEvent = (MessageEvent) event;
      caller = mEvent.getUser().getNick();
      message = Colors.removeFormattingAndColors(mEvent.getMessage());
      channel = mEvent.getChannel().getName();

      if (message.contains("#")) {
        refChan = "#" + message.split("#")[0].split(" ")[0];
      }

      getCommands();
    }// END MESSAGE EVENT SPECIFIC PARSING
    else if (event instanceof PrivateMessageEvent) { // PRIVATE MESSAGE EVENT SPECIFIC PARSING
      eventType = EventType.PrivateMessageEvent;
      PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
      message = Colors.removeFormattingAndColors(pmEvent.getMessage());
      caller = pmEvent.getUser().getNick();

      if (message.contains("#")) {
        refChan = "#" + message.split("#")[0].split(" ")[0];
      }
      getCommands();
    }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
    else {
      isSupportedEventType = false;
    }
  }

  /**
   * Process and parse the event while also verifing the sender is registered
   * and logged in (they are who they say they are by their nick).
   */
  private void processFullEvent() {
    if (event instanceof MessageEvent) { // MESSAGE EVENT SPECIFIC PARSING
      eventType = EventType.MessageEvent;
      MessageEvent mEvent = (MessageEvent) event;
      caller = mEvent.getUser().getNick();
      message = Colors.removeFormattingAndColors(mEvent.getMessage());
      channel = mEvent.getChannel().getName();

      if (message.contains("#")) {
        refChan = "#" + message.split("#")[1].split(" ")[0];
        isVerified = mEvent.getUser().isVerified();
        isBotOwner = caller.equalsIgnoreCase(Global.botOwner);

        if (mEvent.getUser() != null && mEvent.getBot().getUserChannelDao().containsChannel(refChan)) {
          isChanOwner = mEvent.getUser().getChannelsOwnerIn().contains(mEvent.getBot().getUserChannelDao().getChannel(refChan));
        }
      } else {

        isVerified = mEvent.getUser().isVerified();
        isBotOwner = caller.equalsIgnoreCase(Global.botOwner);
        isChanOwner = mEvent.getChannel().isOwner(mEvent.getUser());
      }

      getCommands();
    }// END MESSAGE EVENT SPECIFIC PARSING
    else if (event instanceof PrivateMessageEvent) { // PRIVATE MESSAGE EVENT SPECIFIC PARSING
      eventType = EventType.PrivateMessageEvent;
      PrivateMessageEvent pmEvent = (PrivateMessageEvent) event;
      message = Colors.removeFormattingAndColors(pmEvent.getMessage());
      caller = pmEvent.getUser().getNick();

      if (message.contains("#")) {
        refChan = "#" + message.split("#")[1].split(" ")[0];
        isVerified = pmEvent.getUser().isVerified();
        isBotOwner = caller.equalsIgnoreCase(Global.botOwner);

        if (pmEvent.getUser() != null && pmEvent.getBot().getUserChannelDao().containsChannel(refChan)) {
          isChanOwner = pmEvent.getUser().getChannelsOwnerIn().contains(pmEvent.getBot().getUserChannelDao().getChannel(refChan));
        }
      } else {
        isVerified = pmEvent.getUser().isVerified();
        isBotOwner = caller.equalsIgnoreCase(Global.botOwner);
      }
      getCommands();
    }// END PRIVATE MESSAGE EVENT SPECIFIC PARSING
    else {
      isSupportedEventType = false;
    }
  }

  /**
   * If the message contains the command prefix as the first character, look for
   * command strings and parse them out.
   */
  private void getCommands() {

    // IF THE COMMAND STARTS WITH THE PREDETERMINED PREFIX AND CONTAINS MORE THAN JUST SPACES AND COMMAND CHARACTERS
    if (message.startsWith(Global.commandPrefix) && !message.matches("([ ]{0,}" + Global.commandPrefix + "{1,}[ ]{0,}){1,}")) {

      command = message.split(Global.commandPrefix)[1].split(" ")[0];
      cmdSplit = message.split(Global.commandPrefix, 2)[1].split(" ");
    }
  }

  /**
   * Checks that the input event can be parsed by this object
   *
   * @return TRUE if the event is supported
   */
  public boolean isSupportedEventType() {
    return isSupportedEventType;
  }

  /**
   * Verifies that the event sender is the bot owner, registered nick, and
   * logged in.
   *
   * @return TRUE if the event sender is the bot owner.
   */
  public boolean isVerifiedBotOwner() {
    return (isVerified && isBotOwner);
  }

  /**
   * Verifies that the user who sent the event is registered, logged in, and the
   * channel owner for the event (MessageEvents only).
   *
   * @return TRUE if the user is the channel owner and verified.
   */
  public boolean isVerifiedChanOwner() {
    return (isVerified && isBotOwner);
  }

  /**
   * Verifies that the user who sent the event is registered, logged in, and the
   * channel owner or the bot owner (MessageEvents only for channel owner).
   *
   * @return TRUE if the user is the channel owner or bot owner and verified.
   */
  public boolean isVerifiedChanBotOwner() {
    return (isVerified && (isBotOwner || isChanOwner));
  }

  /**
   * Verifies that the user is registered and logged in.
   *
   * @return TRUE if the user is registered and logged in.
   */
  public boolean isVerified() {
    return isVerified;
  }

  /**
   * Returns the nickname of the person who sent the event.
   *
   * @return name of the user who sent the event.
   */
  public String getCaller() {
    return (caller);
  }

  /**
   * Returns the message from the event.
   *
   * @return Message from the event.
   */
  public String getMessage() {
    return (message);
  }

  /**
   * Splits up the message for command parsing
   *
   * @return String[] of the message split up by spaces
   */
  public String[] getCommandSplit() {
    return (cmdSplit);
  }

  /**
   * Returns the command in the message if there was one
   *
   * @return String form of the command.
   */
  public String getCommand() {
    return (command);
  }

  /**
   * Returns the type of event that was processed.
   *
   * @return EventType of the event
   */
  public EventType getEventType() {
    return (eventType);
  }

  /**
   * If the event came from a MessageEvent, returns the channel for the event.
   *
   * @return Channel the event originated in.
   */
  public String getEventChannel() {
    return (channel);
  }

  /**
   * Gets the channel the command should be acted upon.
   * 
   * @return Channel name for the command.
   */
  public String getCommandChannel() {

    if (channel == null && refChan != null) {
      return (refChan);
    } else if (channel != null && refChan == null) {
      return (channel);
    } else if (channel != null && refChan != null) {

      if (!refChan.equalsIgnoreCase(channel)) {
        return (refChan);
      } else {
        return (channel);
      }
    } else {
      return (null);
    }
  }

  /**
   * Returns a location that the bot can respond to, either in a PM or to a
   * channel included in the message. EX: !send #channel message<br>
   * This will get #channel out of the message and return that, or it will
   * return the user who sent the private message.
   *
   * @return Location to respond to.
   */
  public String respondToCallerOrMessageChan() {
    if (eventType == EventType.PrivateMessageEvent && refChan == null) {
      return caller;
    } else {
      return getCommandChannel();
    }
  }

  /**
   * Returns the location the bot should respond to, ignoring any text in the
   * command. This will return a channel name if the event came from a channel,
   * or the nick for the user who sent the PM.
   *
   * @return Channel name or private message nick.
   */
  public String respondToIgnoreMessage() {
    if (eventType == EventType.PrivateMessageEvent) {
      return caller;
    } else {
      return getEventChannel();
    }
  }

  /**
   * Determines if the user is in the channel for the current event (assuming
   * the event is from a channel.
   *
   * @param user Username to check.
   * @return TRUE if the user is in the event channel. FALSE otherwise.
   */
  public boolean isUserInChannel(String user) {
    if (eventType != EventType.MessageEvent || user == null || channel == null) {
      return false;
    }
    return isUserInChannel(user, channel);
  }

  /**
   * Determines if the user is in the input channel.
   *
   * @param user Username to check
   * @param channel Channel to check the user being in.
   * @return TRUE if the user is in the channel.
   */
  public boolean isUserInChannel(String user, String channel) {
    if (eventType != EventType.MessageEvent || user == null || channel == null) {
      return false;
    }
    MessageEvent mEvent = (MessageEvent) event;
    if (mEvent.getBot().getUserChannelDao().containsChannel(channel) && mEvent.getBot().getUserChannelDao().containsUser(user)) {
      return (mEvent.getBot().getUserChannelDao().getChannels(mEvent.getBot().getUserChannelDao().getUser(user)).contains(mEvent.getBot().getUserChannelDao().getChannel(channel)));
    }
    return false;
  }
}
