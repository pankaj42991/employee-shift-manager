#!/usr/bin/env sh

DEFAULT_JVM_OPTS=""

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"

APP_HOME=`pwd -P`

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

JAVA_EXEC="java"

exec "$JAVA_EXEC" $DEFAULT_JVM_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
