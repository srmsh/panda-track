
dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.1.1")
    implementation("com.google.code.gson:gson:2.8.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
}

tasks.withType<Jar> {
    manifest {
        attributes(Pair("Premain-Class", "org.track.TrackAgent"))
    }
}