package live.luna.entity

import com.alsaril.graphql.GraphQLField
import com.alsaril.graphql.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "service_type")
@GraphQLObject
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

        @Column(name = "name", unique = true, nullable = false)
        @GraphQLField
        val name: String

) {
    constructor() : this(name = "", parent = null)
}