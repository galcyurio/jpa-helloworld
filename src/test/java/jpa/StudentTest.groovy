package jpa

import entity.student.eager.Guide
import entity.student.eager.Student
import org.hibernate.LazyInitializationException
import org.junit.After
import org.junit.Before
import org.junit.Test
import util.HibernateUtil

import javax.persistence.EntityManager
import javax.persistence.Query

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class StudentTest {
    Guide guide
    Student student1
    Student student2

    @Before
    void setUp() throws Exception {
        guide = new Guide()

        student1 = new Student(name: "Student1", guide: this.guide)
        student2 = new Student(name: "Student2", guide: this.guide)
        guide.addStudent(this.student1)
        guide.addStudent(this.student2)

        EntityManager manager = emf.createEntityManager()
        manager.transaction.begin()
        manager.persist(this.guide)
        manager.transaction.commit()
        manager.close()

        em = emf.createEntityManager()
        em.transaction.begin()
    }

    @After
    void tearDown() throws Exception {
        em.transaction.commit()
        em.close()

        EntityManager manager = emf.createEntityManager()
        manager.transaction.begin()

        Query query1 = manager.createNativeQuery("DELETE FROM Student")
        Query query2 = manager.createNativeQuery("DELETE FROM Guide")
        query1.executeUpdate()
        query2.executeUpdate()

        manager.transaction.commit()
        manager.close()
    }

    @Test(expected = LazyInitializationException.class)
    void lazyFetch_findGuide__mustNotLoadStudents_andThrowLazyInitializationException() throws Exception {
        EntityManager manager = emf.createEntityManager()
        manager.transaction.begin()

        Guide guide = manager.find(Guide, guide.id)
        println "----------- after find Guide -------------"

        println guide.toString()

        manager.transaction.commit()
        manager.close()

        println guide.students.toString()   // will throw LazyInitializationException
    }

    @Test
    void eagerFetch_findStudent__mustLoadGuideUsingJoinStatement() throws Exception {
        Student student = null
        HibernateUtil.execute { EntityManager em ->
            student = em.find(Student, guide.students[0].id)
        }

        println student
        println student?.guide
    }

    @Test
    void equalityTestWithSameEntityManager__referencesMustEqual() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            Guide guide1 = em.find(Guide, this.guide.id)
            Guide guide2 = em.find(Guide, this.guide.id)

            assertTrue(guide1.hashCode() == guide2.hashCode())
            assertTrue(guide1 == guide2)
            assertTrue(guide1.is(guide2))
        }
    }

    @Test
    void equalityTestWithOtherEntityManager__referencesMustNotEqual() throws Exception {
        def guide1 = HibernateUtil.execute({ EntityManager em ->
            return em.find(Guide, this.guide.id)
        })

        def guide2 = HibernateUtil.execute({ EntityManager em ->
            return em.find(Guide, this.guide.id)
        })

        println "guide1 = $guide1"
        println "guide2 = $guide2"

        assertTrue(guide1.hashCode() == guide2.hashCode())
        assertTrue(guide1 == guide2)
        assertFalse(guide1.is(guide2))
    }

}
