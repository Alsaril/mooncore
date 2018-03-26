package live.luna.entity

import live.luna.graphql.GraphQLField
import live.luna.graphql.GraphQLObject
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
        @GraphQLField
        val name: String,

        @OneToOne
        @JoinColumn(name = "user_id", unique = true, nullable = false)
        @GraphQLField
        val user: User,

        @ManyToOne
        @JoinColumn(name = "address_id", nullable = false)
        @GraphQLField
        val address: Address,

        @OneToOne
        @JoinColumn(name = "avatar_id", nullable = false)
        @GraphQLField
        val avatar: Photo,

        @ManyToOne
        @JoinColumn(name = "salon_id", nullable = true)
        @GraphQLField(nullable = true)
        val salon: Salon?,

        @Column(name = "stars", nullable = false)
        @GraphQLField
        val stars: Int = 0,

        @ManyToMany(cascade = [(CascadeType.ALL)])
        @JoinTable(
                name = "master_sign",
                joinColumns = [(JoinColumn(name = "master_id"))],
                inverseJoinColumns = [(JoinColumn(name = "sign_id"))]
        )
        @GraphQLField(of = Sign::class)
        val signs: List<Sign>,

        @ManyToMany(cascade = [(CascadeType.ALL)])
        @JoinTable(
                name = "master_photo",
                joinColumns = [(JoinColumn(name = "master_id"))],
                inverseJoinColumns = [(JoinColumn(name = "photo_id"))]
        )
        @GraphQLField(of = Photo::class)
        val photos: List<Photo>,

        @OneToMany(mappedBy = "master", cascade = [(CascadeType.ALL)])
        @GraphQLField(of = Service::class)
        val services: List<Service>

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