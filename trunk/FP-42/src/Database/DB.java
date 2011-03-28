/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;

package Database;

/**
 *
 * @author Snorre
 */
public class DB {

    private static Connection dbConnection;

    public static void initializeDB
                (String userName, String password, String databaseLocation)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException{

        if (dbConnection != null) {
            dbConnection.close();
        }
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        dbConnection = DriverManager.getConnection(
                databaseLocation, userName, password);
    }


    public static ArrayList getPersons() throws SQLException{
        final String query = "SELECT * FROM bruker ORDER BY brukernavn ASC";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);
        ResultSet result = stat.getResultSet();

        ArrayList p = new ArrayList();
        while (result.next()){
            p.add(result.getString("brukernavn"));
        }

        result.close();
        stat.close();

        return p;
    }

    public static Object getPerson(String brukernavn) throws SQLException{
        final String query = "SELECT * FROM bruker WHERE brukernavn = "+brukernavn+"";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);
        ResultSet result = stat.getResultSet();

        if(result!=null){
            String navn = result.getString("navn");
            String mail = result.getString("mailadresse");
        }

        Person p = new Person(navn, mail);
        result.close();
        stat.close();

        return p;
    }

 /*   public static void addAvtale(Avtale avtaler)
            throws SQLException {


        String query = "INSERT INTO avtale "
                + "(Oppretter, Starttidspunkt, Sluttidspunt, Beskrivelse, Sted, M_ID) VALUES ("+
                avtaler.moteleder.getName() + ", " +
                avtaler.start + ", " +
                avtaler.slutt + ", " +
                avtaler.beskrivelse + ", " +
                avtaler.sted + ", " +
                avtaler.getM_ID() + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }
  */
}
