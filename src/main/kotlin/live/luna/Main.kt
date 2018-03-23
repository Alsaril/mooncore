package live.luna

import graphql.GraphQL
import graphql.Scalars.GraphQLLong
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema
import live.luna.entity.Address
import live.luna.graphql_object.addressObject
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

        val objectType = newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("address")
                        .type(addressObject)
                        .argument(GraphQLArgument.newArgument().name("id").type(GraphQLNonNull(GraphQLLong)))
                        .dataFetcher { null })
                .build()


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


    }
}