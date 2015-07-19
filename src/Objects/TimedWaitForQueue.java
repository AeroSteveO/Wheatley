/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    QueueTime
 * - Linked Classes
 *    N/A
 * 
 */
public  class TimedWaitForQueue extends WaitForQueue{
    int time;
    private QueueTime runnable = null;
    Thread t;
    
    public TimedWaitForQueue(MessageEvent event, int time, int key) throws InterruptedException {
        super(event.getBot());
        this.time=time;
        QueueTime runnable = new QueueTime(event, time, key);
        this.t = new Thread(runnable);
        runnable.giveT(t);
        t.start();
    }

    public void end() throws InterruptedException{
        this.t.interrupt();
        this.close(); //Close this EventQueue
        t.join(1000); //Ensure the thread also closes
    }
}