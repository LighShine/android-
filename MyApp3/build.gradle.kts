// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra["compose_version"] = "1.4.3"
}

plugins {

    id("com.android.application") version "8.3.2" apply false
    id("com.android.library") version "8.3.2" apply false
    kotlin("android") version "1.8.0" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
}
