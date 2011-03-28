/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

/**
 *
 * @author Anders
 */
public class Appointment {
    private Person creator;
    private int start;
    private int end;
    private String description;
    private String place;

    public String getCreator() {
        return creator.getName();
    }

    public String getDescription() {
        return description;
    }

    public int getEnd() {
        return end;
    }

    public String getPlace() {
        return place;
    }

    public int getStart() {
        return start;
    }

    
}
