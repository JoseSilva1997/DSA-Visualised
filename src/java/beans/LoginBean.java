/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import entities.Users;
import entities.util.JsfUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import java.io.Serializable;

/**
 * This class manages the user login functionality. Once a user is logged in,
 * their information is stored in "currentUser". The class is SessionScoped to
 * retain information about user who has logged in.
 *
 * @author shade
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceUnit(unitName = "Coursework2PU")
    private EntityManagerFactory emf;

    // Stores the data input from the username input field of the login page.
    private String username;
    // Stores the data input from the password input secret field of the login page.
    private String password;
    // Stores the user currently logged in.
    private Users currentUser;

    /*
       Getters and setters
     */
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    /**
     * Verifies if the user input matches a user from the database. If it does, retrieve the user's
     * information from the database and store it in currentUser.
     * 
     * @return webpage redirect once user has successfully logged in or null otherwise.
     */
    public String validateUser() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();

            TypedQuery<Users> query = em.createNamedQuery("Users.findByUsername", Users.class);
            query.setParameter("username", username);

            Users user = null;
            try {
                user = query.getSingleResult();
            } catch (Exception e) {
                throw new IllegalArgumentException("Wrong username or password");
            }

            if (user != null && user.getPassword().equals(password)) {

                //Login successful
                currentUser = user;
                clearCredentials();

            } else {
                throw new Exception("Wrong username or password");
            }

            //Any unforseen error occurred
            if (currentUser == null) {
                throw new Exception("An error occurred. Please try again later.");
            }

            return "index?faces-redirect=true";

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.getMessage());
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Log the user out by clearing currentUser and invalidating session.
     * @return redirect link to main page.
     */
    public String logout() {

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        currentUser = null;

        return "index?faces-redirect=true";
    }

    /**
     * Check if user is logged in.
     * @return true if user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Check if user is an admin.
     * @return true if user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return isLoggedIn() && "admin".equalsIgnoreCase(currentUser.getRole());
    }

    /**
     * Clear the data from the user inputs.
     */
    private void clearCredentials() {
        username = null;
        password = null;
    }
}
