package jpa

import entity.student.eager.Guide as EagerGuide
import entity.student.eager.Student as EagerStudent
import entity.student.lazy.Guide as LazyGuide
import entity.student.lazy.Student as LazyStudent
import org.hibernate.LazyInitializationException
import org.junit.After
import org.junit.Before
import org.junit.Test
import util.HibernateUtil

import javax.persistence.EntityManager

import static junit.framework.Assert.assertTrue

/**
 * @author galcyurio
 */
class NPlus1SelectProblemTest {

    @Before
    void setUp() throws Exception {
        def eagerGuides = [
                new EagerGuide(name: "Mike"),
                new EagerGuide(name: "Ian"),
                new EagerGuide(name: "David")
        ]

        def eagerStudents = [
                new EagerStudent(name: "Amy", guide: eagerGuides[1]),
                new EagerStudent(name: "John", guide: eagerGuides[1]),
                new EagerStudent(name: "Bruce"),
                new EagerStudent(name: "Rahul", guide: eagerGuides[2])
        ]

        def lazyGuides = [
                new LazyGuide(name: "Mike"),
                new LazyGuide(name: "Ian"),
                new LazyGuide(name: "David")
        ]

        def lazyStudents = [
                new LazyStudent(name: "Amy", guide: lazyGuides[1]),
                new LazyStudent(name: "John", guide: lazyGuides[1]),
                new LazyStudent(name: "Bruce"),
                new LazyStudent(name: "Rahul", guide: lazyGuides[2])
        ]

        HibernateUtil.execute { EntityManager em ->
            eagerGuides.each { em.persist(it) }
            eagerStudents.each { em.persist(it) }
            lazyGuides.each { em.persist(it) }
            lazyStudents.each { em.persist(it) }
        }

        println "========== after setUp ==========="
    }

    @After
    void tearDown() throws Exception {
        println "========== before tearDown ==========="

        HibernateUtil.execute { EntityManager em ->
            em.createQuery("delete from eager_student ").executeUpdate()
            em.createQuery("delete from eager_guide ").executeUpdate()
            em.createQuery("delete from lazy_student ").executeUpdate()
            em.createQuery("delete from lazy_guide ").executeUpdate()
        }
    }

    /**
     * TODO: N + 1 statements
     */
    @Test
    void findAllStudentsWithEagerFetch__mustExecuteNPlus1SqlStatement() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            List<EagerStudent> students = em.createQuery("select s from eager_student s").resultList
            students.each {
                println "The enrollmentId of ${it.name} is ${it.enrollmentId}"
            }
        }
    }

    /**
     * TODO: 1 sql + no guide
     */
    @Test(expected = LazyInitializationException)
    void findAllStudentsWithLazyFetch__mustExecute1SqlStatementWithNoGuide() throws Exception {
        List<LazyStudent> students = []
        HibernateUtil.execute { EntityManager em ->
            students = em.createQuery("select s from lazy_student s").resultList
        }
        println "========== query executed ==========="

        assertTrue(students.size() != 0)
        students.each {
            println "Guide should be null -> ${it.guide}"
        }
    }

    /**
     * TODO: 1 sql + null or notNull guide
     */
    @Test
    void findAllStudentsWithLeftJoinFetch__mustExecute1JoinSqlStatementAndExistsGuide() throws Exception {
        List<LazyStudent> students = []
        HibernateUtil.execute { EntityManager em ->
            students = em.createQuery("select s from lazy_student s left join fetch s.guide").resultList
        }
        println "========== query executed ============"

        assertTrue(students.size() != 0)
        students.each {
            println "Guide can be null -> $it"
        }
    }
}
