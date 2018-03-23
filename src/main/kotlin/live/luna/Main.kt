package live.luna

import graphql.GraphQL
import graphql.Scalars.*
import graphql.schema.*
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import live.luna.entity.Address
import live.luna.service.AddressService
import org.jetbrains.annotations.Nullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct


@SpringBootApplication
class Application {

    @Autowired
    lateinit var addressService: AddressService

    @PostConstruct
    fun init() {
        val wiring = RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("address") { addressService.getById(it.getArgument("id")) })
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createAddress") {
                            val lat = it.getArgument<String>("lat").toDouble()
                            val lon = it.getArgument<String>("lon").toDouble()
                            addressService.save(Address(lat = lat, lon = lon, description = "descr"))
                        })
                .type(newTypeWiring("Address")
                        .dataFetcher("id") { it.getSource<Address>().id }
                        .dataFetcher("lat") { (it.getSource() as Address).lat }
                        .dataFetcher("lon") { (it.getSource() as Address).lon }
                        .dataFetcher("description") { (it.getSource() as Address).description })
                .build()

        val addressObject = newObject()
                .name("Address")
                .description("Address object")
                .field(newFieldDefinition()
                        .name("id")
                        .type(GraphQLLong)
                        .dataFetcher { null })
                .field(newFieldDefinition()
                        .name("lat")
                        .type(GraphQLFloat)
                        .dataFetcher { it.getSource<Address>().lat })
                .field(newFieldDefinition()
                        .name("lon")
                        .type(GraphQLFloat)
                        .dataFetcher { it.getSource<Address>().lon })
                .field(newFieldDefinition()
                        .name("description")
                        .type(GraphQLString)
                        .dataFetcher { it.getSource<Address>().description })

        /*

            val addressObject = newObject("Address", "Address object", Address::class) {
                field("id", GraphQLLong, "descr") { id }
                field("id", GraphQLLong) {
                    required("id", GraphQLLong)
                    optional("id", GraphQLLong)
                } { id }
            }

         */

        val objectType = newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("address")
                        .type(addressObject)
                        .argument(GraphQLArgument.newArgument().name("id").type(GraphQLNonNull(GraphQLLong)))
                        .dataFetcher { null })
                .build()

        val obj = Companion.newObject<Address>("Address") {
            notNull("id", GraphQLBigInteger) { id }
            notNull("lat", GraphQLFloat) { lat }
            notNull("lon", GraphQLFloat) { lon }
            notNull("description", GraphQLString) { description }
        }

        val cobj = Companion.newObject<Client>("Client") {
            notNull("address", obj) { address }
        }

        val query = Companion.newObject<Any>("Query") {
            notNull("address", obj) {
            }
        }

        val schema = GraphQLSchema.newSchema().query(objectType).mutation(objectType).build()
        graphQL = GraphQL.newGraphQL(schema).build()
    }

    companion object {
        @Nullable
        lateinit var graphQL: GraphQL

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }

        class Client(val address: Address)

        class FieldBuilder<out T> {
            val fields = mutableListOf<GraphQLFieldDefinition.Builder>()

            fun <F> notNull(name: String, type: GraphQLOutputType, description: String? = null, dataFetcher: T.() -> F) {
                nullable(name, GraphQLNonNull(type), description, dataFetcher)
            }

            fun <F> nullable(name: String, type: GraphQLOutputType, description: String? = null, dataFetcher: T.() -> F) {
                val field = GraphQLFieldDefinition.Builder().name(name).type(type).dataFetcher { dataFetcher.invoke(it.getSource<T>()) }
                description?.let { field.description(description) }
                fields.add(field)
            }
        }

        fun <T> newObject(name: String, description: String? = null, fieldCreator: FieldBuilder<T>.() -> Unit): GraphQLOutputType {
            val objectType = newObject().name(name)
            description?.let { objectType.description(it) }
            val fieldBuilder = FieldBuilder<T>()
            fieldCreator.invoke(fieldBuilder)
            fieldBuilder.fields.forEach { objectType.field(it) }
            return objectType.build()
        }
    }
}