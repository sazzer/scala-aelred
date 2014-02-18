package uk.co.grahamcox.aelred.webapp

import com.twitter.finatra._
import com.twitter.finatra.ContentType._
import uk.co.grahamcox.aelred.webapp.controllers.debug._

/**
 * The core application. This is the actual server that does all of the work
 */
object App extends FinatraServer {
  // Register all of the controllers
  register(new DebugController())
}

