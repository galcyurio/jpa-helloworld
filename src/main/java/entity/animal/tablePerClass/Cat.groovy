package entity.animal.tablePerClass

import javax.persistence.Entity

/**
 * @author galcyurio
 */
@Entity(name = "per_class_cat")
class Cat extends Animal {
    @Override
    def makeNoise() {
        return "meow meow..."
    }
}
