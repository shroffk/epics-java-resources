package org.epics.training.gpclient;

import java.time.Duration;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVEvent;
import org.epics.gpclient.PVReader;
import org.epics.vtype.VType;

/**
 * @author Kunal Shroff
 *
 */
public class Example1aConnection {

    public static void main(String[] args) throws InterruptedException {
        // Connect to a channel that every 1 second changes connection state and
        // returns the string "init" as a value
        PVReader<VType> noise = GPClient.read("sim://intermittentChannel(1, \"init\")")
            .addReadListener((event, pvReader) -> {
                if (event.isType(PVEvent.Type.READ_CONNECTION)) {
                    System.out.println("Connection changed: " + pvReader.isConnected());
                }
                if (event.isType(PVEvent.Type.VALUE)) {
                    System.out.println("Value changed: " + pvReader.getValue());
                }
            })
            .start();
        
        Thread.sleep(5000);
        
        noise.close();
    }

}
