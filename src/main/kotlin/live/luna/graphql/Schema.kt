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