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
    public static void addAppointment(Appointment appointment, boolean meeting)
            throws SQLException {


        String query = "INSERT INTO appointment "
                + "(meeting, creator, start_time, end_time, description, place, M_ID) VALUES ("+
                (meeting ? "1" : "0") + ",'" +
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


        public static Appointment getAppointment (int id, String username) throws SQLException {
        String guery = "SELECT * FROM appointment WHERE"
                + "appointment.creator = '" + username +
                "' AND A_ID=" + id;
        Statement stat = dbConnection.createStatement();

        stat.executeQuery(guery);
        ResultSet result = stat.getResultSet();
        Appointment a;
        if (!result.next()) throw new SQLException("No appointment");

        Person creator          = getPerson(result.getString("creator"));
        Timespan time           = new Timespan(result.getTimestamp("start_time"), result.getTimestamp("end_time"));
        String description      = result.getString("description");
        String place            = result.getString("place");

        a  = new Appointment(creator, time, description, place);
        return a;
    }

    /*
     * Metode som henter ut alle avtalene i kalenderen til en gitt person.
     * @param Person person
     * @return ArrayList
     */
    public static ArrayList<Appointment> getAppointments(Person person)
                throws SQLException {
        /*SELECT DISTINCT appointment.A_ID, creator, start_time, end_time, description, place
        FROM appointment, participant
        WHERE creator =  "ove" OR ( username =  "ove" AND appointment.A_ID = participant.A_ID )*/

        String query =    "SELECT DISTINCT appointment.A_ID, creator, start_time, end_time, description, place "
                        + "FROM appointment "
                        + "JOIN participant "
                        + "ON creator='" + person.getUsername() + "' "
                        + "OR (username= '" + person.getUsername() + "' "
                        + "AND appointment.A_ID = participant.A_ID) "
                        + "ORDER BY start_time";
        System.out.println(query);

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

        String query ="UPDATE appointment SET "
                + "start_time='" + appointment.getTime().getStart()
                + "', end_time='" + appointment.getTime().getEnd()
                + "' WHERE A_ID=" + appointment.getId();
        System.out.println(query);

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);

        String subject = "Appointment " + appointment.getId() + " edited";
        String content = "Appointment " + appointment.getId() + " had been"
                + "edited to be from " + appointment.getTime().getStart()
                + " to " + appointment.getTime().getEnd();

        Message mail = new Message(subject, content);
        for (Person user : DB.getParticipants(appointment.getId(), status.ALL)){
            addMessage(mail, user, appointment.getCreator());
            changeStatus(user, appointment.getId(), status.NOT_ANSWERED);
        }
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
            Timespan time = new Timespan(result.getTimestamp("start_time"), result.getTimestamp("end_time"));
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

    public static Meeting getMeeting(int id)
            throws SQLException {

        String query = "SELECT * FROM appointment WHERE A_ID="
            + id + " AND meeting=1";
        Statement stat = dbConnection.createStatement();

        stat.executeQuery(query);
        
        ResultSet result = stat.getResultSet();
        if (!result.next()) { throw new SQLException("No meeting");
       }

        Person creator = getPerson(result.getString("creator"));
        Timespan time = new Timespan(result.getTimestamp("start_time"), result.getTimestamp("end_time"));
        String description = result.getString("description");
        String place = result.getString("place");
        MeetingRoom meetingroom = getMeetingRoom(result.getInt("M_ID"));

        Meeting meeting;
        if (place.equals("")){
        meeting = new Meeting(id, creator, time, description, meetingroom);
        }
        else {
        meeting = new Meeting(id, creator, time, description, place);
        }
        
        return meeting;

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

    public static ArrayList<Integer> getMeetingRoomID () throws SQLException{
        String query = "SELECT M_ID FROM appointment";
        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);

        ResultSet result = stat.getResultSet();

        ArrayList<Integer> isMeeting = new ArrayList<Integer>();
        while(result.next()) {
            int isMeet = result.getInt("M_ID");
            isMeeting.add(isMeet);
        }

        return isMeeting;
    }

    public static ArrayList<MeetingRoom> getMeetingRooms (int size, Timestamp start, Timestamp end)
             throws SQLException {
       String query = "SELECT * FROM meeting_room WHERE size > " + size + " AND "
               + "not exists (SELECT * FROM appointment WHERE "
               + "meeting_room.M_ID = appointment.M_ID AND "
               + "((start_time > '" + start + "' AND start_time < '" + end + "') OR "
               + "(end_time > '" + start + "' AND end_time < '" + end + "') OR "
               + "(start_time < '" + start + "' AND end_time > '" + end + "')))";
       System.out.println(query + "her linje 412");
       Statement stat = dbConnection.createStatement();
       stat.executeQuery(query);

       ResultSet result = stat.getResultSet();
       ArrayList<MeetingRoom> r = new ArrayList<MeetingRoom>();
       while (result.next()){
            String name  = result.getString("name");
            int capacity  = result.getInt("size");
            int id = result.getInt("M_ID");

            r.add(new MeetingRoom(id,name,capacity));
       }
       return r;
    }

    public static boolean isMeetingRoomAvailable(int id, Timestamp start, Timestamp end)throws SQLException{
        String query = "SELECT * FROM appointment, meeting_room WHERE"
               + " meeting_room.M_ID = appointment.M_ID AND meeting_room.M_ID = " + id
               + " AND ((start_time > '" + start + "' AND start_time < '" + end + "') OR "
               + "(end_time > '" + start + "' AND end_time < '" + end + "') OR "
               + "(start_time < '" + start + "' AND end_time > '" + end + "'))";
        System.out.println(query + "her linje 434");
        Statement stat = dbConnection.createStatement();
        ResultSet r = stat.executeQuery(query);
        return !r.next();
    }

    /*
     * Metode som henter ut et spesifikt møterom fra databasen.
     * @param int id
     * @return MeetingRoom
     */

    public static MeetingRoom getMeetingRoom (int id)
         throws SQLException {
       String query = "SELECT * FROM meeting_room WHERE M_ID=" + id;
       System.out.println(query);
       Statement stat = dbConnection.createStatement();
       stat.executeQuery(query);

       ResultSet result = stat.getResultSet();
       if (!result.next()) throw new SQLException("No MeetingRoom");
       return new MeetingRoom(result.getInt("M_ID"), result.getString("name"), result.getInt("size"));

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
    public static ArrayList<Person> getParticipants(int id, status st)
            throws SQLException {
       String query = "SELECT * FROM participant WHERE A_ID = " +
               id + " AND status = '" + st + "'";

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
    public static void changeStatus(Person person, int id, status st)
            throws SQLException{
        String query = "UPDATE participant WHERE username = '" + person.getUsername()
                + "' AND M_ID=" + id
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
    public static void addMessage(Message message, Person receiver, Person sender)
                throws SQLException {
        String query = "INSERT INTO message"
                + "(receiver, sender, subject, text) VALUES ('"+
                receiver.getUsername()+"', '"+
                sender.getUsername()+"', '"+
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

        String query = "SELECT * FROM message WHERE receiver='"+username+"'";
        //String query1 = "UPDATE message SET read=1";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);

        ResultSet result = stat.getResultSet();
        //stat.executeQuery(query1);

        ArrayList<Message> m = new ArrayList<Message>();

        while(result.next()){
            String subject = result.getString("subject");
            String content = result.getString("text");
            String from = result.getString("sender");
            m.add(new Message(subject, content, from));
        }
        return m;
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
    public static ArrayList getMeetings(String username)
                throws SQLException{
        String query = "SELECT * FROM appointment WHERE creator= "+username+" AND meeting=1";

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);

        ArrayList<Meeting> m = new ArrayList<Meeting>();

        ResultSet result = stat.getResultSet();

        while (result.next()){
            int id                  = result.getInt("A_ID");
            Person creator          = getPerson(result.getString("creator"));
            Timespan time           = new Timespan(result.getTimestamp("start_time"), result.getTimestamp("end_time"));
            String description      = result.getString("description");
            String place            = result.getString("place");


            if (place != null) {
                m.add(new Meeting(id, creator, time, description, place));
            }
            else {
                MeetingRoom meetingroom = getMeetingRoom(result.getInt("M_ID"));
                m.add(new Meeting(id, creator, time, description, meetingroom));
            }



        }
        return m;
    }

     *
     */

    public static String traceMeeting(String id)
            throws SQLException{
        String query = "SELECT * FROM participant WHERE A_ID= "+id;

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);

        ResultSet result = stat.getResultSet();


        ArrayList<String> s = new ArrayList<String>();
        int na =0;
        int np=0;

        while(result.next()){
            String status = result.getString("status");
            if (status.equals("NOT_ANSWERED")){
                if (na == 0){
                    s.add("En eller flere møtedeltakere har ikke svart på innkallingen.");
                    na++;
                }
            }else if(status.equals("NOT_PARTCIPATING")){
                if(np==0){
                    s.add("En eller flere møtedeltakere deltar ikke på møtet.");
                }
            }




        }
        if (na==0 && np==0){
            s.add("Alle møtedeltakere deltar på møtet.");
        }
        String st ="";
        for(int i=0; i<s.size(); i++){
            st += s.get(i);
        }
        return st;
    }

    /*
     * Metode som oppretter en møteinnkallelse i databasen.
     * @param Invitation invitation
     * @param String to
     * @param String from
     */
     public static void addInvitation(Invitation invitation, String receiver, String sender)
                throws SQLException {
                String text = "";
                text += "Møte id er: " + invitation.getMeet().getId() + "\nMøte holdes " +invitation.getMeet().getTime().toString();
                text += " i rom " + invitation.getMeet().getMeetingRoom().getName();
                text += " og gjelder " + invitation.getMeet().getDescription();

        String query = "INSERT INTO message "
                + "(receiver, sender, subject, text) VALUES ('"+
                receiver +"', '"+
                sender +"','"+
                invitation.getMeet().getDescription()+"','"+
                text+"')";

        System.out.println(query);

        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);
    }

    public static void answerInvitation(int id, String username, String answer)throws SQLException{
        String query = "";
        if(answer.equals("no")){
        query = "UPDATE participant SET status = 'NOT_PARTICIPATING'"
                + " WHERE A_ID=" + id + " AND username = '" + username + "'";
        }else if(answer.equals("yes")){
            query = "UPDATE participant SET status = 'PARTICIPATING'"
                + " WHERE A_ID=" + id + " AND username = '" + username + "'";
        }
        Statement stat = dbConnection.createStatement();
        stat.executeUpdate(query);
    }
}
