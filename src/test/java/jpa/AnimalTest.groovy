package jpa

import entity.animal.joined.Animal as JoinedAnimal
import entity.animal.joined.Cat as JoinedCat
import entity.animal.joined.Dog as JoinedDog
import entity.animal.singleTable.Animal as SingleAnimal
import entity.animal.singleTable.Cat as SingleCat
import entity.animal.singleTable.Dog as SingleDog
import entity.animal.tablePerClass.Animal as PerClassAnimal
import entity.animal.tablePerClass.Cat as PerClassCat
import entity.animal.tablePerClass.Dog as PerClassDog
import org.junit.After
import org.junit.Before
import org.junit.Test
import util.HibernateUtil

import javax.persistence.EntityManager

/**
 * @author galcyurio
 */
class AnimalTest {

    @Before
    void setUp() throws Exception {
        SingleAnimal singleDog = new SingleDog(name: "Single Dog")
        SingleAnimal singleCat = new SingleCat(name: "Single Cat")
        JoinedAnimal joinedDog = new JoinedDog(name: "Joined Dog")
        JoinedAnimal joinedCat = new JoinedCat(name: "Joined Cat")
        PerClassAnimal perClassDog = new PerClassDog(name: "Per class Dog")
        PerClassAnimal perClassCat = new PerClassCat(name: "Per class Cat")

        HibernateUtil.execute { EntityManager em ->
            em.persist(singleDog)
            em.persist(singleCat)
            em.persist(joinedDog)
            em.persist(joinedCat)
            em.persist(perClassDog)
            em.persist(perClassCat)
            println "========== after setup ============="
        }
    }

    @After
    void tearDown() throws Exception {
        println "=========== before tearDown =============="
        HibernateUtil.execute { EntityManager em ->
            em.createQuery("delete from single_animal").executeUpdate()
            em.createQuery("delete from joined_animal").executeUpdate()
            em.createQuery("delete from per_class_animal").executeUpdate()
        }
    }

    // ~ Single Table

    /**
     * TODO: check how many query executed
     */
    @Test
    void singleTable_persistDogAndCat__mustUseEachSqlStatement() throws Exception {
        SingleAnimal dog = new SingleDog(name: "Foo")
        SingleAnimal cat = new SingleCat(name: "Bar")

        HibernateUtil.execute { EntityManager em ->
            em.persist(dog)
            em.persist(cat)
        }
    }

    /**
     * TODO: check what query executed
     */
    @Test
    void singleTable__findDogAndCat__mustUse1SqlStatement() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            def animals = em.createQuery("select a from single_animal a").resultList
            animals.each { println it }
        }
    }

    // ~ Joined

    /**
     * TODO: check how many query executed
     */
    @Test
    void joined_persistDogAndCat__mustUseDoubleEachSqlStatement() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            JoinedDog dog = new JoinedDog(name: "dog")
            JoinedCat cat = new JoinedCat(name: "cat")

            em.persist(dog)
            em.persist(cat)
        }
    }

    /**
     * TODO: check what query executed
     */
    @Test
    void joined_findDogAndCat__mustUse1JoinSqlStatement() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            def animals = em.createQuery("select a from joined_animal a").resultList
            animals.each { println it }
        }
    }

    // ~ TablePerClass

    /**
     * TODO: check how many query executed
     * SELECT FOR UPDATE, INSERT INTO, UPDATE SEQUENCE
     */
    @Test
    void perClass_persistDogAndCat__mustUseTripleEachSqlStatement() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            def dog = new PerClassDog(name: "dog")
            def cat = new PerClassCat(name: "cat")

            em.persist(dog)
            em.persist(cat)
        }
    }

    /**
     * TODO: check what query executed
     */
    @Test
    void perClass_findDogAndCat__mustUse1UnionSqlStatement() throws Exception {
        HibernateUtil.execute { EntityManager em ->
            def animals = em.createQuery("select a from per_class_animal a").resultList
            animals.each { println it }
        }
    }
}
