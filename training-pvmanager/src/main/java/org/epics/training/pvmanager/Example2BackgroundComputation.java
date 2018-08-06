package org.epics.training.pvmanager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.diirt.datasource.PVManager;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PV;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.expression.WriteMap;
import org.diirt.util.array.ListNumber;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;
import org.diirt.vtype.ValueUtil;

/**
 * 
 * @author Kunal Shroff
 *
 */
public class Example2BackgroundComputation {
    
    public static void main(String[] args) throws InterruptedException {
        
        // PVManager allows to put move computation off in the background
        // thread pool, so that the value is already calculated before the
        // notification. This is particularly helpful when the notification
        // has to happen on a single threaded subsystem (like a UI).
        // Note that thread throttling and thread limiting will make sure
        // that the calculation is only performed if the receiving
        // subsystem is ready to process the data.
        
        PVReader<VDouble> average = PVManager.read(resultOf((VNumberArray array) -> {
            ListNumber data = array.getData();

            if (data.size() == 0) {
                return ValueFactory.newVDouble(0.0);
            }

            double avg = 0;
            for (int i = 0; i < data.size(); i++) {
                avg = data.getDouble(i);
            }

            return ValueFactory.newVDouble(avg);
        }, latestValueOf(channel("sim://noiseWaveform(-5,5,0.1)", VNumberArray.class, Object.class))))
                .readListener((PVReaderEvent<Object> pvre) -> {
                    System.out.println("Average: " + pvre.getPvReader().getValue());
                })
                .maxRate(Duration.ofMillis(200));

        Thread.sleep(5000);
        
        average.close();
        
    }
}
