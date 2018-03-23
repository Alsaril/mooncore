package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "salon")
data class Salon(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        val name: String,

        @ManyToOne
        @JoinColumn(name = "address_id", nullable = false)
        val address: Address,

        @OneToOne
        @JoinColumn(name = "photo_id", nullable = false)
        val photo: Photo,

        @Column(name = "stars", nullable = false)
        val stars: Int = 0

) {
    constructor() : this(address = Address(), photo = Photo(), name = "")
}