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
    /*
     * Denne skal sende beskjed ved å lagre, den beskjeden den tar inn,
     * i databasetabellen message.
     *
     * @param message Beskjeden som skal sendes
     *
     * @return void
     */
    public void sendMessage(Message message) {
        for(Person person : participants) {
            try{
                DB.addMessage(message, person, user);
            }
            catch (SQLException e) {

            }
        }

    }


    /*
     * Denne skal sende end invitasjon ved å lagre, den invitasjonen den tar inn,
     * i databasetabellen message. Den lagrer alle parametrene som invitasjon bruker
     * som string verdier i tekst.
     *
     * @param invitation Invitasjonen som skal sendes
     *
     * @return void
     */
    public void sendInvitation(Invitation invitation) {
        for(Person person : participants) {
            try{
                DB.addInvitation(invitation, person, this.user);
            }
            catch(SQLException e) {

            }
        }

    }

    /*
     * Legge til deltaker i tabellen
     *
     * @param person Personen som skal legges til
     *
     * @return void
     */

    public void addParticipant(Person person) {
        this.participants.add(person);
    }
    /*
     * Fjerne deltaker fra tabellen
     *
     * @param person Personen som skal fjernes
     *
     * @return void
     */
    public void removeParticipant(Person person) {
        this.participants.remove(person);
    }

}
