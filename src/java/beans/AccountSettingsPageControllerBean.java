/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * This class is used to load different pages onto the account settings main content panel.
 * It currently only loads one page but it's here for scalability. If we add extra user features this class will be used to swap features.
 * @author shade
 */
@Named
@ViewScoped
public class AccountSettingsPageControllerBean implements Serializable {
    
    private String currentPage = "/account-info.xhtml";
    
    public String getCurrentPage() {
        return currentPage;
    }
    
    public void loadAccountInfo() {
        this.currentPage = "/account-info.xhtml";
    }
    
}
