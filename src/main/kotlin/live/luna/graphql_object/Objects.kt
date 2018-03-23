package live.luna.graphql_object

import graphql.Scalars.*
import graphql.schema.GraphQLList
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLOutputType
import live.luna.entity.*

val ID: GraphQLOutputType = GraphQLLong
fun graphQLListOfNonNull(type: GraphQLOutputType) = GraphQLList(GraphQLNonNull(type))

val addressMetroObject = newObject<AddressMetro>("AddressMetro") {
    (notNull("id", ID) {}) { it("id") }
    notNull("station", GraphQLString) { station }
    notNull("line", GraphQLString) { line }
    notNull("color", GraphQLString) { color }
    notNull("distance", GraphQLFloat) { distance }
}

val addressObject = newObject<Address>("Address") {
    notNull("id", ID) { id }
    notNull("lat", GraphQLFloat) { lat }
    notNull("lon", GraphQLFloat) { lon }
    notNull("description", GraphQLString) { description }
    //notNull("metro", graphQLListOfNonNull(addressMetroObject)) { addressMetro }
}

val tagObject = newObject<Tag>("Tag") {
    notNull("id", ID) { id }
    notNull("name", GraphQLString) { name }
}

val photoObject = newObject<Photo>("Photo") {
    notNull("id", ID) { id }
    notNull("path", GraphQLString) { path }
    notNull("tags", graphQLListOfNonNull(tagObject)) { tags }
}

val userObject = newObject<User>("User") {
    notNull("id", ID) { id }
    notNull("role", GraphQLInt) { role }
    notNull("ctime", GraphQLString) { ctime }
}

val clientObject = newObject<Client>("Client") {
    notNull("id", ID) { id }
    notNull("name", GraphQLString) { name }
    notNull("user", userObject) { user }
    notNull("photo", photoObject) { photo }
}

val salonObject = newObject<Salon>("Salon") {
    notNull("id", ID) { id }
    notNull("name", GraphQLString) { name }
    notNull("address", addressObject) { address }
    notNull("photo", photoObject) { photo }
    notNull("stars", GraphQLInt) { stars }
}

val signObject = newObject<Sign>("Sign") {
    notNull("id", ID) { id }
    notNull("icon", GraphQLString) { icon }
    notNull("description", GraphQLString) { description }
}

val masterObject = newObject<Master>("Master") {
    notNull("id", ID) { id }
    notNull("name", GraphQLString) { name }
    notNull("user", userObject) { user }
    notNull("address", addressObject) { address }
    notNull("photo", photoObject) { photo }
    nullable("salon", salonObject) { salon }
    notNull("stars", GraphQLInt) { stars }
    notNull("signs", graphQLListOfNonNull(signObject)) { signs }
    notNull("photos", graphQLListOfNonNull(photoObject)) { photos }
}

val serviceObject = newObject<Service>("Service") {
    notNull("id", ID) { id }
    notNull("name", GraphQLString) { name }
    notNull("master", masterObject) { master }
    notNull("stars", GraphQLInt) { stars }
    notNull("description", GraphQLString) { description }
    notNull("ctime", GraphQLString) { ctime }
}
