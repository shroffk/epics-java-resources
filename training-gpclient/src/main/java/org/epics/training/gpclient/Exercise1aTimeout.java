package org.epics.training.gpclient;

import java.time.Duration;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVReader;
import org.epics.vtype.VType;

/**
 * Add a connection timeout while reading a channel. To test, use channel
 * sim://delayedConnectionChannel(2, "init") which will connect with a 
 * two-second delay and then send the string value "init"
 * 
 * @author Kunal Shroff
 *
 */
public class Exercise1aTimeout {

    public static void main(String[] args) throws InterruptedException {
        // Connect to a channel that connects with a 2 seconds delay
        PVReader<VType> noise = GPClient.read("sim://delayedConnectionChannel(2, \"init\")")
            .connectionTimeout(Duration.ofSeconds(1), "Still waiting...")
            .addListener((event) -> {
                System.out.println(event);
            })
            .start();
        
        Thread.sleep(5000);
        
        noise.close();
    }

}
