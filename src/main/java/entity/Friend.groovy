package entity

import entity.person.Address
import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@TupleConstructor(excludes = "id")
@Canonical
class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String name
    String email

    @ElementCollection
    @CollectionTable(name = "friend_nickname", joinColumns = @JoinColumn(name = "friend_id"))
    @Column(name = "nickname")
    List<String> nicknames = new ArrayList<>()

    @ElementCollection
    @CollectionTable(name = "friend_address", joinColumns = @JoinColumn(name = "friend_id"))
    @AttributeOverrides([
            @AttributeOverride(name = "zipcode", column = @Column(name = "friend_zipcode")),
            @AttributeOverride(name = "street", column = @Column(name = "friend_street")),
            @AttributeOverride(name = "city", column = @Column(name = "friend_city"))
    ])
    List<Address> friendAddress
}
