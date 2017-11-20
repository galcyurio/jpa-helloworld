package entity.animal.singleTable

import javax.persistence.Entity

/**
 * @author galcyurio
 */
@Entity(name = "single_dog")
class Dog extends Animal {

    @Override
    def makeNoise() {
        return "woof woof..."
    }
}
