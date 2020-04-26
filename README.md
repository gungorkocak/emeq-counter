# Getting Started

This project is a monorepo for two different applications as respectively called `backend` and `frontend`.

In development environment, in order to develop for both `backend` and `frontend` projects, you'll need to run relevant gradle tasks for each of them.

In production envrionment, at the build stage, `frontend` just builds itself and
contents of the `dist` folder is copied to `backend`s `static` folder.
Therefore, there is no need to run/deploy both applications, so just running backend application would suffice.

Let's see how we can bootstrap both projects in macOS and Windows.

## Setting it up for macOS

### Backend Project

First clone this project to your machine.

```sh
git clone git@github.com:gungorkocak/emeq-counter.git && cd emeq-counter
```

If you want to run this project in your local settings, you need to follow the steps shown below. After installation, you can run the project on your computer.

#### 1. Create Database and user
You need to create database and user in Postgresql. Login at Postgresql with `psql`. Than follow these steps.


```sql
create user emeq;
alter user emeq with encrypted password 'password';
create database emeqdb;
grant ALL privileges on database emeqDB to emeq;
```

Logout with `\q`.

#### 2. Install Java

I prefer to use [Jenv](https://github.com/jenv/jenv) to manage Java versions on my mac.
Install Jenv via [homebrew](https://brew.sh/).

```sh
brew install jenv
jenv doctor
jenv enable-plugin export
```
Restart your shell by running `exec $SHELL -l` in the current session for the changes to take affect.

Now install latest Java version via homebrew.

```sh
brew cask install java
```

Then add Java to jenv for recognize it.

```sh
jenv add $(/usr/libexec/java_home)
```


#### 3.pre Install SpringBoot (Optional)

Install Spring via homebrew

```sh
brew install springboot
```

#### 3. Install Gradle

Install Gradle and Gradle completion via homebrew:

```sh
brew install gradle gradle-completion
```

For more visit [gradle-completion](https://github.com/gradle/gradle-completion) repo on Github. You need to make your configurations for `bash` or `zsh`.


#### 4. Finally run the project with...

```sh
gradle bootRun
```

or

```sh
./gradlew bootRun
```

### Frontend Project

Currently frontend project is an `npm` project, built via `parcel-bundler`, integrated to `gradle` and uses `elm` as the programming language choice on the browser.


#### 1. Environment Setup

Make sure you are running your development environment with relevant versions:

1. `node`: '12.13.0'
2. `npm`: '6.12.0'
3. `parcel-bundler`: '1.12.4'
4. `elm`: '0.19.1'


#### 2. npm Dependencies

After installing all required tools, you'll need to install npm libraries:

```sh
./gradlew frontend_install
```

#### 3. Running in Dev Mode (Locally)

If you reach this point, also make sure that you are running backend project via `./gradlew bootRun`, so that frontend could request required data from `backend` api.

Then, only thing to do is running `./gradlew frontend_start`.

After some info, and initial build process, you'll be able to visit `http://localhost:1234` and see frontend application runs!


#### 4. Build Process

Although this step **is not required for development workflow**, it makes sense to know how our build process works.

`frontend` project is build via running `./gradlew frontend_build`, but we shouldn't directly use that other than debugging purposes.

Actual build pipeline is triggered from `backend` project by running `./gradlew build`
and all related `frontend` build tasks automatically run alongside with it.

As the last step, that `build` task makes sure `frontend_build` succeeds
and it copies contents of the `frontend/dist` folder to `backend/assets` directory.

## Setting it up for Windows

### Backend Project

First open Eclipse Project Explorer to import this project and clone to your machine.

#### 1. Import the project into your workspace by using Eclipse IDE

Project Explorer --> Import --> Projects from Git --> Clone URI

 git@github.com:gungorkocak/emeq-counter.git

If you want to run this project in your local settings, you need to follow the steps shown below. After installation, you can run the project on your computer.

#### 2. Create Database and userYou need to create database and user in Postgresql.

  Login at Postgresql with `psql`. Than follow that steps.create user emeq;

  ```
  alter user emeq with encrypted password 'password';
  create database emeqdb;
  grant ALL privileges on database emeqDB to emeq;
  ```

#### 3. Install Java

Download Java SE Development Kit 13
Search for Environment Variables then select Edit the system environment variables
Click the Environment Variables button.
Under System Variables, click New.
In the Variable Name field, enter either:
`JAVA_HOME` if you installed the JDK (Java Development Kit)

or

`JRE_HOME` if you installed the JRE (Java Runtime Environment)
In the Variable Value field, enter your JDK or JRE installation path .

OR

configure project build path

Java Build Path --> JRE System Library[jdk-13.0.2]

#### 4. Install Gradle

Install gradle latest version and run it.
https://gradle.org/install/

#### 5. Install Project Lombok

Download latest version of lombok.jar from 

```sh
https://projectlombok.org/download
```

Right click backend --> Build Path --> Configure Build Path

Open Libraries tab --> classpath 

Select Add External JARs and add lombok.jar

#### 6. Run the project on you IDE.

Run EmeqCounterApplication.java file

## Running Tests

Before running test units you need to create test db as `emeqdb_test`. You can create new database like this;

```sql
create database emeqdb_test;
grant ALL privileges on database emeqdb_test to emeq;
```

You can run the test suite via gradle.

```sh
gradle test
```

or

```sh
gradle clean test
```
