/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import Database.DB;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.ntnu.fp.model.Appointment;
import no.ntnu.fp.model.Invitation;
import no.ntnu.fp.model.Meeting;
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
            username = input.nextLine();

            System.out.print("Password: ");
            password = input.nextLine();
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
            System.out.print("$# ");

            String line         = input.nextLine();

            Scanner arguments   = new Scanner(line);
            
            String command      = arguments.next();

           if (command.equals("addappointment")){
            	String type = arguments.next();
            	Timestamp start = Timestamp.valueOf(arguments.next() + " " + arguments.next());
            	Timestamp end = Timestamp.valueOf(arguments.next() + " " + arguments.next());
            	Timespan span = new Timespan(start, end);

            	String place = arguments.next();
            	String description = arguments.nextLine();

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
                	DB.addAppointment(app, type.equals("meeting"));
            	} catch (SQLException ex) {
                	System.out.println("FAIL: " + ex.getMessage());
            	}

            }

            else if(command.equals("showmeetingrooms")) {

                System.out.println("");
                int size = Integer.parseInt(arguments.next());
                Timestamp start = Timestamp.valueOf(arguments.next() + " " + arguments.next());
                Timestamp end = Timestamp.valueOf(arguments.next() + " " + arguments.next());

                ArrayList<MeetingRoom> rooms = new ArrayList<MeetingRoom>();
                    try {
                        rooms = DB.getMeetingRooms(size, start, end);
                    } catch (SQLException ex) {

                    }
                if(rooms.isEmpty()) {
                    System.out.println("No available rooms of this size");
                }
                for (MeetingRoom m : rooms) {
                        System.out.println(m.toString());
                }
            }
            else if(command.equals("editappointment")){
                int id = Integer.parseInt(arguments.next());
                Timestamp start = Timestamp.valueOf(arguments.next() + " " + arguments.next());
                Timestamp end = Timestamp.valueOf(arguments.next() + " " + arguments.next());
                try{
                    Meeting m = DB.getMeeting(id);
                    if(m.getMeetingRoom() != null){
                        if(DB.isMeetingRoomAvailable(m.getMeetingRoom().getId(), start, end)){
                            DB.editAppointment(m);
                            System.out.println("Møte oppdatert");
                        }
                        else{
                            System.out.println("Møterommet var ikke ledig på dette tidspunktet");
                        }
                    }
                    else{
                        DB.editAppointment(m);
                        System.out.println("Møte oppdatert");
                    }
                }catch(SQLException e){}
            }
            else if(command.equals("deleteappointment")){
                int id = Integer.parseInt(arguments.next());
                try{
                    DB.removeAppointment(id);
                    System.out.println("Appointment " +id+ " has been deleted!");
                }
                catch(SQLException e) {
                }
            }

            else if(command.equals("addparticipant")){
                String to = arguments.next();
                int id = Integer.parseInt(arguments.next());
                try{
                    DB.addParticipant(to, id);
                    Meeting m = DB.getMeeting(id);
                    Invitation i = new Invitation(m, Invitation.status.NOT_ANSWERED);
                    DB.addInvitation(i, to, username);
                }
                catch(SQLException e){}
            }
            else if(command.equals("addmeetingroom")) {
                int size = Integer.parseInt(arguments.next());
                String name = arguments.next();

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
                String user;
                try {
                    user = arguments.next();
                }
                catch (NoSuchElementException e) {
                    try {
                        for (Appointment app : DB.getAppointments(me)) {
                            System.out.println(app.toString());
                        }
                        continue;
                    } catch (SQLException ex) {
                        System.out.println("FAIL: " + e.getMessage());
                        continue;
                    }
                }

                System.out.println("");
                System.out.println("Your appointments:");
                try {
                    for (Appointment app : DB.getAppointments(DB.getPerson(user))){
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
            else if(command.equals("tracemeetings")){
                System.out.println("");
                    
                System.out.println("Hvilket møte vil du sjekke møteinnkallingen for?");

                String id = input.nextLine();

                try{
                    System.out.println(DB.traceMeeting(id));
                }catch (SQLException ex) {

                }
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
        System.out.println("** addappointment ** -- type(\"meeting\"/\"appointment\") start end place/#meetingRoomSize description. Want to book room? Type first showmeetingrooms");
        System.out.println("** showmeetingrooms ** -- size");
        System.out.println("** editappointment ** -- id start end place/#meetingRoom description");
        System.out.println("** deleteappointment ** -- id");
        System.out.println("** addparticipant ** -- meetingID personID[,personID,,,]");
        System.out.println("** addmeetingroom ** -- size and name");
        System.out.println("** answerinvitation ** -- id yes/no");
        System.out.println("** viewcalendar ** -- me/username views your/user's calendar");
        System.out.println("** close ** --  stops the program");
        System.out.println("** help ** -- views the help menu");
        System.out.println("");
    }

    static void error(){
        System.out.println("");
        System.out.println("Error: Check your syntax");
        System.out.println("");
    }
}
