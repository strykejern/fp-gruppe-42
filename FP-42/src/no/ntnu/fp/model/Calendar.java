/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

import Database.DB;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Anders
 */
public class Calendar {

    private Person bruker;
    private ArrayList<Meeting> meetings;
    private ArrayList<Appointment> appointments;

    public Calendar (Person bruker)
        throws SQLException {
        this.bruker = bruker;
        this.meetings = DB.getMeetings(bruker);
        this.appointments = DB.getAppointments(bruker);

    }

    public boolean logOn(String brukernavn, String passord)
        throws SQLException {
        Person bruker = DB.getPerson(brukernavn);
        if (bruker.getPassword() == passord) {
            Calendar c = new Calendar(bruker);
            return true;
        }
    }



}
