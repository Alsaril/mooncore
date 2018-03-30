package live.luna.graphql

import live.luna.entity.Master
import live.luna.entity.User
import live.luna.graphql.annotations.*
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

class Limit
@GraphQLInputObject
constructor(
        @GraphQLInputField("offset")
        var offset: Int,
        @GraphQLInputField("limit")
        var limit: Int
)


class Point
@GraphQLInputObject
constructor(
        @GraphQLInputField("lat")
        var lat: Double,
        @GraphQLInputField("lon")
        var lon: Double
)

class Area
@GraphQLInputObject
constructor(
        @GraphQLInputField("from")
        var from: Point,
        @GraphQLInputField("to")
        var to: Point
)

@GraphQLObject
class Query {
    @GraphQLField(nullable = true)
    fun master(@GraphQLArgument(name = "id") id: Long, @GraphQLContext context: UserContext): Master? {
        return masterService.getById(id)
    }

    @GraphQLField(of = Master::class)
    fun feed(@GraphQLArgument("limit") limit: Limit,
             @GraphQLArgument("area", nullable = true) area: Area?,
             @GraphQLArgument("prevArea", nullable = true) prevArea: Area?): List<Master> {
        return masterService.getList(limit, area, prevArea)
    }

    /*@GraphQLUnion(nullable = true, types = [Master::class, Client::class])
    fun viewer(@GraphQLContext context: UserContext): Any? {
        return null
    }*/
}

@GraphQLObject
class Mutation {
    @GraphQLField
    fun createUser(@GraphQLArgument("email") email: String,
                   @GraphQLArgument("password") password: String,
                   @GraphQLArgument("name") name: String,
                   @GraphQLArgument("role") role: Int): User? {
        return userService.createUser(email, password, name, role)
    }

    @GraphQLField
    fun token(@GraphQLArgument("email") email: String,
              @GraphQLArgument("password") password: String): String? {
        return userService.token(email, password)
    }

}