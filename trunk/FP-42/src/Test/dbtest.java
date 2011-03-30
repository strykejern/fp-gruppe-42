/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import Database.DB;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.ntnu.fp.model.Person;

/**
 *
 * @author omorch
 */
public class dbtest {
    public static void main(String[] args) {
        try {
            DB.initializeDB("ovemor_fp", "fp42", "jdbc:mysql://mysql.stud.ntnu.no/ovemor_fp");
        } catch (ClassNotFoundException ex) {

        } catch (InstantiationException ex) {

        } catch (IllegalAccessException ex) {

        } catch (SQLException ex) {

        }
        try {
            DB.addPerson(new Person("ove", "Ove", "ove.no"));
        } catch (SQLException ex) {
         
        }
    }

}
