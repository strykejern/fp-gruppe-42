/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;

package Database;

/**
 *
 * @author Snorre
 */
public class DB {

    public static void initializeDB
                (String userName, String password, String databaseLocation)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException{

        if (dbConnection != null) {
            dbConnection.close();
        }
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        dbConnection = DriverManager.getConnection(
                databaseLocation, userName, password);
    }


    public static Object getPerson() throws SQLException{
        final String query = "SELECT * FROM menu ORDER BY dish_id ASC";

        Statement stat = dbConnection.createStatement();
        stat.executeQuery(query);
        ResultSet result = stat.getResultSet();

        Menu m = new Menu();
        while (result.next()){
            int id          = result.getInt("dish_id");
            String name     = result.getString("name");
            int price       = result.getInt("price");
            String comment  = result.getString("description");

            m.addDish(new Dish(id, name, price, comment));
        }

        result.close();
        stat.close();

        return m;
    }

 /*   public static void addAvtale(Avtale avtaler)
            throws SQLException {


        String query = "INSERT INTO avtale "
                + "(Oppretter, Starttidspunkt, Sluttidspunt, Beskrivelse, Sted, M_ID) VALUES ("+
                avtaler.moteleder.getName() + ", " +
                avtaler.start + ", " +
                avtaler.slutt + ", " +
                avtaler.beskrivelse + ", " +
                avtaler.sted + ", " +
                avtaler.getM_ID() + ",)";

        Statement stat = dbConnection.createStatement();

        stat.executeUpdate(query);
    }
  */
}