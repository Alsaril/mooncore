package live.luna.entity

import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject

@GraphQLObject
class SignGroup(
        @GraphQLField
        val sign: Sign,

        @GraphQLField
        val count: Int
)