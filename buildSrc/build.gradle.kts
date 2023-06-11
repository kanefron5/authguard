import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")
}
gradlePlugin {
    plugins {
        create("changelog-gradle-plugin") {
            id = "dev.zabolotskikh.changelog-gradle-plugin"
            implementationClass = "dev.zabolotskikh.ChangelogPlugin"
        }
    }
}