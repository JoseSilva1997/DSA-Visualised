/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import entities.exceptions.IllegalOrphanException;
import entities.exceptions.NonexistentEntityException;
import entities.exceptions.PreexistingEntityException;
import entities.exceptions.RollbackFailureException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Regular Jpa controller class generated from Netbeans.
 * A method in this class was modified to account for auto generated fields:
 * Method modified:
 * - create()
 * 
 * @author shade
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Creates a new user with the given parameters.
     * Modified to:
     * Automatically set the user's role.
     * Automatically set the user's is_active attribute.
     * @param users
     * @throws PreexistingEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void create(Users users) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (users.getAlgorithmsCollection() == null) {
            users.setAlgorithmsCollection(new ArrayList<Algorithms>());
        }
        if (users.getDataStructuresCollection() == null) {
            users.setDataStructuresCollection(new ArrayList<DataStructures>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Algorithms> attachedAlgorithmsCollection = new ArrayList<Algorithms>();
            for (Algorithms algorithmsCollectionAlgorithmsToAttach : users.getAlgorithmsCollection()) {
                algorithmsCollectionAlgorithmsToAttach = em.getReference(algorithmsCollectionAlgorithmsToAttach.getClass(), algorithmsCollectionAlgorithmsToAttach.getAlgId());
                attachedAlgorithmsCollection.add(algorithmsCollectionAlgorithmsToAttach);
            }
            users.setAlgorithmsCollection(attachedAlgorithmsCollection);
            Collection<DataStructures> attachedDataStructuresCollection = new ArrayList<DataStructures>();
            for (DataStructures dataStructuresCollectionDataStructuresToAttach : users.getDataStructuresCollection()) {
                dataStructuresCollectionDataStructuresToAttach = em.getReference(dataStructuresCollectionDataStructuresToAttach.getClass(), dataStructuresCollectionDataStructuresToAttach.getDsId());
                attachedDataStructuresCollection.add(dataStructuresCollectionDataStructuresToAttach);
            }
            users.setDataStructuresCollection(attachedDataStructuresCollection);
            
            TypedQuery<Users> query = em.createNamedQuery("Users.findByEmail", Users.class);
            query.setParameter("email", users.getEmail());
            List<Users> existingUsers = query.getResultList();
            
            if(!existingUsers.isEmpty())
                throw new PreexistingEntityException("A user with that email already exists.");
            
            // Set a new user's "role" to "User" by default
            users.setRole("User");
            
            em.persist(users);
            
            for (Algorithms algorithmsCollectionAlgorithms : users.getAlgorithmsCollection()) {
                Users oldEditorUsernameOfAlgorithmsCollectionAlgorithms = algorithmsCollectionAlgorithms.getEditorUsername();
                algorithmsCollectionAlgorithms.setEditorUsername(users);
                algorithmsCollectionAlgorithms = em.merge(algorithmsCollectionAlgorithms);
                if (oldEditorUsernameOfAlgorithmsCollectionAlgorithms != null) {
                    oldEditorUsernameOfAlgorithmsCollectionAlgorithms.getAlgorithmsCollection().remove(algorithmsCollectionAlgorithms);
                    oldEditorUsernameOfAlgorithmsCollectionAlgorithms = em.merge(oldEditorUsernameOfAlgorithmsCollectionAlgorithms);
                }
            }
            for (DataStructures dataStructuresCollectionDataStructures : users.getDataStructuresCollection()) {
                Users oldEditorUsernameOfDataStructuresCollectionDataStructures = dataStructuresCollectionDataStructures.getEditorUsername();
                dataStructuresCollectionDataStructures.setEditorUsername(users);
                dataStructuresCollectionDataStructures = em.merge(dataStructuresCollectionDataStructures);
                if (oldEditorUsernameOfDataStructuresCollectionDataStructures != null) {
                    oldEditorUsernameOfDataStructuresCollectionDataStructures.getDataStructuresCollection().remove(dataStructuresCollectionDataStructures);
                    oldEditorUsernameOfDataStructuresCollectionDataStructures = em.merge(oldEditorUsernameOfDataStructuresCollectionDataStructures);
                }
            }
            
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                if (findUsers(users.getUsername()) != null)
                    throw new PreexistingEntityException("A user with that username already exists.", ex); 
                else
                    throw new RollbackFailureException("Oops, an error occurred. Ensure all fields are valid.", re);             
            }           
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users persistentUsers = em.find(Users.class, users.getUsername());
            Collection<Algorithms> algorithmsCollectionOld = persistentUsers.getAlgorithmsCollection();
            Collection<Algorithms> algorithmsCollectionNew = users.getAlgorithmsCollection();
            Collection<DataStructures> dataStructuresCollectionOld = persistentUsers.getDataStructuresCollection();
            Collection<DataStructures> dataStructuresCollectionNew = users.getDataStructuresCollection();

            Collection<Algorithms> attachedAlgorithmsCollectionNew = new ArrayList<Algorithms>();
            for (Algorithms algorithmsCollectionNewAlgorithmsToAttach : algorithmsCollectionNew) {
                algorithmsCollectionNewAlgorithmsToAttach = em.getReference(algorithmsCollectionNewAlgorithmsToAttach.getClass(), algorithmsCollectionNewAlgorithmsToAttach.getAlgId());
                attachedAlgorithmsCollectionNew.add(algorithmsCollectionNewAlgorithmsToAttach);
            }
            algorithmsCollectionNew = attachedAlgorithmsCollectionNew;
            users.setAlgorithmsCollection(algorithmsCollectionNew);
            Collection<DataStructures> attachedDataStructuresCollectionNew = new ArrayList<DataStructures>();
            for (DataStructures dataStructuresCollectionNewDataStructuresToAttach : dataStructuresCollectionNew) {
                dataStructuresCollectionNewDataStructuresToAttach = em.getReference(dataStructuresCollectionNewDataStructuresToAttach.getClass(), dataStructuresCollectionNewDataStructuresToAttach.getDsId());
                attachedDataStructuresCollectionNew.add(dataStructuresCollectionNewDataStructuresToAttach);
            }
            dataStructuresCollectionNew = attachedDataStructuresCollectionNew;
            users.setDataStructuresCollection(dataStructuresCollectionNew);
            users = em.merge(users);

            for (Algorithms algorithmsCollectionOldAlgorithms : algorithmsCollectionOld) {
                if (!algorithmsCollectionNew.contains(algorithmsCollectionOldAlgorithms)) {
                    algorithmsCollectionOldAlgorithms.setEditorUsername(null);
                    algorithmsCollectionOldAlgorithms = em.merge(algorithmsCollectionOldAlgorithms);
                }
            }
            for (Algorithms algorithmsCollectionNewAlgorithms : algorithmsCollectionNew) {
                if (!algorithmsCollectionOld.contains(algorithmsCollectionNewAlgorithms)) {
                    Users oldEditorUsernameOfAlgorithmsCollectionNewAlgorithms = algorithmsCollectionNewAlgorithms.getEditorUsername();
                    algorithmsCollectionNewAlgorithms.setEditorUsername(users);
                    algorithmsCollectionNewAlgorithms = em.merge(algorithmsCollectionNewAlgorithms);
                    if (oldEditorUsernameOfAlgorithmsCollectionNewAlgorithms != null && !oldEditorUsernameOfAlgorithmsCollectionNewAlgorithms.equals(users)) {
                        oldEditorUsernameOfAlgorithmsCollectionNewAlgorithms.getAlgorithmsCollection().remove(algorithmsCollectionNewAlgorithms);
                        oldEditorUsernameOfAlgorithmsCollectionNewAlgorithms = em.merge(oldEditorUsernameOfAlgorithmsCollectionNewAlgorithms);
                    }
                }
            }
            for (DataStructures dataStructuresCollectionOldDataStructures : dataStructuresCollectionOld) {
                if (!dataStructuresCollectionNew.contains(dataStructuresCollectionOldDataStructures)) {
                    dataStructuresCollectionOldDataStructures.setEditorUsername(null);
                    dataStructuresCollectionOldDataStructures = em.merge(dataStructuresCollectionOldDataStructures);
                }
            }
            for (DataStructures dataStructuresCollectionNewDataStructures : dataStructuresCollectionNew) {
                if (!dataStructuresCollectionOld.contains(dataStructuresCollectionNewDataStructures)) {
                    Users oldEditorUsernameOfDataStructuresCollectionNewDataStructures = dataStructuresCollectionNewDataStructures.getEditorUsername();
                    dataStructuresCollectionNewDataStructures.setEditorUsername(users);
                    dataStructuresCollectionNewDataStructures = em.merge(dataStructuresCollectionNewDataStructures);
                    if (oldEditorUsernameOfDataStructuresCollectionNewDataStructures != null && !oldEditorUsernameOfDataStructuresCollectionNewDataStructures.equals(users)) {
                        oldEditorUsernameOfDataStructuresCollectionNewDataStructures.getDataStructuresCollection().remove(dataStructuresCollectionNewDataStructures);
                        oldEditorUsernameOfDataStructuresCollectionNewDataStructures = em.merge(oldEditorUsernameOfDataStructuresCollectionNewDataStructures);
                    }
                }
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
                String id = users.getUsername();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }

            Collection<Algorithms> algorithmsCollection = users.getAlgorithmsCollection();
            for (Algorithms algorithmsCollectionAlgorithms : algorithmsCollection) {
                algorithmsCollectionAlgorithms.setEditorUsername(null);
                algorithmsCollectionAlgorithms = em.merge(algorithmsCollectionAlgorithms);
            }
            Collection<DataStructures> dataStructuresCollection = users.getDataStructuresCollection();
            for (DataStructures dataStructuresCollectionDataStructures : dataStructuresCollection) {
                dataStructuresCollectionDataStructures.setEditorUsername(null);
                dataStructuresCollectionDataStructures = em.merge(dataStructuresCollectionDataStructures);
            }
            em.remove(users);
            utx.commit();
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

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
