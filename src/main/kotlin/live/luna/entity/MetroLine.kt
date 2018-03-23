package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "metro_line")
data class MetroLine(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @Column(name = "name", nullable = false, unique = true)
        val name: String,

        @Column(name = "color", nullable = false)
        val color: String
) {
    constructor() : this(name = "", color = "")
}