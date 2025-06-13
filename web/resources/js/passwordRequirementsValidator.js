/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// Ensure script runs only when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function () {

    // Function to display password requirements panel
    window.showPasswordRequirements = function () {
        const requirementsElement = document.querySelector('[id$="passwordRequirements"]');
        if (requirementsElement) {
            requirementsElement.style.display = 'flex';
            requirementsElement.style.flexDirection = 'column';
        } else {
            console.error("Cannot find element with ID 'signupForm:passwordRequirements'");
        }

        updatePasswordRequirements();
    };

    // Function to hide password requirements panel
    window.hidePasswordRequirements = function () {
        const requirementsElement = document.getElementById('signupForm:passwordRequirements');
        if (requirementsElement) {
            requirementsElement.style.display = 'none';
        }
    };

    // Function that validates password against requirements and updates UI
    window.updatePasswordRequirements = function () {
        let passwordInput = document.getElementById('signupForm:passwordInputText');
        if (!passwordInput) {
            console.error("Password input field not found!");
            return;
        }

        let password = passwordInput.value;

        checkRequirement('signupForm:length-req', password.length >= 8, 'At least 8 characters');
        checkRequirement('signupForm:uppercase-req', /[A-Z]/.test(password), 'At least 1 uppercase letter');
        checkRequirement('signupForm:number-req', /[0-9]/.test(password), 'At least 1 number');
        checkRequirement('signupForm:special-req', /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>\/?]/.test(password), 'At least 1 special character');
    };

    // Helper function to update a single requirement indicator in the UI
    // Changes text color based on whether the requirement is met
    function checkRequirement(elementId, isValid, text) {
        const element = document.getElementById(elementId);
        if (!element) {
            console.error("Cannot find element with ID '" + elementId + "'");
            return;
        }

        element.textContent = text;

        // Set color based on validation state (black=neutral, green=valid, red=invalid)
        if (document.getElementById('signupForm:passwordInputText').value === '') {
            element.style.color = 'black';
        } else if (isValid) {
            element.style.color = 'green';
        } else {
            element.style.color = 'red';
        }
    }
});
