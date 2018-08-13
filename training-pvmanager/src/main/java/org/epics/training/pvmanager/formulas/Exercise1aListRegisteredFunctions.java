package org.epics.training.pvmanager.formulas;

import java.time.Duration;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.formula.ExpressionLanguage;
import org.diirt.datasource.formula.FormulaFunction;
import org.diirt.datasource.formula.FormulaFunctions;
import org.diirt.datasource.formula.FormulaRegistry;

/**
 * Creating formula function to process the data coming from the datasource
 * before notifying the client.
 * 
 * @author Kunal Shroff
 *
 */
public class Exercise1aListRegisteredFunctions {

    /**
     * Create a formula function for extracting an element at a given index from a
     * waveform
     */

    /**
     * Create an formula function that maps a pvStructure to a java map
     * 
     */
    
    public static void main(String[] args) {
        for (String functionSet : FormulaRegistry.getDefault().listFunctionSets()) {
            System.out.println("Functions for " + functionSet);
            for (FormulaFunction function : FormulaRegistry.getDefault().findFunctionSet(functionSet).getFunctions()) {
                System.out.println(FormulaFunctions.formatSignature(function));
            }
            System.out.println();
        }
    }
        
}
