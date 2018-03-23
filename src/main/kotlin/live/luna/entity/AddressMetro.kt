package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "address_metro")
data class AddressMetro(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "address_id", nullable = false)
        val address: Address,

        @Column(name = "station", nullable = false)
        val station: String,

        @Column(name = "line", nullable = false)
        val line: String,

        @Column(name = "color", nullable = false)
        val color: String,

        @Column(name = "distance", nullable = false)
        val distance: Double

) {
    constructor() : this(address = Address(), station = "", line = "", color = "", distance = 0.0)
}