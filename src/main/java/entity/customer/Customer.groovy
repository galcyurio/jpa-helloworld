package entity.customer

import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@TupleConstructor(excludes = "id")
@Canonical
class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    String name

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "passport_id", nullable = false)
    Passport passport

    Customer(String name = "empty", Passport passport = new Passport()) {
        this.name = name
        this.passport = passport
    }
}
