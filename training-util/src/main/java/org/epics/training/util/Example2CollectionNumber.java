/**
 * Copyright information and license terms for this software can be
 * found in the file LICENSE.TXT included with the distribution.
 */
package org.epics.training.util;

import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInteger;
import org.epics.util.array.ArrayUByte;
import org.epics.util.array.CollectionNumber;
import org.epics.util.array.IteratorNumber;

/**
 *
 */
public class Example2CollectionNumber {

    public static void main(String[] args) {
        // Calculate average independently of type
        CollectionNumber coll = ArrayUByte.of(new byte[] {-1,-2,-3,-4,-5});
        System.out.println("Average of " + coll + " is " + calculateAverage(coll));
        coll = ArrayDouble.of(1,2,3,4,5);
        System.out.println("Average of " + coll + " is " + calculateAverage(coll));
        coll = ArrayInteger.of(1,2,3,4,5);
        System.out.println("Average of " + coll + " is " + calculateAverage(coll));
        
    }

    public static double calculateAverage(CollectionNumber coll) {
        if (coll.size() == 0) {
            return Double.NaN;
        }
        
        IteratorNumber iter = coll.iterator();
        double total = 0;
        while (iter.hasNext()) {
            total += iter.nextDouble();
        }
        
        return total / coll.size();
    }
}
