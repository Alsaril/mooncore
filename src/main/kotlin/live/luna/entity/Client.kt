package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject
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
        @JoinColumn(name = "avatar_id", nullable = true)
        @GraphQLField
        val avatar: Photo? = null,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "client_favorites",
                joinColumns = [JoinColumn(name = "client_id")],
                inverseJoinColumns = [JoinColumn(name = "master_id")]
        )
        @GraphQLField(of = Master::class)
        val favorites: List<Master> = listOf()

) {
    constructor() : this(user = User(), avatar = Photo(), name = "", favorites = ArrayList())
}