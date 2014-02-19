package uk.co.grahamcox.aelred.webapp

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import com.twitter.finagle.http.{Request, Response}
import com.twitter.app.App
import scala.collection.mutable.HashMap
import com.twitter.finagle.http.Request

/**
 * Runtime store of objects that need to be kept at Request Scope
 */
object RequestStore {
    /** The actual map of values */
    private val values = HashMap.empty[Request, HashMap[String, Any]]
    /**
     * Start handling the request
     * @param request The request to start handling
     */
    def startRequest(request: Request) {
        values += (request -> HashMap.empty[String, Any])
    }
    /**
     * Stop handling the request
     * @param request The request to stop handling
     */
    def stopRequest(request: Request) {
        values -= request
    }
    /**
     * Add a new object to the store
     * @param request The request we are working against
     * @param name The name to store the object under
     * @param val The value to store
     */
    def set(request: Request, name: String, value: Any) {
        values(request) += (name -> value)
    }

    /**
     * Get a value from the store
     * @param request The request we are working against
     * @param name The name to store the object under
     * @return the value
     */
    def get(request: Request, name: String): Option[Any] = {
        values(request).get(name)
    }
    /**
     * Remove an object to the store
     * @param request The request we are working against
     * @param name The name to store the object under
     */
    def remove(request: Request, name: String) {
        values(request) -= name
    }
}

/**
 * Filter to manage the Request Store for the lifespan of requests
 */
class RequestStoreFilter extends SimpleFilter[Request, Response] with App {
    def apply(request: Request, service: Service[Request, Response]) = {
        RequestStore.startRequest(request)
        val response = service(request)
        RequestStore.stopRequest(request)

        response
    }
}

