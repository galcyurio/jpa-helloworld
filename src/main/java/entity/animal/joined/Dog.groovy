package entity.animal.joined

import javax.persistence.Entity

/**
 * @author galcyurio
 */
@Entity(name = "joined_dog")
class Dog extends Animal {

    @Override
    def makeNoise() {
        return "woof woof..."
    }
}
