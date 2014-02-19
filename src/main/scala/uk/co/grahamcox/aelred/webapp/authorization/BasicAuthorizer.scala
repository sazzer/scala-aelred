package uk.co.grahamcox.aelred.webapp.authorization

import org.apache.commons.codec.binary.Base64

case class BasicCredentials(username: String, password: String) extends Credentials
/**
 * Authorizer for understanding HTTP Basic Authorization as described in RFC 2617
 */
class BasicAuthorizer extends MethodAuthorizer("Basic") {
    private val Pattern = "^(.*?):(.*?)$".r

    /**
     * Authorize the given credentials after they have been extracted from the header
     * @param credentials the credentials to authorize
     * @return The credentials represented by this header
     */
    def authorizeCredentials(credentials: String): Credentials = {
        if (!Base64.isBase64(credentials)) {
            throw new DecodeException
        }
        new String(Base64.decodeBase64(credentials)) match {
            case Pattern(username, password) => BasicCredentials(username, password)
            case _ => throw new DecodeException
        }
        
    }
}
