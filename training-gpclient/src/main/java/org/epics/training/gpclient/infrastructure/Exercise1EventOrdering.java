package org.epics.training.gpclient.infrastructure;

import org.epics.gpclient.CollectorExpression;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVReader;
import org.epics.vtype.VType;

/**
 * 
 * By using sending data directly to the read collector, experiment to see
 * how the GPClient aggregates events while keeping the ordering.
 * For example, notify an exception before or after a value and see that,
 * even if the event are aggregated, one can still know whether the exception
 * came before or after the value.
 * 
 * @author Kunal Shroff
 *
 */
public class Exercise1EventOrdering {
    
    public static void main(String[] args) throws InterruptedException {
        CollectorExpression<VType, VType, Object> collector = GPClient.collector();
        PVReader<VType> reader = GPClient.read(collector)
                .addReadListener((event, pvReader) -> {
                    System.out.println(event + " - " + pvReader.getValue());
                })
                .start();
        
        collector.getReadCollector().updateConnection(true);
        collector.getReadCollector().updateValue(VType.toVType(3.0));
        Thread.sleep(25);
        collector.getReadCollector().updateValue(VType.toVType("GPClient"));
        collector.getReadCollector().notifyError(new RuntimeException("Problem before the value"));
        Thread.sleep(45);
        collector.getReadCollector().notifyError(new RuntimeException("Problem after the value"));
        collector.getReadCollector().updateValue(VType.toVType("GPClient1"));
        Thread.sleep(45);
        collector.getReadCollector().notifyError(new RuntimeException("Problem before disconnection"));
        collector.getReadCollector().updateConnection(false);
        Thread.sleep(45);
        
        reader.close();
    }
}
