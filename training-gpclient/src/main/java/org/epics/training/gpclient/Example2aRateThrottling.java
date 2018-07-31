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
public class Example2aRateThrottling {
    
    public static void main(String[] args) throws InterruptedException {
        // This client will process events fast enough to go through
        // all values
        PVReader<VType> ramp = GPClient.read("sim://ramp")
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .start();
        
        Thread.sleep(5000);
        
        ramp.close();
        
        // This client will process events at a rate slower than they are produced.
        // GPClient will automatically adapt the rate and skip the events in between.
        PVReader<VType> slowRamp = GPClient.read("sim://ramp")
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                })
                .start();
        
        Thread.sleep(5000);
        
        slowRamp.close();
        
    }
}
