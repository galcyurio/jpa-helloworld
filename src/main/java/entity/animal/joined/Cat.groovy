package entity.animal.joined

import javax.persistence.Entity

/**
 * @author galcyurio
 */
@Entity(name = "joined_cat")
class Cat extends Animal {
    @Override
    def makeNoise() {
        return "meow meow..."
    }
}
