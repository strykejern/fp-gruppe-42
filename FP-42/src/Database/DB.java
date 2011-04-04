package Database;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.String;
import java.sql.*;
import java.util.ArrayList;
import no.ntnu.fp.model.Appointment;
import no.ntnu.fp.model.Meeting;
import no.ntnu.fp.model.MeetingRoom;
import no.ntnu.fp.model.MeetingRoom;
import no.ntnu.fp.model.Message;
import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Timespan;

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


    public static ArrayList<Person> getPersons() throws SQLException{
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

    public static Person getPerson(String brukernavn) throws SQLException{
        final String query = "SELECT * FROM bruker WHERE brukernavn = "+brukernavn+"";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);
        ResultSet result = stat.getResultSet();

        if(result!=null){
            String navn = result.getString("name");
            String mail = result.getString("email");
            Person p = new Person(brukernavn, navn, mail);

            result.close();
            stat.close();

            return p;
        } else throw new SQLException();

        
        
    }

    public static void addPerson(Person bruker)
            throws SQLException {


        String query = "INSERT INTO BRUKER "
                + "(Brukernavn, Passord, Navn, Mailadresse) VALUES ('" +
                bruker.getUsername() + "', '" +
                bruker.getPassword() + "', '" +
                bruker.getName() + "', '" +
                bruker.getEmail() + "')";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static void removePerson(Person bruker)
            throws SQLException {


        String query = "DELETE * FROM bruker WHERE brukernavn="+bruker.getUsername();

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);

    }

    public static void addAppointment(Appointment appointment, boolean mote)
            throws SQLException {


        String query = "INSERT INTO avtale "
                + "(Mote, Oppretter, Starttidspunkt, Sluttidspunt, Beskrivelse, Sted, M_ID) VALUES ("+
                mote + "," +
                appointment.getCreator() + ", " +
                appointment.getTime() + ", " +
                appointment.getDescription() + ", " +
                appointment.getPlace() + ", " +
                appointment.getId() + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static ArrayList<Appointment> getAppointments(Person person)
                throws SQLException {
        String query = "SELECT * FROM avtale, deltaker WHERE Mote=false AND (avtale.Oppretter=" + person.getUsername() +
                "OR (avtale.S_ID=deltaker.S_ID AND deltaker.brukernavn=" + person.getUsername() + "));";
        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);

        ResultSet result = stat.getResultSet();
        ArrayList<Appointment> a = new ArrayList<Appointment>();
        while (result.next()){
            int id = result.getInt("S_ID");
            Person creator = getPerson(result.getString("Oppretter"));
            Timespan time = new Timespan(result.getDate("Starttid"), result.getDate("Sluttid"));
            String description = result.getString("Beskrivelse");
            String place = result.getString("Sted");
            MeetingRoom meetingroom = getMeetingRoom(result.getInt("M_ID"));
            if (place != null) {
                a.add(new Appointment(id, creator, time, description, place));
            }
            else {
                a.add(new Appointment(id, creator, time, description, meetingroom));
            }
        }
        return a;
    }

        public static ArrayList<Meeting> getMeetings(Person person)
                throws SQLException {
        String query = "SELECT * FROM avtale, deltaker WHERE Mote=TRUE AND (Oppretter=" + person.getUsername() +
                "OR (avtale.S_ID=deltaker.S_ID AND deltaker.brukernavn=" + person.getUsername() + "));";
        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);

        ResultSet result = stat.getResultSet();
        ArrayList<Meeting> m = new ArrayList<Meeting>();
        while (result.next()){
            int id = result.getInt("S_ID");
            Person creator = getPerson(result.getString("Oppretter"));
            Timespan time = new Timespan(result.getDate("Starttid"), result.getDate("Sluttid"));
            String description = result.getString("Beskrivelse");
            String place = result.getString("Sted");
            MeetingRoom meetingroom = getMeetingRoom(result.getInt("M_ID"));
            if (place != null) {
                m.add(new Meeting(id, creator, time, description, place));
            }
            else {
                m.add(new Meeting(id, creator, time, description, meetingroom));
            }
        }

        return m;
    }

    public static void removeAppointment(Appointment appointment)
            throws SQLException {
        String query = "DELETE * FROM avtale WHERE S_ID=" + appointment.getId() + ";";
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

    public static ArrayList<MeetingRoom> getMeetingRooms (int number)
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

       return r;

    }


        public static MeetingRoom getMeetingRoom (int id)
             throws SQLException {
       String query = "SELECT * FROM Moterom WHERE M_ID>=" +id+ ";";
       Statement stat = dbConnection.createStatement();
       stat.executeUpdate(query);

       ResultSet result = stat.getResultSet();
       return new MeetingRoom(result.getString("Navn"), result.getInt("Storrelse"));

    }

    public static void removeMeetingRoom (MeetingRoom meetingroom)
              throws SQLException {
       String query = "DELETE * FROM Moterom WHERE M_ID=" + meetingroom.getId() + ";";
       Statement stat = dbConnection.createStatement();
       stat.executeUpdate(query);


    }
    
    public static void addParticipants (Person person, Appointment appointment)
               throws SQLException {
         String query = "INSERT INTO Deltaker "
                + "(Brukernavn, S_ID, Status) VALUES (" +
                person.getUsername() + ", " +
                appointment.getId() + ", " +
                status.NOT_ANSWERED + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }
    
    public static ArrayList<Person> getParticipants(Meeting meeting, status st)
            throws SQLException {
       String query = "SELECT * FROM Deltaker WHERE S_ID=" +
               meeting.getId() + "AND status=" + st + ";";

       Statement stat = dbConnection.createStatement();
       stat.executeUpdate(query);

       ResultSet result = stat.getResultSet();

       ArrayList<Person> p = new ArrayList<Person>();
       while (result.next()){
            String username  = result.getString("username");

            p.add(getPerson(username)); 
       }
       return p;
    } 

    public static void changeStatus(Person person, Meeting meeting, status st)
        throws SQLException{
        String query = "INSERT INTO Deltaker WHERE Brukernavn=" + person.getUsername()
                + "AND M_ID=" + meeting.getId()
                + "(Status) VALUES ("
                + st
                +",)";

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);

    }
    
        
    public static void removeParticipant(Person person)
               throws SQLException {
        String query = "DELETE * FROM Deltaker WHERE brukernavn="+person.getUsername();

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);
        
    }

    public static void addMessage(Message message, Person til, Person fra)
                throws SQLException {
        String query = "INSERT INTO Melding "
                + "(Til, Fra, Emne, Tekst) VALUES ("+
                til.getUsername()+", "+
                fra.getUsername()+", "+
                message.getSubject()+", "+
                message.getContent()+",)";

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);


    }

    public static Message getMessage(int id)
                throws SQLException{

        String query = "SELECT * FROM Melding WHERE M_ID= "+id;

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);

        ResultSet result = stat.getResultSet();

        if(result!=null){
            String subject = result.getString("Emne");
            String content = result.getString("Tekst");
            Message m = new Message(subject, content);

            result.close();
            stat.close();

            return m;
         } else throw new SQLException();
    }

    public static void removemessage(int id)
                throws SQLException{
        String query = "DELETE * FROM Melding WHERE M_ID= "+id;

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);
    }

}

