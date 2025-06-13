/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 * NOTE: I had to add extra quotes to each of the attributes and table name (something derby related).
 * If your pages can't communicate properly with the database like an java.sql.SQLSyntaxErrorException, remove the quotes.
 * @author shade
 */
@Entity
@Table(name = "\"Users\"")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "Users.findByLastName", query = "SELECT u FROM Users u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "Users.findByRole", query = "SELECT u FROM Users u WHERE u.role = :role")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "\"username\"")
    private String username;
    @Basic(optional = false)
    @Column(name = "\"password\"")
    private String password;
    @Basic(optional = false)
    @Column(name = "\"email\"")
    private String email;
    @Basic(optional = false)
    @Column(name = "\"first_name\"")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "\"last_name\"")
    private String lastName;
    @Column(name = "\"role\"")
    private String role;
    @OneToMany(mappedBy = "editorUsername")
    private Collection<Algorithms> algorithmsCollection;
    @OneToMany(mappedBy = "editorUsername")
    private Collection<DataStructures> dataStructuresCollection;

    public Users() {
    }

    public Users(String username) {
        this.username = username;
    }

    public Users(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collection<Algorithms> getAlgorithmsCollection() {
        return algorithmsCollection;
    }

    public void setAlgorithmsCollection(Collection<Algorithms> algorithmsCollection) {
        this.algorithmsCollection = algorithmsCollection;
    }

    public Collection<DataStructures> getDataStructuresCollection() {
        return dataStructuresCollection;
    }

    public void setDataStructuresCollection(Collection<DataStructures> dataStructuresCollection) {
        this.dataStructuresCollection = dataStructuresCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Users[ username=" + username + " ]";
    }
    
}
