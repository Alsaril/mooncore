package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLListField
import live.luna.graphql.annotations.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "photo")
@GraphQLObject
data class Photo(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "path", nullable = false)
        @GraphQLField
        val path: String,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "photo_tag",
                joinColumns = [JoinColumn(name = "photo_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_id")]
        )
        @GraphQLListField(type = Tag::class)
        val tags: List<Tag>
) {
        constructor() : this(path = "", tags = ArrayList())
}