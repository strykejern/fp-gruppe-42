/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import Database.DB;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
        while (true) {
        String command = input.next();

        if (command.equals("addappointment")){
            String type = input.next();
            Timestamp start = Timestamp.valueOf(input.next() + " " + input.next());
            Timestamp end = Timestamp.valueOf(input.next() + " " + input.next());
            Timespan span = new Timespan(start, end);

            String place = input.next();
            String description = input.nextLine();

            Appointment app;
            int meeting = 0;
            if (type.equals("meeting")){
                meeting = 1;
            }

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
                DB.addAppointment(app, meeting);
            } catch (SQLException ex) {
                System.out.println("FAIL: " + ex.getMessage());
            }

        }
        else if(command.equals("showmeetingrooms")) {
            int size = Integer.parseInt(input.next());
            ArrayList<MeetingRoom> rooms = new ArrayList<MeetingRoom>();
                try {
                    rooms = DB.getMeetingRooms(size);
                } catch (SQLException ex) {

                }
            if(rooms.isEmpty()) {
                System.out.println("No availeble rooms of this size");
            }
            for (MeetingRoom m : rooms) {
                System.out.println(m.toString());
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
            int id = Integer.parseInt(input.next());
            try{
                DB.removeAppointment(id);
                System.out.println("Appointment " +id+ " has been deleted!");
            }
            catch(SQLException e) {
            }
        }

        else if(command.equals("addparticipant")){
            try{
                DB.addParticipant(input.next(), Integer.parseInt(input.next()));
            }
            catch(SQLException e){}
        }
        else if(command.equals("addmeetingroom")) {
            int size = Integer.parseInt(input.next());
            String name = input.next();

            try{
                DB.addMeetingRoom(new MeetingRoom(name, size));
                System.out.println(name + "added to database");
            }
            catch(SQLException e) {

            }
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
        else if(command.equals("help")) {
            help();
        }

        else if(command.equals("close")) {
            break;
        }
        
        else{
            System.out.println("Invalid command");
        }
       }

    }

    static void help(){
        System.out.println("");
        System.out.println("Commands");
        System.out.println("*****************************************************************************************************************************************************");
        System.out.println("** addappointment ** -- type(\"meeting\"/\"appointment\") start end place/#meetingRoom description. Want to book room? Type first showmeetingrooms");
        System.out.println("** showmeetingrooms ** -- size");
        System.out.println("** editappointment ** -- id start end place/#meetingRoom description");
        System.out.println("** deleteappointment ** -- id");
        System.out.println("** addparticipant ** -- meetingID personID[,personID,,,]");
        System.out.println("** addmeetingroom ** -- size and name");
        System.out.println("** answerinvitation ** -- id yes/no");
        System.out.println("** viewcalendar ** -- views users calendar");
        System.out.println("** close ** --  stops the program");
        System.out.println("** help ** -- views the help menu");
        System.out.println("");
    }
}
