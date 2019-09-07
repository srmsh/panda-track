
dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.5.1")
}

tasks.withType<Jar> {
    manifest {
        attributes(Pair("Premain-Class", "org.track.TrackAgent"))
    }
}