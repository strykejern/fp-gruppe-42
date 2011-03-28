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

    public Invitation(Meeting meet, status answered) {
        this.meet = meet;
        this.answered = answered;
    }

    public Invitation(int id, Meeting meet, status answered) {
        this.id = id;
        this.meet = meet;
        this.answered = answered;
    }

    public status getAnswered() {
        return answered;
    }

    public void setAnswered(status answered) {
        this.answered = answered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Meeting getMeet() {
        return meet;
    }

    public void setMeet(Meeting meet) {
        this.meet = meet;
    }

    
}
