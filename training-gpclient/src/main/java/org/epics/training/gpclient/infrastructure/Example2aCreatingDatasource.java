package org.epics.training.gpclient.infrastructure;

import java.util.HashMap;
import java.util.Map;
import org.epics.gpclient.GPClient;
import org.epics.gpclient.PVReader;
import org.epics.gpclient.datasource.ChannelHandler;
import org.epics.gpclient.datasource.DataSource;
import org.epics.gpclient.datasource.MultiplexedChannelHandler;
import org.epics.vtype.VType;

/**
 * Create a new simple datasource for kafka streams
 * @author Kunal Shroff
 *
 */
public class Example2aCreatingDatasource {
    
    // Creates the new datasource
    static DataSource myDataSource = new DataSource() {
        
        // Logic to create a ChannelHandler for a given name
        @Override
        protected ChannelHandler createChannel(String channelName) {
            // Create the custom channel.
            // The two types represent the connection payload (i.e. all the information
            // about the connection status of the channel) and the message payload
            // (i.e. all the information about a message coming in).
            // These objects are cached by the framework and are best implemented as 
            // immutable snapshots of the communication. This allows the framework
            // to do the appropriate caching and multiplexing across multiple
            // listeners.
            
            // In this example, the connection payload is just an object that,
            // if not null, tells the channel to proces incoming messages.
            // The message payload, instead, is simply the value coming in.
            return new MultiplexedChannelHandler<Object, VType>(channelName) {
                @Override
                protected void connect() {
                    // Put all the connection logic here.
                    
                    // As a response to a connection message, call
                    // processConnection with the appropriate payload
                    processConnection(new Object());

                    // As a response to an value message, call
                    // processMessage with the appropriate payload
                    processMessage(VType.toVType("Value for " + channelName));
                }

                @Override
                protected boolean isConnected(Object payload) {
                    // This allows to change the logic that extracts the
                    // connection flag from the connection payload
                    return payload != null;
                    
                    // The default logic is that a channel is connected
                    // if one or more readers are subscribed
                }

                // More advanced, you can also rewire the logic as to how message
                // payloads are mapped to the type requested.
                // @Override
                // protected DataSourceTypeAdapter<Object, VType> findTypeAdapter(ReadCollector<?, ?> cache, Object connection) {
                //     return super.findTypeAdapter(cache, connection); //To change body of generated methods, choose Tools | Templates.
                // }
                // The idea is that each readCollector can be introspected to know
                // what type was requested. findTypeAdapter will be used when
                // the connection changes to find a match between the type requested
                // and the type the specific connection can provide. When a message
                // comes, the chosen adapter is used to extract the value
                // from the message payload and feed it to the collector.
                // See the PVADataSource for an example.
                

                @Override
                public Map<String, Object> getProperties() {
                    // Optionally, you can generate a map from
                    // connection and message payload. This allows debugging
                    // 
                    
                    Map<String, Object> properties = new HashMap<>();
                    // properties.put("ProtocolVersion", getConnectionPayload().getProtocolVersion());
                    // properties.put("LastMessageSource", getMessagePayload().getMessageSourceIP());
                    return properties;
                }
                
                @Override
                protected void disconnect() {
                    // Put all the disconnection logic here.
                    
                    // As a response to a connection message, call
                    // processConnection with the appropriate payload
                    processConnection(null);
                }
            };
        }
    };
    
    public static void main(String[] args) throws InterruptedException {
        PVReader<VType> channel1 = GPClient.read("channel1")
                .from(myDataSource)
                .addReadListener((event, pvReader) -> {
                    System.out.println(event + " - " + pvReader.getValue());
                })
                .start();
        
        Thread.sleep(5000);
        
        channel1.close();
    }
}
