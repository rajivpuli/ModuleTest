plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
    signing
}

group = "io.github.rajivpuli"   // Must match the groupId you got approved
version = "1.0.0"

android {
    namespace = "com.example.mavencentraltesting"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                afterEvaluate {
                    from(components["release"])
                }
                groupId = group.toString()
                artifactId = "mylibrary" // your artifact name
                version = version

                pom {
                    name.set("My Library")
                    description.set("A useful Android library for ...")
                    url.set("https://github.com/your/repo")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("yourid")
                            name.set("Your Name")
                            email.set("you@example.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/your/repo.git")
                        developerConnection.set("scm:git:ssh://github.com:your/repo.git")
                        url.set("https://github.com/your/repo")
                    }
                }
            }
        }
        repositories {
            maven {
                name = "OSSRH"
                url = uri(
                    if (version.toString().endsWith("SNAPSHOT"))
                        "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                    else
                        "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                )
                credentials {
                    username = findProperty("ossrhUsername") as String
                    password = findProperty("ossrhPassword") as String
                }
            }
        }
    }
    signing {
        sign(publishing.publications["release"])
    }
}

