# Getting Started

## For macOS

First clone this project to your machine.

```sh
git clone git@github.com:gungorkocak/emeq-counter.git && cd emeq-counter
```

If you want to run this project in your local settings, you need to follow the steps shown below. After installation, you can run the project on your computer.

<details>
<summary>1. Create Database and user</summary>
You need to create database and user in Postgresql. Login at Postgresql with `psql`. Than follow that steps.

```sql
create user emeq;
alter user emeq with encrypted password 'password';
create database emeqdb;
grant ALL privileges on database emeqDB to emeq;
```

Logout with `\q`.
</details>
<details>
<summary>2. Install Java</summary>

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
</details>
<details>
<summary>3.a. Install SpringBoot (Optional)</summary>

Install Spring via homebrew

```sh
brew install springboot
```
</details>
<details>
<summary>3. Install Gradle</summary>

Install Gradle and Gradle completion via homebrew

```sh
brew install gradle gradle-completion
```
For more visit [gradle-completion]https://github.com/gradle/gradle-completion repo on Github. You need to make your configurations for `bash` or `zsh`.

</details>
<details>
<summary>4. Now run the project with...</summary>
```sh
gradle bootRun
```
or
```sh
./gradlew bootRun
``**
</details>

## For Windows

**TBD***

# Running Test

You can run test via gradle.

```
gradle test
```
