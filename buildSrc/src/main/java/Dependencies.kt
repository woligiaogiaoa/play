import Versions.kotlinCoroutineAndroidVersion
import Versions.kotlinCoroutineCoreVersion

object Versions {
    val kotlin = "1.3.72"
    val kotlinCoroutineCoreVersion="1.3.7"
    val kotlinCoroutineAndroidVersion="1.3.5"

    val naviagtionVersion="2.3.0-alpha01"
}




object Dependencies {

    val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    val kotlinCoroutineCore="org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineCoreVersion"

    val kotlinCoroutineAndroid="org.jetbrains.kotlinx:kotlinx-coroutines-android:${kotlinCoroutineAndroidVersion}"
}