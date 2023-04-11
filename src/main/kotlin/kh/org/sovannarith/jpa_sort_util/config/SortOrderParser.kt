package kh.org.sovannarith.jpa_sort_util.config

import kh.org.sovannarith.jpa_sort_util.helper.Extension.notOnlyDots
import org.springframework.data.domain.Sort
import org.springframework.util.StringUtils
import java.util.*
import java.util.function.Consumer

open class SortOrderParser(
    private val elements: Array<String?>,
    lastIndex: Int = elements.size,
    private val direction: Optional<Sort.Direction> = Optional.empty(),
    private val ignoreCase: Optional<Boolean> = Optional.empty()
) {
    private val lastIndex: Int

    init {
        this.lastIndex = 0.coerceAtLeast(lastIndex)
    }

    /**
     * Parse the `ignoreCase` portion of the sort specification.
     *
     * @return a new parsing state object.
     */
    open fun parseIgnoreCase(): SortOrderParser {
        val ignoreCase = if (lastIndex > 0) fromOptionalString(elements[lastIndex - 1] ?: "") else Optional.empty()
        return SortOrderParser(elements, lastIndex - if (ignoreCase.isPresent) 1 else 0, direction, ignoreCase)
    }

    /**
     * Parse the Order portion of the sort specification.
     *
     * @return a new parsing state object.
     */
    open fun parseDirection(): SortOrderParser {
        val direction = if (lastIndex > 0) Sort.Direction.fromOptionalString(
            elements[lastIndex - 1] ?: ""
        ) else Optional.empty()
        return SortOrderParser(elements, lastIndex - if (direction.isPresent) 1 else 0, direction, ignoreCase)
    }

    /**
     * Notify a [callback function][Consumer] for each parsed Order object.
     *
     * @param callback block to be executed.
     */
    open fun forEachOrder(callback: Consumer<in Sort.Order>?) {
        for (i in 0 until lastIndex) {
            toOrder(elements[i] ?: "").ifPresent(callback!!)
        }
    }

    open fun fromOptionalString(value: String): Optional<Boolean> {
        return if (IGNORECASE.equals(value, ignoreCase = true)) Optional.of(true) else Optional.empty()
    }

    open fun toOrder(property: String): Optional<Sort.Order> {
        if (!StringUtils.hasText(property)) {
            return Optional.empty()
        }
        val order = direction.map { it: Sort.Direction? ->
            Sort.Order(
                it,
                property
            )
        }.orElseGet {
            Sort.Order.by(
                property
            )
        }
        return if (ignoreCase.isPresent) {
            Optional.of(order.ignoreCase())
        } else Optional.of(order)
    }

    companion object {
        private const val IGNORECASE = "ignorecase"

        /**
         * Parse the raw sort string delimited by `delimiter`.
         *
         * @param part sort part to parse.
         * @param delimiter the delimiter to be used to split up the source elements, will never be null.
         * @return the parsing state object.
         */
        fun parse(part: String, delimiter: String): SortOrderParser {
            val elements = Arrays.stream(part.split(delimiter.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()) //
                .filter { source: String? ->
                    notOnlyDots(
                        source!!
                    )
                }.toArray { size -> arrayOfNulls<String>(size) }
            return SortOrderParser(elements)
        }
    }
}