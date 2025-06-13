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
public class AlgorithmsJpaController implements Serializable {
    
    public AlgorithmsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     *  Adds a new algorithm to the database.
     *  Modified this method to:
     *  Automatically set the algorithm's resource path to the path of the uploaded file.
     *  Automatically set the editor's name to the name of the admin editing this Algorithm.
     * @param algorithms Algorithm to add.
     * @param resourcePath Path to the animation.
     * @param fileName Name of the file. 
     * @param file File containing the algorithm animation.
     * @param admin Object containing current user logged in.
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void create(Algorithms algorithms, String resourcePath, String fileName, Part file, LoginBean admin) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            
            // Set the algorithm's resource path 
            if (resourcePath != null && !resourcePath.isEmpty())
                algorithms.setResourcePath(resourcePath);
            
            
            String adminUsername;
            // Retrieve the username of the currently logged in admin
            if (admin != null)
                adminUsername = admin.getCurrentUser().getUsername();
            else
                throw new Exception("Something went wrong. Admin status not recognised. Please logout and log in again.");
            
            // Set the editorUsername attribute with the current admin's username
            if (adminUsername != null && !adminUsername.isEmpty()) {
                
                Users editor = em.find(Users.class, adminUsername);
                
                if (editor != null) {
                    
                    algorithms.setEditorUsername(editor);
                    editor.getAlgorithmsCollection().add(algorithms);
                    em.merge(editor);
                } else
                    throw new Exception("User not found in database: " + adminUsername);     
            } else 
                throw new Exception("Editor username cannot be null or empty.");
            
            em.persist(algorithms);
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
     * Edits an algorithm from the database
     * Modified this method to:
     * Change the file associated with the selected algorithm if the user uploads a new file.
     * @param algorithms Algorithm to edit.
     * @param file New file containing the algorithm's animation.
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void edit(Algorithms algorithms, Part file) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            
            Algorithms persistentAlgorithms = em.find(Algorithms.class, algorithms.getAlgId());
            Users editorUsernameOld = persistentAlgorithms.getEditorUsername();
            Users editorUsernameNew = algorithms.getEditorUsername();
            if (editorUsernameNew != null) {
                editorUsernameNew = em.getReference(editorUsernameNew.getClass(), editorUsernameNew.getUsername());
                algorithms.setEditorUsername(editorUsernameNew);
            }
            algorithms = em.merge(algorithms);
            if (editorUsernameOld != null && !editorUsernameOld.equals(editorUsernameNew)) {
                editorUsernameOld.getAlgorithmsCollection().remove(algorithms);
                editorUsernameOld = em.merge(editorUsernameOld);
            }
            if (editorUsernameNew != null && !editorUsernameNew.equals(editorUsernameOld)) {
                editorUsernameNew.getAlgorithmsCollection().add(algorithms);
                editorUsernameNew = em.merge(editorUsernameNew);
            }
            // If user uploaded a new file and this file is valid, delete old file and add the new one
            if(file != null) {
                FileUploadUtil.validateFile(file);
                
                // Change resource path in case user updated file name
                String newFileName = FileUploadUtil.getSanitisedName(algorithms.getName());
                
                String newResourcePath = FileUploadUtil.getResourcePath(newFileName);
                
                // Delete old file
                FileUploadUtil.deleteFile(persistentAlgorithms.getResourcePath());
                
                algorithms.setResourcePath(newResourcePath);
                
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
                Integer id = algorithms.getAlgId();
                if (findAlgorithms(id) == null) {
                    throw new NonexistentEntityException("The algorithms with id " + id + " no longer exists.");
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
     * Delete the selected algorithm from the database.
     * Modified this method to:
     * Also delete the file associated with the algorithm.
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
            Algorithms algorithms;
            try {
                algorithms = em.getReference(Algorithms.class, id);
                algorithms.getAlgId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The algorithms with id " + id + " no longer exists.", enfe);
            }
            
            Users editorUsername = algorithms.getEditorUsername();
            if (editorUsername != null) {
                editorUsername.getAlgorithmsCollection().remove(algorithms);
                editorUsername = em.merge(editorUsername);
            }
            
            em.remove(algorithms);
            utx.commit();
            
            //Delete file from animations folder if it exists and there were no errors during the transition
            if(FileUploadUtil.fileExists(algorithms.getResourcePath()))
                FileUploadUtil.deleteFile(algorithms.getResourcePath());
            
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

    public List<Algorithms> findAlgorithmsEntities() {
        return findAlgorithmsEntities(true, -1, -1);
    }

    public List<Algorithms> findAlgorithmsEntities(int maxResults, int firstResult) {
        return findAlgorithmsEntities(false, maxResults, firstResult);
    }

    private List<Algorithms> findAlgorithmsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Algorithms.class));
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

    public Algorithms findAlgorithms(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Algorithms.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlgorithmsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Algorithms> rt = cq.from(Algorithms.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
