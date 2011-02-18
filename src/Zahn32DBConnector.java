import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: sodoku
 */
public class Zahn32DBConnector {

    private Connection dbConnection;

    public Zahn32DBConnector(String filename) {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            dbConnection = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" + filename + ";READONLY=false}", "", "");
        } catch (SQLException ex) {
            Logger.getLogger(Zahn32DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
        }
    }

    public String readLastDatasetHL7(){
        Statement statement = null;
        String name ="";
        try {
            statement = dbConnection.createStatement();
            statement.executeQuery("SELECT * FROM Pat");
            ResultSet rs = statement.getResultSet();
            name ="";
            while (rs.next()) {
                    name = "MSH|^~\\&|KIS||SUBSYSTEM||20021014141550||ADT^A01|0000020|P|2.3|PID|||0815|1235468790|" + rs.getString("Vorname") + "^" + rs.getString("Name") + "||" + rs.getString("GebDatum") + "|" + rs.getString("Geschlecht") + "|||" + rs.getString("Ort") + "^^"+rs.getString("Stra?e")+"^^" + rs.getString("PLZ") + "^D||||D||||1234|||||||||PV1||stat|01^^^6^^^536^^HNO||^||123^Meister Eder^^Dr.med.||||||||||||1234567890||||||||||||||||||||||||20060520717||||||";
                }
                rs.close();
            System.out.println("HL7Message = " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public int getDatasetAmmounts() {

        int numRows = -1;
        try {
            Statement statement = dbConnection.createStatement();
            if (statement != null) {
                statement.executeQuery("SELECT Vorname, Name, GebDatum, Geschlecht, Ort, PLZ FROM Pat");
                ResultSet rs = statement.getResultSet();
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                rs.close();
                numRows = count;
            }
            statement.close();

        } catch (SQLException ex) {
        }
        return numRows;
    }
}
