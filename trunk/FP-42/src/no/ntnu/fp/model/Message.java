/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

/**
 *
 * @author Anders
 */
public class Message {



    private int id;
    private String subject;
    private String content;
    private String from;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


        /*
     * oppretter en melding
     * @param subject, meldingsemne
     * @param content, meldinsgtekst
     */
    public Message(String subject, String content, String from) {
        this.subject = subject;
        this.content = content;
        this.from = from;
    }

    /*
     * oppretter en melding
     * @param subject, meldingsemne
     * @param content, meldinsgtekst
     * @param id, meldingsid
     */
    public Message(int id, String subject, String content) {
        this.id = id;
        this.subject = subject;
        this.content = content;
    }

    public Message(String subject, String content){
        this.content = content;
        this.subject = subject;
    }

    /*
     * metode som henter meldingsinnhold
     * @return, meldingsinnhold
     */

    public String getContent() {
        return content;
    }

    /*
     * metode som endrer meldingsinnhold
     * @param content, nytt meldingsinnhold
     */
    public void setContent(String content) {
        this.content = content;
    }

    /*
     * metode som henter ut meldingsid
     * @return meldingsid
     */
    public int getId() {
        return id;
    }

    /*
     * metode som endrer meldingsid
     * @param id, ny meldingsid
     */
    public void setId(int id) {
        this.id = id;
    }

    /*
     * metode som henter ut meldingsemne
     * @return meldingsemne
     */
    public String getSubject() {
        return subject;
    }


    /*
     * metode som endrer emnet
     * @param subject, nytt emne
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String toString() {
        return "Fra: " + getFrom() + "\nEmne: " + getSubject() + "\nMelding: " +getContent();
    }


}
