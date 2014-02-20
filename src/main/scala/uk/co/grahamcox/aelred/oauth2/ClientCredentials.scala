package uk.co.grahamcox.aelred.oauth2

/**
 * Representation of the Credentials of a Client
 * @param key the Client Key. 
 * @param secret the Client Secret. This may be an empty string if no secret is known
 */
case class ClientCredentials(key: String, secret: String)

/**
 * Representation of the details of a Client
 * @param key the Client Key. 
 * @param secret the Client Secret
 */
class ClientDetails(val key: String, val secret: Option[String]) {
}

/**
 * Service to manage Clients with
 */
class ClientService {
    /** Mock map of client details */
    private val details = Map("client" -> new ClientDetails("client", Some("secret")))
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
