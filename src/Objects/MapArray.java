/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class handles a log of messages for a set of channels. It maps a List of
 * username and messages to a channel name.
 *
 * @author Stephen
 */
public class MapArray {

  Map<String, ArrayList<ArrayList<String>>> log = Collections.synchronizedMap(new TreeMap<String, ArrayList<ArrayList<String>>>());
  private final int size;

  public MapArray(int size) {
    this.size = size;
  }

  /**
   * Gets the backing List structure for the input channel
   *
   * @param channel Channel whos log should be output
   * @return List of the channels log
   */
  public ArrayList<ArrayList<String>> getArray(String channel) {
    synchronized (log) {
      channel = channel.toLowerCase();
      if (log.containsKey(channel)) {
        ArrayList<ArrayList<String>> newArray = new ArrayList<>();
        newArray.addAll(log.get(channel));
        return newArray;
      } else {
        return null;
      }
    }
  }

  /**
   * Determines if the log for the input channel is empty. Returns true if the
   * log List is empty, and false otherwise.
   *
   * @param channel Channel who's log should be evaluated
   * @return
   */
  public boolean isEmpty(String channel) {
    synchronized (log) {
      channel = channel.toLowerCase();
      if (!log.containsKey(channel) || getArray(channel).isEmpty()) {
        return true;
      }
      return false;
    }
  }

  /**
   * Adds the input message to the log List for the input channel
   *
   * @param channel Channel whos log should be appended
   * @param message Message to append to the log
   */
  public void addToLog(String channel, String message) {
    synchronized (log) {
      ArrayList<String> array = new ArrayList<>();
      array.add(message);
      addToLog(channel.toLowerCase(), array);
    }
  }

  /**
   * Adds the input message List to the input channel's log. The message list
   * should be formatted as a list containing the username in the first element,
   * and the message in the second.
   *
   * @param channel Channel who's log should be appended
   * @param message Message to append to the log
   */
  public void addToLog(String channel, ArrayList<String> message) {
    synchronized (log) {
      channel = channel.toLowerCase();
      if (!log.containsKey(channel)) {
        ArrayList<ArrayList<String>> channelLog = new ArrayList<>();
        channelLog.add(message);
        log.put(channel, channelLog);
      } else {
        log.get(channel).add(message);
        if (log.get(channel).size() > size) {
          log.get(channel).remove(0);
        }
      }
    }
  }
}
