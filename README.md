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

<!-- variables -->

[milestone-url]: https://github.com/soramitsukhmer/jpa-sort-util/milestones
[issues-url]: https://github.com/soramitsukhmer/jpa-sort-util/issues
[pullrequest-url]: https://github.com/soramitsukhmer/jpa-sort-util/pulls
