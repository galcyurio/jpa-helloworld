package hibernate

import entity.Friend
import entity.person.Address
import org.hibernate.Session
import org.hibernate.Transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import util.HibernateUtil

class FriendTest {

    Session mSession
    Transaction transaction

    @Before
    void setUp() throws Exception {
        mSession = HibernateUtil.sessionFactory.openSession()
        transaction = mSession.beginTransaction()
    }

    @After
    void tearDown() throws Exception {
        transaction.commit()
        mSession.close()
    }


    @Test
    void persistFriend__mustSuccess() throws Exception {
        Transaction transaction = mSession.beginTransaction()

        def friend = new Friend(
                "dummy name",
                "dummy email",
                new ArrayList<>(Arrays.asList("donn", "sam", "park")))
        mSession.persist(friend)

    }

    @Test
    void persistFriendAddress__mustSuccess() throws Exception {
        def friend = new Friend(
                "dummy name",
                "dummy email",
                null,
                new ArrayList<>(Arrays.asList(
                        new Address("dummy street", "dummy city", "dummy zipcode"),
                        new Address("dummy street2", "dummy city2", "dummy zipcode2")
                ))
        )

        mSession.persist(friend)
    }

}
