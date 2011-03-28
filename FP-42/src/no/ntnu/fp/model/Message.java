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

    public Message(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public Message(int id, String subject, String content) {
        this.id = id;
        this.subject = subject;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


}
