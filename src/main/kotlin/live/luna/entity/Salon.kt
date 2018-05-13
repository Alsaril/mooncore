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
        @GraphQLField(nullable = true)
        val address: Address,

        @OneToOne
        @JoinColumn(name = "avatar_id", nullable = false)
        @GraphQLField
        val avatar: Photo,

        @OneToMany(mappedBy = "salon", cascade = [CascadeType.ALL])
        @GraphQLListField(type = Master::class)
        val masters: List<Master>,

        @Transient
        @GraphQLListField(type = Review::class)
        val lastReviews: List<Review> = listOf()
) {

    @GraphQLListField(type = Photo::class)
    fun photos(): List<Photo> {
        return masters.flatMap { it.photos }.shuffled().take(10)
    }

    @GraphQLField
    fun stars(): Int {
        return if (masters.isEmpty()) 0 else masters.map { it.stars }.sum() / masters.size
    }

    @GraphQLListField(type = Sign::class)
    fun signs(): List<Sign> {
        return masters.flatMap { it.signs }
    }

    @GraphQLListField(type = Service::class)
    fun services(): List<Service> {
        return masters.flatMap { it.services }.distinctBy { it.id }
    }

    @GraphQLField
    fun ratesCount(): Int = masters.map { it.ratesCount }.sum()

    @GraphQLField
    fun commentsCount(): Int = masters.map { it.commentsCount }.sum()

    @GraphQLListField(type = SignGroup::class)
    fun signGroup(): List<SignGroup> {
        val result = mutableMapOf<Sign, Int>()
        masters.forEach {
            it.mapSigns().forEach {
                result[it.key] = result.getOrDefault(it.key, 0) + it.value
            }
        }
        return result.map { SignGroup(it.key, it.value) }
    }

    @GraphQLInputObject(name = "SalonInput")
    constructor(
            @GraphQLInputField(name = "name") name: String,
            @GraphQLInputField(name = "address") address: Address,
            @GraphQLInputField(name = "avatar") avatar: Photo) :
            this(id = 0, name = name, address = address, avatar = avatar, masters = emptyList())

    constructor() : this(address = Address(), avatar = Photo(), name = "", masters = emptyList())
}