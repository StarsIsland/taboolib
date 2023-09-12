repositories {
    maven("https://repo.minebench.de/")
    maven("https://www.jitpack.io")
    mavenLocal()
}

dependencies {
    compileOnly("cn.powernukkitx:powernukkitx:1.20.10-r1")
    compileOnly("org.tabooproject.reflex:reflex:1.0.19")
    compileOnly(project(":module:module-lang"))
    compileOnly("org.tabooproject.reflex:analyser:1.0.19")
    compileOnly(project(":common"))
}