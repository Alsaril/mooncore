package live.luna.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "service")
data class Service(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        val name: String,

        @ManyToOne
        @JoinColumn(name = "master_id", nullable = false)
        val master: Master,

        @Column(name = "price", nullable = false)
        val price: Double = 0.0,

        @Column(name = "description", nullable = false, columnDefinition = "TEXT")
        val description: String = "",

        @Column(name = "ctime", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        val ctime: Date = Date()

) {
    constructor() : this(name = "", master = Master())
}