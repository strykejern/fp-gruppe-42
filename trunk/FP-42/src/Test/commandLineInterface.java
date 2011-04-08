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
import no.ntnu.fp.model.Message;
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
        commandloop: while (true) {
            System.out.print("$# ");

            String line         = input.nextLine();

            if (line.isEmpty()) continue;

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
                Timespan span = new Timespan(start, end);
                try{
                    Meeting meeting = DB.getMeeting(id);
                    if(meeting.getMeetingRoom() != null){
                        if(DB.isMeetingRoomAvailable(meeting.getMeetingRoom().getId(), start, end)){
                            meeting.setTime(span);
                            DB.editAppointment(meeting);
                            System.out.println("Møte oppdatert");
                        }
                        else{
                            System.out.println("Møterommet var ikke ledig på dette tidspunktet");
                        }
                    }
                    else{
                        DB.editAppointment(meeting);
                        System.out.println("Møte oppdatert");
                    }
                }catch(SQLException e){
                    System.out.println("Hei" + e.getMessage());
                }
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
                int id = Integer.parseInt(arguments.next());
                String receiver = arguments.next();
                try{
                    Meeting m = DB.getMeeting(id);
                    if(m != null){
                        DB.addParticipant(receiver, id);
                        Invitation i = new Invitation(m, Invitation.status.NOT_ANSWERED);
                        DB.addInvitation(i, receiver, username);
                    }
                }
                catch(SQLException e){
                    System.out.println("FAIL: " + e.getMessage());
                    error();
                }
            }
            else if(command.equals("addmeetingroom")) {
                int size = Integer.parseInt(arguments.next());
                String name = arguments.next();

                try{
                    DB.addMeetingRoom(new MeetingRoom(name, size));
                    System.out.println(name + "added to database");
                }
                catch(SQLException e) {
                    System.out.println("FAIL: " + e.getMessage());
                    error();
                }
            }
            else if(command.equals("answerinvitation")){
                int id = Integer.parseInt(input.next());
                String answer = input.next();
                try{
                    DB.answerInvitation(id, me.getUsername(), answer);
                }
                catch(SQLException e){
                    System.out.println("FAIL: " + e.getMessage());
                    error();
                }
            }
            else if(command.equals("viewcalendar")){
                String user;
                try {
                    user = arguments.next();
                }
                catch (NoSuchElementException e) {
                    user = me.getUsername();
                }

                if (user.equals("me")) user = me.getUsername();

                System.out.println("");
                System.out.println("Your appointments:");
                try {
                    for (Appointment app : DB.getAppointments(DB.getPerson(user))){
                        System.out.println(app.toString());
                    }
                } catch (SQLException ex) {
                    System.out.println("FAIL: " + ex.getMessage());
                    error();
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
            else if(command.equals("inbox")){
                try{
                    ArrayList<Message> inbox = DB.getMessages(me.getUsername());
                    for(Message m : inbox){
                        System.out.println(m.toString());
                    }
               }catch(SQLException ex){
                   System.out.println("FAIL: " + ex.getMessage());
                   error();
               }
            }
            else{
                System.out.println("Invalid command");
                error();
            }
        }
    }

    static void help(){
        System.out.println("");
        System.out.println("Commands");
        System.out.println("*****************************************************************************************************************************************************");
        System.out.println("** addappointment ** -- type(\"meeting\"/\"appointment\") start end place/#meetingRoomSize description. Want to book room? Type first showmeetingrooms");
        System.out.println("** showmeetingrooms ** -- size");
        System.out.println("** editappointment ** -- id start end");
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
