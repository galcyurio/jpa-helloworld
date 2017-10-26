package jpa

import entity.Message
import org.junit.After
import org.junit.Before
import org.junit.Test

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.EntityTransaction
import javax.persistence.Persistence

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class MessageTest {

    EntityManagerFactory emf
    EntityManager em
    EntityTransaction tx

    @Before
    void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("hello-world")
        em = emf.createEntityManager()
        tx = em.transaction
        tx.begin()
    }

    @After
    void tearDown() throws Exception {
        tx?.commit()
        em?.close()
    }

    @Test
    void save__mustNotThrowException() throws Exception {
        Message message = new Message("Message Test")
        em.persist(message)
    }

    @Test
    void detachEntity__mustNotReflectToDatabase() throws Exception {
        EntityManager em1 = emf.createEntityManager()
        em1.transaction.begin()

        Message message1 = new Message("Detached Message")
        em.persist(message1)
        em.detach(message1)
        message1.text = "Edited detached message"

        em1?.transaction?.commit()
        em1?.close()

        EntityManager em2 = emf.createEntityManager()
        em2.transaction.begin()

        Message message2 = em.find(Message, message1.id)
        println message1
        println message2
        assertFalse(message2.text.equals(message1.text))

        em2?.transaction?.commit()
        em2?.close()
    }

    @Test
    void mergeEntity__mustReflectToDatabase() throws Exception {
        EntityManager em1 = emf.createEntityManager()
        em1.transaction.begin()

        Message message = new Message("Merged Message")
        em1.persist(message)

        em1?.transaction?.commit()
        em1?.close()

        EntityManager em2 = emf.createEntityManager()
        em2.transaction.begin()

        Message mergedMessage = em2.merge(message)
        assertTrue(message.equals(mergedMessage))
        assertTrue(message == mergedMessage)
        println "message = $message"
        println "mergedMessage = $mergedMessage"

        mergedMessage.text = "Edited ${message.text}"

        em2?.transaction?.commit()
        em2?.close()
    }

    @Test
    void firstLevelCachingUsingSameEntityManager__mustEqualsEachMessages_mustIssuesOneSqlStatement() throws Exception {
        EntityManager saveEm = emf.createEntityManager()
        saveEm.transaction.begin()

        Message message = new Message("cached message using same EntityManager")
        saveEm.persist(message)

        saveEm?.transaction?.commit()
        saveEm?.close()

        EntityManager em = emf.createEntityManager()
        em.transaction.begin()

        Message message1 = em.find(Message, message.id)
        println "------------ first find called ------------"

        Message message2 = em.find(Message, message.id)
        println "------------ second find called ------------"

        assertTrue(message1 == message2)

        em?.transaction?.commit()
        em?.close()
    }

    @Test
    void firstLevelCachingUsingAnotherEntityManager__mustEqualsEachMessages_mustIssuesTwoSqlStatements() throws Exception {
        // Save
        EntityManager em1 = emf.createEntityManager()
        em1.transaction.begin()

        Message message1 = new Message("cached message")
        em1.persist(message1)

        em1?.transaction?.commit()
        em1?.close()

        // Retrieve
        EntityManager em2 = emf.createEntityManager()
        em2.transaction.begin()

        Message message2 = em2.find(Message, message1.id)
        println "------------ first find called ------------"

        em2?.transaction?.commit()
        em2.close()

        EntityManager em3 = emf.createEntityManager()
        em3.transaction.begin()

        Message message3 = em3.find(Message, message1.id)
        println "------------ second find called ------------"

        em3?.transaction?.commit()
        em3?.close()

        println "message2 = $message2"
        println "message3 = $message3"
        assertTrue(message2.equals(message3))
        assertTrue(message2 == message3)
    }
}
