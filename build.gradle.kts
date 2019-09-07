group = "org.track"
version = "1.0"

allprojects {

}

subprojects {
    apply(plugin = "java")
    repositories {
        mavenLocal()
        mavenCentral()
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

}