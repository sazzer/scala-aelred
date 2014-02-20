package uk.co.grahamcox.aelred.oauth2

/**
 * Representation of the Credentials of a Client
 * @param key the Client Key. 
 * @param secret the Client Secret. This may be an empty string if no secret is known
 */
case class ClientCredentials(key: String, secret: String)

/**
 * Representation of the details of a Client
 */
class ClientDetails {
}

/**
 * Service to manage Clients with
 */
class ClientService {
    /**
     * Check if the credentials of a given Client are still valid
     * @param credentials The credentials of the client
     * @return True if the credentials are valid. False if not
     */
    def isClientValid(credentials: ClientCredentials): Boolean = false
}
