package org.epics.training.pvmanager.formulas;

import java.time.Duration;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.formula.ExpressionLanguage;
import org.diirt.datasource.formula.FormulaRegistry;

/**
 * 
 * @author Kunal Shroff
 *
 */
public class Example1UsingFormulas {
    
    public static void main(String[] args) throws InterruptedException {
        // To use formulas, use expression language from the formula package
        PVReader<?> formula = PVManager.read(ExpressionLanguage.formula("='sim://noise'+100"))
                .readListener((pvre) -> {
                    System.out.println(pvre.getPvReader().getValue());
                }).maxRate(Duration.ofMillis(50));
        
        Thread.sleep(5000);
        
        formula.close();
        
        
    }
        
}
