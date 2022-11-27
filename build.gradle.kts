import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
}

group = "com.bhft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

class JMeterRule : ComponentMetadataRule {
    override fun execute(context: ComponentMetadataContext) {
        context.details.allVariants {
            withDependencies {
                removeAll { it.group == "org.apache.jmeter" && it.name == "bom" }
            }
        }
    }
}

dependencies {
    implementation("us.abstracta.jmeter:jmeter-java-dsl:1.3")

    components {
        withModule("org.apache.jmeter:ApacheJMeter_core", JMeterRule::class.java)
        withModule("org.apache.jmeter:ApacheJMeter_java", JMeterRule::class.java)
        withModule("org.apache.jmeter:ApacheJMeter", JMeterRule::class.java)
        withModule("org.apache.jmeter:jorphan", JMeterRule::class.java)
        withModule("org.apache.jmeter:ApacheJMeter_http", JMeterRule::class.java)
        withModule("org.apache.jmeter:ApacheJMeter_functions", JMeterRule::class.java)
        withModule("org.apache.jmeter:ApacheJMeter_components", JMeterRule::class.java)
        withModule("org.apache.jmeter:ApacheJMeter_config", JMeterRule::class.java)
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}