/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

import java.sql.SQLException;


/**
 *
 * @author Anders
 */
public class Appointment {
    private int id;
    private Person creator;
    private Timespan time;
    private String description;
    private String place;
    private MeetingRoom meetingRoom;
    private boolean atMeetingRoom;

    public Appointment(int id, Person creator, Timespan time, String description, String place) {
        this.id = id;
        this.creator = creator;
        this.time = time;
        this.description = description;
        this.place = place;
        atMeetingRoom = false;
    }

    public Appointment(int id, Person creator, Timespan time,String description, MeetingRoom meetingRoom) {
        this.id = id;
        this.creator = creator;
        this.time = time;
        this.description = description;
        this.meetingRoom = meetingRoom;
        atMeetingRoom = true;
    }

    public Appointment(Person creator, Timespan time, String description, MeetingRoom meetingRoom) {
        this.creator = creator;
        this.time = time;
        this.description = description;
        this.meetingRoom = meetingRoom;
        atMeetingRoom = true;
    }

    public Appointment(Person creator, Timespan time, String description, String place) {
        this.creator = creator;
        this.time = time;
        this.description = description;
        this.place = place;
        atMeetingRoom = false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timespan getTime() {
        return time;
    }

    public void setTime(Timespan time) {
        this.time = time;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public boolean isAtMeetingRoom(){
        return atMeetingRoom;
    }

    public String toString () {
        return "Møte er kl :" + getTime().toString() + "i rom " + getPlace() + "og gjelder " + getDescription();
    }
}
