/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// Function to toggle the user dropdown menu visibility
function toggleUserDropdown() {
    document.getElementById("userDropdownMenu").classList.toggle("show");
}

// Function to toggle admin sidebar dropdowns
function toggleAdminDropdown(dropdownId) {
    // First close any open dropdowns
    let adminDropdowns = document.getElementsByClassName("admin-dropdown-content");
    for (let i = 0; i < adminDropdowns.length; i++) {
        let openDropdown = adminDropdowns[i];
        if (openDropdown.id !== dropdownId && openDropdown.classList.contains('show')) {
            openDropdown.classList.remove('show');
        
            // Reset the arrow for other dropdowns
            if (openDropdown.previousElementSibling) {
                openDropdown.previousElementSibling.classList.remove('active');
            }
        
        }
    }
    
    // Get the dropdown element
    const dropdown = document.getElementById(dropdownId);
    
    // Toggle the clicked dropdown
    dropdown.classList.toggle("show");
    
    // Toggle an active class on the link to change the arrow
    if (dropdown.previousElementSibling) {
        dropdown.previousElementSibling.classList.toggle('active');
    }
    
    // Position it vertically
    if (dropdown.classList.contains('show')) {
        // Get the position of the trigger element
        const trigger = dropdown.previousElementSibling;
        const triggerRect = trigger.getBoundingClientRect();
        
        // Set the dropdown's top position to match the trigger
        dropdown.style.top = triggerRect.top + 'px';
    }
}

// Close dropdowns if clicked outside
window.onclick = function(event) {
    // Handle user dropdown in header
    if (!event.target.matches('.user-dropdown-btn')) {
        let dropdowns = document.getElementsByClassName("dropdown-content");
        for (let i = 0; i < dropdowns.length; i++) {
            let openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
};