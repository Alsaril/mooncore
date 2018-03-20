package live.luna.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID", nullable = false)
        val id: Long = 0,

        @Column(name = "email", nullable = false)
        val email: String,

        @Column(name = "password", nullable = false)
        val password: String,

        @Column(name = "role", nullable = false)
        val role: Int,

        @Column(name = "ctime", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        val ctime: Date = Date()

) {
    constructor() : this(email = "", password = "", role = -1)
}