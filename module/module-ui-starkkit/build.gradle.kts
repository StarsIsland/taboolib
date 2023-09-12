repositories {
    maven("https://repo.minebench.de/")
    maven("https://www.jitpack.io")
    mavenLocal()
}

dependencies {
    compileOnly("cn.powernukkitx:powernukkitx:1.20.10-r1")
    compileOnly("com.google.code.gson:gson:2.8.7")
    compileOnly(project(":common"))
}