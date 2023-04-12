import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("maven-publish")
	kotlin("jvm") version "1.7.22"
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:${property("jackson-module.version")}")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework:spring-webmvc:${property("spring.version")}")
	compileOnly("org.springframework.boot:spring-boot-starter-validation:${property("spring-boot.version")}")
	implementation("org.springframework.boot:spring-boot-configuration-processor:${property("spring-boot.version")}")
	implementation("org.springframework.data:spring-data-commons:3.0.3")
	compileOnly("javax.servlet:servlet-api:2.5")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = project.properties["group"].toString()
			artifactId = "datetime-converter"
			version = project.properties["version"].toString()
			from(components["kotlin"])
		}
	}
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/sovannarithcheav/*")
			credentials {
				username = System.getenv("GIT_PUBLISH_USER")
				password = System.getenv("GIT_PUBLISH_PASSWORD")
			}
		}
	}
}