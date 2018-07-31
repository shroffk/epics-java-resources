package org.epics.training.gpclient;

import org.epics.gpclient.GPClient;
import static org.epics.gpclient.GPClient.*;
import org.epics.gpclient.PVReader;
import org.epics.vtype.VDouble;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;

public class Example3TypedRead {

    public static void main(String[] args) throws InterruptedException {
        PVReader<VDouble> ramp = GPClient.read(channel("sim://ramp", cacheLastValue(VDouble.class)))
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .start();
        
        Thread.sleep(5000);
        
        ramp.close();
        
        PVReader<VString> strings = GPClient.read(channel("sim://strings", cacheLastValue(VString.class)))
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .start();
        
        Thread.sleep(5000);
        
        strings.close();

        PVReader<VNumber> noise = GPClient.read(channel("sim://noise", cacheLastValue(VNumber.class)))
                .addReadListener((event, pvReader) -> {
                    System.out.println("Value: " + pvReader.getValue());
                })
                .start();
        
        Thread.sleep(5000);
        
        noise.close();

        PVReader<VNumber> notNumber = GPClient.read(channel("sim://strings", cacheLastValue(VNumber.class)))
                .addReadListener((event, pvReader) -> {
                    System.out.println(event);
                })
                .start();
        
        Thread.sleep(5000);
        
        notNumber.close();
    }

}
