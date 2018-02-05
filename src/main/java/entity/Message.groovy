package entity

import javax.persistence.*

@Entity
@Table(name = "message")
class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id
    private String text

    Message(String text = "DummyText") {
        this.text = text
    }
}