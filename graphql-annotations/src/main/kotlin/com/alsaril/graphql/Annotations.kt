package com.alsaril.graphql

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Retention
annotation class GraphQLObject(
        val name: String = "",
        val description: String = "",
        val implements: Array<KClass<*>> = []
)

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Retention
annotation class GraphQLInterface(
        val name: String = "",
        val description: String = ""
)

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class GraphQLField(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false
)

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class GraphQLListField(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false,
        val depth: Int = 1,
        val type: KClass<*>
)

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class S(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false,
        val depth: Int = 1,
        val type: KClass<*>
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLArgument(
        val name: String,
        val description: String = "",
        val nullable: Boolean = false
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLListArgument(
        val name: String,
        val description: String = "",
        val nullable: Boolean = false,
        val depth: Int = 1,
        val type: KClass<*>
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