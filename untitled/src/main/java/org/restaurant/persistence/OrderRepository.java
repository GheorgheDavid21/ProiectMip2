package org.restaurant.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.restaurant.model.Order;
import java.util.List;

public class OrderRepository {
    private EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public void save(Order order){
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save order", e);
        } finally {
            em.close();
        }
    }

    public List<Order> findAll() {
        try (EntityManager em = getEntityManager()) {
            return em.createQuery("SELECT o FROM Order o LEFT JOIN FETCH o.user ORDER BY o.orderDate DESC", Order.class)
                    .getResultList();
        }
    }

    public void deleteOrdersByUser(Long userId)
    {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createQuery("DELETE FROM Order o WHERE o.user.id = :uid")
                    .setParameter("uid", userId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Order> findOrdersByUser(Long userId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.user.id = :uid ORDER BY o.orderDate DESC", Order.class)
                    .setParameter("uid", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
