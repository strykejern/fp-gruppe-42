package no.ntnu.fp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

/**
 * The <code>Person</code> class stores information about a single person.
 * 
 * @author Thomas &Oslash;sterlie
 *
 * @version $Revision: 1.5 $ - $Date: 2005/02/20 14:52:29 $
 */
public class Person {


	private String name;

	private String email;
	
        private String username;

        private String password;
	
	/**
	 * Constructs a new <code>Person</code> object with specified name, email, and date
	 * of birth.
	 * 
	 * @param name The name of the person.
	 * @param email The person's e-mail address
	 * @param dateOfBirth The person's date of birth.
	 */
	public Person(String username, String name, String email, String password) {
		this.username = username;
                this.name = name;
		this.email = email;
                this.password = password;
	}

        /**
         * Sets the person's name.
         *
         * @return void.
         */
	public void setName(String name) {
		this.name = name;
	}

        /**
         * Sets the person's password.
         *
         * @return void.
         */
        public void setPassword(String password){
            this.password = password;
        }

        /**
         * Sets the person's email.
         *
         * @return void.
         */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Returns the person's name.
	 * 
	 * @return The person's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the person's email address.
	 * 
	 * @return The person's email address.
	 */
	public String getEmail() {
		return email;
	}

        /**
	 * Returns the person's password.
	 *
	 * @return The person's password.
	 */
        public String getPassword() {
            return password;
        }

        /**
	 * Returns the person's username.
	 *
	 * @return The person's username.
	 */
        public String getUsername() {
            return username;
        }
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		String s = "Name: " + getName() + "; ";
		s += "Email: " + getEmail() + "; ";
		s += "User name: " + getUsername();
		return s;
	}
}
