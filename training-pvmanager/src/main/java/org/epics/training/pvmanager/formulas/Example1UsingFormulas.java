package org.epics.training.pvmanager.formulas;

import java.time.Duration;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.formula.ExpressionLanguage;
import org.diirt.datasource.formula.FormulaRegistry;

/**
 * Creating formula function to process the data coming from the datasource
 * before notifying the client.
 * 
 * @author Kunal Shroff
 *
 */
public class Example1UsingFormulas {

    /**
     * Create a formula function for extracting an element at a given index from a
     * waveform
     */

    /**
     * Create an formula function that maps a pvStructure to a java map
     * 
     */
    
    public static void main(String[] args) throws InterruptedException {
        PVReader<?> formula = PVManager.read(ExpressionLanguage.formula("='sim://noise'+100"))
                .readListener((pvre) -> {
                    System.out.println(pvre.getPvReader().getValue());
                }).maxRate(Duration.ofMillis(50));
        
        Thread.sleep(5000);
        
        formula.close();
        
        
    }
        
}
