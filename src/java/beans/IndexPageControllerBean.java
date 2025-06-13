package beans;

import entities.AlgorithmsController;
import entities.DataStructuresController;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * This class is used to load different pages onto the admin page main content panel.
 * I am using Ajax to swap from page to page. This changes the page being rendered depending on user selection.
 * 
 * @author shade
 */
@Named
@ViewScoped
public class IndexPageControllerBean implements Serializable {
    
    @Inject
    private AlgorithmsController algorithmsController;
    
    @Inject
    private DataStructuresController dataStructuresController;
    
    private String currentPage = "/welcome.xhtml"; //default page
    
    
    public String getCurrentPage() {
        return this.currentPage;
    }
    
    public void loadWelcome() {
        resetSelected();
        this.currentPage = "/welcome.xhtml";
    }
    
    public void loadAlgorithmsView() {
        this.currentPage = "/CRUD/algorithms/View.xhtml";
    }
    
    public void loadDataStructuresView() {
        this.currentPage = "/CRUD/dataStructures/View.xhtml";
    }
    

    private void resetSelected() {
        algorithmsController.resetSelected();
        dataStructuresController.resetSelected();
    }
}

