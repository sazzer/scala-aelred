package uk.co.grahamcox.aelred.webapp.controllers.debug

/**
 * Case Class to represent the debug details to return
 * @param groupId The Maven Group ID
 * @param artifactId The Maven Artifact ID
 * @param version The project version
 * @param scalsVersion The Scala version
 */
class Debug(val groupId: String, val artifactId: String, val version: String, val scalaVersion: String) {
}

