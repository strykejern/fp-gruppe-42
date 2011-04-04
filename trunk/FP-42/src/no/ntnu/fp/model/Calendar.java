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

    private Person bruker;
    private ArrayList<Meeting> meetings;
    private ArrayList<Appointment> appointments;

    public Calendar (Person bruker)
        throws SQLException {
        this.bruker = bruker;
        this.meetings = DB.getMeetings(bruker);
        this.appointments = DB.getAppointments(bruker);

    }

    public boolean logOn(String brukernavn, String passord)
        throws SQLException {
        Person bruker = DB.getPerson(brukernavn);
        if (bruker.getPassword() == passord) {
            Calendar c = new Calendar(bruker);
            return true;
        }
        return false;
    }

    public void newMeeting() throws SQLException{
        /*
         * Bare en test så det ikke er error i filen
         */

        Timestamp start = Timestamp.valueOf("2011-05-04 12:15:00");
        Timestamp slutt = Timestamp.valueOf("2011-05-04 16:00:00");


        int id = meetings.size()+1;
        Person creator = bruker;
        Timespan time = new Timespan(start,slutt);
        String description = /*String fra GUI, beskrivelse av møte/avtale*/"";
        String place = "";
        ArrayList meetingRooms = DB.getMeetingRoom(/*input fra GUI, størrelse på ønsket rom*/);
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



    public void editAppointment(Appointment appointment, Date start, Date end, String description) {
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
            DB.removeAppointment(appointment);
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

    public void recieveMessage() {

    }

    public void changeMeetingTime () {
      
    }




}
