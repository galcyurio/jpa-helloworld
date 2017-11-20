package entity.animal.joined

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.InheritanceType

/**
 * @author galcyurio
 */
@Entity(name = "joined_animal")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String name

    abstract def makeNoise()

    @Override
    String toString() {
        return "${name} making ${makeNoise()} noises"
    }
}
