/**
 * Copyright information and license terms for this software can be
 * found in the file LICENSE.TXT included with the distribution.
 */
package org.epics.training.util;

import org.epics.util.number.UnsignedConversions;

/**
 * Write a loop that increments a byte 256 times and prints its value as an
 * unsigned byte
 */
public class Exercise1aSolution {

    public static void main(String[] args) {
        byte unsigned = 0;
        for (int i = 0; i < 256; i++) {
            unsigned++;
            System.out.println("Unsigned byte value " + UnsignedConversions.toInt(unsigned));
        }
    }
    
}
