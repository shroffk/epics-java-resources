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
 * 
 * @author Kunal Shroff
 *
 */
public class Example2CreateFormulaFunction {
    
    // A Formula functions that adds a constant to an array
    // Instead of implementing directly a FormulaFunction, we use one of the
    // abstract functions that has most of the implementation done.
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

    
    public static void main(String[] args) throws InterruptedException {
        // Register the function in the registry. This would be typically done
        // with a ServiceLoader
        FormulaFunctionSet formulaFunctionSet = new FormulaFunctionSet(new FormulaFunctionSetDescription("custom", "my test functions").addFormulaFunction(new ArrayConstantSumFunction())) {};
        FormulaRegistry.getDefault().registerFormulaFunctionSet(formulaFunctionSet);

        // Simply use the new function in an expression.
        PVReader<?> formula = PVManager.read(ExpressionLanguage.formula("='sim://noiseWaveform'+100"))
                .readListener((pvre) -> {
                    System.out.println(pvre.getPvReader().getValue());
                }).maxRate(Duration.ofMillis(50));
        
        Thread.sleep(5000);
        
        formula.close();
        
        
    }
        
}
