repositories {
    maven("https://repo.minebench.de/")
    maven("https://www.jitpack.io")
    mavenLocal()
}

dependencies {
    compileOnly("cn.powernukkitx:powernukkitx:1.20.10-r1")
    compileOnly("com.google.code.gson:gson:2.8.7")
    compileOnly(project(":common"))
    implementation("com.google.auto.service:auto-service:1.1.1")
    implementation("org.json:json:20230618")

    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}