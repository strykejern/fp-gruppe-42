/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import Database.DB;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.ntnu.fp.model.Appointment;
import no.ntnu.fp.model.MeetingRoom;
import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Timespan;


/**
 *
 * @author Anders
 */
public class commandLineInterface {

    public static void main(String[] args) {
        try {
            DB.initializeDB("ovemor_fp", "fp42", "jdbc:mysql://mysql.stud.ntnu.no/ovemor_fp_test");
        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }

        Scanner input = new Scanner(System.in);

        String username;
        String password;

        do {
            System.out.println("LOGIN");

            System.out.print("Username: ");
            username = input.next();

            System.out.print("Password: ");
            password = input.next();
        }
        while (!DB.login(username, password));

        Person me;
        try {
            me = DB.getPerson(username);
        } catch (SQLException ex) {
            System.out.println("FAIL: " + ex.getMessage());
            return;
        }

        System.out.println("SUCCESS! Logged in as \"" + username + "\"");

        help();

        String command = input.next();

        if (command.equals("addappointment")){
            Timestamp start = Timestamp.valueOf(input.next() + " " + input.next());
            Timestamp end = Timestamp.valueOf(input.next() + " " + input.next());
            Timespan span = new Timespan(start, end);

            String place = input.next();
            String description = input.nextLine();

            Appointment app;
            if (place.startsWith("#")){
                MeetingRoom room;
                try {
                    room = DB.getMeetingRoom(Integer.parseInt(place.substring(1)));
                } catch (SQLException ex) {
                    System.out.println("FAIL: " + ex.getMessage());
                    return;
                }
                app = new Appointment(me, span, description, room);
            }
            else{
                app = new Appointment(me, span, description, place);
            }
            try {
                DB.addAppointment(app, false);
            } catch (SQLException ex) {
                System.out.println("FAIL: " + ex.getMessage());
            }

        }
        else if(command.equals("editappointment")){
            int appID = Integer.parseInt(input.next());

            Timestamp start = Timestamp.valueOf(input.next() + " " + input.next());
            Timestamp end = Timestamp.valueOf(input.next() + " " + input.next());
            Timespan span = new Timespan(start, end);

            String place = input.next();
            String description = input.nextLine();

            Appointment app;
            if (place.startsWith("#")){
                MeetingRoom room;
                try {
                    room = DB.getMeetingRoom(Integer.parseInt(place.substring(1)));
                } catch (SQLException ex) {
                    System.out.println("FAIL: " + ex.getMessage());
                    return;
                }
                app = new Appointment(appID, me, span, description, room);
            }
            else{
                app = new Appointment(appID, me, span, description, place);
            }
            try {
                DB.editAppointment(app);
            } catch (SQLException ex) {
                System.out.println("FAIL: " + ex.getMessage());
            }

        }
        else if(command.equals("deleteappointment")){

        }
        else if(command.equals("addparticipant")){

        }
        else if(command.equals("answerinvitation")){

        }
        else if(command.equals("viewcalendar")){
            System.out.println("");
            System.out.println("Your appointments:");
            try {
                for (Appointment app : DB.getAppointments(me)) {
                    System.out.println(app.toString());
                }
            } catch (SQLException ex) {
                System.out.println("FAIL: " + ex.getMessage());
            }
        }
        else{
            System.out.println("Invalid command");
        }

    }

    static void help(){
        System.out.println("");
        System.out.println("addappointment start end place/#meetingRoom description");
        System.out.println("");
        System.out.println("editappointment id start end place/#meetingRoom description");
        System.out.println("");
        System.out.println("deleteappointment id");
        System.out.println("");
        System.out.println("addparticipant meetingID personID[,personID,,,]");
        System.out.println("");
        System.out.println("answerinvitation id yes/no");
        System.out.println("");
        System.out.println("viewcalendar");
        System.out.println("");
    }
}
