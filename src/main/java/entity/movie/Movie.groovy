package entity.movie

import groovy.transform.Canonical
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@TupleConstructor
@Canonical
class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    String name

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "movie_actor",
            joinColumns = [@JoinColumn(name = "movie_id")],
            inverseJoinColumns = [@JoinColumn(name = "actor_id")]
    )
    Set<Actor> actors

    Movie(String name = "emptyMovie", Set<Actor> actors = new HashSet<>()) {
        this.name = name
        this.actors = actors
    }
}
