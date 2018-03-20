package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "master")
data class Master(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID", nullable = false)
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        val name: String,

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        val user: User,

        @ManyToOne
        @JoinColumn(name = "address_id", nullable = false)
        val address: Address,

        @ManyToOne
        @JoinColumn(name = "photo_id", nullable = false)
        val photo: Photo,

        @ManyToOne
        @JoinColumn(name = "salon_id", nullable = false)
        val salon: Salon,

        @Column(name = "stars", nullable = false)
        val stars: Int = 0

) {
    constructor() : this(name = "", user = User(), address = Address(), photo = Photo(), salon = Salon())
}