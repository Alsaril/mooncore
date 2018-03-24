package live.luna.entity

import javax.persistence.*

@Entity
@Table(name = "photo")
data class Photo(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Long = 0,

        @Column(name = "path", nullable = false)
        val path: String,

        @ManyToMany(cascade = [(CascadeType.ALL)])
        @JoinTable(
                name = "photo_tag",
                joinColumns = [(JoinColumn(name = "photo_id"))],
                inverseJoinColumns = [(JoinColumn(name = "tag_id"))]
        )
        val tags: Set<Tag>
) {
    constructor() : this(path = "", tags = HashSet())
}