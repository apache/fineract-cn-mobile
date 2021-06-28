### Status

[![Build Status](https://travis-ci.org/openMF/Fineract-CN-mobile.svg?branch=development)](https://travis-ci.org/openMF/Fineract-CN-mobile)

# Android Client of Apache Fineract CN

Android Client is built on top of the Apache Fineract CN platform. It is the mobile field agent application for its web counterpart

## Building the Project
1. Install [Java Development Kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Download, install, and run the latest version of [Android Studio](http://developer.android.com/sdk/installing/studio.html).
3. Fork our repo from `https://github.com/apache/fineract-cn-mobile`
4. From your terminal, download a copy of the forked repo with `git clone https://github.com/your_username/fineract-cn-mobile.git` where `your_username` is your GitHub username.
5. Enter the new **fineract-cn-mobile** directory with `cd fineract-cn-mobile`.
6. Set the upstream remote to the original repository url so that git knows where to fetch updates from in future: `git remote add upstream https://github.com/apache/fineract-cn-mobile.git`
7. Import the project to Android Studio.
 - Launch the Android Studio application
 - Click on `File>Open...>{$FOLDER NAME}`
5. Once the gradle build is finished, add a virtual device
 - Click on the dropdown on the toolbar beside the green play button that has "No Devices" selected
 ![android_no-devices](https://user-images.githubusercontent.com/40563356/96556805-5afd6780-12b1-11eb-8b44-d06c00415712.png)
 - Select the "AVD MANAGER" option
 - Click on the "Create Virtual Device" button
 - Select a hardware device and choose "Next"
 - Select a system image and choose "Next". You will need to download it first if it's your first time running android studio.
 - Configure the settings for your virtual device and choose "Finish" (Default settings are recommended)
6. Launch the created AVD in the emulator
 - Click on the play button on the newly opened window (Android Virtual Device Manager) to launch the virtual device.
7. Once the application has been built successfully, the login screen should appear:

![fineract-mobile](https://user-images.githubusercontent.com/40563356/96556409-db6f9880-12b0-11eb-8187-dfcc8c7955fa.png)


### Demo Credentials
You can use these credentials to log in.
```
Tenant: playground
Username: apache.demo
Password: ven3t1@n
```
### Common Errors and Fixes
1. /dev/kvm permission denied
 - When choosing a system image, you may come accross this issue. You can find a solution to this [here](https://stackoverflow.com/questions/37300811/android-studio-dev-kvm-device-permission-denied)
2. System UI isn't responding
 - Some time after launching the virtual device, you might encounter this error. This can be solved by choosing a virtual device of lower resolution such as the Nexus 4

## Contributing

We love Pull Requests, Bug Reports, ideas, code reviews or any other kind of positive contribution. 

- [Current issues](https://issues.apache.org/jira/browse/FINCN-13?jql=project%20%3D%20FINCN%20AND%20component%20%3D%20fineract-cn-mobile) 
- [Pull requests](https://github.com/apache/fineract-cn-mobile/pulls)


### Commit Style Guide

 We have set of [Commit Style Guidelines](https://github.com/apache/fineract-cn-mobile/blob/development/.github/COMMIT_STYLE.md) and [Contribution Guidelines](https://github.com/apache/fineract-cn-mobile/blob/development/.github/CONTRIBUTING.md). We follow these guideline to track the every change, any bug fixes, any enhancement and any new feature addition. We also suggest you to follow these guidelines to help us manage every commit.
