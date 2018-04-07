package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "metro_station")
data class MetroStation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Int = 0,

        @Column(name = "name", nullable = false)
        val name: String,

        @Column(name = "lat", nullable = false)
        val lat: Double,

        @Column(name = "lon", nullable = false)
        val lon: Double,

        @ManyToOne
        @JoinColumn(name = "line_id", nullable = false)
        val line: MetroLine

) {
    constructor() : this(name = "", lat = 0.0, lon = 0.0, line = MetroLine())
}