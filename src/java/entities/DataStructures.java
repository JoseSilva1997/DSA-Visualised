/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * NOTE: I had to add extra quotes to each of the attributes and table name (something derby related).
 * If your pages can't communicate properly with the database like an java.sql.SQLSyntaxErrorException, remove the quotes.
 * @author shade
 */
@Entity
@Table(name = "\"DataStructures\"")
@NamedQueries({
    @NamedQuery(name = "DataStructures.findAll", query = "SELECT d FROM DataStructures d"),
    @NamedQuery(name = "DataStructures.findByDsId", query = "SELECT d FROM DataStructures d WHERE d.dsId = :dsId"),
    @NamedQuery(name = "DataStructures.findByName", query = "SELECT d FROM DataStructures d WHERE d.name = :name"),
    @NamedQuery(name = "DataStructures.findByKeyAspects", query = "SELECT d FROM DataStructures d WHERE d.keyAspects = :keyAspects"),
    @NamedQuery(name = "DataStructures.findByOperations", query = "SELECT d FROM DataStructures d WHERE d.operations = :operations"),
    @NamedQuery(name = "DataStructures.findByWhenToUse", query = "SELECT d FROM DataStructures d WHERE d.whenToUse = :whenToUse"),
    @NamedQuery(name = "DataStructures.findByWhenNotTo", query = "SELECT d FROM DataStructures d WHERE d.whenNotTo = :whenNotTo"),
    @NamedQuery(name = "DataStructures.findByResourcePath", query = "SELECT d FROM DataStructures d WHERE d.resourcePath = :resourcePath")})
public class DataStructures implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "\"ds_id\"")
    private Integer dsId;
    @Basic(optional = false)
    @Column(name = "\"name\"")
    private String name;
    @Column(name = "\"key_aspects\"")
    private String keyAspects;
    @Column(name = "\"operations\"")
    private String operations;
    @Column(name = "\"when_to_use\"")
    private String whenToUse;
    @Column(name = "\"when_not_to\"")
    private String whenNotTo;
    @Column(name = "\"resource_path\"")
    private String resourcePath;
    @JoinColumn(name = "\"editor_username\"", referencedColumnName = "username")
    @ManyToOne
    private Users editorUsername;

    public DataStructures() {
    }

    public DataStructures(Integer dsId) {
        this.dsId = dsId;
    }

    public DataStructures(Integer dsId, String name) {
        this.dsId = dsId;
        this.name = name;
    }

    public Integer getDsId() {
        return dsId;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyAspects() {
        return keyAspects;
    }

    public void setKeyAspects(String keyAspects) {
        this.keyAspects = keyAspects;
    }

    public String getOperations() {
        return operations;
    }

    public void setOperations(String operations) {
        this.operations = operations;
    }

    public String getWhenToUse() {
        return whenToUse;
    }

    public void setWhenToUse(String whenToUse) {
        this.whenToUse = whenToUse;
    }

    public String getWhenNotTo() {
        return whenNotTo;
    }

    public void setWhenNotTo(String whenNotTo) {
        this.whenNotTo = whenNotTo;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Users getEditorUsername() {
        return editorUsername;
    }

    public void setEditorUsername(Users editorUsername) {
        this.editorUsername = editorUsername;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dsId != null ? dsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataStructures)) {
            return false;
        }
        DataStructures other = (DataStructures) object;
        if ((this.dsId == null && other.dsId != null) || (this.dsId != null && !this.dsId.equals(other.dsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DataStructures[ dsId=" + dsId + " ]";
    }
    
}
