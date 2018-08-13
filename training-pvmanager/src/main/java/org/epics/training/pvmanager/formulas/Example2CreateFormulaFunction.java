package org.epics.training.pvmanager.formulas;

import java.time.Duration;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.formula.AbstractVNumberArrayVNumberToVNumberArrayFormulaFunction;
import org.diirt.datasource.formula.ExpressionLanguage;
import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FormulaFunctionSetDescription;
import org.diirt.datasource.formula.FormulaRegistry;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListNumber;

/**
 * Creating formula function to process the data coming from the datasource
 * before notifying the client.
 * 
 * @author Kunal Shroff
 *
 */
public class Example2CreateFormulaFunction {
    
    public static class ArrayConstantSumFunction extends AbstractVNumberArrayVNumberToVNumberArrayFormulaFunction {

        public ArrayConstantSumFunction() {
            super("+", "Sums an array to a constant", "An array value", "A numeric value");
        }

        @Override
        public ListNumber calculate(ListNumber arg1, double arg2) {
            return new ListDouble() {
                @Override
                public double getDouble(int i) {
                    return arg1.getDouble(i) + arg2;
                }

                @Override
                public int size() {
                    return arg1.size();
                }
            };
        }
        
    }

    /**
     * Create a formula function for extracting an element at a given index from a
     * waveform
     */

    /**
     * Create an formula function that maps a pvStructure to a java map
     * 
     */
    
    public static void main(String[] args) throws InterruptedException {
        FormulaFunctionSet formulaFunctionSet = new FormulaFunctionSet(new FormulaFunctionSetDescription("custom", "my test functions").addFormulaFunction(new ArrayConstantSumFunction())) {};
        FormulaRegistry.getDefault().registerFormulaFunctionSet(formulaFunctionSet);
        
        PVReader<?> formula = PVManager.read(ExpressionLanguage.formula("='sim://noiseWaveform'+100"))
                .readListener((pvre) -> {
                    System.out.println(pvre.getPvReader().getValue());
                }).maxRate(Duration.ofMillis(50));
        
        Thread.sleep(5000);
        
        formula.close();
        
        
    }
        
}
