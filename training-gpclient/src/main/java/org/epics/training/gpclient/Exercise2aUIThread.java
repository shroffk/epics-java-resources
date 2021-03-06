package org.epics.training.gpclient;

import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVReader;
import org.epics.util.concurrent.Executors;
import org.epics.vtype.VType;

/**
 * Use notifyOn, tell the GPClient to send the event to the UI thread.
 * For example, use org.epics.util.concurrent.Executors.swingEDT()
 * to send events to the Swing thread.
 * 
 * @author Kunal Shroff
 *
 */
public class Exercise2aUIThread {
    
    public static void main(String[] args) throws InterruptedException {
        // The GPClient allows you to set what thread you want to get notify
        // on. This is not just a convenience: it is how the framework can
        // sense whether the client has a slow response. If the event is
        // redispatched to another thread, rate throttling will not work.
        
        PVReader<VType> noise = GPClient.read("sim://ramp")
            .notifyOn(Executors.swingEDT())
            .addListener((event) -> {
                System.out.println(event + " on " + Thread.currentThread());
            })
            .start();
        
        Thread.sleep(5000);
        
        noise.close();
    }

}
