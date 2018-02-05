package entity.student.eager

import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity(name = "eager_student")
@TupleConstructor(excludes = "id, guide")
@Canonical
class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(name = "enrollment_id", nullable = false)
    String enrollmentId
    String name

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE], fetch = FetchType.EAGER)
    @JoinColumn(name = "guide_id")
    Guide guide

    Student(String enrollmentId = UUID.randomUUID().toString(), String name = "Jane") {
        this.enrollmentId = enrollmentId
        this.name = name
    }
}
