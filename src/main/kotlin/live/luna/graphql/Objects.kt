package live.luna.graphql

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