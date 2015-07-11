/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Objects.Shorten;

/**
 *
 * @author Stephen
 */
public interface ShortenerInterface {
    public String shorten(String url);
    public boolean isShortIdentifier(String id);
    public String getInfo();
    public String getName();
}
