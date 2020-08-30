/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rapternet.irc.bots.common.objects;

import org.junit.After;		
import org.junit.AfterClass;		
import org.junit.Before;		
import org.junit.BeforeClass;		
import org.junit.Ignore;		
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Stephen
 */
public class SettingsTest {
  
    @Test
    public void testSettings() {
        Settings s = new Settings();
        List<String> l = new ArrayList<>();
        l.add("testKey");
        l.add("newVal");
        s.create(l);
        assertEquals("newVal", s.get("testKey"));

        assertTrue(s.contains("testKey"));
        assertTrue(s.contains("testkey")); // keys are case insensitive

        boolean success = s.set("testKey","testVal");
        assertTrue(success);
        assertEquals("testVal", s.get("testKey"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedCreate(){
        Settings s = new Settings();
        s.create(new ArrayList<String>());
    }
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedCreate2(){
        Settings s = new Settings();
        List<String> l = new ArrayList<>();
        l.add("oneSetting");
        s.create(l);
    }
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedChannelSetting(){
        Settings s = new Settings();
        List<String> l = new ArrayList<>();
        l.add("#ircchannel");
        l.add("settingitem");
        s.create(l);
    }

    @Test
    public void testChannelList() {
        Settings s = new Settings();
        List<String> l = new ArrayList<>();
        l.add("#channel");
        l.add("setting");
        l.add("option");

        s.create(l);

        List<String> channels = s.getChannelList();

        assertEquals(1, channels.size());
        assertEquals("#channel", channels.get(0));

        l.set(0, "#chan");
        s.create(l);

        channels = s.getChannelList();
        assertEquals(2, channels.size());
        assertTrue(channels.contains("#chan"));
        assertTrue(channels.contains("#channel"));
    }
}
