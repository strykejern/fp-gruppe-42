/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

import Database.DB;
import java.sql.SQLException;
import java.util.ArrayList;

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
        int id = meetings.size()+1;
        Person creator = bruker;
        Timespan time = new Timespan(/*input fra GUI, dato på avtale/møte*/);
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
}
