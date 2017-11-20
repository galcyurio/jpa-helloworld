package entity.animal.tablePerClass

import javax.persistence.Entity

/**
 * @author galcyurio
 */
@Entity(name = "per_class_dog")
class Dog extends Animal {

    @Override
    def makeNoise() {
        return "woof woof..."
    }
}
