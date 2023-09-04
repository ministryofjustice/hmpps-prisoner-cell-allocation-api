plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.4.0"
  kotlin("plugin.spring") version "1.9.0"
}

val jsonwebtokenVersion by extra("0.11.5")
val springBootStarterOauth2Version by extra("3.1.2")

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
  implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootStarterOauth2Version")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:$springBootStarterOauth2Version")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client:$springBootStarterOauth2Version")

  testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:2.35.0")
  testImplementation("org.eclipse.jetty:jetty-reactive-httpclient:3.0.8")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("io.jsonwebtoken:jjwt-api:$jsonwebtokenVersion")
  testImplementation("io.jsonwebtoken:jjwt-impl:$jsonwebtokenVersion")
  testImplementation("io.jsonwebtoken:jjwt-orgjson:$jsonwebtokenVersion")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(19))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
      apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_19)
    }
  }
}
