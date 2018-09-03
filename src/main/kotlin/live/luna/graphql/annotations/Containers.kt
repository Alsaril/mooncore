package live.luna.graphql.annotations

import graphql.Scalars
import graphql.schema.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

data class InputTypeWrapper(val type: GraphQLInputType, val creator: InputObjectCreator? = null)

class ProcessorContext(private val knownInputTypes: MutableMap<Klass, InputTypeWrapper> = mutableMapOf(),
                       private val processingInputTypes: MutableSet<Klass> = mutableSetOf(),
                       private val knownOutputTypes: MutableMap<Klass, GraphQLOutputType> = mutableMapOf(),
                       private val processingOutputTypes: MutableMap<Klass, String> = mutableMapOf(),
                       private val processingInterfaces: MutableMap<Klass, String> = mutableMapOf(),
                       private val implementations: Map<Klass, List<Klass>>) {
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
        knownInputTypes[Long::class.java] = InputTypeWrapper(Scalars.GraphQLID) { _, value ->
            (value as String).toLong()
        }
        knownInputTypes[java.lang.Long::class.java] = InputTypeWrapper(Scalars.GraphQLID)
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
        knownOutputTypes[Long::class.java] = Scalars.GraphQLID
        knownOutputTypes[java.lang.Long::class.java] = Scalars.GraphQLID
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

    fun setInputAsProcessing(klass: Klass) {
        processingInputTypes.add(klass)
    }

    fun setInputAsKnown(klass: Klass, type: GraphQLInputType, creator: InputObjectCreator) {
        processingInputTypes.remove(klass)
        knownInputTypes[klass] = InputTypeWrapper(type, creator)
    }

    fun isProcessingInput(klass: Klass) = processingInputTypes.contains(klass)

    fun getOutputType(klass: Klass, processor: (Klass, ProcessorContext) -> GraphQLType): GraphQLOutputType? = knownOutputTypes[klass]
            ?: processingOutputTypes[klass]?.let { GraphQLTypeReference(it) }
            ?: processor.invoke(klass, this) as? GraphQLOutputType?
            ?: throw GraphQLSchemaBuilderException("Unexpected behaviour: cannot create type for ${klass.name}\"")

    fun setOutputTypeAsProcessing(klass: Klass, name: String) {
        processingOutputTypes[klass] = name
    }

    fun setOutputTypeAsKnown(klass: Klass, type: GraphQLOutputType) {
        processingOutputTypes.remove(klass)
        knownOutputTypes[klass] = type
    }

    fun setInterfaceAsProcessing(klass: Klass, name: String) {
        processingInterfaces[klass] = name
    }

    fun getInterface(klass: Klass, processor: (Klass, ProcessorContext) -> GraphQLTypeReference) =
            processingInterfaces[klass]?.let { GraphQLTypeReference(it) }
                    ?: processor.invoke(klass, this) as GraphQLTypeReference?
            ?: throw GraphQLSchemaBuilderException("Unexpected behaviour: cannot create type for ${klass.name}\"")

    fun getImplementations(klass: Klass) = implementations[klass] ?: listOf()

    fun getInterfaces() = processingInterfaces
}

internal data class MethodSignatureHolder(val arguments: List<GraphQLArgument>, val argumentInjectors: List<(EnvironmentWrapper) -> Any?>)

internal class EnvironmentWrapper(val environment: DataFetchingEnvironment, klass: Klass) {
    val source: Any = environment.getSource() ?: klass.getDeclaredConstructor().newInstance()
    val context: Any = environment.getContext()
            ?: throw NullPointerException("Context hasn't been set, but is requested")

    operator fun invoke(name: String): Any? = environment.getArgument<Any?>(name)
}


data class InputFieldWrapper(val field: GraphQLInputObjectField, val creator: Pair<String, InputObjectCreator?>)