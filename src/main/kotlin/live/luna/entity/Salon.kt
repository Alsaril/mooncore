package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "salon")
@GraphQLObject
data class Salon(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        @GraphQLField
        val name: String,

        @ManyToOne
        @JoinColumn(name = "address_id", nullable = false)
        @GraphQLField
        val address: Address,

        @OneToOne
        @JoinColumn(name = "photo_id", nullable = false)
        @GraphQLField
        val photo: Photo,

        @Column(name = "stars", nullable = false)
        @GraphQLField
        val stars: Int = 0

) {
    constructor() : this(address = Address(), photo = Photo(), name = "")
}