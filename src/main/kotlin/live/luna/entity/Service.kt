package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLListField
import live.luna.graphql.annotations.GraphQLObject
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "service")
@GraphQLObject
data class Service(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "type", nullable = false)
        @GraphQLField
        val type: ServiceType,

        @ManyToOne
        @JoinColumn(name = "master_id", nullable = false)
        @GraphQLField
        val master: Master,

        @Column(name = "price", nullable = false)
        @GraphQLField
        val price: BigDecimal,

        @Column(name = "description", nullable = false, columnDefinition = "TEXT")
        @GraphQLField
        val description: String = "",

        @Column(name = "duration", nullable = false)
        @GraphQLField
        val duration: Long = 0,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "service_material",
                joinColumns = [JoinColumn(name = "service_id")],
                inverseJoinColumns = [JoinColumn(name = "material_id")]
        )
        @GraphQLListField(type = Material::class)
        val materials: List<Material>,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "service_photo",
                joinColumns = [JoinColumn(name = "service_id")],
                inverseJoinColumns = [JoinColumn(name = "photo_id")]
        )
        @GraphQLListField(type = Photo::class)
        val photos: List<Photo>,

        @Column(name = "ctime", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val ctime: Date = Date()

) {
    constructor() : this(price = BigDecimal.ZERO, type = ServiceType(), master = Master(), materials = ArrayList(), photos = ArrayList())
}