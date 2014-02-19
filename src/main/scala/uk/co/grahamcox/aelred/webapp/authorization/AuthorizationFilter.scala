package uk.co.grahamcox.aelred.webapp.authorization

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import com.twitter.finagle.http.{Request, Response}
import com.twitter.app.App

/**
 * Trait to indicate the authorized credentials
 */
trait Credentials {
}

/**
 * Exception thrown if the Authorization header can not be decoded
 */
class DecodeException extends Exception

/**
 * Trait to indicate a class that can attempt authorization
 */
trait Authorizer {
    /**
     * Attempt to authorize the given Authorization Header, returning credentials as appropriate
     * @param header The Authorization Header to authorize
     * @return The credentials represented by this header, or None if we couldn't decode it
     */
    def authorize(header: String): Option[Credentials]
}
/**
 * Base class for Authorizers that have the credentials prefixed by a known Method. This is the common case and so
 * makes the stripping off of the Method easier to manage
 */
abstract class MethodAuthorizer(method: String) extends Authorizer {
    /** The Regex pattern to use to match the Authorization header */
    private val Pattern = s"^$method (.*)$$".r
    /**
     * Attempt to authorize the given Authorization Header, returning credentials as appropriate
     * @param header The Authorization Header to authorize
     * @return The credentials represented by this header, or None if we couldn't decode it
     */
    def authorize(header: String): Option[Credentials] = header match {
        case Pattern(credentials) => Some(authorizeCredentials(credentials))
        case _ => None
    }

    /**
     * Authorize the given credentials after they have been extracted from the header
     * @param credentials the credentials to authorize
     * @return The credentials represented by this header
     */
    def authorizeCredentials(credentials: String): Credentials
}
/**
 * Filter that can sort out the Authorization Header as needed
 */
class AuthorizationFilter extends SimpleFilter[Request, Response] with App {
    def apply(request: Request, service: Service[Request, Response]) = {
        println(request.authorization)
        service(request)
    }
}
