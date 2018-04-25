package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "review", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("client_id", "seance_id"))])
@GraphQLObject
data class Review(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "client_id", nullable = false)
        @GraphQLField
        val client: Client,

        @OneToOne
        @JoinColumn(name = "seance_id", nullable = false)
        @GraphQLField
        val seance: Seance,

        @Column(name = "stars", nullable = false)
        @GraphQLField
        val stars: Int,

        @Column(name = "message", nullable = true, columnDefinition = "TEXT")
        @GraphQLField(nullable = true)
        val message: String?,

        @Column(name = "date", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val date: Date = Date()

) {
    constructor() : this(client = Client(), seance = Seance(), stars = 0, message = null)
}
