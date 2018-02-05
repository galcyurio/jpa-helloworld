package entity.person

import groovy.transform.Canonical

import javax.persistence.Embeddable

@Canonical
@Embeddable
class Address {
    String street
    String city
    String zipcode
}
