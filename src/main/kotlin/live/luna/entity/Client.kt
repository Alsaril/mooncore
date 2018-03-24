package live.luna.entity

import live.luna.GraphQLField
import live.luna.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "client")
@GraphQLObject
data class Client(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        @GraphQLField
        val name: String,

        @OneToOne
        @JoinColumn(name = "user_id", nullable = false)
        @GraphQLField
        val user: User,

        @ManyToOne
        @JoinColumn(name = "photo_id", nullable = false)
        @GraphQLField
        val photo: Photo

) {
    constructor() : this(user = User(), photo = Photo(), name = "")
}