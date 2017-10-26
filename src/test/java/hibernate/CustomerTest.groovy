package hibernate

import entity.customer.Customer
import entity.customer.Passport
import org.hibernate.Session
import org.hibernate.Transaction
import org.junit.Test
import util.HibernateUtil

class CustomerTest {

    @Test
    void saveCustomerWithPassport__mustSuccess() {
        Session session = HibernateUtil.sessionFactory.openSession()
        Transaction transaction = session.beginTransaction()

        Passport passport = new Passport("1111")
        Customer customer = new Customer("dummyName", passport)
        session.persist(customer)

        transaction.commit()
        session.close()
    }

    @Test
    void savePassportWithCustomer__mustSuccess() {
        Session session = HibernateUtil.sessionFactory.openSession()
        Transaction transaction = session.beginTransaction()

        Passport passport = new Passport("2222")
        passport.customer = new Customer(passport: passport)
        session.persist(passport)

        transaction.commit()
        session.close()
    }

}
