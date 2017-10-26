package entity.customer

import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@TupleConstructor(excludes = "id")
@Canonical
class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(name = "passport_number", nullable = false)
    String passportNumber

    @OneToOne(mappedBy = "passport", cascade = CascadeType.PERSIST)
    Customer customer

    Passport(String passportNumber = "empty") {
        this.passportNumber = passportNumber
    }
}
