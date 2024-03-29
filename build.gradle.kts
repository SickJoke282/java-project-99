import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	application
	id("java")
	id("jacoco")
	id("checkstyle")
	id("io.freefair.lombok") version "8.4"
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.github.ben-manes.versions") version "0.50.0"
	id("com.github.johnrengelman.shadow") version "8.1.1"
	id("io.sentry.jvm.gradle") version "4.1.1"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass.set("hexlet.code.AppApplication")
}

java {
	sourceCompatibility = JavaVersion.VERSION_20
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("io.sentry:sentry-spring-boot-starter:7.1.0")
	implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.1.0")

	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.mapstruct:mapstruct:1.6.0.Beta1")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0.Beta1")

	implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.2.0")

	implementation("org.instancio:instancio-junit:3.6.0")
	implementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	implementation("net.datafaker:datafaker:2.0.2")

	runtimeOnly("com.h2database:h2:2.2.224")
	implementation("org.postgresql:postgresql:42.6.0")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation(platform("org.junit:junit-bom:5.10.1"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		showStandardStreams = true
		finalizedBy(tasks.jacocoTestReport)
	}
}

sentry {
	val env = System.getenv("APP_ENV")
	if (env != null && env.contentEquals("production")) {
		includeSourceContext = true
		org = "egorkrylatov"
		projectName = "java-project-99"
		authToken = System.getenv("SENTRY_AUTH_TOKEN")
	}
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
	}
}
