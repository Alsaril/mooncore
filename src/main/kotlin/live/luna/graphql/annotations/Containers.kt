package live.luna.graphql.annotations

import graphql.Scalars
import graphql.schema.*
import graphql.schema.GraphQLArgument
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

data class InputTypeWrapper(val type: GraphQLInputType, val creator: InputObjectCreator? = null)

class ProcessorContext(private val knownInputTypes: MutableMap<Klass, InputTypeWrapper> = mutableMapOf(),
                       private val processingInputTypes: MutableSet<Klass> = mutableSetOf(),
                       private val knownOutputTypes: MutableMap<Klass, GraphQLOutputType> = mutableMapOf(),
                       private val processingOutputTypes: MutableMap<Klass, String> = mutableMapOf()) {
    init {
        knownInputTypes[Boolean::class.java] = InputTypeWrapper(Scalars.GraphQLBoolean)
        knownInputTypes[java.lang.Boolean::class.java] = InputTypeWrapper(Scalars.GraphQLBoolean)
        knownInputTypes[Byte::class.java] = InputTypeWrapper(Scalars.GraphQLByte)
        knownInputTypes[java.lang.Byte::class.java] = InputTypeWrapper(Scalars.GraphQLByte)
        knownInputTypes[Short::class.java] = InputTypeWrapper(Scalars.GraphQLShort)
        knownInputTypes[java.lang.Short::class.java] = InputTypeWrapper(Scalars.GraphQLShort)
        knownInputTypes[Char::class.java] = InputTypeWrapper(Scalars.GraphQLChar)
        knownInputTypes[java.lang.Character::class.java] = InputTypeWrapper(Scalars.GraphQLChar)
        knownInputTypes[Int::class.java] = InputTypeWrapper(Scalars.GraphQLInt)
        knownInputTypes[java.lang.Integer::class.java] = InputTypeWrapper(Scalars.GraphQLInt)
        knownInputTypes[Long::class.java] = InputTypeWrapper(Scalars.GraphQLLong)
        knownInputTypes[java.lang.Long::class.java] = InputTypeWrapper(Scalars.GraphQLLong)
        knownInputTypes[Float::class.java] = InputTypeWrapper(Scalars.GraphQLFloat)
        knownInputTypes[java.lang.Float::class.java] = InputTypeWrapper(Scalars.GraphQLFloat)
        knownInputTypes[Double::class.java] = InputTypeWrapper(Scalars.GraphQLFloat)
        knownInputTypes[java.lang.Double::class.java] = InputTypeWrapper(Scalars.GraphQLFloat)
        knownInputTypes[String::class.java] = InputTypeWrapper(Scalars.GraphQLString)
        knownInputTypes[BigDecimal::class.java] = InputTypeWrapper(Scalars.GraphQLBigDecimal)
        knownInputTypes[BigInteger::class.java] = InputTypeWrapper(Scalars.GraphQLBigInteger)

        knownOutputTypes[Boolean::class.java] = Scalars.GraphQLBoolean
        knownOutputTypes[java.lang.Boolean::class.java] = Scalars.GraphQLBoolean
        knownOutputTypes[Byte::class.java] = Scalars.GraphQLByte
        knownOutputTypes[java.lang.Byte::class.java] = Scalars.GraphQLByte
        knownOutputTypes[Short::class.java] = Scalars.GraphQLShort
        knownOutputTypes[java.lang.Short::class.java] = Scalars.GraphQLShort
        knownOutputTypes[Char::class.java] = Scalars.GraphQLChar
        knownOutputTypes[java.lang.Character::class.java] = Scalars.GraphQLChar
        knownOutputTypes[Int::class.java] = Scalars.GraphQLInt
        knownOutputTypes[java.lang.Integer::class.java] = Scalars.GraphQLInt
        knownOutputTypes[Long::class.java] = Scalars.GraphQLLong
        knownOutputTypes[java.lang.Long::class.java] = Scalars.GraphQLLong
        knownOutputTypes[Float::class.java] = Scalars.GraphQLFloat
        knownOutputTypes[java.lang.Float::class.java] = Scalars.GraphQLFloat
        knownOutputTypes[Double::class.java] = Scalars.GraphQLFloat
        knownOutputTypes[java.lang.Double::class.java] = Scalars.GraphQLFloat
        knownOutputTypes[String::class.java] = Scalars.GraphQLString
        knownOutputTypes[BigDecimal::class.java] = Scalars.GraphQLBigDecimal
        knownOutputTypes[BigInteger::class.java] = Scalars.GraphQLBigInteger
        knownOutputTypes[Date::class.java] = Scalars.GraphQLString
    }

    fun getInputType(klass: Klass): InputTypeWrapper? = knownInputTypes[klass]

    fun processInput(klass: Klass) {
        processingInputTypes.add(klass)
    }

    fun knowInput(klass: Klass, type: GraphQLInputType, creator: InputObjectCreator) {
        processingInputTypes.remove(klass)
        knownInputTypes[klass] = InputTypeWrapper(type, creator)
    }

    fun isProcessingInput(klass: Klass) = processingInputTypes.contains(klass)

    fun getOutputType(klass: Klass): GraphQLOutputType? = knownOutputTypes[klass]
            ?: processingOutputTypes[klass]?.let { GraphQLTypeReference(it) }

    fun processOutput(klass: Klass, name: String) {
        processingOutputTypes[klass] = name
    }

    fun knowOutput(klass: Klass, type: GraphQLOutputType) {
        processingOutputTypes.remove(klass)
        knownOutputTypes[klass] = type
    }
}

internal data class MethodSignatureHolder(val arguments: List<GraphQLArgument>, val argumentInjectors: List<(EnvironmentWrapper) -> Any?>)

internal class EnvironmentWrapper(val environment: DataFetchingEnvironment, klass: Klass) {
    val source: Any = environment.getSource() ?: klass.newInstance()
    val context: Any = environment.getContext()
            ?: throw NullPointerException("Context hasn't been set, but is requested")

    operator fun invoke(name: String): Any? = environment.getArgument<Any?>(name)
}


data class InputFieldWrapper(val field: GraphQLInputObjectField, val creator: Pair<String, InputObjectCreator?>)