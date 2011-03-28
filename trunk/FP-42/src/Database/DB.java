package Database;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;
import no.ntnu.fp.model.Person;

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

        ArrayList<Person> p = new ArrayList<Person>();
        while (result.next()){
            String username  = result.getString("username");
            String name  = result.getString("name");
            String email  = result.getString("email");

            p.add(new Person(username, name, email));
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
            no.ntnu.fp.model.Person p = new no.ntnu.fp.model.Person(brukernavn, navn, mail);

            result.close();
            stat.close();

            return p;
        } else throw new SQLException();

        
        
    }

    public static void addAppointment(no.ntnu.fp.model.Appointment appointment)
            throws SQLException {


        String query = "INSERT INTO avtale "
                + "(Oppretter, Starttidspunkt, Sluttidspunt, Beskrivelse, Sted, M_ID) VALUES ("+
                appointment.getCreator() + ", " +
                appointment.getStart() + ", " +
                appointment.getEnd() + ", " +
                appointment.getDescription() + ", " +
                appointment.getPlace() + ", " +
                appointment.Meeting.getId() + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static void addPerson(no.ntnu.fp.model.Person bruker)
            throws SQLException {


        String query = "INSERT INTO Bruker "
                + "(Brukernavn, Passord, Navn, Mailadresse) VALUES (" +
                bruker.getUsername() + ", " +
                bruker.getPassword() + ", " +
                bruker.getName() + ", " +
                bruker.getEmail() + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static void addMeetingRoom(no.ntnu.fp.model.MeetingRoom room)
            throws SQLException {


        String query = "INSERT INTO Moterom "
                + "(M_ID, Navn, Storrelse) VALUES (" +
                room.getId() + ", " +
                room.getName() + ", " +
                room.getSize()  + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static Moterom getMoterom (int size)
             throws SQLException {
       String query = "SELECT * FROM Moterom WHERE size>=" +size+ "ORDER BY size";

       Statement stat = dbConnection.createStatement();

       stat.executeUpdate(query);

       ResultSet result = stat.getResultSet();

       


    }

}
