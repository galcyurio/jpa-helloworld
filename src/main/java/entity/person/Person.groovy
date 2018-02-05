package entity.person

import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.*

@Canonical
@TupleConstructor(excludes = "id")
@Entity
class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    String name

    @Embedded
    Address address
}
