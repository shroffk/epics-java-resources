/**
 * Copyright information and license terms for this software can be
 * found in the file LICENSE.TXT included with the distribution.
 */
package org.epics.training.util;

import org.epics.util.number.UByte;
import org.epics.util.number.UInteger;
import org.epics.util.number.ULong;
import org.epics.util.number.UShort;
import org.epics.util.number.UnsignedConversions;

/**
 *
 */
public class Example1UnsignedIntegers {

    public static void main(String[] args) {
        byte byteValue = (byte) 255;
        System.out.println("Unsigned byte value: " + UnsignedConversions.toInt(byteValue));
        long longValue = -1;
        System.out.println("Unsigned long converted to BigInteger: " + UnsignedConversions.toBigInteger(longValue));
        
        Number number = new UByte((byte) 167);
        System.out.println("Unsigned byte value: " + number);
        number = new UShort((short) 65535);
        System.out.println("Unsigned short value: " + number);
        number = new UInteger((int) 4294967295L);
        System.out.println("Unsigned int value: " + number);
        number = new ULong(-1);
        System.out.println("Unsigned long value: " + number);
        
        number = new UShort((short) 54321);
        System.out.println("Unsigned byte value converted to");
        System.out.println("  byte: " + number.byteValue());
        System.out.println("  short: " + number.shortValue());
        System.out.println("  int: " + number.intValue());
        System.out.println("  long: " + number.longValue());
        System.out.println("  float: " + number.floatValue());
        System.out.println("  double: " + number.doubleValue());
    }
    
}
