package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "address")
data class Address(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @Column(name = "lat", nullable = false)
        val lat: Double,

        @Column(name = "lon", nullable = false)
        val lon: Double,

        @Column(name = "description", nullable = false)
        val description: String,

        @OneToMany(mappedBy = "address", cascade = [(CascadeType.ALL)])
        val metros: List<AddressMetro>
) {
    constructor() : this(lat = 0.0, lon = 0.0, description = "", metros = ArrayList())
}