/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import Database.DB;
import java.sql.Timestamp;
import java.sql.SQLException;
import no.ntnu.fp.model.Invitation;
import no.ntnu.fp.model.Meeting;
import no.ntnu.fp.model.Person;
import no.ntnu.fp.model.Timespan;

/**
 *
 * @author omorch
 */
public class dbtest {
    public static void main(String[] args) {
        try {
            DB.initializeDB("ovemor_fp", "fp42", "jdbc:mysql://mysql.stud.ntnu.no/ovemor_fp_test");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            Timestamp start = Timestamp.valueOf("2011-05-04 12:15:00");
            Timestamp slutt = Timestamp.valueOf("2011-05-04 16:15:00");
            DB.addInvitation(new Invitation(new Meeting(DB.getPerson("ove"), new Timespan(start,slutt), "Test", "blabla"), Invitation.status.NOT_ANSWERED), new Person("ove", "Ove", "ove.no"), new Person("kristine", "Kristine", "kristine.no"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

     
      /* try {
            Timestamp start = Timestamp.valueOf("2011-05-04 12:15:00");
            Timestamp slutt = Timestamp.valueOf("2011-05-04 16:15:00");
            DB.addAppointment(new Appointment(new Person("ove", "Ove", "ove.no"), new Timespan(start, slutt), "Progge", "Gløs"), false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

       */

    }

}
