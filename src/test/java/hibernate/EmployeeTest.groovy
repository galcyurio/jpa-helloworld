package hibernate

import entity.employee.Employee
import entity.employee.EmployeeStatus
import org.hibernate.Session
import org.hibernate.Transaction
import org.junit.Test
import util.HibernateUtil

class EmployeeTest {

    @Test
    void save__mustSuccess() throws Exception {
        Session session = HibernateUtil.sessionFactory.openSession()
        Transaction transaction = session.beginTransaction()

        Employee employee1 = new Employee(UUID.randomUUID().toString(), EmployeeStatus.FULL_TIME, "Derek")
        Employee employee2 = new Employee(UUID.randomUUID().toString(), EmployeeStatus.PART_TIME, "Sam")
        Employee employee3 = new Employee(UUID.randomUUID().toString(), EmployeeStatus.CONTRACT, "Jane")

        session.persist(employee1)
        session.persist(employee2)
        session.persist(employee3)

        transaction.commit()
        session.close()
    }
}
