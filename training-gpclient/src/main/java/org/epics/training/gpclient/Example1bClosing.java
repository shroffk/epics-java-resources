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
public class Example1bClosing {
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
        // Start a pv and forget to close, or forget to keep a handle so that
        // you can close it later.
        createReader();
        Thread.sleep(5000);
        
        // The GPClient will detect the garbage collection and help you find
        // leaked readers. This way, if your application has a bug, it will
        // not leak resources and you will be aware of what line of code
        // that created the pv that was later leaked.
        System.gc();
        
        Thread.sleep(5000);

        // This helps detect and correct the following bugs:
        // * open a new pv while not closing the old one
        // * open a new pv but forget to add to a list of managed pvs
        // * close a widget without closing all the used pvs
    }

}
