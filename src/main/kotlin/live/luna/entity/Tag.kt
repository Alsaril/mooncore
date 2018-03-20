package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "tag")
data class Tag(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID", nullable = false)
        val id: Long = 0,

        @Column(name = "name", nullable = false)
        val name: String

) {
    constructor() : this(name = "")
}