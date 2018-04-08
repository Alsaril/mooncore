package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject
import java.util.*
import javax.persistence.*

/**
 * Объект класса Schedule представляет отрезок времени, когда мастер на работе
 *  (мастер может быть как свободен, так и занят клиентом)
 */

@Entity
@Table(name = "schedule")
@GraphQLObject
data class Schedule(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "master_id", nullable = false)
        @GraphQLField
        val master: Master,

        @Column(name = "start_time", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val startTime: Date,

        @Column(name = "end_time", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val endTime: Date

) {
    constructor() : this(master = Master(), startTime = Date(), endTime = Date())
}