package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLListField
import live.luna.graphql.annotations.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "master")
@GraphQLObject
data class Master(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "name", nullable = false)

        val name: String,

        @OneToOne
        @JoinColumn(name = "user_id", unique = true, nullable = false)
        @GraphQLField
        val user: User,

        @ManyToOne
        @JoinColumn(name = "address_id", nullable = true)
        @GraphQLField(nullable = true)
        val address: Address? = null,

        @OneToOne
        @JoinColumn(name = "avatar_id", nullable = true)
        @GraphQLField(nullable = true)
        val avatar: Photo? = null,

        @ManyToOne
        @JoinColumn(name = "salon_id", nullable = true)
        @GraphQLField(nullable = true)
        val salon: Salon? = null,

        @Column(name = "stars", nullable = false)
        @GraphQLField
        val stars: Int = 0,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "master_sign",
                joinColumns = [JoinColumn(name = "master_id")],
                inverseJoinColumns = [JoinColumn(name = "sign_id")]
        )
        @GraphQLListField(type = Sign::class)
        val signs: List<Sign> = listOf(),

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "master_photo",
                joinColumns = [JoinColumn(name = "master_id")],
                inverseJoinColumns = [JoinColumn(name = "photo_id")]
        )
        @GraphQLListField(type = Photo::class)
        val photos: List<Photo> = listOf(),

        @OneToMany(mappedBy = "master", cascade = [CascadeType.ALL])
        @GraphQLListField(type = Service::class)
        val services: List<Service> = listOf()

) {
    constructor() : this(
            name = "",
            user = User(),
            address = Address(),
            avatar = Photo(),
            salon = Salon(),
            signs = ArrayList(),
            photos = ArrayList(),
            services = ArrayList()
    )
}