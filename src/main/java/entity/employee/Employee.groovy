package entity.employee

import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@TupleConstructor(excludes = "id")
@Canonical
class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(name = "employee_id", nullable = false)
    String employeeId

    @Column(name = "employee_status", nullable = false)
    EmployeeStatus employeeStatus

    @Column(nullable = false)
    String name
}
