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

    /*
     * Metode som tar inn brukernvan og passord, og sjekker opp mot databasen.
     * Hvis den finner brukernavnet og passordet stemmer, blir det vellykket pålogging,
     * hvis ikke får man ikke logget på systemet.
     * @Param username
     *  brukernavn som men prøver å logge på, string, kan ikke være null
     * @Param password
     *  passord tilhørende brukernavnet, string, kan ikke være null.
     * @return true/false
     */
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

    /*
     * Metode som henter ut en en arraylist med alle personer i databasen.
     * @return ArrayList
     */

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

    /*
     * Metode som henter ut navn og e-post, basert på brukernavn.
     * @param username
     * @return Person
     */
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

    /*
     * Metode som legger til en person i databasen.
     * @param Person user
     */
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

    /*
     * Metode som fjerner en person fra databasen.
     * @param Person user
     */
    public static void removePerson(Person user)
            throws SQLException {


        String query = "DELETE FROM user WHERE username='"+user.getUsername() +"'";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);

    }

    /*
     * Metode som legger til en avatale eller møte i databasen.
     * Int meeting bestemmer om det er en avtale eller et møte.
     * @param Appointment appointment
     * @param int meeting
     */
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


    /*public static Appointment getAppointment (int id, Person person) throws SQLException {
        String guery = "SELECT * FROM appointment WHERE"
                + "appointment.creator = '" + person.getUsername() +
                "')";
        Statement stat = dbConnection.createStatement();

        stat.executeQuery(guery);
        ResultSet result = stat.getResultSet();
        Appointment a;
        while(result.next()){
            Person creator          = getPerson(result.getString("creator"));
            Timespan time           = new Timespan(result.getTimestamp("start_time"), result.getTimestamp("end_time"));
            String description      = result.getString("description");
            String place            = result.getString("place");

            a  = new Appointment(creator, time, description, place);
        }
        return a;
    }*/

    /*
     * Metode som henter ut alle avtalene i kalenderen til en gitt person.
     * @param Person person
     * @return ArrayList
     */
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

    /*
     * Metode som endrer en metode. Metoden som tas inn har oppdatert informasjon
     * som blir endret i databasen.
     * @param Appointment appointment
     */
    public static void editAppointment(Appointment appointment)
        throws SQLException{

        String query ="UPDATE appointment" +
                "(start_time, end_time, description, place) SET ('" + appointment.getTime().getStart() + "', '" +
                appointment.getTime().getEnd() + "', '" +
                appointment.getDescription() + "', '" +
                appointment.getPlace() + "',)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);

    }


    /*
     * Metode som fjerner en metode fra databasen.
     * @param int id
     */
    public static void removeAppointment(int id)
            throws SQLException {
        String query = "DELETE FROM appointment WHERE A_ID=" +
                id;
        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    /*
     * Metode som henter ut alle møter i kalenderen til en person.
     * @param Person person
     * @return ArrayList
     */
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

    /*
     * Metode som legger et møterom til i databasen.
     * @param MeetingRoom room
     */
    public static void addMeetingRoom(MeetingRoom room)
            throws SQLException {

        String query = "INSERT INTO meeting_room "
                + "(name, size) VALUES ('" +
                room.getName() + "', " +
                room.getSize()  + ")";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }

    /*
     * Metode som henter ut alle møterom over en gitt minimumskapasitet.
     * @param int number
     * @return ArrayList
     */
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


    /*
     * Metode som henter ut et spesifikt møterom fra databasen.
     * @param int id
     * @return MeetingRoom
     */
        public static MeetingRoom getMeetingRoom (int id)
             throws SQLException {
       String query = "SELECT * FROM meeting_room WHERE M_ID = " +id;
       Statement stat = dbConnection.createStatement();
       stat.executeQuery(query);

       ResultSet result = stat.getResultSet();
       if (!result.next()) throw new SQLException("No MeetingRoom");
       return new MeetingRoom(id, result.getString("name"), result.getInt("size"));

    }

    /*
     * Metode som sletter et møterom fra databasen.
     * @param int id
     */
    public static void removeMeetingRoom (int id)
              throws SQLException {
       String query = "DELETE FROM meeting_room WHERE M_ID = " + id;
       Statement stat = dbConnection.createStatement();
       stat.executeUpdate(query);


    }

    /*
     * Metode som legger til en deltaker til et møte.
     * @param String username
     * @param int m_id
     */
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

    /*
     * Metode som henter ut alle deltakere på et møte.
     * @param Meeting meeting
     * @param status st
     * @return ArrayList
     */
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

    /*
     * Metode som endrer statusen for en persons deltakelse på et møte.
     * @param Person person
     * @param Meeting meeting
     * @param status st
     */
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
    
    /*
     * Metode som fjerner en deltaker fra listen over møtedeltakere.
     * @param Person person
     */
    public static void removeParticipant(Person person)
               throws SQLException {
        String query = "DELETE FROM participant WHERE username = '"+person.getUsername() + "'";

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);
        
    }

    /*
     * Metode som oppretter en melding i databasen, med en avsender og mottaker.
     * @param Message message
     * @param Person to
     * @param Person from
     */
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

    /*
     * metode som henter ut en melding fra databasen.
     * @param int id
     * @return Message
     */
    public static ArrayList<Message> getMessages(String username)
                throws SQLException{

        String query = "SELECT * FROM message WHERE to= "+username+"";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);

        ResultSet result = stat.getResultSet();

        ArrayList<Message> m = new ArrayList<Message>();

        if(result!=null){
            String subject = result.getString("subject");
            String content = result.getString("text");
            m.add(new Message(subject, content));

            result.close();
            stat.close();

            return m;
         } else throw new SQLException();
    }

    /*
     * Metode som fjerner en melding fra databasen.
     * @param int id
     */
    public static void removemessage(int id)
                throws SQLException{
        String query = "DELETE FROM message WHERE M_ID= "+id;

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);
    }

    /*
     * Metode som oppretter en møteinnkallelse i databasen.
     * @param Invitation invitation
     * @param Person to
     * @param Person from
     */
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


