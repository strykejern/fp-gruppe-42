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
import no.ntnu.fp.model.Message;
import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Invitation;
import no.ntnu.fp.model.Timespan;

/**
 *
 * @author Snorre, Jan-Tore
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

    public static boolean login(String username, String password){
        final String query = "SELECT password FROM user WHERE username = '"+username+"'";
        try{
            Statement stat = dbConnection.createStatement();

            stat.executeQuery(query);

            ResultSet result = stat.getResultSet();

            if (!result.next()) return false;
        
            if (result.getString("password").equals(password)) return true;
        } catch (SQLException e){

        }
        return false;
    }


    public static ArrayList<Person> getPersons() throws SQLException{
        final String query = "SELECT * FROM user ORDER BY username ASC";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);
        ResultSet result = stat.getResultSet();

        ArrayList<Person> p = new ArrayList<Person>();
        while (result.next()){
            String username  = result.getString("username");
            String name  = result.getString("name");
            String email  = result.getString("email");
            String password = result.getString("password");

            p.add(new Person(username, name, email, password));
        }

        result.close();
        stat.close();

        return p;
    }

    public static Person getPerson(String username) throws SQLException{
        final String query = "SELECT * FROM user WHERE username = '"+username+"'";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);
        ResultSet result = stat.getResultSet();

        if(result.next()){
            String name = result.getString("name");
            String email = result.getString("email");
            String password = result.getString("password");
            Person p = new Person(username, name, email, password);

            result.close();
            stat.close();

            return p;
        } else throw new SQLException();
    }

    public static void addPerson(Person user)
            throws SQLException {


        String query = "INSERT INTO user "
                + "(username, password, name, email) VALUES ('" +
                user.getUsername() + "', '" +
                user.getPassword() + "', '" +
                user.getName() + "', '" +
                user.getEmail() + "')";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static void removePerson(Person user)
            throws SQLException {


        String query = "DELETE FROM user WHERE username='"+user.getUsername() +"'";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);

    }

    public static void addAppointment(Appointment appointment, int meeting)
            throws SQLException {


        String query = "INSERT INTO appointment "
                + "(meeting, creator, start_time, end_time, description, place, M_ID) VALUES ("+
                meeting + ",'" +
                appointment.getCreator().getUsername() + "', '" +
                appointment.getTime().getStart() + "', '" +
                appointment.getTime().getEnd() + "', '" +
                appointment.getDescription() + "";


        if (appointment.isAtMeetingRoom() == 1 ){
            query += "','', " + appointment.getMeetingRoom().getId();
        }
         else {
            query += "', '" + appointment.getPlace() + "', NULL";
         }

        query += ")";

        System.out.println(query);

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static ArrayList<Appointment> getAppointments(Person person)
                throws SQLException {
        String query = "SELECT * FROM appointment, participant WHERE "
                + "appointment.creator = '" + person.getUsername() +
                "' OR (appointment.A_ID = participant.A_ID AND participant.username= '"
                + person.getUsername() + "') ORDER BY start_time";
        Statement stat = dbConnection.createStatement();

        stat.executeQuery(query);

        ResultSet result = stat.getResultSet();

        ArrayList<Appointment> a = new ArrayList<Appointment>();

        while (result.next()){
            int id                  = result.getInt("A_ID");
            Person creator          = getPerson(result.getString("creator"));
            Timespan time           = new Timespan(result.getTimestamp("start_time"), result.getTimestamp("end_time"));
            String description      = result.getString("description");
            String place            = result.getString("place");
            

            if (place != null) {
                a.add(new Appointment(id, creator, time, description, place));
            }
            else {
                MeetingRoom meetingroom = getMeetingRoom(result.getInt("M_ID"));
                a.add(new Appointment(id, creator, time, description, meetingroom));
            }
        }
        return a;
    }

    public static void editAppointment(Appointment appointment)
        throws SQLException{

        String query ="UPDATE appointment" +
                "(start_time, end_time, description, place) SET (" +
                appointment.getTime().getStart() +", " +
                appointment.getTime().getEnd() + ", " +
                appointment.getDescription() + ", " +
                appointment.getPlace() + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);

    }


    public static void removeAppointment(int id)
            throws SQLException {
        String query = "DELETE FROM appointment WHERE A_ID=" +
                id;
        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

        public static ArrayList<Meeting> getMeetings(Person person)
                throws SQLException {
        String query = "SELECT * FROM appointment, participant WHERE "
                + "meeting = 1 AND (creator = '" + person.getUsername() + "' OR "
                + "(appointment.A_ID = participant.A_ID AND "
                + "participant.username = '" + person.getUsername() + "'))";
        Statement stat = dbConnection.createStatement();

        stat.executeQuery(query);

        ResultSet result = stat.getResultSet();
        ArrayList<Meeting> m = new ArrayList<Meeting>();
        while (result.next()){
            int id = result.getInt("A_ID");
            Person creator = getPerson(result.getString("creator"));
            Timespan time = new Timespan(result.getTimestamp("start_time"), result.getTimestamp("Sluttid"));
            String description = result.getString("description");
            String place = result.getString("place");
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

    public static void addMeetingRoom(MeetingRoom room)
            throws SQLException {

        String query = "INSERT INTO meeting_room "
                + "(name, size) VALUES ('" +
                room.getName() + "', " +
                room.getSize()  + ")";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    public static ArrayList<MeetingRoom> getMeetingRooms (int number)
             throws SQLException {
       String query = "SELECT * FROM meeting_room WHERE size >= "+ number +
               " ORDER BY size ASC";
       Statement stat = dbConnection.createStatement();
       stat.executeQuery(query);

       ResultSet result = stat.getResultSet();
       ArrayList<MeetingRoom> r = new ArrayList<MeetingRoom>();
       while (result.next()){
            String name  = result.getString("name");
            int size  = result.getInt("size");
            int id = result.getInt("M_ID");

            r.add(new MeetingRoom(id,name,size));
       }

       return r;

    }


        public static MeetingRoom getMeetingRoom (int id)
             throws SQLException {
       String query = "SELECT * FROM meeting_room WHERE M_ID = " +id;
       Statement stat = dbConnection.createStatement();
       stat.executeQuery(query);

       ResultSet result = stat.getResultSet();
       if (!result.next()) throw new SQLException("No MeetingRoom");
       return new MeetingRoom(id, result.getString("name"), result.getInt("size"));

    }

    public static void removeMeetingRoom (int id)
              throws SQLException {
       String query = "DELETE FROM meeting_room WHERE M_ID = " + id;
       Statement stat = dbConnection.createStatement();
       stat.executeUpdate(query);


    }
    
    public static void addParticipant (String username, int m_id)
               throws SQLException {
         String query = "INSERT INTO participant "
                + "(username, A_ID, status) VALUES ('" +
                username + "', " +
                m_id + ", '" +
                status.NOT_ANSWERED + "')";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }
    
    public static ArrayList<Person> getParticipants(Meeting meeting, status st)
            throws SQLException {
       String query = "SELECT * FROM participant WHERE A_ID = " +
               meeting.getId() + "AND status='" + st + "'";

       Statement stat = dbConnection.createStatement();
       stat.executeQuery(query);

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
        String query = "UPDATE participant WHERE username = '" + person.getUsername()
                + "' AND M_ID=" + meeting.getId()
                + "(status) VALUES ('"
                + st
                +"')";

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);

    }
    
        
    public static void removeParticipant(Person person)
               throws SQLException {
        String query = "DELETE FROM participant WHERE username = '"+person.getUsername() + "'";

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);
        
    }

    public static void addMessage(Message message, Person to, Person from)
                throws SQLException {
        String query = "INSERT INTO message"
                + "(to, from, subject, text) VALUES ('"+
                to.getUsername()+"', '"+
                from.getUsername()+"', '"+
                message.getSubject()+"', '"+
                message.getContent()+"')";

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);


    }

    public static Message getMessage(int id)
                throws SQLException{

        String query = "SELECT * FROM message WHERE M_ID= "+id+"";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);

        ResultSet result = stat.getResultSet();

        if(result!=null){
            String subject = result.getString("subject");
            String content = result.getString("text");
            Message m = new Message(subject, content);

            result.close();
            stat.close();

            return m;
         } else throw new SQLException();
    }

    public static void removemessage(int id)
                throws SQLException{
        String query = "DELETE FROM message WHERE M_ID= "+id;

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);
    }

     public static void addInvitation(Invitation invitation, Person to, Person from)
                throws SQLException {
                String text = "";
                text += "Møte holdes " +invitation.getMeet().getTime().toString();
                text += " i rom " + invitation.getMeet().getMeetingRoom().getName();
                text += " og gjelder " + invitation.getMeet().getDescription();

        String query = "INSERT INTO message "
                + "(to, from, subject, text) VALUES ('"+
                to.getUsername()+"', '"+
                from.getUsername()+"','"+
                invitation.getMeet().getDescription()+"','"+
                text+"')";

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);


    }

}


