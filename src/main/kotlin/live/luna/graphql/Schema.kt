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
lateinit var salonService: SalonService
lateinit var serviceTypeService: ServiceTypeService
lateinit var serviceService: ServiceService
lateinit var scheduleService: ScheduleService
lateinit var reviewService: ReviewService

@Component
class Initter
@Autowired constructor(
        _masterService: MasterService,
        _userService: UserService,
        _salonService: SalonService,
        _serviceTypeService: ServiceTypeService,
        _serviceService: ServiceService,
        _clientService: ClientService,
        _scheduleService: ScheduleService,
        _reviewService: ReviewService
) {
    init {
        masterService = _masterService
        userService = _userService
        salonService = _salonService
        serviceTypeService = _serviceTypeService
        serviceService = _serviceService
        clientService = _clientService
        scheduleService = _scheduleService
        reviewService = _reviewService
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

@GraphQLInterface
class FeedItem(
        @GraphQLField
        val id: Long,
        @GraphQLField(nullable = true)
        val name: String?,
        @GraphQLField(nullable = true)
        val address: Address?,
        @GraphQLField(nullable = true)
        val avatar: Photo?,
        @GraphQLListField(type = Photo::class)
        val photos: List<Photo>,
        @GraphQLField
        val stars: Int,
        @GraphQLListField(type = Sign::class)
        val signs: List<Sign>,
        @GraphQLListField(type = Service::class)
        val services: List<Service>
)

@GraphQLObject
class Query {
    @GraphQLField(nullable = true)
    fun master(@GraphQLArgument(name = "id") id: Long): Master? {
        return masterService.getById(id)
    }

    @GraphQLField(nullable = true)
    fun salon(
            @GraphQLArgument(name = "id") id: Long,
            @GraphQLListArgument("service_types",
                    type = Long::class,
                    nullable = true) serviceTypes: List<Long>?): Salon? {
        return salonService.getById(id, serviceTypes)
    }

    @GraphQLListField(type = FeedItem::class)
    fun feed(@GraphQLArgument("limit") limit: Limit,
             @GraphQLArgument("area", nullable = true) area: Area?,
             @GraphQLArgument("prevArea", nullable = true) prevArea: Area?,
             @GraphQLListArgument("service_types",
                     type = Long::class,
                     nullable = true) serviceTypes: List<Long>?): List<Any> {

        // TODO exclude masters from the feed if they are already inside salon
        // TODO think about feed algorithm and remove shuffle

        val resultList = ArrayList<Any>()
        resultList.addAll(salonService.getList(limit, area, prevArea, serviceTypes).filter { it.photos().isNotEmpty() })
        resultList.addAll(
                masterService
                        .getList(limit, area, prevArea, serviceTypes)
                        .filter { it.salon == null && it.avatar != null && it.photos.isNotEmpty() }
                        .take(75)
        )

        if (limit.offset >= resultList.size) {
            return emptyList()
        }
        return resultList.shuffled().subList(limit.offset, resultList.size).take(limit.limit)
    }

    @GraphQLListField(type = ServiceType::class)
    fun serviceTypes(): List<ServiceType> {
        return serviceTypeService.getAll()
    }

    @GraphQLListField(type = Seance::class)
    fun clientSeances(@GraphQLContext context: UserContext): List<Seance> {
        return clientService.getClientSeances(context)
    }

    @GraphQLListField(type = ScheduleService.Period::class)
    fun masterFreeTime(
            @GraphQLArgument(name = "master_id") masterId: Long,
            @GraphQLArgument(name = "days") days: Int): List<ScheduleService.Period> {
        return scheduleService.getMasterFreeTime(masterId, days)
    }
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

    @GraphQLField(nullable = true)
    fun addReview(
            @GraphQLArgument(name = "master_id") masterId: Long,
            @GraphQLArgument(name = "seance_id") seanceId: Long,
            @GraphQLArgument(name = "stars") stars: Int,
            @GraphQLArgument(name = "message", nullable = true) message: String,
            @GraphQLContext context: UserContext): Review? {
        return reviewService.addReview(masterId, seanceId, stars, message, context)
    }
}