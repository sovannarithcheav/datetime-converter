![banner](.github/assets/banner.svg)

## About

This is common library for backend service.

[Issues][issues-url] | [Milestone][milestone-url] | [Pull requests][pullrequest-url] |

![divider](.github/assets/divider.png)


## Usage
### build.gradle.kts:
1. Configure GitHub credential
```sh
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/soramitsukhmer/jpa-sort-util.git")
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
  	implementation("kh.org.soramitsu:jpa-sort-util:${property("jpa-sort-util.version")}")
}
```
3. Create sorting mapper object
```kotlin
import kh.org.nbc.hrm.system_setting.model.TypeOfSeniority
import kh.org.soramitsu.jpa_sort_util.annotation.JpaSortMappingResource
import kh.org.soramitsu.jpa_sort_util.annotation.JpaSortMappingSupport
import org.springframework.stereotype.Component

/**
 * Represent to model [TypeOfSeniority]
 * @see kh.org.nbc.hrm.system_setting.response.TypeOfSeniorityRes
 * Draft: [TypeOfSeniority]::class.java.declaredFields.map { it.name }
 *
 *
 */
@Component
class TypeOfSenioritySortMapping : JpaSortMappingResource<TypeOfSeniority>, JpaSortMappingSupport<TypeOfSeniority>() {

    // You can use default JpaSortMappingSupport.getSource() or override it like below
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
    @JpaSortMapping(value = TypeOfSenioritySortMapping::class)
    fun findAll(
        filter: TypeOfSeniorityReq.Filter?,
        @SortDefault(sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ResponseWrapper>? {
        return ok(typeOfSeniorityService.findAll(filter, pageable))
    }
```



<!-- variables -->

[milestone-url]: https://github.com/soramitsukhmer/jpa-sort-util/milestones
[issues-url]: https://github.com/soramitsukhmer/jpa-sort-util/issues
[pullrequest-url]: https://github.com/soramitsukhmer/jpa-sort-util/pulls
