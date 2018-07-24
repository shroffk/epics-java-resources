/**
 * Copyright information and license terms for this software can be
 * found in the file LICENSE.TXT included with the distribution.
 */
package org.epics.training.util;

import org.epics.util.array.ArrayByte;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInteger;
import org.epics.util.array.CollectionNumbers;
import org.epics.util.array.IteratorNumber;
import org.epics.util.array.ListByte;
import org.epics.util.array.ListInteger;
import org.epics.util.array.ListNumber;
import org.epics.util.number.UByte;
import org.epics.util.number.UInteger;
import org.epics.util.number.ULong;
import org.epics.util.number.UShort;
import org.epics.util.number.UnsignedConversions;

/**
 *
 */
public class NumericCollections {

    public static void main(String[] args) {
        // Wrap an array to a numeric collection
        int[] intArray = new int[] {1,2,3,4,5};
        ListNumber list = CollectionNumbers.toList(intArray);
        
        // Print information about the numeric list
        System.out.println("List values " + list);
        System.out.println("Contains integers: " + (list instanceof ListInteger));
        System.out.println("Contains bytes: " + (list instanceof ListByte));
        
        // Read and write values
        System.out.println("List at 0 " + list.getLong(0));
        list.setDouble(0, 3.14);
        System.out.println("List at 0 " + list.getLong(0));
        // Note: the original array changed
        System.out.println("Original array at 0 " + intArray[0]);

        // Read-only wrapper
        list = ArrayInteger.of(intArray);
        try {
            list.setDouble(0, 3.14);
        } catch (UnsupportedOperationException e) {
            System.out.println("Can't modify the list");
        }
        
        // Calculate average independently of type
        list = ArrayByte.of(new byte[] {1,2,3,4,5});
        System.out.println("Average of " + list + " is " + calculateAverage(list));
        list = ArrayDouble.of(1,2,3,4,5);
        System.out.println("Average of " + list + " is " + calculateAverage(list));
        
    }

    public static double calculateAverage(ListNumber list) {
        if (list.size() == 0) {
            return Double.NaN;
        }
        
        IteratorNumber iter = list.iterator();
        double total = 0;
        while (iter.hasNext()) {
            total += iter.nextDouble();
        }
        
        return total / list.size();
    }
}
