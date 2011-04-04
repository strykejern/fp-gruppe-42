/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import Database.DB;
import java.util.Scanner;


/**
 *
 * @author Anders
 */
public class commandLineInterface {

    public static void main(String[] args) {
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
        while (DB.login(username, password));

    }
}
