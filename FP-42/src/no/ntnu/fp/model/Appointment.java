/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

import java.sql.SQLException;


/**
 *
 * @author Anders, Jan-Tore, Snorre, Thomas
 */
public class Appointment {
    private int id;
    private Person creator;
    private Timespan time;
    private String description;
    private String place;
    private MeetingRoom meetingRoom;
    private int atMeetingRoom;

    /*
     * Konstruktør som tar inn id, oppretter, tid, beskrivelse og sted
     *
     * @param id Avtalens id
     * @param creator Hvem som oppretter avtalen
     * @param time Når avtalen
     * @param description Beskrivelse av avtalen
     * @param place Hvor avtalen skal være
     *
     * @return Appointment Avtalen som ble opprettet
     */
    public Appointment(int id, Person creator, Timespan time, String description, String place) {
        this.id = id;
        this.creator = creator;
        this.time = time;
        this.description = description;
        this.place = place;
        atMeetingRoom = 0;
    }

    /*
     * Konstruktør som tar inn id, oppretter, tid, beskrivelse og møterom
     *
     * @param id Avtalens id
     * @param creator Hvem som oppretter avtalen
     * @param time Når avtalen
     * @param description Beskrivelse av avtalen
     * @param møterom Hvor avtalen skal være
     *
     * @return Appointment Avtalen som ble opprettet
     */
    public Appointment(int id, Person creator, Timespan time,String description, MeetingRoom meetingRoom) {
        this.id = id;
        this.creator = creator;
        this.time = time;
        this.description = description;
        this.meetingRoom = meetingRoom;
        atMeetingRoom = 1;
    }

    /*
     * Konstruktør som tar inn oppretter, tid, beskrivelse og møterom
     *
     * @param creator Hvem som oppretter avtalen
     * @param time Når avtalen
     * @param description Beskrivelse av avtalen
     * @param meetingRoom Hvor avtalen skal være
     *
     * @return Appointment Avtalen som ble opprettet
     */
    public Appointment(Person creator, Timespan time, String description, MeetingRoom meetingRoom) {
        this.creator = creator;
        this.time = time;
        this.description = description;
        this.meetingRoom = meetingRoom;
        atMeetingRoom = 1;
    }

    /*
     * Konstruktør som tar inn oppretter, tid, beskrivelse og sted
     *
     * @param creator Hvem som oppretter avtalen
     * @param time Når avtalen
     * @param description Beskrivelse av avtalen
     * @param place Hvor avtalen skal være
     *
     * @return Appointment Avtalen som ble opprettet
     */
    public Appointment(Person creator, Timespan time, String description, String place) {
        this.creator = creator;
        this.time = time;
        this.description = description;
        this.place = place;
        atMeetingRoom = 0;
    }

    /*
     * Metode som returnerer id'en til avtalen
     *
     * @return int Id'en til avtalen
     */
    public int getId() {
        return id;
    }

    /*
     * Metode som setter id'en til avtalen
     *
     * @return void
     */
    public void setId(int id) {
        this.id = id;
    }

    /*
     * Metode som returnerer når avtalen skal være
     *
     * @return Timespan
     */
    public Timespan getTime() {
        return time;
    }

    /*
     * Metode som setter når avtalen skal være
     *
     * @return void
     */
    public void setTime(Timespan time) {
        this.time = time;
    }

    /*
     * Metode som returnerer personen som har opprettet avtalen
     *
     * @return Person Personen som har opprettet avtalen
     */
    public Person getCreator() {
        return creator;
    }

    /*
     * Metode som endrer hvem som har opprettet avtalen
     *
     * @param creator Personen som har opprettet møtet
     *
     * @return void
     */
    public void setCreator(Person creator) {
        this.creator = creator;
    }

    /*
     * Metode som returnerer beskrivelsen av avtalen
     *
     * @return String Beskrivelsen av møtet
     */
    public String getDescription() {
        return description;
    }

    /*
     * Metode som gir avtalen en ny beskrivelse
     *
     * @param description Den nye beskrivelsen
     *
     * @reutn void
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * Metode som returnerer møterommet hvor avtalen skal være
     *
     * @return MeetingRoom Møterommet hvor avtalen skal være
     */
    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    /*
     * Metode som endrer på hvor avtalen skal være
     *
     * @param meetingRoom
     *
     * @return void
     */
    public void setMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    /*
     * Metode som returnerer hvor avtalen skal være
     *
     * @return String Hvor avtalen skal være
     */
    public String getPlace() {
        return place;
    }

    /*
     * Metode som endrer hvor avtalen skal være
     *
     * @param String place
     *
     * @return void
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /*
     * Metode som returnerer om avtalen er i et møterom
     *
     * @return int 1 hvis avtalen er i et møterom, 0 hvis ikke.
     */
    public int isAtMeetingRoom(){
        return atMeetingRoom;
    }

    /*
     * Metode som returnerer en string med informasjon om avtalen
     *
     * @return String String med informasjon om avtalen
     */
    public String toString () {
        String room;

        if(atMeetingRoom == 1) {
            room = meetingRoom.getName();
        }
        else{
            room = getPlace();
        }
        return "Møte IDen er "+getId()+" og " + getTime().toString() + " i rom " + room + " og gjelder " + getDescription();
    }
}
