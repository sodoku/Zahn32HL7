import org.nule.integrateclient.common.*;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sodoku
 */
public class Zahn32In extends IntegrateClient implements InboundClient {


    public static final String DESCRIPTION = "Simple Zahn32 in client";

    protected String name;

    protected String databaseFileName;

    protected int lastID;

    protected Thread t;

    protected boolean running;

    protected String status = "Zahn32In not initialized.";

    protected long count;

    protected Logger logger;

    protected ProcessorAgent pa;
//    private boolean haltOnAr = true;
//    private boolean haltOnAe = true;

    public Zahn32In(String name, Logger logger, ProcessorAgent pa) {
        super(name, logger, pa);
        this.name = name;
        this.logger = logger;
        this.pa = pa;
    }

    public Zahn32In(String name, Logger logger, ProcessorAgent pa, Properties p) {
        super(name, logger, pa, p);
        this.name = name;
        this.logger = logger;
        this.pa = pa;
        loadProperties(p);
    }

    @Override
    public Properties getStatus() {
        logger.info(name + ": Handling request for status.");
        Properties p = new Properties();
        p.put("databaseFileName", databaseFileName);
        p.put("status", status);
        p.put("lastID", Long.toString(count));
        return p;
    }

    @Override
    public Boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean startClient() {
        // Confirm that fileName as been configured
        if (databaseFileName == null)
            return false;
        status = "Zahn32In starting.";
        logger.info(name + ": Client has been told to start.");
        if (isRunning())
            stopClient();
        t = new Thread(this);
        t.start();
        return true;
    }

    public boolean stopClient() {
        status = "Zahn32In shutting down.";
        logger.info(name + ": Client has been told to shutdown.");
        running = false;
        if (t != null && t.isAlive())
            t.interrupt();
        t = null;
        status = "Zahn32In shut down.";
        return true;
    }


    public boolean isRunning() {
        return running;
    }

    @Override
    public long getCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void run() {
        running = true;
        status = "Zahn32eIn running.";
        Zahn32DBConnector dbc = new Zahn32DBConnector(databaseFileName);
        int lastPatientID = 0;
        int patientsAmmount;

        logger.info("Starting to monitor Zahn32 DB changes");
        while (running) {
            if (lastPatientID != (patientsAmmount = dbc.getDatasetAmmounts()) ) {
                logger.info("Found new Patient");
                pa.dataTransfer(dbc.readLastDatasetHL7());
                lastPatientID = patientsAmmount;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {}

        }
    }


    public static Properties getMandatory() {
        Properties p = new Properties();
        p.put("databaseFileName", "");
        return p;
    }

    public static Properties getOptional() {
        Properties p = new Properties();
        p.put("lastID", "");
        return p;
    }

    public static Properties getDefaults() {
        Properties p = new Properties();
        p.put("databaseFileName", "Zahn32.dat");
        p.put("lastID", "1");
        return p;
    }

    public static Properties getTypes() {
        Properties p = new Properties();
        p.put("databaseFileName", FieldTypes.FILE);
        p.put("lastID", FieldTypes.INTEGER);
        return p;
    }

    public static Properties getDescriptions() {
        Properties p = new Properties();
        p.put("databaseFileName", "A database file to readLastDatasetHL7 in.");
        p.put("lastID", "The number the last id to readLastDatasetHL7.");
        return p;
    }

    public void loadProperties(Properties p) {
        databaseFileName = p.getProperty("databaseFileName");
        if (databaseFileName == null) {
            logger.error(name + ": databaseFileName must be provided.");
            throw new IllegalArgumentException("Invalid argument");
        }
        lastID = 1;
        if (p.getProperty("lastID") != null && !"".equals(p.getProperty("lastID"))) {
            try {
                lastID = Integer.parseInt(p.getProperty("lastID"));
            } catch (NumberFormatException e) {
                logger.warn(name + ": Must provide integer for lastID.");
            }
        }
        status = "Zahn32In configured.";
    }

    public String getState() {
        return status;
    }
}