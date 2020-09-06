package rapternet.irc.bots;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import rapternet.irc.bots.common.objects.GameListTest;
import rapternet.irc.bots.common.objects.SettingsTest;
import rapternet.irc.bots.wheatley.objects.games.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SettingsTest.class,
        GameListTest.class,
        NoModifyWordTest.class,
        BlankWordTest.class,
        ShuffleWordTest.class,
        ReverseWordTest.class,
})
public class WheatleySuite {

}
