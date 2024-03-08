plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "ru.ndsmc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("papermc") {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("sonatype") {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven("jitpack") {
        url = uri("https://jitpack.io/")
    }
    maven("aikar") {
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
}

dependencies {
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("com.github.LeonMangler:SuperVanish:6.2.18-3")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
    testImplementation("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    testImplementation("net.luckperms:api:5.4")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("org.mockito:mockito-core:2.1.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
}

tasks.shadowJar {
    relocate("co.aikar", "ru.ndsmc.spectatormode.libraries")
    relocate("org.bstats", "ru.ndsmc.spectatormode.libraries")
    minimize()
}

val targetJavaVersion = 17
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"

    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
