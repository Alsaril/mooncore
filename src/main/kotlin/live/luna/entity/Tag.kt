package live.luna.entity

import live.luna.graphql.GraphQLField
import live.luna.graphql.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "tag")
@GraphQLObject
data class Tag(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        @GraphQLField
        val name: String
) {
    constructor() : this(name = "")
}