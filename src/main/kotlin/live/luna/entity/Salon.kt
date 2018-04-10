package live.luna.entity

import live.luna.graphql.FeedItem
import live.luna.graphql.annotations.*
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
        @JoinColumn(name = "avatar_id", nullable = false)
        @GraphQLField
        val avatar: Photo,

        @OneToMany(mappedBy = "salon", cascade = [CascadeType.ALL])
        @GraphQLListField(type = Master::class)
        val masters: List<Master>
) {

    constructor() : this(address = Address(), avatar = Photo(), name = "", masters = emptyList())
}