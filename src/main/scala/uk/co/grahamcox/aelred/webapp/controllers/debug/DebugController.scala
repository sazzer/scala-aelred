package uk.co.grahamcox.aelred.webapp.controllers.debug

import com.twitter.finatra._
import com.twitter.finatra.ContentType._

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

