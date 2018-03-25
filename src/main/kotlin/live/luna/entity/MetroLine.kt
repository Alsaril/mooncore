package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "metro_line")
data class MetroLine(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Int = 0,

        @Column(name = "name", nullable = false, unique = true)
        val name: String,

        @Column(name = "color", nullable = false)
        val color: String,

        @OneToMany(mappedBy = "line", cascade = [(CascadeType.ALL)])
        val stations: List<MetroStation>
) {
    constructor() : this(name = "", color = "", stations = ArrayList())
}