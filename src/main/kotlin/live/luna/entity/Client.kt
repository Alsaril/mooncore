package live.luna.entity

import com.alsaril.graphql.GraphQLField
import com.alsaril.graphql.GraphQLListField
import com.alsaril.graphql.GraphQLObject
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

        @Column(name = "name", nullable = true)
        @GraphQLField(nullable = true)
        val name: String?,

        @OneToOne
        @JoinColumn(name = "user_id", nullable = false)
        @GraphQLField
        val user: User,

        @ManyToOne
        @JoinColumn(name = "avatar_id", nullable = true)
        @GraphQLField(nullable = true)
        val avatar: Photo? = null,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "client_favorites",
                joinColumns = [JoinColumn(name = "client_id")],
                inverseJoinColumns = [JoinColumn(name = "master_id")]
        )
        @GraphQLListField(type = Master::class)
        val favorites: List<Master> = listOf()

) {
    constructor() : this(user = User(), avatar = null, name = null)
}