package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "photo")
data class Photo(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID", nullable = false)
        val id: Long = 0,

        @Column(name = "path", nullable = false)
        val path: String
) {
    constructor() : this(path = "")
}