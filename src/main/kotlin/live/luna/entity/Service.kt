package live.luna.entity

import live.luna.graphql.GraphQLField
import live.luna.graphql.GraphQLObject
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

        @Column(name = "name", nullable = false)
        @GraphQLField
        val name: String,

        @ManyToOne
        @JoinColumn(name = "master_id", nullable = false)
        @GraphQLField
        val master: Master,

        @Column(name = "price", nullable = false)
        @GraphQLField
        val price: Double = 0.0,

        @Column(name = "description", nullable = false, columnDefinition = "TEXT")
        @GraphQLField
        val description: String = "",

        @Column(name = "ctime", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val ctime: Date = Date(),

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "service_material",
                joinColumns = [JoinColumn(name = "service_id")],
                inverseJoinColumns = [JoinColumn(name = "material_id")]
        )
        @GraphQLField(of = Material::class)
        val materials: List<Material>,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "service_photo",
                joinColumns = [JoinColumn(name = "service_id")],
                inverseJoinColumns = [JoinColumn(name = "photo_id")]
        )
        @GraphQLField(of = Photo::class)
        val photos: List<Photo>

) {
    constructor() : this(name = "", master = Master(), materials = ArrayList(), photos = ArrayList())
}