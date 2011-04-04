/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

import java.util.Date;

/**
 *
 * @author Anders
 */
public class Timespan {

    private Date start;
    private Date end;

    public Timespan(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String toString() {
        String tid = "Starter klokken " + start.toString() + " og slutter klokken " + end.toString();
        return tid;
    }

    
}
