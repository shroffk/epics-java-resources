package org.epics.training.gpclient;

import org.epics.gpclient.GPClient;
import static org.epics.gpclient.GPClient.*;
import org.epics.gpclient.PV;
import org.epics.vtype.VType;

public class Example4Write {

    public static void main(String[] args) throws InterruptedException {
        // When opening a channel, one can ask to be guaranteed that the values
        // are of a certain type (e.g. a gauge widget will only work with a 
        // numeric pv). If the type does not match, an error event is sent 
        // instead of a value event.
        
        PV<VType, Object> locA = GPClient.readAndWrite(channel("loc://a"))
                .addListener((event, pv) -> {
                    System.out.println(event + " - value: " + pv.getValue());
                })
                .start();

        // Wait for write connection event before writing
        Thread.sleep(100);
        
        // Write asynch with no callback: write result notified on the standard listener
        locA.write(42);
        
        Thread.sleep(1000);
        
        // Wrie asynch with callback: write result notified on the given callback
        // No notification on the standard listener
        locA.write(3.14, (event, pvWriter) -> {
            System.out.println("On callback - " + event);
        });
        
        Thread.sleep(1000);
        
        // Wrie synch: write result notified on this thread
        // No notification on the standard listener
        locA.writeAndWait(0);
        
        System.out.println("On thread - write succeded");
        
        locA.close();
        
        // The idea is that only one write event is returned, whether on the
        // standard listener, the ad-hoc listener or the current thread.
        // This one to one relationship between write events and writes
        // makes it easier to correlate them when building an application.
    }

}
