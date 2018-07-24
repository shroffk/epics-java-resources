/**
 * Copyright information and license terms for this software can be
 * found in the file LICENSE.TXT included with the distribution.
 */
package org.epics.training.util;

import java.util.Arrays;
import java.util.List;
import org.epics.util.number.UByte;
import org.epics.util.number.ULong;

/**
 * Create a list of numbers that mixes signed and unsigned wrapper. First print
 * the list and then print the type and value of each element on a new line.
 */
public class Exercise1bSolution {

    public static void main(String[] args) {
        List<Number> numbers = Arrays.asList(123, 45L, 13.0, new UByte((byte)234), new ULong(-1));
        System.out.println("Numbers: " + numbers);
        
        for (Number number : numbers) {
            System.out.println(number.getClass().getSimpleName() + " " + number);
        }
    }
    
}
