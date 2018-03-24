package live.luna.graphql_object

import live.luna.Argument
import live.luna.GraphQLField
import live.luna.GraphQLObject
import live.luna.entity.Master
import live.luna.service.MasterService

lateinit var masterService: MasterService

@GraphQLObject
class Query {
    @GraphQLField(nullable = true)
    fun master(@Argument("id") id: Long): Master? {
        return masterService.getById(id)
    }
}