package live.luna.graphql

import live.luna.entity.Master
import live.luna.entity.User
import live.luna.service.MasterService
import live.luna.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

lateinit var masterService: MasterService
lateinit var userService: UserService

@Component
class Initter
@Autowired constructor(
        _masterService: MasterService,
        _userService: UserService
) {
    init {
        masterService = _masterService
        userService = _userService
    }
}

class UserContext(val user: User?)

@GraphQLObject
class Query {
    @GraphQLField(nullable = true)
    fun master(@Argument(name = "id") id: Long, @GraphQLContext context: UserContext): Master? {
        context.user
        return masterService.getById(id)
    }

    @GraphQLField(of = Master::class)
    fun feed(@Argument("limit") limit: Int, @Argument("offset") offset: Int): List<Master> {
        return masterService.getList(limit, offset)
    }

    @GraphQLField(of = Master::class)
    fun mastersInArea(
            @Argument("limit") limit: Int,
            @Argument("offset") offset: Int,
            @Argument("lat1") lat1: Double,
            @Argument("lon1") lon1: Double,
            @Argument("lat2") lat2: Double,
            @Argument("lon2") lon2: Double,
            @Argument("prevLat1", nullable = true) prevLat1: Double?,
            @Argument("prevLon1", nullable = true) prevLon1: Double?,
            @Argument("prevLat2", nullable = true) prevLat2: Double?,
            @Argument("prevLon2", nullable = true) prevLon2: Double?): List<Master> {

        return masterService.getInArea(limit, offset, prevLat1, prevLon1, prevLat2, prevLon2, lat1, lon1, lat2, lon2)
    }

    /*@GraphQLUnion(nullable = true, types = [Master::class, Client::class])
    fun viewer(@GraphQLContext context: UserContext): Any? {
        return null
    }*/
}

@GraphQLObject
class Mutation {
    @GraphQLField
    fun createUser(@Argument("email") email: String,
                   @Argument("password") password: String,
                   @Argument("name") name: String,
                   @Argument("role") role: Int): User? {
        return userService.createUser(email, password, name, role)
    }

    @GraphQLField
    fun token(@Argument("email") email: String,
              @Argument("password") password: String): String? {
        return userService.token(email, password)
    }

}