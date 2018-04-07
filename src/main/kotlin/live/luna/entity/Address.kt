package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLListField
import live.luna.graphql.annotations.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "address")
@GraphQLObject
data class Address(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "lat", nullable = false)
        @GraphQLField
        val lat: Double,

        @Column(name = "lon", nullable = false)
        @GraphQLField
        val lon: Double,

        @Column(name = "description", nullable = false)
        @GraphQLField
        val description: String,

        @OneToMany(mappedBy = "address", cascade = [CascadeType.ALL])
        @GraphQLListField(type = AddressMetro::class)
        val metros: List<AddressMetro>
) {
    constructor() : this(lat = 0.0, lon = 0.0, description = "", metros = ArrayList())
}