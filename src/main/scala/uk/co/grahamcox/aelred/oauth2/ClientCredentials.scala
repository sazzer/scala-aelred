package uk.co.grahamcox.aelred.oauth2

import uk.co.grahamcox.aelred.users._

/**
 * Representation of the Credentials of a Client
 * @param key the Client Key. 
 * @param secret the Client Secret. This may be an empty string if no secret is known
 */
case class ClientCredentials(key: String, secret: String)

/**
 * Enumeration of the supported authentication types supported by a client
 */
object SupportedAuthTypes extends Enumeration {
    type SupportedAuthTypes = Value
    val AuthorizationCode, 
        Implicit, 
        ResourceOwnerPasswordCredentials, 
        ClientCredentials, 
        Refresh = Value
}

import SupportedAuthTypes._

/**
 * Representation of the details of a Client
 * @param key the Client Key. 
 * @param secret the Client Secret
 */
class ClientDetails(val key: String, val secret: Option[String], val supportedAuthTypes: Seq[SupportedAuthTypes] = Nil, val clientCredentialsUserId: Option[UserId] = None) {
    /**
     * Check if this client supports the requested authentication type
     * @param authType The authentication type to check for
     * @return True if it is supported. False if not
     */
    def supports(authType: SupportedAuthTypes): Boolean = supportedAuthTypes.contains(authType)
}

/**
 * Service to manage Clients with
 */
class ClientService {
    /** Mock map of client details */
    private val details = Map("client" -> new ClientDetails("client", Some("secret"), Seq(SupportedAuthTypes.ClientCredentials, SupportedAuthTypes.ResourceOwnerPasswordCredentials), Some(UserId("2"))))
    /**
     * Get the Client Details for the given Client ID
     * @param key The Client ID to get the details for
     * @return the Client Details, if they can be loaded
     */
    def getClientDetails(key: String): Option[ClientDetails] = details.get(key)
    /**
     * Get the Client Details for the given Client Credentials
     * @param credentials The credentials of the client
     * @return the Client Details, if they can be loaded
     */
    def getClientDetails(credentials: ClientCredentials): Option[ClientDetails] = {
        getClientDetails(credentials.key).filter {
            details => details.secret match {
                case Some(secret) => secret == credentials.secret
                case None => true
            }
        }
    }
}
