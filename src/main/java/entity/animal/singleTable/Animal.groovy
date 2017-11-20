package entity.animal.singleTable

import javax.persistence.*
/**
 * @author galcyurio
 */
@Entity(name = "single_animal")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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
