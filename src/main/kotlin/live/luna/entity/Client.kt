package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "client")
data class Client(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        val name: String,

        @OneToOne
        @JoinColumn(name = "user_id", nullable = false)
        val user: User,

        @ManyToOne
        @JoinColumn(name = "photo_id", nullable = false)
        val photo: Photo

) {
    constructor() : this(user = User(), photo = Photo(), name = "")
}