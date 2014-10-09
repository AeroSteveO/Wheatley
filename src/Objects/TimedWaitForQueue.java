/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import Wheatley.Global;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public  class TimedWaitForQueue extends WaitForQueue{
    int time;
    private QueueTime runnable = null;
    Thread t;
    public TimedWaitForQueue(PircBotX bot,int time, Channel chan,User user, int key) throws InterruptedException {
        super(bot);
        this.time=time;
        QueueTime runnable = new QueueTime(Global.bot,time,chan,user,key);
        this.t = new Thread(runnable);
        runnable.giveT(t);
        t.start();
    }
    public TimedWaitForQueue(MessageEvent event, int time, int key) throws InterruptedException {
        super(event.getBot());
        this.time=time;
        QueueTime runnable = new QueueTime(Global.bot,time,event.getChannel(),event.getBot().getUserBot(),key);
        this.t = new Thread(runnable);
        runnable.giveT(t);
        t.start();
    }
    public void end() throws InterruptedException{
        this.close(); //Close this EventQueue
        t.join(1000); //Ensure the thread also closes
    }
}
