/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import entities.AlgorithmsController;
import entities.DataStructuresController;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * This class is used to load different pages onto the admin page main content panel.
 * I am using ajax to swap from page to page. This changes the page being rendered depending on user selection.
 * @author shade
 */

@Named
@ViewScoped
public class AdminPageControllerBean implements Serializable {
    
    @Inject
    private DataStructuresController dataStructuresController;
    
    @Inject
    private AlgorithmsController algorithmsController;
    
    private String currentPage = "/admindashboard.xhtml";
    
    
    public String getCurrentPage() {
        return this.currentPage;
    }
    
    public void loadDashboard() {
        resetSelected();
        this.currentPage = "/admindashboard.xhtml";
    }
    
    public void loadAlgorithmsCreate() {
        resetSelected();
        this.currentPage = "/CRUD/algorithms/Create.xhtml";
    }
    
    public void loadAlgorithmsEdit() {
        resetSelected();
        this.currentPage = "/CRUD/algorithms/Edit.xhtml";
    }
    
    public void loadAlgorithmsDelete() {
        resetSelected();
        this.currentPage = "/CRUD/algorithms/Delete.xhtml";
    }
    
    public void loadDataStructuresCreate() {
        resetSelected();
        this.currentPage = "/CRUD/dataStructures/Create.xhtml";
    }
    
    public void loadDataStructuresEdit() {
        resetSelected();
        this.currentPage = "/CRUD/dataStructures/Edit.xhtml";
    }
    
    public void loadDataStructuresDelete() {
        resetSelected();
        this.currentPage = "/CRUD/dataStructures/Delete.xhtml";
    }
    
    
    private void resetSelected() {
        algorithmsController.resetSelected();
        dataStructuresController.resetSelected();
    }
}
