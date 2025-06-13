/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import beans.LoginBean;
import entities.exceptions.NonexistentEntityException;
import entities.exceptions.RollbackFailureException;
import entities.util.FileUploadUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.Part;
import jakarta.transaction.UserTransaction;
import java.util.List;

/**
 * Regular Jpa controller class generated from Netbeans.
 * Some of the methods in this class were modified to account for auto generated fields.
 * List of modified methods:
 * - create()
 * - edit()
 * - destroy()
 * 
 * @author shade
 */
public class DataStructuresJpaController implements Serializable {
    
    public DataStructuresJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     *  Adds a new data structure to the database.
     *  Modified this method to:
     *  Automatically set the data structure's resource path to the path of the uploaded file.
     *  Automatically set the editor's name to the name of the admin editing this Algorithm.
     * @param dataStructures data structure to add.
     * @param resourcePath Path to the animation.
     * @param fileName Name of the file. 
     * @param file File containing the algorithm animation.
     * @param admin Object containing current user logged in.
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void create(DataStructures dataStructures, String resourcePath, String fileName, Part file, LoginBean admin) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            
            // Set the dataStructure's resource path
            if (resourcePath != null && !resourcePath.isEmpty())
                dataStructures.setResourcePath(resourcePath);
            
            
            // Retrieve the username of the currently logged in admin
            String adminUsername;
            if (admin != null)
                adminUsername = admin.getCurrentUser().getUsername();
            else
                throw new Exception("Something went wrong. Admin status not recognised. Please logout and log in again.");
            
            
            // Populate the editorUsername attribute with the current admin's username
            if (adminUsername != null && !adminUsername.isEmpty()) {       
                Users editor = em.find(Users.class, adminUsername);
                
                if (editor != null) {
                    dataStructures.setEditorUsername(editor);
                    
                    editor.getDataStructuresCollection().add(dataStructures);
                    em.merge(editor);
                } else
                    throw new Exception("User not found in database: " + adminUsername);     
            } else 
                throw new Exception("Editor username cannot be null or empty.");
            
            em.persist(dataStructures);
            utx.commit();
            
            // Copy file to destination folder if there were no errors during the transition
            FileUploadUtil.copyFileToDestination(file, fileName);
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edits a data structure from the database.
     * Modified this method to:
     * Change the file associated with the selected data structure if the user uploads a new file.
     * @param dataStructures Data structure to edit.
     * @param file New file containing the data structure's animation.
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void edit(DataStructures dataStructures, Part file) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DataStructures persistentDataStructures = em.find(DataStructures.class, dataStructures.getDsId());
            Users editorUsernameOld = persistentDataStructures.getEditorUsername();
            Users editorUsernameNew = dataStructures.getEditorUsername();
            if (editorUsernameNew != null) {
                editorUsernameNew = em.getReference(editorUsernameNew.getClass(), editorUsernameNew.getUsername());
                dataStructures.setEditorUsername(editorUsernameNew);
            }
            dataStructures = em.merge(dataStructures);
            if (editorUsernameOld != null && !editorUsernameOld.equals(editorUsernameNew)) {
                editorUsernameOld.getDataStructuresCollection().remove(dataStructures);
                editorUsernameOld = em.merge(editorUsernameOld);
            }
            if (editorUsernameNew != null && !editorUsernameNew.equals(editorUsernameOld)) {
                editorUsernameNew.getDataStructuresCollection().add(dataStructures);
                editorUsernameNew = em.merge(editorUsernameNew);
            }
            // If user uploaded a new file and this file is valid, delete old file and add the new one
            if(file != null) {
                FileUploadUtil.validateFile(file);
                
                // Change resource path in case user updated file name
                String newFileName = FileUploadUtil.getSanitisedName(dataStructures.getName());
                
                String newResourcePath = FileUploadUtil.getResourcePath(newFileName);
                
                // Delete old file
                FileUploadUtil.deleteFile(persistentDataStructures.getResourcePath());
                
                dataStructures.setResourcePath(newResourcePath);
                
                FileUploadUtil.copyFileToDestination(file, newFileName);
            }
            
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dataStructures.getDsId();
                if (findDataStructures(id) == null) {
                    throw new NonexistentEntityException("The dataStructures with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    
    /**
     * Delete the data structure algorithm from the database.
     * Modified this method to:
     * Also delete the file associated with the data structure.
     * @param id Id of the algorithm to delete.
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DataStructures dataStructures;
            try {
                dataStructures = em.getReference(DataStructures.class, id);
                dataStructures.getDsId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dataStructures with id " + id + " no longer exists.", enfe);
            }
            
            Users editorUsername = dataStructures.getEditorUsername();
            if (editorUsername != null) {
                editorUsername.getDataStructuresCollection().remove(dataStructures);
                editorUsername = em.merge(editorUsername);
            }

            em.remove(dataStructures);
            utx.commit(); 

            //Delete file from animations folder if it exists and there were no errors during the transition
            if(FileUploadUtil.fileExists(dataStructures.getResourcePath()))
                FileUploadUtil.deleteFile(dataStructures.getResourcePath());
            
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DataStructures> findDataStructuresEntities() {
        return findDataStructuresEntities(true, -1, -1);
    }

    public List<DataStructures> findDataStructuresEntities(int maxResults, int firstResult) {
        return findDataStructuresEntities(false, maxResults, firstResult);
    }

    private List<DataStructures> findDataStructuresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DataStructures.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DataStructures findDataStructures(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DataStructures.class, id);
        } finally {
            em.close();
        }
    }

    public int getDataStructuresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DataStructures> rt = cq.from(DataStructures.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
