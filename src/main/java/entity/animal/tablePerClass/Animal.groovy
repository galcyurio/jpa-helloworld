package entity.animal.tablePerClass

import javax.persistence.*

/**
 * @author galcyurio
 */
@Entity(name = "per_class_animal")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id
    String name

    abstract def makeNoise()

    @Override
    String toString() {
        return "${name} making ${makeNoise()} noises"
    }
}
