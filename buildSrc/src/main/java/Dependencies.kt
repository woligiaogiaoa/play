import Versions.kotlinCoroutineAndroidVersion
import Versions.kotlinCoroutineCoreVersion
import Versions.preferenceVersion

object Versions {
    val kotlin = "1.3.72"
    val kotlinCoroutineCoreVersion="1.3.7"
    val kotlinCoroutineAndroidVersion="1.3.5"

    val naviagtionVersion="2.3.0-alpha01"



    val preferenceVersion="1.1.0"

}




object Dependencies {

    val preferenceKtx ="androidx.preference:preference-ktx:$preferenceVersion"
    val preference ="androidx.preference:preference:$preferenceVersion"

    val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    val kotlinCoroutineCore="org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineCoreVersion"

    val kotlinCoroutineAndroid="org.jetbrains.kotlinx:kotlinx-coroutines-android:${kotlinCoroutineAndroidVersion}"
}