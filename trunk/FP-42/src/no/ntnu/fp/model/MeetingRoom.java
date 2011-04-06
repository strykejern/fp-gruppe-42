/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ntnu.fp.model;

/**
 *
 * @author Anders, Jan-Tore, Snorre, Thomas
 */
public class MeetingRoom {

    private int id;
    private String name;
    private int size;

    /*
     * Konstruktør som tar inn navn og størrelse
     *
     * @param name Navnet på møterommet
     * @param size Størrelsen på møterommet, i.e., hvor mange personer det er plass til
     *
     * @return MeetingRoom Møterommet som blir opprettet
     */
    public MeetingRoom(String name, int size) {

        this.name = name;
        this.size = size;
    }

       /*
     * Konstruktør som tar inn id, navn og størrelse
     *
     * @param id Møterommets id
     * @param name Navnet på møterommet
     * @param size Størrelsen på møterommet, i.e., hvor mange personer det er plass til
     *
     * @return MeetingRoom Møterommet som blir opprettet
     */
    public MeetingRoom(int id, String name, int size) {

        this.id = id;
        this.name = name;
        this.size = size;

    }

    /*
     * Metode som returnerer id'en til møterommet
     *
     * @return int Id'en til møterommet
     */
    public int getId() {
        return id;
    }

    /*
     * Metode som returnerer navnet på møterommet
     *
     * @return String Navnet på møterommet
     */
    public String getName() {
        return name;
    }

    /*
     * Metode som returnerer størrelsen på møteromnmet
     *
     * @param int Størrelsen på møterommet
     */
    public int getSize() {
        return size;
    }

    /*
     * Metode som lager en string som beskriver møterommet og returnerer denne
     *
     * @return String Stringen som beskriver møtet
     */
    public String toString() {
        return "ID: " + id + " Navn: " + name + " Size: " + size;
    }

}
