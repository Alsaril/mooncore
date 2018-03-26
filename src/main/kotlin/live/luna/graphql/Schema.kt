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
            @Argument("x1") x1: Double,
            @Argument("y1") y1: Double,
            @Argument("x2") x2: Double,
            @Argument("y2") y2: Double,
            @Argument("prevX1", nullable = true) prevX1: Double?,
            @Argument("prevY1", nullable = true) prevY1: Double?,
            @Argument("prevX2", nullable = true) prevX2: Double?,
            @Argument("prevY2", nullable = true) prevY2: Double?): List<Master> {

        return masterService.getInArea(limit, offset, prevX1, prevY1, prevX2, prevY2, x1, y1, x2, y2)
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