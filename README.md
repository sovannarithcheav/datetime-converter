## Usage
### build.gradle.kts:
1. Configure GitHub credential
```sh
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/sovannarith/jpa-sort-util.git")
        credentials {
            username = System.getenv("GIT_PUBLISH_USER")
            password = System.getenv("GIT_PUBLISH_PASSWORD")
        }
    }
}
```
2. Add dependency
```sh
dependencies {
  	implementation("kh.org.sovannarith:jpa-sort-util:${property("jpa-sort-util.version")}")
}
```
3. Create sorting mapper object
```kotlin
@Component
class UserSortMapping : JpaSortMappingResource<User>, JpaSortMappingSupport<User>() {
    // You can use default JpaSortMappingSupport.getSource() to get all declared fields or override it like below
    override fun getSource(): MutableMap<String, String> {
        val map = mutableMapOf("name" to "nameKH")
        super.getSource().putAll(map)
        return super.getSource()
    }

}
```

4. Use sorting mapper object on Rest Controller getting endpoint
```kotlin
    @GetMapping
    @JpaSortMapping(value = UserSortMapping::class)
    fun findAll(
        filter: UserReq.Filter?,
        @SortDefault(sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ResponseWrapper>? {
        return ok(userService.findAll(filter, pageable))
    }
```