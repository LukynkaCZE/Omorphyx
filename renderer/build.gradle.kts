plugins {
    kotlin("jvm")
}

group = "cz.lukynka.omorphyx.renderer"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.bindables)
    api(libs.bundles.skia)
    implementation(libs.logger)
    implementation(libs.bundles.serialization)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}