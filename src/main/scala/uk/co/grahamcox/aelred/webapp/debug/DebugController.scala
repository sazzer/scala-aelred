package uk.co.grahamcox.aelred.webapp.debug

import com.twitter.finatra._
import com.twitter.finatra.ContentType._

/**
 * Case Class to represent the debug details to return
 * @param groupId The Maven Group ID
 * @param artifactId The Maven Artifact ID
 * @param version The project version
 * @param scalsVersion The Scala version
 */
class Debug(val groupId: String, val artifactId: String, val version: String, val scalaVersion: String)


/**
 * Controller for access to Debug information
 */
class DebugController extends Controller {
    /**
     * Get the version information
     */
    get("/api/debug/version") { request =>
      render.json(new Debug("uk.co.grahamcox.aelred", "aelred", "0.0.1-SNAPSHOT", "2.10.3")).toFuture
    }

}

