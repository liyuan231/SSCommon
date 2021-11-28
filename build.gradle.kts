/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
    `maven-publish`
//    id 'o.spring.dependency-management' version '1.0.11.RELEASE'
}
val localRepo = "file://" + File("E:\\m2").absoluteFile

repositories {
    maven(localRepo)
    maven(url = "https://maven.aliyun.com/repository/jcenter")
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.3.2.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-security:2.3.2.RELEASE")
    implementation("org.springframework.boot:spring-boot-devtools:2.3.2.RELEASE")
    implementation("org.springframework.boot:spring-boot-configuration-processor:2.3.2.RELEASE")
    implementation("com.alibaba:fastjson:1.2.73")
    implementation("org.springframework.security:spring-security-jwt:1.0.11.RELEASE")
//    implementation("org.springframework.boot:spring-boot-starter-mail:2.1.5.RELEASE")

    testImplementation("junit:junit:4.13")
}

group = "liyuan.user"
version = "1.0-SNAPSHOT"
description = "base-jwt-security"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "utf-8"
}


publishing {
    repositories {
        maven(localRepo)
    }
}

