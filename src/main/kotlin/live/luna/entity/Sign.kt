package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "sign")
data class Sign(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID", nullable = false)
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        val name: String,

        @Column(name = "icon", nullable = false)
        val icon: String,

        @Column(name = "description", nullable = false)
        val description: String
) {
    constructor() : this(name = "", icon = "", description = "")
}