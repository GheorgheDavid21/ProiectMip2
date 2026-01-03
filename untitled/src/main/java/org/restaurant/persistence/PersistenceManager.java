package org.restaurant.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public class PersistenceManager {

    private static final PersistenceManager INSTANCE = new PersistenceManager();
    private final EntityManagerFactory emf;

    private PersistenceManager() {
        try {
            this.emf = Persistence.createEntityManagerFactory("RestaurantPU");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing EntityManagerFactory");
        }
    }

    public static PersistenceManager getInstance() {
        return INSTANCE;
    }

    public EntityManagerFactory getEntityManagerFactory() { return emf; }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
