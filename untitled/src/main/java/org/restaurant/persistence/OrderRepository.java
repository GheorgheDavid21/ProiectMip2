package org.restaurant.persistence;

import jakarta.persistence.EntityManager;
import org.restaurant.model.Order;
import java.util.List;

public class OrderRepository {
    private EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public List<Order> findAll() {
        try (EntityManager em = getEntityManager()) {
            return em.createQuery("SELECT o FROM Order o LEFT JOIN FETCH o.user ORDER BY o.orderDate DESC", Order.class)
                    .getResultList();
        }
    }
}
