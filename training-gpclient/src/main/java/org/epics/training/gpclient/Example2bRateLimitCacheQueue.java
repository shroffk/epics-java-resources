package org.epics.training.gpclient;

import java.time.Duration;
import java.util.List;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVReader;
import org.epics.vtype.VType;

import static org.epics.gpclient.GPClient.*;

/**
 * 
 * @author Kunal Shroff
 *
 */
public class Example2bRateLimitCacheQueue {
    
    public static void main(String[] args) throws InterruptedException {
        // Rate limiting puts a maximum to the incoming rate. This avoids
        // the receiver subsystem (e.g. UI, script engine, ...) to be overwhelmed
        // by the incoming rate. It also allows to cut down unneeded processing
        // (e.g. refreshing data on a UI faster than the eye can see, logging
        // more data than is needed, ...)
        
        // As the rate is limited, one can specify whether the latest value
        // is sufficient or all values from the last update are needed.
        
        System.out.println("Opens a 100Hz pv, notified at 2Hz with the latest value");
        PVReader<VType> latestValue = GPClient.read("sim://noise(0,1,0.01)")
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .maxRate(Duration.ofMillis(500))
                .start();
        
        Thread.sleep(2000);
        
        latestValue.close();
        
        System.out.println("\nOpens a 100Hz pv, notified at 2Hz with the all values");
        PVReader<List<VType>> allValues = GPClient.read(channel("sim://noise(0,1,0.01)", queueAllValues(VType.class)))
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .maxRate(Duration.ofMillis(500))
                .start();
        
        Thread.sleep(2000);
        
        allValues.close();
        
        
    }
}
