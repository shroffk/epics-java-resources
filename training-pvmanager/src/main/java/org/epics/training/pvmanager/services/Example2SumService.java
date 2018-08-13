package org.epics.training.pvmanager.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.service.ServiceRegistry;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueFactory;

import static org.diirt.util.concurrent.Executors.namedPool;

/**
 *
 * @author Kunal Shroff
 *
 */
public class Example2SumService {

    public static class AddServiceMethod extends ServiceMethod {

        public AddServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
            super(serviceMethodDescription, serviceDescription);
        }

        // This method is implemented synchronously: just run on the current thread and
        // throw exceptions with a throw
        @Override
        public Map<String, Object> syncExecImpl(Map<String, Object> parameters) {
            VNumber arg1 = (VNumber) parameters.get("arg1");
            VNumber arg2 = (VNumber) parameters.get("arg2");
            VNumber result = ValueFactory.newVDouble(arg1.getValue().doubleValue() + arg2.getValue().doubleValue());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", result);
            return resultMap;
        }
    }

    public static class MultiplyServiceMethod extends ServiceMethod {

        public MultiplyServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
            super(serviceMethodDescription, serviceDescription);
        }
        
        // Use the namedPool from diirt util so that the thread pool is made of
        // daemons and does not block termination
        ExecutorService exec = Executors.newSingleThreadExecutor(namedPool("multiplyService"));

        // This method is implemented asynchronously: use the callbacks when
        // the task is completed
        @Override
        public void asyncExecImpl(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback) {
            exec.submit(() -> {
                try {
                    VNumber arg1 = (VNumber) parameters.get("arg1");
                    VNumber arg2 = (VNumber) parameters.get("arg2");
                    VNumber result = ValueFactory.newVDouble(arg1.getValue().doubleValue() * arg2.getValue().doubleValue());
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("result", result);
                    callback.accept(resultMap);
                } catch (Exception ex) {
                    errorCallback.accept(ex);
                }
            });
        }
    }

    public static ServiceMethodDescription addMethod() {
        return new ServiceMethodDescription("add", "Adds two numbers.") {

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
                return new AddServiceMethod(this, serviceDescription);
            }
        }.addArgument("arg1", "First argument", VNumber.class)
                .addArgument("arg2", "Second argument", VNumber.class)
                .addResult("result", "The sum of arg1 and arg2", VNumber.class);
    }

    public static ServiceMethodDescription multiplyMethod() {
        return new ServiceMethodDescription("multiply", "Multiplies two numbers.") {

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDesccription) {
                return new MultiplyServiceMethod(this, serviceDesccription);
            }
        }.addArgument("arg1", "First argument", VNumber.class)
                .addArgument("arg2", "Second argument", VNumber.class)
                .addResult("result", "The product of arg1 and arg2", VNumber.class);
    }

    public static Service createMathService() {
        return new ServiceDescription("math", "Simple math service")
                .addServiceMethod(addMethod())
                .addServiceMethod(multiplyMethod())
                .createService();
    }

    /**
     * Create a simple math service to perform mathematical operation on 2
     * scalars
     */
    /**
     * Create a simple programmatically client to the above service to add to
     * numbers
     */
    public static void main(String[] args) {
        // Register the service. This is would be done with the ServiceLoader
        ServiceRegistry.getDefault().registerService(createMathService());
        
        // Find the services and execute
        ServiceMethod exec = ServiceRegistry.getDefault().findServiceMethod("math/add");
        Map<String, Object> callArgs = new HashMap<>();
        callArgs.put("arg1", ValueFactory.toVType(3));
        callArgs.put("arg2", ValueFactory.toVType(4.5));
        Map<String, Object> result = exec.executeSync(callArgs);
        System.out.println(result.get("result"));
        
        exec = ServiceRegistry.getDefault().findServiceMethod("math/multiply");
        result = exec.executeSync(callArgs);
        System.out.println(result.get("result"));
        
        // Note that the execution does not care whether the implementation is
        // synch or asynch. The framework takes care of wiring any type of call
        // with any type of implementation. You can also provide both synch
        // and asynch implementaion, and the framework will wire appropriately.
    }
}
