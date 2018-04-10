package live.luna.entity

import live.luna.graphql.FeedItem
import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLListField
import live.luna.graphql.annotations.GraphQLObject
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

    @GraphQLListField(type = Photo::class)
    fun photos(): List<Photo> {
        return masters.flatMap { it.photos }
    }

    @GraphQLField
    fun stars(): Int {
        return masters.map { it.stars }.sum() / masters.size
    }

    @GraphQLListField(type = Sign::class)
    fun signs(): List<Sign> {
        return masters.flatMap { it.signs }.distinctBy { it.id }
    }

    @GraphQLListField(type = Service::class)
    fun services(): List<Service> {
        return masters.flatMap { it.services }.distinctBy { it.id }
    }

    constructor() : this(address = Address(), avatar = Photo(), name = "", masters = emptyList())
}