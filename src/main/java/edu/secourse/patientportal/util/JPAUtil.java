package edu.secourse.patientportal.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class that manages the JPA {@link EntityManagerFactory} lifecycle.
 * <p>
 * The factory is created once at startup (reads {@code META-INF/persistence.xml})
 * and closed when the application shuts down. Call {@link #getEntityManager()}
 * to get a short-lived {@link EntityManager} for each transaction.
 *
 * <p><b>Usage pattern in any service/controller:</b>
 * <pre>
 *     EntityManager em = JPAUtil.getEntityManager();
 *     try {
 *         em.getTransaction().begin();
 *         em.persist(object);           // INSERT
 *         em.getTransaction().commit();
 *     } catch (Exception e) {
 *         em.getTransaction().rollback();
 *     } finally {
 *         em.close();
 *     }
 * </pre>
 */
public class JPAUtil {

    /** Must match the persistence-unit name in META-INF/persistence.xml */
    private static final String PERSISTENCE_UNIT = "patientportal";

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

    private JPAUtil() {}

    /**
     * Returns a new {@link EntityManager} backed by the shared factory.
     * The caller is responsible for closing it after use.
     *
     * @return a new EntityManager connected to MySQL
     */
    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    /**
     * Closes the shared {@link EntityManagerFactory}.
     * Call once when the application shuts down.
     */
    public static void close() {
        if (FACTORY != null && FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
