package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "service_type")
data class ServiceType(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "parent_id", nullable = true)
        val parent: ServiceType?,

        @Column(name = "name", nullable = false)
        val name: String

) {
    constructor() : this(name = "", parent = null)
}