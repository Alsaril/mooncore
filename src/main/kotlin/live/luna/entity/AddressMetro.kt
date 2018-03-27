package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "address_metro")
@GraphQLObject
data class AddressMetro(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "address_id", nullable = false)
        @GraphQLField
        val address: Address,

        @Column(name = "station", nullable = false)
        @GraphQLField
        val station: String,

        @Column(name = "line", nullable = false)
        @GraphQLField
        val line: String,

        @Column(name = "color", nullable = false)
        @GraphQLField
        val color: String,

        @Column(name = "distance", nullable = false)
        @GraphQLField
        val distance: Double

) {
    constructor() : this(address = Address(), station = "", line = "", color = "", distance = 0.0)
}