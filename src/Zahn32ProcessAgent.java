import java.util.*;

import org.nule.integrateclient.common.*;
import org.nule.integrateclient.net.*;
import org.nule.lighthl7lib.hl7.*;

/**
 * Created by IntelliJ IDEA.
 * User: sodoku
 */
public class Zahn32ProcessAgent extends ProcessorAgent {

    private ArrayList<InboundClient> inClients = new ArrayList<InboundClient>(); // Store inbound clients
    private ArrayList<OutboundClient> outClients = new ArrayList<OutboundClient>(); // Store outbound clients

    private Logger log = new Logger();

    public Zahn32ProcessAgent() {
        // Create an inbound Zahn32 client
        Properties inZahn32Conf = new Properties();
        //Read the configs from a file
        //inZahn32Conf.load(new FileInputStream("zahn32.properties"));
        inZahn32Conf.setProperty("databaseFileName", "C:/Zahn32/Z32.dat");
        inClients.add(new Zahn32In("Zahn32",log,this,inZahn32Conf));

        // Create an outbound client to send to localhost:12345
        Properties outClientConf = new Properties();
        outClientConf.setProperty("sendHost", "localhost");
        outClientConf.setProperty ("sendPort", "12345");
        outClients.add(new Hl7Client("Client", log, this, outClientConf));

        log.addClient(new DetailedLoggerClient() {
            @Override
            public void putMessage(Date timeStamp, String level, String message) {
                System.out.println("[" + level + "] " + timeStamp + " " + message);
            }
        });
    }

    public boolean start() {
        Iterator it = outClients.iterator();
        while ( it.hasNext()) {
            IntegrateClient ic = (IntegrateClient) it.next();
            ic.startClient(); // Should check return to see if it started
        }
        it = inClients.iterator();
        while (it.hasNext ()) {
            IntegrateClient ic = (IntegrateClient) it.next();
            ic.startClient(); // Should check return to see if it started

        }
        return true;
    }

    public boolean dataTransfer(String msg) {
        String out = msg;
        for (OutboundClient outClient : outClients) {
            outClient.putMessage(msg);
        }
        return true;
    }

    public Properties getConfig() {
        throw new RuntimeException("Not supported yet.");
    }

    public String getLastError() {
        throw new RuntimeException("Not supported yet.");
    }

    public int[] getStats() {
        throw new RuntimeException("Not supported yet.");
    }

    public long getTotal() {
        throw new RuntimeException("Not supported yet.");
    }

    public boolean isConfigured() {
        throw new RuntimeException("Not supported yet.");
    }

    public boolean isRunning() {
        throw new RuntimeException("Not supported yet.");
    }

    public void loadProperties(Properties p) throws IllegalArgumentException {
        throw new RuntimeException("Not supported yet.");
    }

    public boolean reconfigure(Map m) {
        throw new RuntimeException("Not supported yet.");
    }

    public boolean reconfigure(Properties p) {
        throw new RuntimeException("Not supported yet.");
    }

    @Override
    public boolean reconfigure(ConfigurationMap configurationMap) {
        return false;
    }

    public boolean stop() {
        throw new RuntimeException("Not supported yet.");
    } 
}
