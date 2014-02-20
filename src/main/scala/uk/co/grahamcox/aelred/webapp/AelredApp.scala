package uk.co.grahamcox.aelred.webapp

import com.twitter.finatra._
import com.twitter.finatra.ContentType._
import uk.co.grahamcox.aelred.webapp.debug._
import uk.co.grahamcox.aelred.webapp.oauth2._
import uk.co.grahamcox.aelred.oauth2._
import uk.co.grahamcox.aelred.webapp.authorization._

/**
 * The core application. This is the actual server that does all of the work
 */
object AelredApp extends FinatraServer {
    val clientService = new ClientService()

    addFilter(new RequestStoreFilter)
    addFilter(new AuthorizationFilter(Seq(new BasicAuthorizer)))
    // Register all of the controllers
    register(new DebugController())
    register(new OAuth2Controller(clientService))
}

