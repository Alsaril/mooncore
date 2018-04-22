package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "review")
@GraphQLObject
data class Review(

        @EmbeddedId
        val id: ReviewId = ReviewId(),

        @Column(name = "stars", nullable = false)
        @GraphQLField
        val stars: Int,

        @Column(name = "message", nullable = true)
        @GraphQLField
        val message: String?,

        @Column(name = "date", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val date: Date = Date()

) {
    constructor() : this(stars = 0, message = null)

    @Embeddable
    data class ReviewId(
            @ManyToOne
            @JoinColumn(name = "client_id", nullable = false)
            @GraphQLField
            val client: Client,

            @OneToOne
            @JoinColumn(name = "seance_id", nullable = false)
            @GraphQLField
            val seance: Seance
    ) : Serializable {
        constructor() : this(client = Client(), seance = Seance())
    }
}
