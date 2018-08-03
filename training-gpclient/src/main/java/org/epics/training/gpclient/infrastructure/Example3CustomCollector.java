package org.epics.training.gpclient.infrastructure;

import java.util.function.Consumer;
import org.epics.gpclient.CollectorExpression;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVEvent;
import org.epics.gpclient.PVReader;
import org.epics.gpclient.ReadCollector;
import org.epics.vtype.VNumber;
import org.epics.vtype.VType;

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
                return value;
            }
        }

        @Override
        public void updateValue(VNumber newValue) {
            Consumer<PVEvent> listener;
            synchronized(lock) {
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
    
    public static void main(String[] args) throws InterruptedException {
    }
}
