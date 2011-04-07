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

    /*
     * oppretter et tidsrom
     * @param start, starttidspunkt
     * @param end, sluttidspunkt
     */
    public Timespan(Timestamp start, Timestamp end) {
        this.start = start;
        this.end = end;
    }

    /*
     * henter ut sluttidspunkt
     * @return sluttidspunkt
     */
    public Timestamp getEnd() {
        return end;
    }

    /*
     * endrer sluttidspunkt
     * @param end, nytt sluttidspunkt
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /*
     * henter ut starttidspunktet
     * @return startidspunkt
     */
    public Timestamp getStart() {
        return start;
    }

    /*
     * setter nytt starttidspunkt
     * @param start, starttidspunkt
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    @Override
    public String toString() {
        String tid = "Fra: " + start.toString() + " Til: " + end.toString();
        return tid;
    }

    
}
