package live.luna.graphql.annotations

import java.util.function.Supplier
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class GraphQLObject(
        val name: String = "",
        val description: String = ""
)

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class GraphQLField(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false,
        val of: KClass<*> = Void::class,
        val ofNullable: Boolean = false
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLArgument(
        val name: String,
        val description: String = "",
        val nullable: Boolean = false,
        val default: KClass<out Supplier<out Any>> = DefaultSupplier::class
)

@Target(AnnotationTarget.CONSTRUCTOR)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class GraphQLInputObject(
        val name: String = "",
        val description: String = ""
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLInputField(
        val name: String,
        val description: String = "",
        val nullable: Boolean = false,
        val of: KClass<*> = Void::class,
        val ofNullable: Boolean = false
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLContext

@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class GraphQLUnion(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false,
        val types: Array<KClass<out Any>> = []
)

private class DefaultSupplier : Supplier<Unit> {
    override fun get() = Unit
}

enum class GraphQLModifier {
    NOT_NULL, LIST
}