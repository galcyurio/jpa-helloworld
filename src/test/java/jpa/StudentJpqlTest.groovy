package jpa

import entity.student.eager.Guide
import entity.student.eager.Student
import org.junit.After
import org.junit.Before
import org.junit.Test
import util.HibernateUtil

import javax.persistence.EntityManager

import static junit.framework.TestCase.assertNotNull
import static junit.framework.TestCase.assertTrue

class StudentJpqlTest {

    @Before
    void setUp() throws Exception {
        Student student1 = new Student(name: "Tom")
        Student student2 = new Student(name: "Kane")
        Student student3 = new Student(name: "John")
        Student student4 = new Student(name: "Charm")

        Guide guide1 = new Guide(name: "Sally").addStudent(student1).addStudent(student2)
        Guide guide2 = new Guide(name: "Pool").addStudent(student3)

        HibernateUtil.execute({ EntityManager em ->
            em.persist(student1)
            em.persist(student2)
            em.persist(student3)
            em.persist(student4)

            em.persist(guide1)
            em.persist(guide2)
        })
        println "----------------- StudentJpqlTest.setUp after -----------------"
    }

    @After
    void tearDown() throws Exception {
        println "----------------- StudentJpqlTest.tearDown before -----------------"
        HibernateUtil.execute({ EntityManager em ->
            em.createNativeQuery("DELETE FROM student").executeUpdate()
            em.createNativeQuery("DELETE FROM guide").executeUpdate()
        })
    }

    @Test
    void findAllStudentsSpecificFieldsWithoutGuide__mustReturnStudentsSpecificFields() throws Exception {
        def students = null
        HibernateUtil.execute({ EntityManager em ->
            students = em.createQuery("select s.name, s.enrollmentId from eager_student s").resultList
        })

        assertNotNull(students)
        assertTrue(students.size() != 0)
        students.each { println it }
    }

    @Test
    void findAllGuidesSpecificFieldsWithoutStudents__mustReturn() throws Exception {
        def guides = null
        HibernateUtil.execute { EntityManager em ->
            guides = em.createQuery("select g.name, g.staffId from eager_guide g").resultList
        }

        assertTrue(guides != null && guides.size() != 0)
        guides.each { println it }
    }

    // ##### Querying Entities #####

    @Test
    void findAllStudents__mustReturnAllStudents() throws Exception {
        def students = null
        HibernateUtil.execute { EntityManager em ->
            students = em.createQuery("select s from eager_student s").getResultList()
        }

        assertTrue(students != null && students.size() != 0)
        students.each { println it }
    }

    @Test
    void findAllGuides__mustReturnAllGuides() {
        def guides = null
        HibernateUtil.execute { EntityManager em ->
            guides = em.createQuery("select g from Guide g").resultList
        }

        assertTrue(guides != null && guides?.size() != 0)
        guides.each { println it }
    }

    // ##### Reporing Queries #####

    @Test
    void findSpecificFields__mustReturnSpecificFields() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            def list = em.createQuery("select s.name from eager_student s").resultList
            list.each { println it }
        }
    }

    // ##### Dynamic Queries #####

    @Test
    void findGuideUsingSecureDynamicQuery__mustReturnGuide() throws Exception {
        def guide1 = new Guide()
        HibernateUtil.execute { EntityManager em ->
            em.persist(guide1)

            def guide2 = em.createQuery("select g from eager_guide g where g.staffId = :staffId")
                    .setParameter("staffId", guide1.staffId)
                    .resultList

//            assertTrue(guide1 == guide2)
            println guide2
        }
    }

    // ##### Wild Cards #####

    @Test
    void findGuidesUsingWildcard__mustReturnGuides() throws Exception {
        Guide guide1 = new Guide(name: "Amy")
        Guide guide2 = new Guide(name: "Ally")
        Guide guide3 = new Guide(name: "Ball")

        HibernateUtil.execute { EntityManager em ->
            em.persist(guide1)
            em.persist(guide2)
            em.persist(guide3)

            List<Guide> guides = em.createQuery("select g from eager_guide g where g.name like 'A%'").resultList

            guides.each {
                println it
                assertTrue(it.name.startsWith("A"))
            }
        }
    }

    // #### Native SQL #####
    @Test
    void findGuidesUsingSQL__mustReturnGuides() throws Exception {
        Guide guide1 = new Guide()
        HibernateUtil.execute { EntityManager em ->
            em.persist(guide1)
            def guides = em.createNativeQuery("SELECT * FROM guide").resultList

            assertTrue(guides != null && guides.size() != 0)
            guides.each { println it }
        }
    }

    // ##### Named Query ####
    @Test
    void findGuidesUsingNamedQuery__mustReturnGuides() throws Exception {
        Guide guide1 = new Guide()

        HibernateUtil.execute { EntityManager em ->
            em.persist(guide1)
            def guide2 = em.createNamedQuery("findByStaffId")
                    .setParameter("staffId", guide1.staffId)
                    .singleResult

            assertTrue(guide1.is(guide2))
            println guide2
        }
    }

    // ##### Aggregate Functions #####
    @Test
    void countGuide__mustReturnNumberOfGuide() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            long count = em.createQuery("select count(g) from Guide g").singleResult as long

            assertTrue(count >= 1)
            println(count)
        }
    }

    @Test
    void findMaxSalaryCount__mustReturn() throws Exception {
        Guide guide1 = new Guide(salary: 10)
        Guide guide2 = new Guide(salary: 50000)

        HibernateUtil.execute { EntityManager em ->
            em.persist(guide1)
            em.persist(guide2)

            List<Integer> resultList = em.createQuery("select min(g.salary), max(g.salary) from eager_guide g").singleResult as List<Integer>
            resultList.sort()   // for safe

            assertTrue(resultList[0] <= guide1.salary)
            assertTrue(resultList[1] >= guide2.salary)
            println resultList
        }
    }

    // ##### Join #####
    @Test
    void findStudentsWithGuideUsingInnerJoin__mustReturnMatchingValuesInBoth() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            List<Student> students = em.createQuery("select s from eager_student s inner join s.guide g").resultList
            students.each {
                println it
                assertNotNull(it.guide)
            }
        }
    }

    @Test
    void findStudentsWithGuideUsingLeftJoin__mustReturnAllOfStudentsThatMatchedGuide() throws Exception {
        List<Student> students = null
        HibernateUtil.execute { EntityManager em ->
            students = em.createQuery("select s from eager_student s left outer join s.guide g").resultList

            assertNotNull(students)
            assertTrue(students.size() != 0)
        }
        students.each { println it }

        def nullGuideList = students.findAll { it.guide == null }
        assertTrue(nullGuideList.size() != 0)
    }

    @Test
    void findStudentsWithGuideUsingRightJoin__mustReturnAllGuidesThatMatchesStudent() throws Exception {
        List<Student> students = null
        HibernateUtil.execute { EntityManager em ->
            students = em.createQuery("select s from eager_student s right outer join s.guide g").resultList

            assertNotNull(students)
            assertTrue(students.size() != 0)
        }

        students.each {
            println it
            assertNotNull(it.guide)
        }
    }

    // ##### Join Fetch #####

    /**
     * TODO: check how many query executed
     */
    @Test
    void findStudentsWithMultipleQueryLikesLazyFetch__mustReturn() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            println em.createQuery("select s from eager_student s inner join s.guide g").resultList
        }
    }

    /**
     * TODO: check how many query executed
     */
    @Test
    void findStudentsWithOneQueryLikesEagerFetch__mustReturn() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            em.createQuery("select s from eager_student s inner join fetch s.guide g").resultList.each {
                println it
            }
        }
    }
}
