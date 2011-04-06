/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

/**
 *
 * @author Anders
 */
public class Invitation {
    private int id;

    private Meeting meet;

    public static enum status{
        PARTICIPATING,
        NOT_PARTICIPATING,
        NOT_ANSWERED
    }

    public status answered;

    /*
    *Konstruktør som tar inn møtet og status
    *
    *@param meet møtet invitasjonen gjelder, Meeting, kan ikke være null
    *@param answered enum som viser hva personen som er invitert har svart, blir satt til NOT_ANSWERED som default
    *
    *@return Invitation
    */
    public Invitation(Meeting meet, status answered) {
        this.meet = meet;
        this.answered = answered;
    }

    /*
    *Konstruktør som tar inn id, møtet og status
    *
    *@param id invitasjonens id
    *@param meet møtet invitasjonen gjelder, Meeting, kan ikke være null
    *@param answered enum som viser hva personen som er invitert har svart, blir satt til NOT_ANSWERED som default
    *
    *@return Invitation
    */
    public Invitation(int id, Meeting meet, status answered) {
        this.id = id;
        this.meet = meet;
        this.answered = answered;
    }

    /*
    *Metode som returnerer hva personen som er invitert har svart.
    *
    *@return status Statusen til invitasjonen
    */
    public status getAnswered() {
        return answered;
    }

    /*
    *Metode som setter statusen til invitasjonen.
    *
    *@param answered Den nye statusen til invitasjonen
    *
    *@return void
    *
    */
    public void setAnswered(status answered) {
        this.answered = answered;
    }

    /*
     * Metode som returnerer id'en til invitasjonen
     *
     * @return id id'en til invitasjonen
     */
    public int getId() {
        return id;
    }

    /*
     * metode som setter id'en til invitasjonen
     *
     * @return void
     */
    public void setId(int id) {
        this.id = id;
    }

    /*
     * Metode som returnerer møtet som invitasjonen gjelder
     *
     * @return Meeting Møtet invitasjonen gjelder
     */
    public Meeting getMeet() {
        return meet;
    }

     /*
     *Metode som setter møtet som invitasjonen skal gjelde
     *
     *@param møtet invitasjonen skal gjelde
     *
     *@return void
     */
    public void setMeet(Meeting meet) {
        this.meet = meet;
    }

    /*
     * Metode som lager en beskrivende string av invitasjonen og returnerer denne
     *
     * @return String Stringen som beskriver invitasjonen
     */
    public String toString() {
        return "Invitasjon til" + meet.toString() + " og din status er:" + getAnswered().toString();
    }
}
