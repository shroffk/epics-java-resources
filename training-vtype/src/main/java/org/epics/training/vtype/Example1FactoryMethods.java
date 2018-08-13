package org.epics.training.vtype;

import org.epics.util.number.UByte;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VString;
import org.epics.vtype.VType;

public class Example1FactoryMethods {

    public static void main(String[] args) {
        // Wrap standard Java object into a VType
        System.out.println(VType.toVType(3.14));
        System.out.println(VType.toVType("Hello!"));
        System.out.println(VType.toVType(3L));
        System.out.println(VType.toVType(new UByte((byte) -1)));
        System.out.println(VType.toVType(null));
        System.out.println(VType.toVType(new Object()));
        System.out.println();
        
        // Unconvertible types are converted to null. You can
        // use the checked conversion to make them throw an
        // exception
        try {
            System.out.println(VType.toVTypeChecked(3.14));
            System.out.println(VType.toVTypeChecked("Hello!"));
            System.out.println(VType.toVTypeChecked(3L));
            System.out.println(VType.toVTypeChecked(new UByte((byte) -1)));
            System.out.println(VType.toVTypeChecked(new Object()));
        } catch(IllegalArgumentException ex) {
            System.out.println("Couldn't convert type: " + ex.getMessage());
        }
        System.out.println();
        
        // We can add alarm, time and display information
        // The unused metadata (e.g. the diaplay for the string) is ignored
        System.out.println(VType.toVType(3.14, Alarm.low(), Time.now(), Display.none()));
        System.out.println(VType.toVType("Hello!", Alarm.low(), Time.now(), Display.none()));
        System.out.println();

        // The previous methods convert a generic object to a VType.
        // All types provide more specific static factory methods.
        
        VDouble vDouble = VDouble.of(3.14, Alarm.high(), Time.now(), Display.none());
        System.out.println(vDouble);
        
        VString vString = VString.of("Hello", Alarm.lolo(), Time.now());
        System.out.println(vString);
        System.out.println();
        
        // We can use these methods to create a new value with the same metadata
        
        VDouble vDouble2 = VDouble.of(42, vDouble.getAlarm(), vDouble.getTime(), vDouble.getDisplay());
        System.out.println(vDouble2);
    }
}
