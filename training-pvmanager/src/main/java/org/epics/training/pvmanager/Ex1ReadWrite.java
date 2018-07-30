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
import org.diirt.vtype.ValueUtil;

/**
 * Refer to pvmanager-sample project
 * @author Kunal Shroff
 *
 */
public class Ex1ReadWrite {
    
    public static class BackgroundLogic {

        private final PV<Map<String, Object>, Map<String, Object>> outputs;
        private final PVReader<Map<String, Object>> inputs;
        
        public BackgroundLogic() {
        outputs = PVManager.readAndWrite(mapOf(latestValueOf(channels("loc://output/sum", "loc://output/difference", "loc://output/product"))))
                .readListener(new PVReaderListener<Map<String, Object>>() {
                    public void pvChanged(PVReaderEvent<Map<String, Object>> pvre) {
                        if (pvre.isValueChanged()) {
                            System.out.println("Ooutput changed: " + pvre.getPvReader().getValue());
                        }
                    }
                })
                .asynchWriteAndMaxReadRate(Duration.ofMillis(50));

        inputs = PVManager.read(mapOf(latestValueOf(channels("loc://input/a", "loc://input/b"))))
                .readListener(new PVReaderListener<Map<String, Object>>() {
                    public void pvChanged(PVReaderEvent<Map<String, Object>> pvre) {
                        Map<String, Object> value = pvre.getPvReader().getValue();
                        System.out.println("Input changed: " + value);
                        if (value != null && value.containsKey("loc://input/a") && value.containsKey("loc://input/b")) {
                            Double a = ValueUtil.numericValueOf(pvre.getPvReader().getValue().get("loc://input/a"));
                            Double b = ValueUtil.numericValueOf(pvre.getPvReader().getValue().get("loc://input/b"));

                            Map<String, Object> newValues = new HashMap<String, Object>();
                            newValues.put("loc://output/sum", a+b);
                            newValues.put("loc://output/difference", a-b);
                            newValues.put("loc://output/product", a*b);
                            outputs.write(newValues);
                        }
                    }
                })
                .maxRate(Duration.ofMillis(50));
        }
        
        public void close() {
            inputs.close();
            outputs.close();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        BackgroundLogic bgLogic = new BackgroundLogic();
        
        PVReader<Object> product = PVManager.read(channel("loc://output/product"))
                .readListener(new PVReaderListener<Object>() {
                    public void pvChanged(PVReaderEvent<Object> pvre) {
                        if (pvre.isValueChanged()) {
                            System.out.println("Product changed: " + pvre.getPvReader().getValue());
                        }
                    }
                })
                .maxRate(Duration.ofMillis(50));
        
        PV<Object, Object> a = PVManager.readAndWrite(channel("loc://input/a"))
                .synchWriteAndMaxReadRate(Duration.ofMillis(50));
        
        PV<Object, Object> b = PVManager.readAndWrite(channel("loc://input/b"))
                .synchWriteAndMaxReadRate(Duration.ofMillis(50));
        
        a.write(10);
        b.write(2);
        Thread.sleep(1000);
        
        a.write(11);
        Thread.sleep(1000);
        
        a.write(13);
        b.write(-1);
        Thread.sleep(1000);
        
        product.close();
        a.close();
        b.close();
        
        bgLogic.close();
        
    }
}
