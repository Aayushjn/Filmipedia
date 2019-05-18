buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.31")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
        mavenCentral()
        maven {
            name = "Sonatype SNAPSHOTs"
            setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
    gradle.projectsEvaluated {
        tasks.withType(JavaCompile::class) {
            options.compilerArgs.addAll(arrayOf("-Xlint:unchecked", "-Xlint:deprecation"))
        }
    }
}

tasks {
    withType(Delete::class) {
        delete(rootProject.buildDir)
    }
}
