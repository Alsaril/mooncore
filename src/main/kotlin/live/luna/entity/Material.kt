package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "material")
data class Material(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @Column(name = "firm", nullable = false, columnDefinition = "TEXT")
        val firm: String,

        @Column(name = "description", nullable = false, columnDefinition = "TEXT")
        val description: String

) {
    constructor() : this(firm = "", description = "")
}