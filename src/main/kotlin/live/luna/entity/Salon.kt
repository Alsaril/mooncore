package live.luna.entity

import com.alsaril.graphql.GraphQLField
import com.alsaril.graphql.GraphQLInputField
import com.alsaril.graphql.GraphQLInputObject
import com.alsaril.graphql.GraphQLObject
import live.luna.graphql.FeedItem
import javax.persistence.*

@Entity
@Table(name = "salon")
@GraphQLObject(implements = [FeedItem::class])
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
    @GraphQLInputObject(name = "SalonInput")
    constructor(
            @GraphQLInputField(name = "name") name: String,
            @GraphQLInputField(name = "address") address: Address,
            @GraphQLInputField(name = "photo") photo: Photo) :
            this(id = 0, name = name, address = address, photo = photo)

    constructor() : this(address = Address(), photo = Photo(), name = "")
}