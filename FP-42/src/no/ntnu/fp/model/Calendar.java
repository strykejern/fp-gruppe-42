/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

import Database.DB;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.ntnu.fp.model.Timespan;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import no.ntnu.fp.model.*;
import java.sql.Timestamp;

/**
 *
 * @author Anders
 */

public class Calendar {

    private Person                  user;
    private ArrayList<Meeting>      meetings;
    private ArrayList<Appointment>  appointments;
    private ArrayList<Message>      messages;

    public Calendar (Person bruker) throws SQLException {
        this.user           = bruker;
        this.meetings       = DB.getMeetings(bruker);
        this.appointments   = DB.getAppointments(bruker);
        this.messages       = DB.getMessages(user.getName());

    }

    public boolean addAppointment(Appointment app, boolean meeting){
        try {
            DB.addAppointment(app, meeting);
        } catch (SQLException ex) {
            return false;
        }
        appointments.add(app);
        return true;
    }

    public void newAppointment() {
        Timestamp start = Timestamp.valueOf("2011-05-04 12:15:00");
        Timestamp slutt = Timestamp.valueOf("2011-05-04 16:00:00");

        int id = meetings.size()+1; //denne må vel hentes ut av databasen etter at den er lagret der?
        Person creator = user;
        Timespan time = new Timespan(start,slutt);
        String description = /*String fra GUI, beskrivelse av møte/avtale*/"";
        String place = "";
        ArrayList<MeetingRoom> meetingRooms = DB.getMeetingRooms(/*input fra GUI, størrelse på ønsket rom*/);
        MeetingRoom meetingRoom = (MeetingRoom) meetingRooms.get(0);
        for(int i = 1; i < meetingRooms.size()-1; i++){
            MeetingRoom nextMeetingRoom = (MeetingRoom) meetingRooms.get(i);
            if(nextMeetingRoom.getSize()>meetingRoom.getSize()){
                meetingRoom = nextMeetingRoom;
            }
        }
        if (meetingRoom == null){
            new Appointment(id, creator, time, description, place);
        }
        new Meeting(id, creator, time, description, meetingRoom);

    }


    public void newMeeting() throws SQLException{
        /*
         * Bare en test så det ikke er error i filen
         */

        Timestamp start = Timestamp.valueOf("2011-05-04 12:15:00");
        Timestamp slutt = Timestamp.valueOf("2011-05-04 16:00:00");


        int id = meetings.size()+1; //denne må vel hentes ut av databasen etter at den er lagret der?
        Person creator = user;
        Timespan time = new Timespan(start,slutt);
        String description = /*String fra GUI, beskrivelse av møte/avtale*/"";
        String place = "";
        ArrayList<MeetingRoom> meetingRooms = DB.getMeetingRooms(/*input fra GUI, størrelse på ønsket rom*/);
        MeetingRoom meetingRoom = (MeetingRoom) meetingRooms.get(0);
        for(int i = 1; i < meetingRooms.size()-1; i++){
            MeetingRoom nextMeetingRoom = (MeetingRoom) meetingRooms.get(i);
            if(nextMeetingRoom.getSize()>meetingRoom.getSize()){
                meetingRoom = nextMeetingRoom;
            }
        }
        if (meetingRoom == null){
            new Meeting(id, creator, time, description, place);
        }
        new Meeting(id, creator, time, description, meetingRoom);
    }



    public void editAppointment(Appointment appointment, Timestamp start, Timestamp end, String description, String place) {
        Timespan time = new Timespan(start, end);
        appointment.setTime(time);
        appointment.setDescription(description);
        try {
            DB.editAppointment(appointment);
        } catch (SQLException ex) {

        }
 }

    

    public void deleteAppointment(Appointment appointment) {
        try {
            DB.removeAppointment(appointment.getId());
        } catch (SQLException ex) {

        }
    }

    public void getCalender(Person person) {
        try{
        DB.getAppointments(person);
        DB.getMeetings(person);
        }
        catch(SQLException e) {

        }
    }

    public void sendMessage() {

    }

    public void recieveMessage(Person person) {
        try {
            ArrayList<Message> m = DB.getMessages(person.getUsername());
            for (int i = 0; i < m.size(); i++) {
                messages.add(m.get(i));
                DB.removemessage(m.get(i).getId());
            }
        } catch (SQLException ex) {

        }
    }

    public void changeMeetingTime () {
      
    }
/*
    public String receiveMessage(Person person){
        String message = "";
        try {
            ArrayList<Message> m = DB.getMessages(person.getUsername());
            int size = m.size();
            for(int i = 0; i <size-1; i++){
                Message mes = m.get(i);
                message += mes.toString();
            }
        } catch (SQLException ex) {

        }
        return message;
    }
 *
 */



}
