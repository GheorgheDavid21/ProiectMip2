package org.restaurant.persistence;

import jakarta.persistence.EntityManager;
import org.restaurant.model.Product;

import java.util.List;

public class ProductRepository {

    private EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public Product saveOrUpdate(Product product) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Product mergedProduct = em.merge(product);
            em.getTransaction().commit();
            return mergedProduct;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Product> findAll() {
        try (EntityManager em = getEntityManager()) {
            return em.createQuery(
                    "SELECT p FROM Product p ORDER BY p.name",
                    Product.class
            ).getResultList();
        }
    }

    public void delete(Product product) {
        if (product == null || product.getId() == null) {
            return;
        }

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            Product toDelete = em.find(Product.class, product.getId());

            if (toDelete != null) {
                em.remove(toDelete);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Could not delete product", e);
        } finally {
            em.close();
        }
    }

}
