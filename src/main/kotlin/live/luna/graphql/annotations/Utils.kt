package live.luna.graphql.annotations

typealias Klass = Class<out Any>
typealias InputObjectCreator = (String, Any?) -> Any

@Suppress("NOTHING_TO_INLINE")
inline fun String.ifEmptyThen(default: String) = ifEmptyThenNull() ?: default

@Suppress("NOTHING_TO_INLINE")
inline fun String.ifEmptyThenNull() = if (!isEmpty()) this else null