# Progressive Image Loader for Responsive Websites

This repository contains the infrastructure and source to run a Spring MVC framework to load progressive-encoded
images on devices with different quality levels. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development 
and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

#### Intellij

The backend is written with Kotlin. Therefore, it is recommended to install 
[IntelliJ IDEA](https://www.jetbrains.com/idea/) to change, improve and run the code in this project.

#### Gradle (for Linux)

In order to run this project via Gradle in the command line, the following steps have to be carried out:

* Install Gradle

```
sudo apt-get install gradle
```

* For this project, Gradle 4.0 or higher is required. Check your Gradle version

```
gradle -version
```

* If Gradle version is below 4.0, update it

```
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update 
sudo apt-get upgrade gradle
```

* Create the gradle wrapper

```
gradle wrapper --gradle-version 2.13
```

#### JAVA_HOME
Make sure Java 8 is set for the JAVA_HOME variable on your local machine (Windows and Linux)

##### Linux
Run the following commands in the command line of Intellij

* Check your JAVA_HOME variable
```
echo $JAVA_HOME
```

* Change your JAVA_HOME path if not Java 8 is selected
```
export JAVA_HOME=/*path to Java 8 folder*/
```

#### MySQL
In order to run this project, an installed and running MySQL server is required. This project have been tested 
with the "MySQL 8 Server" on Windows and Linux. 

##### Download

Get here the latest version for [Windows](https://dev.mysql.com/doc/refman/8.0/en/windows-installation.html)

Get here the latest version for [Linux](https://dev.mysql.com/doc/refman/8.0/en/linux-installation.html)

##### Installation
1. Run the MySQL installer.
2. Install the MySQL server with default settings. Make sure the port **3306** is selected.
3. In the installation wizard, select the option "add User" and add an user with custom user credentials.
4. Finish the installation.
5. Open the property file **application.properties** in the resource folder of this project and edit the username and
password of the previously created MySQL user in the properties **spring.datasource.username** and 
**spring.datasource.password**

Make sure the MySQL server is running before you execute this project!

### Run Project

This project can be executed either via Intellij or Gradle

#### Intellij

1. Open the "Gradle Project" tool at **View -> Tool Windows -> Gradle**.
2. Right-click on the project node **RestApi** and select **Refresh dependencies**. Wait until all dependencies have 
been loaded. The current status is shown in the footer of Intellij.
3. Right-click on the project node **RestApi** and select **Refresh Gradle project**. Wait until the project have 
been updated. The current status is shown in the footer of Intellij.
4. Right-click on file **Application.kt** and click on **Run 'com.restapi.application**

#### Gradle on Linux
Run the following commands in the command line of Intellij

* Refresh the dependencies
```
./gradlew --refresh-dependencies
```

* Run this project
```
gradle bootRun
```

## Built With

* [Kotlin](https://kotlinlang.org/) - Program code language for backend
* [Spring](https://spring.io/) - Application framework
* [Gradle](https://gradle.org/) - Dependency Management
* [MySQL](https://www.mysql.com/) - Database connector API for Java

## Versioning

We use [SemVer](http://semver.org/) for versioning. The current release version is defined in the **build.gradle** 
file. All available versions can be found on our [GitHub repository](https://github.com/BAC1/RestApi/tags). 

## Authors

* **Markus Graf**           <fhs39198@fh-salzburg.ac.at>

## License

This project is not licensed