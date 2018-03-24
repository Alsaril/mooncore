package live.luna.entity

import live.luna.graphql.GraphQLField
import live.luna.graphql.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "material")
@GraphQLObject
data class Material(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "firm", nullable = false, columnDefinition = "TEXT")
        @GraphQLField
        val firm: String,

        @Column(name = "description", nullable = false, columnDefinition = "TEXT")
        @GraphQLField
        val description: String

) {
    constructor() : this(firm = "", description = "")
}