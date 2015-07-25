/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Objects.Shorten;

/**
 *
 * @author Stephen
 * 
 * Interface:
 *      ShortenerInterface
 * - This interface is used by link shorteners to standardize how the bot gets
 *   information from them
 * 
 * Methods:
 *     *getName  - Returns the name of the link shortener
 *     *getInfo  - Returns a string of information on the shortener, including
 *                 the ID and Name
 *     *getShortIdentifier - Returns a single letter identifier (the ID) of the
 *                           shortener, should be unique among link shorteners
 *     *shorten  - Returns a shortened URL using the shortener at hand, returns
 *                 null upon a failure
 * 
 */

public interface ShortenerInterface {
    public String shorten(String url);
    public boolean isShortIdentifier(String id);
    public String getInfo();
    public String getName();
}
