package entity.student.lazy

import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity(name = "lazy_guide")
@TupleConstructor(excludes = "id, students")
@Canonical(excludes = "students")
class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(name = "staff_id", nullable = false)
    String staffId;
    String name
    Integer salary

    @OneToMany(mappedBy = "guide", cascade = CascadeType.PERSIST)
    Set<Student> students = new HashSet<>()

    Guide(String staffId = UUID.randomUUID().toString(), String name = "John Doe", Integer salary = 1000) {
        this.staffId = staffId
        this.name = name
        this.salary = salary
    }

    Guide addStudent(Student student) {
        students.add(student)
        student.guide = this
        return this
    }
}
