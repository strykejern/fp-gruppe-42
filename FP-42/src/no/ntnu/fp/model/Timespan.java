/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

import java.sql.Timestamp;

/**
 *
 * @author Anders
 */
public class Timespan {

    private Timestamp start;
    private Timestamp end;

    public Timespan(Timestamp start, Timestamp end) {
        this.start = start;
        this.end = end;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    @Override
    public String toString() {
        String tid = "starter klokken " + start.toString() + " og slutter klokken " + end.toString();
        return tid;
    }

    
}
