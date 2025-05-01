import org.gradle.internal.os.OperatingSystem

plugins {
    // The Badass Jlink Plugin provides jlink and jpackage
    // functionality and applies the java application plugin
    // https://badass-jlink-plugin.beryx.org
    id("org.beryx.jlink") version "3.1.1"
    // For the asciidoctor docs
    id("org.asciidoctor.jvm.convert") version "4.0.4"
}

dependencies {
    // The Codion framework UI module, transitively pulls in all required
    // modules, such as the model layer and the core database module
    implementation(libs.codion.swing.framework.ui)
    // Include all the standard Flat Look and Feels and a bunch of IntelliJ
    // theme based ones, available via the View -> Select Look & Feel menu
    implementation(libs.codion.plugin.flatlaf)
    implementation(libs.codion.plugin.flatlaf.intellij.themes)

    // Provides the Logback logging library as a transitive dependency
    // and provides logging configuration via the Help -> Log menu
    runtimeOnly(libs.codion.plugin.logback.proxy)
    // Provides the local JDBC connection implementation
    runtimeOnly(libs.codion.framework.db.local)
    // The H2 database implementation
    runtimeOnly(libs.codion.dbms.h2)
    // And the H2 database driver
    runtimeOnly(libs.h2)

    // The domain model unit test module
    testImplementation(libs.codion.framework.domain.test)
    testImplementation(libs.codion.framework.db.local)
}

version = "0.1.0"

java {
    toolchain {
        // Use the latest possible Java version
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            targets {
                all {
                    // System properties required for running the unit tests
                    testTask.configure {
                        // The JDBC url
                        systemProperty("codion.db.url", "jdbc:h2:mem:h2db")
                        // The database initialization script
                        systemProperty("codion.db.initScripts", "classpath:create_schema.sql")
                        // The user to use when running the tests
                        systemProperty("codion.test.user", "scott:tiger")
                    }
                }
            }
        }
    }
}

// Configure the application plugin, the jlink plugin relies
// on this configuration when building the runtime image
application {
    mainModule = "is.codion.demos.template"
    mainClass = "is.codion.demos.template.ui.TemplateAppPanel"
    applicationDefaultJvmArgs = listOf(
        // This app doesn't require a lot of memory
        "-Xmx64m",
        // Specify a local JDBC connection
        "-Dcodion.client.connectionType=local",
        // The JDBC url
        "-Dcodion.db.url=jdbc:h2:mem:h2db",
        // The database initialization script
        "-Dcodion.db.initScripts=classpath:create_schema.sql",
        // Just in case we're debugging in Linux, nevermind
        "-Dsun.awt.disablegrab=true"
    )
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.isDeprecation = true
}

// Configure the docs generation
tasks.asciidoctor {
    inputs.dir("src")
    baseDirFollowsSourceFile()
    attributes(
        mapOf(
            "source-highlighter" to "prettify",
            "tabsize" to "2"
        )
    )
    asciidoctorj {
        setVersion("2.5.13")
    }
}

// Create a version.properties file containing the application version
tasks.register<WriteProperties>("writeVersion") {
    destinationFile = file("${temporaryDir.absolutePath}/version.properties")
    property("version", "${project.version}")
}

// Include the version.properties file from above in the
// application resources, see usage in TemplateAppModel
tasks.processResources {
    from(tasks.named("writeVersion"))
}

// Configure the Jlink plugin
jlink {
    // Specify the jlink image name
    imageName = project.name
    // The options for the jlink task
    options = listOf(
        "--strip-debug",
        "--no-header-files",
        "--no-man-pages",
        // Add the modular runtimeOnly dependencies, which are handled by the ServiceLoader.
        // These don't have an associated 'requires' clause in module-info.java
        // and are therefore not added automatically by the jlink plugin.
        "--add-modules",
        // The local JDBC connection implementation
        "is.codion.framework.db.local," +
                // The H2 database implementation
                "is.codion.dbms.h2," +
                // The Logback plugin
                "is.codion.plugin.logback.proxy"
    )

    mergedModule {
        // Add requires and provides clauses for non modular libraries
        requires("java.naming")
        requires("java.sql")
        provides("java.sql.Driver").with("org.h2.Driver")
    }

    jpackage {
        if (OperatingSystem.current().isLinux) {
            icon = "src/main/icons/template.png"
            installerOptions = listOf(
                "--linux-shortcut"
            )
        }
        if (OperatingSystem.current().isWindows) {
            icon = "src/main/icons/template.ico"
            installerOptions = listOf(
                "--win-menu",
                "--win-shortcut"
            )
        }
    }
}