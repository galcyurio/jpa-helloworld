package entity.animal.singleTable

import javax.persistence.Entity

/**
 * @author galcyurio
 */
@Entity(name = "single_cat")
class Cat extends Animal {
    @Override
    def makeNoise() {
        return "meow meow..."
    }
}
