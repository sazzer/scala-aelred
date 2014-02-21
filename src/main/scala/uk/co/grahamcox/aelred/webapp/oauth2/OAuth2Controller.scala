package uk.co.grahamcox.aelred.webapp.oauth2

import uk.co.grahamcox.aelred.oauth2._
import uk.co.grahamcox.aelred.webapp.authorization._
import uk.co.grahamcox.aelred.webapp.RequestStore
import com.twitter.finatra._
import com.twitter.finatra.ContentType._

/**
 * Response object to send if an error occurred
 * @param error The error code
 * @param error_description The description of the error
 * @param error_uri A URI pointing to the description of the error
 */
class ErrorResponse(error: String, errorDescription: Option[String] = None, errorUri: Option[String] = None) {
    def toMap = Map("error" -> error, 
        "error_description" -> errorDescription, 
        "error_uri" -> errorUri)
}
/**
 * Response object to send if an access token is granted
 */
class AccessTokenResponse(accessToken: String, tokenType: String = "bearer", expires: Option[Int] = None, refreshToken: Option[String] = None, scope: Option[String] = None) {
    def toMap = Map("access_token" -> accessToken,
        "token_type" -> tokenType, 
        "expires_in" -> expires,
        "refresh_token" -> refreshToken,
        "scope" -> scope)
}
/**
 * Base class for errors caused by requesting an Access Token
 * @param error The error code
 * @param errorDescription A description of the error
 */
class AccessTokenException(val error: String, val errorDescription: Option[String] = None) extends Exception

/**
 * Error that means that the grant type was unsupported
 */
class UnsupportedGrantType extends AccessTokenException("unsupported_grant_type", Some("The specified grant type is unsupported by this server"))

/**
 * Error indicating that some field was missing from the request
 * @param errorDescription A description of the error
 */
class InvalidRequest(errorDescription: Option[String]) extends AccessTokenException("invalid_request", errorDescription)

/**
 * Error indicating that the client credentials were invalid
 */
class InvalidClient() extends AccessTokenException("invalid_client", Some("The client credentials were invalid"))

/**
 * Error that means that the grant type was missing
 */
class MissingGrantType extends AccessTokenException("invalid_request", Some("Missing field: grant_type"))

/**
 * Controller for OAuth 2.0 Requests
 */
class OAuth2Controller(clientService: ClientService) extends Controller {
    error { request =>
        request.error match {
            case Some(e:InvalidClient) =>
                render.status(401).json(new ErrorResponse(error = e.error, errorDescription = e.errorDescription).toMap).toFuture
            case Some(e:AccessTokenException) =>
                render.status(400).json(new ErrorResponse(error = e.error, errorDescription = e.errorDescription).toMap).toFuture
            case _ => render.status(500).plain("Unexpected error").toFuture
        }
    }
    post("/oauth2/token") { 
        request => { 
            val clientDetails = getClientDetails(request)

            val accessToken = request.params.get("grant_type") match {
                case Some("password") => resourceOwnerPasswordCredentialsGrant(request, clientDetails)
                case Some(_) => throw new UnsupportedGrantType
                case None => throw new MissingGrantType
            }
            render.json(accessToken.toMap).toFuture
        }
    }

    /**
     * Get the Client Details for the request. If no client credentials are present
     * in the request then None is returned. If the client credentials are present but
     * not valid then an exception is raised instead
     * @param request The request to use to get the details
     * @return the client details if applicable
     */
    def getClientDetails(request: Request): Option[ClientDetails] = {
        val authorization = RequestStore.get(request, "Credentials")
        authorization match {
            case Some(credentials: BasicCredentials) => {
                clientService.getClientDetails(ClientCredentials(credentials.username, credentials.password)) match {
                    case None => throw new InvalidClient
                    case details => details
                }
            }
            case _ => None
        }
    }

    /**
     * Handle a request to perform a Resource Owner Password Credentials Grant - taken from the OAuth 2.0 Spec section 4.3
     * @param request The request to service
     * @param clientDetails The client details, if available
     * @return The details of the Access Token
     */
    def resourceOwnerPasswordCredentialsGrant(request: Request, clientDetails: Option[ClientDetails]): AccessTokenResponse = {
        clientDetails match {
            case Some(cd) if !cd.supports(SupportedAuthTypes.ResourceOwnerPasswordCredentials) => throw new InvalidClient
            case _ => {}
        }
        val username = request.params.get("username") match {
            case Some(u) => u
            case None => throw new InvalidRequest(Some("Missing field: username"))
        }
        val password = request.params.get("password") match {
            case Some(p) => p
            case None => throw new InvalidRequest(Some("Missing field: password"))
        }
        val scopes = request.params.getOrElse("scope", "").split(" ")

        new AccessTokenResponse(accessToken = "abcdef",
            tokenType = "bearer",
            refreshToken = Some("ghijkl"),
            expires = Some(3600),
            scope = Some(scopes.mkString(" ")))
    }
}

