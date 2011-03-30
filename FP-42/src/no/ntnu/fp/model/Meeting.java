/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

import Database.DB;
import Database.DB.status;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Anders
 */
public class Meeting extends Appointment{

    private ArrayList<Person> participants;
    private Person user;
    private ArrayList<Person> accepted;
    private ArrayList<Person> denied;

    public Meeting (int id, Person person, Timespan time, String description, String place)
        throws SQLException {
        super(id, person, time, description, place);
        this.user = person;
        this.participants = DB.getParticipants(this, status.ALL);
        this.accepted = DB.getParticipants(this, status.PARTICIPATING);
        this.denied = DB.getParticipants(this, status.NOT_PARTICIPATING);
    }
    
    public Meeting (int id, Person person, Timespan time,String description, MeetingRoom meetingRoom)
        throws SQLException {
        super(id, person, time, description, meetingRoom);
        this.user = person;
        this.participants = DB.getParticipants(this, status.ALL);
        this.accepted = DB.getParticipants(this, status.PARTICIPATING);
        this.denied = DB.getParticipants(this, status.NOT_PARTICIPATING);
    }

    public Meeting (Person person, Timespan time, String description, MeetingRoom meetingRoom)
        throws SQLException {
        super(person, time, description, meetingRoom);
        this.user = person;
        this.participants = DB.getParticipants(this, status.ALL);
        this.accepted = DB.getParticipants(this, status.PARTICIPATING);
        this.denied = DB.getParticipants(this, status.NOT_PARTICIPATING);
    }

    public Meeting (Person person, Timespan time, String description, String place)
        throws SQLException {
        super(person, time, description, place);
        this.user = person;
        this.participants = DB.getParticipants(this, status.ALL);
        this.accepted = DB.getParticipants(this, status.PARTICIPATING);
        this.denied = DB.getParticipants(this, status.NOT_PARTICIPATING);
    }

    public void sendMessage() {

    }

    public void sendInvitation() {

    }

    public void addParticipant() {

    }

    public void removeParticipant() {

    }

}
