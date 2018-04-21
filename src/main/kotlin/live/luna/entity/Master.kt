package live.luna.entity

import live.luna.graphql.FeedItem
import live.luna.graphql.annotations.*
import javax.persistence.*

@Entity
@Table(name = "master")
@GraphQLObject(implements = [FeedItem::class])
data class Master(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "name", nullable = true)
        @GraphQLField(nullable = true)
        val name: String?,

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
        val services: List<Service> = listOf(),

        @OneToMany(mappedBy = "master", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @GraphQLListField(type = Schedule::class)
        val schedules: List<Schedule> = listOf(),

        @OneToMany(mappedBy = "master", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @GraphQLListField(type = Seance::class)
        val seances: List<Seance> = listOf()

) {
    @GraphQLInputObject(name = "MasterInput")
    constructor(@GraphQLInputField(name = "name", nullable = true) name: String?,
                @GraphQLInputField(name = "address", nullable = true) address: Address?,
                @GraphQLInputField(name = "avatar", nullable = true) avatar: Photo?,
                @GraphQLInputField(name = "salon", nullable = true) salon: Salon?)
            : this(
            name = name ?: "",
            user = User(),
            address = address,
            avatar = avatar,
            salon = salon
    )


    constructor() : this(
            name = null,
            user = User(),
            address = null,
            avatar = null,
            salon = null
    )

    fun supportAllServiceTypes(serviceTypes: List<Long>): Boolean {
        serviceTypes.forEach { type ->
            if (!services.map { it.type.id }.contains(type)) {
                return false
            }
        }
        return true
    }

    class Builder() {
        private var id: Long = 0
        private var name: String? = null
        private var user: User? = null
        private var address: Address? = null
        private var avatar: Photo? = null
        private var salon: Salon? = null
        private var stars: Int = 0
        private var signs: List<Sign> = emptyList()
        private var photos: List<Photo> = emptyList()
        private var services: List<Service> = emptyList()

        fun setId(id: Long): Builder {
            this.id = id
            return this
        }

        fun setName(name: String?): Builder {
            this.name = name
            return this
        }

        fun setUser(user: User): Builder {
            this.user = user
            return this
        }

        fun setAddress(address: Address?): Builder {
            this.address = address
            return this
        }

        fun setAvatar(avatar: Photo?): Builder {
            this.avatar = avatar
            return this
        }

        fun setSalon(salon: Salon?): Builder {
            this.salon = salon
            return this
        }

        fun setStars(stars: Int): Builder {
            this.stars = stars
            return this
        }

        fun setSigns(signs: List<Sign>): Builder {
            this.signs = signs
            return this
        }

        fun setPhotos(photos: List<Photo>): Builder {
            this.photos = photos
            return this
        }

        fun setServices(services: List<Service>): Builder {
            this.services = services
            return this
        }

        fun build() = Master(
                id = id,
                name = name,
                user = user ?: throw NullPointerException("Master cannot be built with null user field!"),
                address = address,
                avatar = avatar,
                salon = salon,
                stars = stars,
                signs = signs,
                photos = photos,
                services = services
        )

        companion object {
            fun from(master: Master): Builder {
                val builder = Builder()
                builder.id = master.id
                builder.name = master.name
                builder.user = master.user
                builder.address = master.address
                builder.avatar = master.avatar
                builder.salon = master.salon
                builder.stars = master.stars
                builder.signs = master.signs
                builder.photos = master.photos
                builder.services = master.services
                return builder
            }
        }
    }
}