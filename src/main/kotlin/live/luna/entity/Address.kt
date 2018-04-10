package live.luna.entity

import live.luna.graphql.annotations.*
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

        @OneToMany
        @JoinColumn(name = "address_id")
        @GraphQLListField(type = AddressMetro::class)
        val metros: List<AddressMetro>
) {
    @GraphQLInputObject(name = "AddressInput")
    constructor(
            @GraphQLInputField(name = "description") description: String,
            @GraphQLInputField(name = "lat") lat: Double,
            @GraphQLInputField(name = "lon") lon: Double) :
            this(description = description, lat = lat, lon = lon, metros = emptyList())

    constructor() : this(lat = 0.0, lon = 0.0, description = "", metros = ArrayList())
}