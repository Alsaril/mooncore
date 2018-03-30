package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import javax.persistence.*

@Entity
@Table(name = "service_type")
data class ServiceType(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "parent_id", nullable = true)
        @GraphQLField(nullable = true)
        val parent: ServiceType?,

        @Column(name = "name", nullable = false)
        @GraphQLField
        val name: String

) {
    constructor() : this(name = "", parent = null)
}