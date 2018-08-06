package org.epics.training.gpclient.infrastructure;

import java.time.Duration;
import java.util.function.Consumer;
import org.epics.gpclient.CollectorExpression;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVEvent;
import org.epics.gpclient.PVReader;
import org.epics.gpclient.ReadCollector;
import org.epics.vtype.VNumber;
import org.epics.vtype.VType;

import static org.epics.gpclient.GPClient.*;

/**
 * 
 * @author Kunal Shroff
 *
 */
public class Example3CustomCollector {
    
    public static class MaxCollector extends ReadCollector<VNumber, VNumber> {
        
        private VNumber value;
        
        public MaxCollector() {
            super(VNumber.class);
        }

        @Override
        public VNumber getValue() {
            synchronized(lock) {
                // Returns the value and clears the queue
                VNumber latestValue = value;
                value = null;
                return latestValue;
            }
        }

        @Override
        public void updateValue(VNumber newValue) {
            Consumer<PVEvent> listener;
            synchronized(lock) {
                // Compare the new value with the old and keep the highest
                if (value == null) {
                    value = newValue;
                } else if (newValue == null) {
                    // Do nothing
                } else if (newValue.getValue().doubleValue() > value.getValue().doubleValue()) {
                    value = newValue;
                }
                listener = collectorListener;
            }
            // Run the task without holding the lock
            if (listener != null) {
                listener.accept(PVEvent.valueEvent());
            }
        }

        @Override
        public void updateValueAndConnection(VNumber value, boolean newConnection) {
            updateConnection(newConnection);
            updateValue(value);
        }
        
    }
    
    public static ReadCollector<VNumber, VNumber> maxWithinBurst() {
        return new MaxCollector();
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Reads a channel that picks a random number between 0 and 1 100 times
        // a second. Notify updates at 2 Hz and return the maximum with the 
        // update interval.
        PVReader<VNumber> noise = GPClient.read(channel("sim://noise(0,1,0.01)", maxWithinBurst()))
                .addReadListener((event, pvReader) -> {
                    System.out.println(event + " " + pvReader.getValue());
                })
                .maxRate(Duration.ofMillis(500))
                .start();

        // Compare when using cacheLastValue(VNumber.class) instead of maxWithinBurst()
        // and see the value oscillates around the average
        
        Thread.sleep(5000);
        
        noise.close();
    }
}
