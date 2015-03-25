package ws.leipold.felix.jgoodies.trigger;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: fleipold
 * Date: Jul 14, 2008
 * Time: 4:22:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class DelayedTriggerTest {
    boolean flag = false;
    @Test
    public void doNotFireBeforeTime(){
        Trigger originalTrigger = new Trigger();
        DelayedTrigger delayedTrigger = new DelayedTrigger(originalTrigger,100);
        delayedTrigger.addTiggerListener(new TriggerListener() {
            public void handleTrigger() {
                flag = true;
            }
        });
        originalTrigger.fire();
        Assert.assertFalse(flag);

    }


    @Test
    public void doFireAfterTime() throws InterruptedException {
        Trigger originalTrigger = new Trigger();
        DelayedTrigger delayedTrigger = new DelayedTrigger(originalTrigger,10);
        delayedTrigger.addTiggerListener(new TriggerListener() {
            public void handleTrigger() {
                flag = true;
            }
        });
        originalTrigger.fire();
        Thread.sleep(1600);
        Assert.assertTrue(flag);

    }


}