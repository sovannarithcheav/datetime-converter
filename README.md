## Usage
### build.gradle.kts:
1. Configure GitHub credential
```sh
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/sovannarith/datetime-converter.git")
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
  	implementation("kh.org.sovannarith:datetime:${property("datetime-converter.version")}")
}
```