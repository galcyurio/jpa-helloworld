package entity.movie

import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
@TupleConstructor
@Canonical
class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    String name

    @ManyToMany(mappedBy = "actors", cascade = CascadeType.PERSIST)
    Set<Movie> movies

    Actor(String name = "emptyActor", Set<Movie> movies = new HashSet<>()) {
        this.name = name
        this.movies = movies
    }
}
