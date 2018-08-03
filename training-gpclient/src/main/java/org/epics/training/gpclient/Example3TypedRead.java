package org.epics.training.gpclient;

import org.epics.gpclient.GPClient;
import static org.epics.gpclient.GPClient.*;
import org.epics.gpclient.PVEvent;
import org.epics.gpclient.PVReader;
import org.epics.vtype.VDouble;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;

public class Example3TypedRead {

    public static void main(String[] args) throws InterruptedException {
        // When opening a channel, one can ask to be guaranteed that the values
        // are of a certain type (e.g. a gauge widget will only work with a 
        // numeric pv). If the type does not match, an error event is sent 
        // instead of a value event.
        
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
                    if (event.isType(PVEvent.Type.EXCEPTION) && event.getException() instanceof IllegalArgumentException) {
                        // TODO Create TypeMismatch exception
                        pvReader.close();
                    }
                })
                .start();
        
        Thread.sleep(5000);
        
        notNumber.close();
        
        // Note: you can close the pv within the listener (from whatever thread) and you can close it twice.
    }

}
