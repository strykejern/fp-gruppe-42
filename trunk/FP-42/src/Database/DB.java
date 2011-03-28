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
    
    public enum status{
        ALL,
        PARTICIPATING,
        NOT_PARTICIPATING,
        NOT_ANSWERED
        
    }
    
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
            String navn = result.getString("name");
            String mail = result.getString("email");
            no.ntnu.fp.model.Person p = new no.ntnu.fp.model.Person(brukernavn, navn, mail);

            result.close();
            stat.close();

            return p;
        } else throw new SQLException();

        
        
    }

    public static void addAppointment(Appointment appointment)
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

    public static void addMeetingRoom(MeetingRoom room)
            throws SQLException {


        String query = "INSERT INTO Moterom "
                + "(M_ID, Navn, Storrelse) VALUES (" +
                room.getId() + ", " +
                room.getName() + ", " +
                room.getSize()  + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static ArrayList<MeetingRoom> getMeetingRoom (int number)
             throws SQLException {
       String query = "SELECT * FROM Moterom WHERE size>=" +number+ "ORDER BY size";
       Statement stat = dbConnection.createStatement();
       stat.executeUpdate(query);

       ResultSet result = stat.getResultSet();
       ArrayList<MeetingRoom> r = new ArrayList<MeetingRoom>();
       while (result.next()){
            String name  = result.getString("name");
            int size  = result.getInt("size");

            r.add(new MeetingRoom(name,size));
        }

    }
    
    public static void addParticipants () 
               throws SQLException {
        
    }
    
    public static ArrayList<Person> getParticipants(Appointment appointment, status st) 
            throws SQLException {
        
        if (st==status.ALL) {
           String query = "SELECT * FROM Deltaker WHERE S_ID=" +appointment.getID() + ";";

        }
        else if(st == status.PARTICIPATING) {
           query = query + "WHERE status=" + st + ";";
 
        }
        else if(st == status.NOT_PARTICIPATING) {
           
        }
        else if(st==status.NOT_ANSWERED) {
           
        }

       Statement stat = dbConnection.createStatement();
       stat.executeUpdate(query);

       ResultSet result = stat.getResultSet();

       ArrayList<Person> p = new ArrayList<Person>();
       while (result.next()){
            String name  = result.getString("name");
            int size  = result.getInt("size");

            r.add(getPerson(username));
        }
    } 
                      
    }
        
    public static void removeParticipants () 
               throws SQLException {
        
    }

}
