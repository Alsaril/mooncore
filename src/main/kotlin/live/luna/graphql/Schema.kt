package live.luna.graphql

import live.luna.entity.*
import live.luna.graphql.annotations.*
import live.luna.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*

lateinit var masterService: MasterService
lateinit var clientService: ClientService
lateinit var userService: UserService
lateinit var serviceTypeService: ServiceTypeService
lateinit var serviceService: ServiceService

@Component
class Initter
@Autowired constructor(
        _masterService: MasterService,
        _userService: UserService,
        _serviceTypeService: ServiceTypeService,
        _serviceService: ServiceService,
        _clientService: ClientService
) {
    init {
        masterService = _masterService
        userService = _userService
        serviceTypeService = _serviceTypeService
        serviceService = _serviceService
        clientService = _clientService
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


@GraphQLObject
class Point
@GraphQLInputObject("InputPoint")
constructor(
        @GraphQLInputField("lat")
        @GraphQLField
        var lat: Double,
        @GraphQLInputField("lon")
        @GraphQLField
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

@GraphQLInterface(implementedBy = [Master::class, Salon::class])
class FeedItem(
        @GraphQLField
        val name: String,

        @GraphQLListField(type = Photo::class)
        val photos: List<Photo>
)

@GraphQLObject
class Query {
    @GraphQLField(nullable = true)
    fun master(@GraphQLArgument(name = "id") id: Long, @GraphQLContext context: UserContext): Master? {
        return masterService.getById(id)
    }

    @GraphQLListField(type = FeedItem::class)
    fun feed(@GraphQLArgument("limit") limit: Limit,
             @GraphQLArgument("area", nullable = true) area: Area?,
             @GraphQLArgument("prevArea", nullable = true) prevArea: Area?,
             @GraphQLListArgument("service_types",
                     type = Long::class,
                     nullable = true) serviceTypes: List<Long>?): List<Any> {
        return listOf(Salon(), Master(), Master())//masterService.getList(limit, area, prevArea, serviceTypes)
    }

    @GraphQLListField(type = Point::class)
    fun test(@GraphQLArgument("count") count: Int,
             @GraphQLListArgument("array", type = Point::class) array: List<Point>): List<Point> {
        return array
    }

    @GraphQLListField(type = ServiceType::class)
    fun serviceTypes(): List<ServiceType> {
        return serviceTypeService.getAll()
    }

    /*@GraphQLUnion(nullable = true, types = [Master::class, Client::class])
    fun viewer(@GraphQLContext context: UserContext): Any? {
        return null
    }*/
}

@GraphQLObject
class Mutation {
    @GraphQLField(nullable = true)
    fun createUser(@GraphQLArgument("email") email: String,
                   @GraphQLArgument("password") password: String,
                   @GraphQLArgument("role") role: Int,
                   @GraphQLArgument("name", nullable = true) name: String?): User? {
        return userService.createUser(email, password, name, role)
    }

    @GraphQLField(nullable = true)
    fun token(@GraphQLArgument("email") email: String,
              @GraphQLArgument("password") password: String): String? {
        return userService.token(email, password)
    }


    @GraphQLField(nullable = true)
    fun updateMaster(@GraphQLArgument("master") master: Master,
                     @GraphQLContext context: UserContext): Master? {
        return masterService.updateMaster(master, context)
    }

    @GraphQLField(nullable = true)
    fun addService(@GraphQLArgument("type_id") typeId: Long,
                   @GraphQLArgument("price") price: BigDecimal,
                   @GraphQLArgument("description") description: String,
                   @GraphQLArgument("duration") duration: Long,
                   @GraphQLListArgument("materials",
                           nullable = true,
                           type = Material::class) materials: List<Material>?,
                   @GraphQLListArgument("photos",
                           nullable = true,
                           type = Photo::class) photos: List<Photo>?,
                   @GraphQLContext context: UserContext): Service? {

        return serviceService.addService(
                typeId = typeId,
                price = price,
                description = description,
                duration = duration,
                materials = materials,
                photos = photos,
                context = context)
    }

    @GraphQLField(nullable = true)
    fun removeService(@GraphQLArgument("service_id") serviceId: Long,
                      @GraphQLContext context: UserContext): Service? {
        return serviceService.removeService(serviceId, context)
    }

    @GraphQLField(nullable = true)
    fun makeAnAppointment(@GraphQLArgument(name = "master_id") masterId: Long,
                          @GraphQLListArgument(name = "services_id", type = Long::class) servicesId: List<Long>,
                          @GraphQLArgument(name = "start_time") startTime: Date,
                          @GraphQLArgument(name = "end_time") endTime: Date,
                          @GraphQLContext context: UserContext): Seance? {
        return clientService.makeAnAppointment(masterId, servicesId, startTime, endTime, context)
    }
}