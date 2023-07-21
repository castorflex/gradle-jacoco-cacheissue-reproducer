# Overview

This is a sample app to demonstrate an issue with jacoco and the Gradle cache.

When overriding the Test task's jvm args, the cached data do not contain the jacoco execution file

# How to reproduce

Launch a first time `./gradlew jacocoTestReport --console=plain`

You will notice that both `:app:testReleaseUnitTest` and `:app:jacocoTestReport` are ran, which is expected.

Then, `rm -r app/build/jacoco` to remove the execution data.

Run again `./gradlew jacocoTestReport --console=plain`

-> you will notice that the `:app:testReleaseUnitTest` was not executed (`FROM_CACHE`), which is expected. However the `:app:jacocoTestReport` is skipped, which is not expected.

By opening `app/build` you will see that the `jacoco` folder, supposed to contain the jacoco execution data, is not created.

# Workaround?

To 'fix' the issue, all it takes is to remove the following line in `app/build.gradle`:

```diff
    testOptions.unitTests.all {
        // this line breaks the cache for jacoco
-       it.allJvmArgs = it.allJvmArgs + listOf("-Xmx4192M")
        it.configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }
```
