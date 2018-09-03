package live.luna.entity

import com.alsaril.graphql.GraphQLField
import com.alsaril.graphql.GraphQLInputField
import com.alsaril.graphql.GraphQLInputObject
import com.alsaril.graphql.GraphQLObject
import javax.persistence.*

@Entity
@Table(name = "material")
@GraphQLObject
data class Material(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        @GraphQLField
        val id: Long = 0,

        @Column(name = "firm", nullable = false, columnDefinition = "TEXT")
        @GraphQLField
        val firm: String,

        @Column(name = "description", nullable = false, columnDefinition = "TEXT")
        @GraphQLField
        val description: String

) {
    @GraphQLInputObject(name = "MaterialInput")
    constructor(
            @GraphQLInputField(name = "firm", nullable = true) firm: String?,
            @GraphQLInputField(name = "description") description: String
    ) : this(firm = firm ?: "", description = description)

    constructor() : this(firm = "", description = "")
}