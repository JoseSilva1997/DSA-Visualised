/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;

/**
 * This class is used to validate the "confirm password".
 * It ensures that the confirmation password matches the original password
 * entered by the user during registration.
 * 
 * @author shade
 */
@Named
@RequestScoped
public class ConfirmPasswordBean {
    
    private String confirmPassword;
    
    
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    /**
     * Validator method that checks if the confirmed password matches the original password.
     * This method is typically bound to a JSF validator in the signup form.
     * 
     * @param context The FacesContext for the current request
     * @param component The UIComponent being validated (the confirm password field)
     * @param value The value submitted for the confirm password field
     */
    public void validatePasswordMatch(FacesContext context, UIComponent component, Object value) {
        
        String confirmPassword = value.toString();
        
        
        // Get the password input component
        UIComponent form = component.getParent();
        UIInput passwordInput = (UIInput) form.findComponent("passwordInputText");
        

        // First check if there's a submitted value for the password field
        String password;
        Object submittedValue = passwordInput.getSubmittedValue();
        if(submittedValue != null)
            // If password was just submitted, use that value
            password = submittedValue.toString();
        else {
            // If submitted value is null (hasn't met all requirements), try to get the local value
            Object localValue = passwordInput.getValue();
            password = (localValue != null) ? localValue.toString() : "";
        }
        
        // Compare the two passwords and throw an exception if they don't match
        if (!confirmPassword.equals(password)) {         
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null));
        }
    }
}
