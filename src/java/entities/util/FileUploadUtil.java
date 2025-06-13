package entities.util;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Named
@RequestScoped
public class FileUploadUtil implements Serializable {
    
    // Base directory for algorithm animations
    private static final String UPLOAD_DIRECTORY = "/resources/animations/";
    
    
    
    /**
    * Uploads a file to the server and stores it in the appropriate directory.
    * 
    * @param file The file to be uploaded
    */
    public static void validateFile(Part file) throws Exception {
        if (file != null && file.getSize() > 0) {
            // Generate absolute path to where file will be stored.
            String uploadPath = getUploadPath(UPLOAD_DIRECTORY);

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
                uploadDir.mkdirs();
        } else
            throw new Exception("No file selected or file is empty.");
    }
    
    public static String getResourcePath(String fileName) {
        return UPLOAD_DIRECTORY + fileName;
    }
    
    public static void copyFileToDestination(Part file, String fileName) throws IOException {
        InputStream input = file.getInputStream();
        Files.copy(input, Paths.get(getUploadPath(UPLOAD_DIRECTORY),fileName), StandardCopyOption.REPLACE_EXISTING);
    }
    
    /**
    * Creates a web-safe filename from an entity's name.
    * Removes spaces, converts to lowercase, and adds .html extension.
    * 
    * @param entityName The name to sanitise for file system use
    * @return Sanitised filename with .html extension
    */
    public static String getSanitisedName(String entityName) {
        
        return entityName.trim().replaceAll("\\s+", "_").toLowerCase() + ".html";
    }
    
    /**
    * Gets the absolute file system path for uploads based on the relative web application path.
    * 
    * @param path The resource path retrieved from an algorithm or data structure
    * @return The absolute file system path for uploads
    */
    private static String getUploadPath(String path) {
        // Create directory if it doesn't exist
        String applicationPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("");
        return applicationPath + path;
    }
    
    /**
    * Checks if a file exists at the specified path.
    * 
    * @param resourcePath The relative path retrieved from a data structure or algorithm entity
    * @return true if the file exists, false otherwise
    */
    public static boolean fileExists(String resourcePath) {
        String filePath = getUploadPath(resourcePath);
        Path path = Paths.get(filePath);
        return Files.exists(path);
    } 
    
    
    /**
    * Deletes a file from the specified path without throwing exceptions.
    * 
    * @param resourcePath The relative path retrieved from a data structure or algorithm entity
    * @return true if the file was successfully deleted, false otherwise
    */
    public static boolean deleteFile(String resourcePath) {
        try {
            String filePath = getUploadPath(resourcePath);
            Path path = Paths.get(filePath);
            
            if(Files.exists(path)) {
                Files.delete(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            JsfUtil.addErrorMessage("Error deleting file: " + e.getMessage());
            return false;
        }
    }
}