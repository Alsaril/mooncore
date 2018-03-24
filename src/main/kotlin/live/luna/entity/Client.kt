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
        @JoinColumn(name = "avatar_id", nullable = false)
        val avatar: Photo,

        @ManyToMany(cascade = [(CascadeType.ALL)])
        @JoinTable(
                name = "client_favorites",
                joinColumns = [(JoinColumn(name = "client_id"))],
                inverseJoinColumns = [(JoinColumn(name = "master_id"))]
        )
        val favorites: List<Master>

) {
    constructor() : this(user = User(), avatar = Photo(), name = "", favorites = ArrayList())
}