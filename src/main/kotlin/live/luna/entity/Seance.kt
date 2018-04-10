package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLListField
import live.luna.graphql.annotations.GraphQLObject
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "seance")
@GraphQLObject
data class Seance(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "master_id", nullable = false)
        @GraphQLField
        val master: Master,

        @ManyToOne
        @JoinColumn(name = "client_id", nullable = false)
        @GraphQLField
        val client: Client,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "seance_service",
                joinColumns = [JoinColumn(name = "seance_id")],
                inverseJoinColumns = [JoinColumn(name = "service_id")]
        )
        @GraphQLListField(type = Service::class)
        val services: List<Service>,

        @Column(name = "start_time", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val startTime: Date,

        @Column(name = "end_time", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val endTime: Date

) {
    constructor() : this(master = Master(), client = Client(),
            services = emptyList(), startTime = Date(), endTime = Date())


    class Builder() {
        var id: Long = 0
        var master: Master? = null
        var client: Client? = null
        var startTime: Date? = null
        var endTime: Date? = null
        var services: List<Service> = emptyList()

        fun build() = Seance(
                id = id,
                master = master ?: throw NullPointerException("You have to provide non-null master field"),
                client = client ?: throw NullPointerException("You have to provide non-null client field"),
                startTime = startTime ?: throw NullPointerException("You have to provide non-null startTime field"),
                endTime = endTime ?: throw NullPointerException("You have to provide non-null endTime field"),
                services = services
        )

        companion object {
            fun from(seance: Seance) = Builder().apply {
                id = seance.id
                master = seance.master
                client = seance.client
                startTime = seance.startTime
                endTime = seance.endTime
                services = seance.services
            }
        }
    }
}
