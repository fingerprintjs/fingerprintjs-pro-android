dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.fpregistry.io/releases") }
    }
}
rootProject.name = "fingerprintjs-pro-android-demo"
include(":app")
