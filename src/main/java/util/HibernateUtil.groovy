
package util

import org.hibernate.SessionFactory
import org.hibernate.boot.Metadata
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistry
import org.hibernate.boot.registry.StandardServiceRegistryBuilder

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

class HibernateUtil {

    static final SessionFactory sessionFactory
    static final EntityManagerFactory emf

    static {
        sessionFactory = buildSessionFactory()
        emf = Persistence.createEntityManagerFactory("hello-world")
    }

    /**
     * Create the SessionFactory from hibernate.cfg.xml
     */
    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build()
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build()
            return metadata.getSessionFactoryBuilder().build()
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex)
            throw new ExceptionInInitializerError(ex)
        }
    }

    static Object execute(Closure closure) {
        EntityManager em = emf.createEntityManager()
        em.transaction.begin()
        def object = closure.call(em)
        em?.transaction?.commit()
        em?.close()
        return object
    }

}



