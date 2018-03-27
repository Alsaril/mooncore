package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user")
@GraphQLObject
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "email", unique = true, nullable = false)
        @GraphQLField
        val email: String,

        @Column(name = "password", nullable = false)
        @GraphQLField
        val password: String,

        @Column(name = "role", nullable = false)
        @GraphQLField
        val role: Int,

        @Column(name = "ctime", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @GraphQLField
        val ctime: Date = Date()

) {
    constructor() : this(email = "", password = "", role = -1)
}