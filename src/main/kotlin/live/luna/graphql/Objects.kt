package live.luna.graphql

import live.luna.entity.Master
import live.luna.service.MasterService

lateinit var masterService: MasterService

@GraphQLObject
class Query {
    @GraphQLField(of = Master::class)
    fun feed(@Argument("limit") limit: Int, @Argument("offset") offset: Int): List<Master> {
        return masterService.getList(limit, offset)
    }

    @GraphQLField(nullable = true)
    fun master(@Argument("id") id: Long): Master? {
        return masterService.getById(id)
    }
}