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

    @GraphQLField(of = Master::class)
    fun mastersInArea(
            @Argument("limit") limit: Int, @Argument("offset") offset: Int,
            @Argument("x1") x1: Double, @Argument("x2") x2: Double,
            @Argument("y1") y1: Double, @Argument("y2") y2: Double): List<Master> {
        return masterService.getInArea(limit, offset, x1, y1, x2, y2)
    }
}