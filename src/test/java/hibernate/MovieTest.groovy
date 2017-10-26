package hibernate

import entity.movie.Actor
import entity.movie.Movie
import org.hibernate.Session
import org.hibernate.Transaction
import org.junit.Test
import util.HibernateUtil

class MovieTest {

    @Test
    void saveMovie__mustSuccess() {
        Session session = HibernateUtil.sessionFactory.openSession()
        Transaction transaction = session.beginTransaction()

        try {
            Movie movie1 = new Movie("movie1")
            Movie movie2 = new Movie("movie2")

            Actor actor1 = new Actor("actor1")
            Actor actor2 = new Actor("actor2")

            movie1.actors?.addAll([actor1, actor2])
            movie2.actors?.add(actor2)

            session.persist(movie1)
            session.persist(movie2)
            transaction.commit()
        } catch (any) {
            transaction?.rollback()
            any.printStackTrace()
        } finally {
            session?.close()
        }
    }

    @Test
    void saveActor__mustSuccess() {
        Session session = HibernateUtil.sessionFactory.openSession()
        Transaction transaction = session.beginTransaction()

        try {
            Movie movie1 = new Movie("movie1")
            Movie movie2 = new Movie("movie2")

            Actor actor1 = new Actor("actor1")
            Actor actor2 = new Actor("actor2")

            actor1.movies.addAll([movie1, movie2])
            actor2.movies.add(movie2)

            session.persist(actor1)
            session.persist(actor2)

            transaction.commit()
        } catch (any) {
            transaction?.rollback()
            any.printStackTrace()
        } finally {
            session?.close()
        }
    }
}
