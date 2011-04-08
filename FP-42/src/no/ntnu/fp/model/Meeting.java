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

    private ArrayList<Person> notAnswered;
    private Person user;
    private ArrayList<Person> accepted;
    private ArrayList<Person> declined;

    /*
     * Konstruktør som tar inn id, person, tid, beskrivelse og sted.
     *
     * @param id id'en til møtet
     * @param person Personen som opprettet møtet
     * @param time Når møtet skal være
     * @param description Beskrivelse av møtet
     * @param place Hvor møtet skal være
     *
     * @return Meeting Møtet som blir opprettet
     */
    public Meeting (int id, Person person, Timespan time, String description, String place)
        throws SQLException {
        super(id, person, time, description, place);
        this.user = person;
        this.notAnswered = DB.getParticipants(this.getId(), status.NOT_ANSWERED);
        this.accepted = DB.getParticipants(this.getId(), status.PARTICIPATING);
        this.declined = DB.getParticipants(this.getId(), status.NOT_PARTICIPATING);
    }

    /*
     * Konstruktør som tar inn id, person, tid, beskrivelse og møterom.
     *
     * @param id id'en til møtet
     * @param person Personen som opprettet møtet
     * @param time Når møtet skal være
     * @param description Beskrivelse av møtet
     * @param meetingRoom Hvor møtet skal være
     *
     * @return Meeting Møtet som blir opprettet
     */
    public Meeting (int id, Person person, Timespan time,String description, MeetingRoom meetingRoom)
        throws SQLException {
        super(id, person, time, description, meetingRoom);
        this.user = person;
        this.notAnswered = DB.getParticipants(this.getId(), status.NOT_ANSWERED);
        this.accepted = DB.getParticipants(this.getId(), status.PARTICIPATING);
        this.declined = DB.getParticipants(this.getId(), status.NOT_PARTICIPATING);
    }

     /*
     * Konstruktør som tar inn person, tid, beskrivelse og møterom.
     *
     * @param person Personen som opprettet møtet
     * @param time Når møtet skal være
     * @param description Beskrivelse av møtet
     * @param meetingRoom Hvor møtet skal være
     *
     * @return Meeting Møtet som blir opprettet
     */
    public Meeting (Person person, Timespan time, String description, MeetingRoom meetingRoom)
        throws SQLException {
        super(person, time, description, meetingRoom);
        this.user = person;
        this.notAnswered = DB.getParticipants(this.getId(), status.NOT_ANSWERED);
        this.accepted = DB.getParticipants(this.getId(), status.PARTICIPATING);
        this.declined = DB.getParticipants(this.getId(), status.NOT_PARTICIPATING);
    }

     /*
     * Konstruktør som tar inn person, tid, beskrivelse og sted.
     *
     * @param person Personen som opprettet møtet
     * @param time Når møtet skal være
     * @param description Beskrivelse av møtet
     * @param meetingRoom Hvor møtet skal være
     *
     * @return Meeting Møtet som blir opprettet
     */
    public Meeting (Person person, Timespan time, String description, String place)
        throws SQLException {
        super(person, time, description, place);
        this.user = person;
        this.notAnswered = DB.getParticipants(this.getId(), status.NOT_ANSWERED);
        this.accepted = DB.getParticipants(this.getId(), status.PARTICIPATING);
        this.declined = DB.getParticipants(this.getId(), status.NOT_PARTICIPATING);
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
        for(Person person : getParticipants()) {
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
        for(Person person : getParticipants()) {
            try{
                DB.addInvitation(invitation, person.getUsername(), this.user.getUsername());
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
        this.notAnswered.add(person);
    }

    public ArrayList<Person> getParticipants() {
        ArrayList<Person> a = new ArrayList<Person>();
        a.addAll(notAnswered);
        a.addAll(accepted);
        a.addAll(declined);
        return a;
    }

    /*
     * Fjerne deltaker fra tabellen
     *
     * @param person Personen som skal fjernes
     *
     * @return void
     */
    public void removeParticipant(Person person) {
        this.getParticipants().remove(person);
    }

}
