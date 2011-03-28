/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

/**
 *
 * @author Anders
 */
public class Appointment {
    private Person creator;
    private int start;
    private int end;
    private String description;
    private String place;
    private MeetingRoom meetingRoom;

    public Appointment(Person creator, int start, int end, String description, String place) {
        this.creator = creator;
        this.start = start;
        this.end = end;
        this.description = description;
        this.place = place;
    }

    public Appointment(Person creator, int start, int end, MeetingRoom meetingRoom) {
        this.creator = creator;
        this.start = start;
        this.end = end;
        this.meetingRoom = meetingRoom;
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

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

}
