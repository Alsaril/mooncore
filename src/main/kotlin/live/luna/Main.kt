package live.luna

import graphql.GraphQL
import live.luna.auth.AuthHelper
import live.luna.auth.AuthInterceptor
import live.luna.entity.AddressMetro
import live.luna.entity.MetroStation
import live.luna.entity.ServiceType
import live.luna.graphql.Mutation
import live.luna.graphql.Query
import live.luna.graphql.annotations.buildSchema
import live.luna.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class Application
@Autowired
constructor(private val userService: UserService,
            private val authHelper: AuthHelper,
            private val serviceTypeService: ServiceTypeService,
            private val addressService: AddressService,
            private val addressMetrosService: AddressMetroService,
            private val stationsService: MetroStationService,
            private val linesService: MetroLineService) {

    @PostConstruct
    fun init() {
        graphQL = GraphQL.newGraphQL(buildSchema(Query::class.java, Mutation::class.java)).build()
        live.luna.auth.userService = userService
        live.luna.auth.authHelper = authHelper
        if (fillServiceTypes) {
            fillServiceTypes()
        }
    }


    private fun fillMetros() {
        val stations = ArrayList<MetroStation>()
        for (i in 0..0) {
            stations.add(stationsService.getById(i.toLong())!!)
        }


        for (i in 442..442) {
            val address = addressService.getById(i.toLong())!!

            val station = stations.random()!!

            val addressMetro = AddressMetro(address = address, station = station.name, line = station.line.name,
                    color = station.line.color, distance = Random().nextInt(8000).toDouble())

            addressMetrosService.insert(addressMetro)
        }
    }

    fun <E> List<E>.random(): E? = if (size > 0) get(Random().nextInt(size)) else null


    @Configuration
    @EnableWebMvc
    class WebMvcConfig : WebMvcConfigurer {
        override fun addInterceptors(registry: InterceptorRegistry) {
            registry.addInterceptor(AuthInterceptor())
        }

        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**")
        }
    }


    private fun fillServiceTypes() {
        serviceTypeService.apply {
            val manicure = ServiceType(name = "Маникюр", parent = null)
            val classicManicure = ServiceType(name = "Обрезной/классический", parent = manicure)
            val hardwareManicure = ServiceType(name = "Аппаратный", parent = manicure)
            val comboManicure = ServiceType(name = "Комбинированный", parent = manicure)
            val euroManicure = ServiceType(name = "Европейский", parent = manicure)

            insert(manicure)
            insert(classicManicure)
            insert(hardwareManicure)
            insert(comboManicure)
            insert(euroManicure)

            val covering = ServiceType(name = "Покрытие", parent = null)
            val shellac = ServiceType(name = "Шеллак", parent = covering)
            val nailPolish = ServiceType(name = "Лак", parent = covering)
            val french = ServiceType(name = "Френч", parent = covering)

            insert(covering)
            insert(shellac)
            insert(nailPolish)
            insert(french)

            val nailDesign = ServiceType(name = "Дизайн ногтя", parent = null)
            val simpleDrawing = ServiceType(name = "Простой рисунок", parent = nailDesign)
            val complexDrawing = ServiceType(name = "Сложный рисунок", parent = nailDesign)

            insert(nailDesign)
            insert(simpleDrawing)
            insert(complexDrawing)

            val extra = ServiceType(name = "Дополнительные услуги", parent = null)
            insert(extra)
        }
    }

    companion object {
        lateinit var graphQL: GraphQL

        // Если true, то инициализируем таблицу с типами услуг
        private var fillServiceTypes = false

        @JvmStatic
        fun main(args: Array<String>) {
            if (args.contains("fill_service_types")) {
                fillServiceTypes = true
            }
            SpringApplication.run(Application::class.java, *args)
        }
    }
}