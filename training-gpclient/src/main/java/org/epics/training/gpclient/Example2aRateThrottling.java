package org.epics.training.gpclient;

import java.time.Duration;
import java.util.List;
import java.util.Random;
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
        // Rate throttling adapts the rate of event generatation to the rate
        // the client can process. This avoids clients that get stuck processing
        // stale data (e.g. a UI that shows data from 30 seconds before).
        
        System.out.println("Fast client: will go through all values");
        PVReader<VType> ramp = GPClient.read("sim://ramp")
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .start();
        
        Thread.sleep(5000);
        
        ramp.close();
        
        final Random rand = new Random(0);
        
        System.out.println("\nSlow client: will occasionally skip a value to catch up");
        PVReader<VType> slowRamp = GPClient.read("sim://ramp")
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                    if (rand.nextBoolean()) {
                        System.out.println("Slow processing");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                })
                .start();
        
        Thread.sleep(5000);
        
        slowRamp.close();
        
        // Note that nothing has to be done at the client side. This behavior
        // is adaptive and automatic
    }
}
