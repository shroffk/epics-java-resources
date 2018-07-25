/**
 * Copyright information and license terms for this software can be
 * found in the file LICENSE.TXT included with the distribution.
 */
package org.epics.training.util;

import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListNumber;

/**
 * Implement a ListDouble that lazily returns the reverse list of a given 
 * ListNumber.
 */
public class Exercise2aSolution {
    
    public static class ReverseListDouble extends ListDouble {
        
        private ListNumber list;

        public ReverseListDouble(ListNumber list) {
            this.list = list;
        }

        @Override
        public double getDouble(int index) {
            return list.getDouble(list.size() - index - 1);
        }

        @Override
        public int size() {
            return list.size();
        }
        
    }

    public static void main(String[] args) {
        ListDouble original = ArrayDouble.of(0,1,2,3,4,5);
        ListDouble reverse = new ReverseListDouble(original);
        
        System.out.println("Original list: " + original);
        System.out.println("Reversed list: " + reverse);
    }
    
}
