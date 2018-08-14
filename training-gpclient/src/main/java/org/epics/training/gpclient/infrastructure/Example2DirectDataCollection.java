package org.epics.training.gpclient.infrastructure;

import org.epics.gpclient.CollectorExpression;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVReader;
import org.epics.vtype.VType;

/**
 * 
 * @author Kunal Shroff
 *
 */
public class Example2DirectDataCollection {
    
    public static void main(String[] args) throws InterruptedException {
        // Unlike PVManager, the GPClient is designed so that data can be
        // gathered from anywhere. The Datasource is the first standard implemented
        // way to gather data. Services and user input are also planned.
        
        // Here we show how we can get a collector and start sending events
        // to a reader.
        
        CollectorExpression<VType, VType, Object> collector = GPClient.collector();
        PVReader<VType> reader = GPClient.read(collector)
                .addReadListener((event, pvReader) -> {
                    System.out.println(event + " - " + pvReader.getValue());
                })
                .start();
        
        collector.getReadCollector().updateValueAndConnection(VType.toVType(3.0), true);
        Thread.sleep(50);
        // Event burst: some of these events are going to be skipped
        collector.getReadCollector().updateValue(VType.toVType("GPClient"));
        collector.getReadCollector().updateValue(VType.toVType("GPClient1"));
        collector.getReadCollector().updateValue(VType.toVType("GPClient2"));
        collector.getReadCollector().updateValue(VType.toVType("GPClient3"));
        collector.getReadCollector().updateValue(VType.toVType("GPClient4"));
        collector.getReadCollector().updateValue(VType.toVType("GPClient5"));
        Thread.sleep(50);
        collector.getReadCollector().updateValue(VType.toVType(new double[] {0,1,2,3,4}));
        Thread.sleep(50);
        collector.getReadCollector().notifyError(new RuntimeException("There was a problem"));
        Thread.sleep(50);
        collector.getReadCollector().updateValue(null);
        Thread.sleep(50);
        collector.getReadCollector().updateConnection(false);
        Thread.sleep(50);
        
        reader.close();
    }
}
