package org.epics.training.pvmanager.services;

import java.util.HashMap;
import java.util.Map;
import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.service.ServiceRegistry;
import org.diirt.vtype.ValueFactory;

/**
 * @author Kunal Shroff
 *
 */
public class Example1UsingServices {
    
    public static void main(String[] args) throws InterruptedException {
        // List all registered services and their method
        for (String serviceName : ServiceRegistry.getDefault().getRegisteredServiceNames()) {
            System.out.println("Methods for " + serviceName);
            for (Map.Entry<String, ServiceMethod> methodEntry : ServiceRegistry.getDefault().findService(serviceName).getServiceMethods().entrySet()) {
                System.out.println(methodEntry.getValue());
            }
        }
        
        // Find the the command line execution service and prepare the command
        ServiceMethod exec = ServiceRegistry.getDefault().findServiceMethod("exec/run");
        Map<String, Object> callArgs = new HashMap<>();
        callArgs.put("command", ValueFactory.toVType("echo This is a test!"));
        
        // Execute the service synchronously: result will wait and return the
        // result on the current thread
        {
            Map<String, Object> result = exec.executeSync(callArgs);
            System.out.println(result.get("output"));
        }
        
        // Execute the service asynchronously: callbacks will be invoked on another
        // thread when they are done
        {
            exec.executeAsync(callArgs, (result) -> {
                System.out.println(result.get("output"));
            }, (ex) -> {
                System.out.println("There was an error " + ex.getMessage());
            });
        }
        
        Thread.sleep(1000);
    }
}
