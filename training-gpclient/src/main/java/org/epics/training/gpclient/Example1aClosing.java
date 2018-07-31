package org.epics.training.gpclient;

import java.time.Duration;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVEvent;
import org.epics.gpclient.PVReader;
import org.epics.vtype.VType;

/**
 * @author Kunal Shroff
 *
 */
public class Example1aClosing {
    public static void createReader() {
        PVReader<VType> noise = GPClient.read("sim://noise")
            .addReadListener((event, pvReader) -> {
                if (event.isType(PVEvent.Type.READ_CONNECTION)) {
                    System.out.println("Connection changed: " + pvReader.isConnected());
                }
                if (event.isType(PVEvent.Type.VALUE)) {
                    System.out.println("Value changed: " + pvReader.getValue());
                }
            })
            .start();
    }

    public static void main(String[] args) throws InterruptedException {
        // You must keep a handle to the readers/writers so you can close them
        createReader();
        Thread.sleep(5000);
        
        // The GPClient will detect the garbage collection and help you find
        // leaked readers
        System.gc();
        
        Thread.sleep(50000);
        
    }

}
