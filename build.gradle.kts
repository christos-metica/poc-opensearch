plugins {
    kotlin("jvm") version "1.8.10"
    id("org.springframework.boot") version "3.0.5"
    kotlin("plugin.spring") version "1.8.10"
//    id ("com.palantir.docker") version "0.33.0"
//    id ("com.palantir.docker-run") version "0.33.0"
    application
}

group = "com.metica.resource-server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


springBoot {

    buildInfo()
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.0.4")

    implementation("org.opensearch.client:spring-data-opensearch-starter:1.0.1")
    implementation("org.opensearch.client:opensearch-java:2.0.0")
    implementation("com.querydsl:querydsl-lucene4:5.0.0")

    testImplementation(kotlin("test"))
    testImplementation("org.opensearch.client:spring-data-opensearch-test-autoconfigure:1.0.0")
}

val sourceCompatibility = "11"
// add the following two variables
val targetCompatibility = "11"
val dockerOwner = "christos"

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveClassifier.set("boot")
}

tasks.named<Jar>("jar") {
    archiveClassifier.set("")
}
tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.metica.resource.HelloWorldApplicationKt")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    manifest {
        attributes("Start-Class" to "com.metica.resource.HelloWorldApplicationKt")
    }
}


// takes templatized Dockerfile, places into buildDir
//tasks.register("prepareDockerfileTemplate", Copy::class) {
//    from("src/main/resources/docker")
//    include("Dockerfile")
//    expand(mapOf(
//        "name" to project.name,
//        "version" to project.version
//    ))
//    into(buildDir)
//}
//
//// add explicit dependency, otherwise we get warning at console
//tasks.getByName("dockerPrepare").dependsOn(tasks.getByName("bootJar"))
//tasks.getByName("bootJar").dependsOn(tasks.getByName("prepareDockerfileTemplate"))
//tasks.getByName("bootJarMainClassName").dependsOn(tasks.getByName("prepareDockerfileTemplate"))
//
//tasks {
//    val dockerImage by registering(DockerBuild::class) {
//        name.set("${dockerOwner}/${project.name}:${project.version}")
//        files.from("$buildDir/libs/${project.name}-${project.version}.jar")
//        dockerfile.set(file("$buildDir/Dockerfile"))
//    }
//
//    val dockerRun by registering(DockerRun::class) {
//        name.set(project.name)
//        image.set("${dockerOwner}/${project.name}:${project.version}")
//        ports.set(listOf("8080:8080", "8081:8081"))
//        clean.set(true)
//        daemonize.set(false)
//    }
//}