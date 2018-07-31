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
        // Open a channel that creates 100 samples per seconds but ask
        // to be notified only every half second with the latest value
        PVReader<VType> latestValue = GPClient.read("sim://noise(0,1,0.01)")
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .maxRate(Duration.ofMillis(500))
                .start();
        
        Thread.sleep(2000);
        
        latestValue.close();
        
        // Open a channel that creates 100 samples per seconds but ask
        // to be notified only every half second with all values
        PVReader<List<VType>> allValues = GPClient.read(channel("sim://noise(0,1,0.01)", queueAllValues(VType.class)))
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .maxRate(Duration.ofMillis(500))
                .start();
        
        Thread.sleep(2000);
        
        allValues.close();
        
        
    }

    /**
     * Read the last event value from a high event rate scalar pv
     */

    /**
     * Read all the event values as a list of events from a high event rate scalar
     * pv
     */

    /**
     * Read the last event value from a high event rate waveform pv
     */

    /**
     * Read all the event values as a list of events from a high event rate waveform
     * pv
     */
}
