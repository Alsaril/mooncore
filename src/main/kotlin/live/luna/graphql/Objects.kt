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

    @GraphQLField(nullable = true)
    fun masterByEmail(@Argument("email") email: String): Master? {
        return masterService.getByEmail(email)
    }
}