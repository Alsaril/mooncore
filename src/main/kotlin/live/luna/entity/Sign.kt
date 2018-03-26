package live.luna.entity

import live.luna.graphql.GraphQLField
import live.luna.graphql.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "sign")
@GraphQLObject
data class Sign(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        @GraphQLField
        val name: String,

        @Column(name = "icon", nullable = false)
        @GraphQLField
        val icon: String,

        @Column(name = "description", nullable = false)
        @GraphQLField
        val description: String,

        @ManyToMany(mappedBy = "signs")
        @GraphQLField(of = Master::class)
        val masters: List<Master>
) {
        constructor() : this(name = "", icon = "", description = "", masters = ArrayList())
}