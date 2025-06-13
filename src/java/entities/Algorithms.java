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
 * 
 * @author shade
 */
@Entity
@Table(name = "\"Algorithms\"")
@NamedQueries({
    @NamedQuery(name = "Algorithms.findAll", query = "SELECT a FROM Algorithms a"),
    @NamedQuery(name = "Algorithms.findByAlgId", query = "SELECT a FROM Algorithms a WHERE a.algId = :algId"),
    @NamedQuery(name = "Algorithms.findByName", query = "SELECT a FROM Algorithms a WHERE a.name = :name"),
    @NamedQuery(name = "Algorithms.findByDescription", query = "SELECT a FROM Algorithms a WHERE a.description = :description"),
    @NamedQuery(name = "Algorithms.findByBTimeComplexity", query = "SELECT a FROM Algorithms a WHERE a.bTimeComplexity = :bTimeComplexity"),
    @NamedQuery(name = "Algorithms.findByWTimeComplexity", query = "SELECT a FROM Algorithms a WHERE a.wTimeComplexity = :wTimeComplexity"),
    @NamedQuery(name = "Algorithms.findByIsStable", query = "SELECT a FROM Algorithms a WHERE a.isStable = :isStable"),
    @NamedQuery(name = "Algorithms.findByIsInPlace", query = "SELECT a FROM Algorithms a WHERE a.isInPlace = :isInPlace"),
    @NamedQuery(name = "Algorithms.findByResourcePath", query = "SELECT a FROM Algorithms a WHERE a.resourcePath = :resourcePath")})
public class Algorithms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "\"alg_id\"")
    private Integer algId;
    @Basic(optional = false)
    @Column(name = "\"name\"")
    private String name;
    @Column(name = "\"description\"")
    private String description;
    @Column(name = "\"b_time_complexity\"")
    private String bTimeComplexity;
    @Column(name = "\"w_time_complexity\"")
    private String wTimeComplexity;
    @Column(name = "\"is_stable\"")
    private Boolean isStable;
    @Column(name = "\"is_in_place\"")
    private Boolean isInPlace;
    @Column(name = "\"resource_path\"")
    private String resourcePath;
    @JoinColumn(name = "\"editor_username\"", referencedColumnName = "username")
    @ManyToOne
    private Users editorUsername;

    public Algorithms() {
    }

    public Algorithms(Integer algId) {
        this.algId = algId;
    }

    public Algorithms(Integer algId, String name) {
        this.algId = algId;
        this.name = name;
    }

    public Integer getAlgId() {
        return algId;
    }

    public void setAlgId(Integer algId) {
        this.algId = algId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBTimeComplexity() {
        return bTimeComplexity;
    }

    public void setBTimeComplexity(String bTimeComplexity) {
        this.bTimeComplexity = bTimeComplexity;
    }

    public String getWTimeComplexity() {
        return wTimeComplexity;
    }

    public void setWTimeComplexity(String wTimeComplexity) {
        this.wTimeComplexity = wTimeComplexity;
    }

    public Boolean getIsStable() {
        return isStable;
    }

    public void setIsStable(Boolean isStable) {
        this.isStable = isStable;
    }

    public Boolean getIsInPlace() {
        return isInPlace;
    }

    public void setIsInPlace(Boolean isInPlace) {
        this.isInPlace = isInPlace;
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
        hash += (algId != null ? algId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Algorithms)) {
            return false;
        }
        Algorithms other = (Algorithms) object;
        if ((this.algId == null && other.algId != null) || (this.algId != null && !this.algId.equals(other.algId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Algorithms[ algId=" + algId + " ]";
    }
    
}
