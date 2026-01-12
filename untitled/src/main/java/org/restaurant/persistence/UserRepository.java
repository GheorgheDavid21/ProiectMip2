package org.restaurant.persistence;

import jakarta.persistence.EntityManager;
import org.restaurant.model.Product;
import org.restaurant.model.User;

import java.util.List;

public class UserRepository {
    private EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public User saveOrUpdate(User user) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User mergedUser = em.merge(user);
            em.getTransaction().commit();
            return mergedUser;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public User findByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class
                    ).setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    public List<User> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u", User.class).getResultList();
        } finally {
            em.close();
        }
    }
    public void delete(User user) {
        if (user == null || user.getId() == null) return;
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User toDelete = em.find(User.class, user.getId());
            if (toDelete != null) {
                em.remove(toDelete);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public User validateCredentials(String username, String password) {
        User user = findByUsername(username);
        if(user == null) {
            return null;
        }
        if(user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
