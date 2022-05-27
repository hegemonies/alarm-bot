import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
    kotlin("plugin.jpa") version "1.5.30"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.30"
    id("com.google.cloud.tools.jib") version "3.1.4"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "ru.bravo"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-quartz")

    // postgres
    runtimeOnly("org.postgresql:postgresql")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

    implementation("joda-time:joda-time:2.10.10")

    // telegram bot
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.5")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    to {
        this.image = "bravo/alarm-bot:latest"
    }
}

tasks.withType<Jar> {
    this.setProperty("archiveFileName", "alarm-bot.jar")
    this.manifest {
        attributes["Main-Class"] = "ru.bravo.alarmbot.AlarmBotApplicationKt"
    }
}
