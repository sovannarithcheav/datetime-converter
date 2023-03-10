package kh.org.soramitsu.jpa_sort_util.annotation

interface JpaSortMappingResource<T> {

    fun getSource(): MutableMap<String, String>

    fun getInternalSortKeyName(name: String): String? {
        val key = this.getSource()[name]
        return if (key == "_") name else key
    }
}