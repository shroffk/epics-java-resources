package org.epics.training.vtype;

import org.epics.util.number.UByte;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Scalar;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VType;

public class Example2GenericClient {

    public static void main(String[] args) {
        // The utility of the VTypes is that they allow to write generic
        // clients (clients that can work with any value) without much
        // effort
        
        // Suppose we are given some objects to display/process
        Object obj1 = VType.toVType("Hello");
        Object obj2 = VType.toVType(3.14, Alarm.lolo());
        Object obj3 = VType.toVType(new int[]{1,2,3,4,5}, Alarm.disconnected());
        Object obj4 = "Hello";
        Object obj5 = null;
        
        // We can retrieve type information using VType.typeOf
        System.out.println("Type is: " + VType.typeOf(obj1));
        System.out.println("Type is: " + VType.typeOf(obj2));
        System.out.println("Type is: " + VType.typeOf(obj3));
        System.out.println("Type is: " + VType.typeOf(obj4));
        System.out.println("Type is: " + VType.typeOf(obj5));
        System.out.println();
        
        // We can retrieve alarm information using Alarm.alarmOf
        System.out.println("Alarm is: " + Alarm.alarmOf(obj1));
        System.out.println("Alarm is: " + Alarm.alarmOf(obj2));
        System.out.println("Alarm is: " + Alarm.alarmOf(obj3));
        System.out.println("Alarm is: " + Alarm.alarmOf(obj4));
        System.out.println("Alarm is: " + Alarm.alarmOf(obj5));
        System.out.println();
        
        // We can retrieve time information using Time.timeOf
        System.out.println("Time is: " + Time.timeOf(obj1));
        System.out.println("Time is: " + Time.timeOf(obj2));
        System.out.println("Time is: " + Time.timeOf(obj3));
        System.out.println("Time is: " + Time.timeOf(obj4));
        System.out.println("Time is: " + Time.timeOf(obj5));
        System.out.println();
        
        // Note how alarmOf and timeOf are thread safe in that
        // they return something even in the case of null
        
        // Using the class hierarchy, we can process the
        // value at the level we want
        processValue(obj1);
        processValue(obj2);
        processValue(obj3);
        processValue(obj4);
        processValue(obj5);
        System.out.println();
    }
    
    public static void processValue(Object obj) {
        if (obj instanceof Scalar) {
            System.out.println("Value is: " + ((Scalar) obj).getValue());
        } else if (obj instanceof VNumberArray) {
            System.out.println("Value is: " + ((VNumberArray) obj).getData());
        } else {
            System.out.println("Ignoring non-VType value");
        }
    }
}
