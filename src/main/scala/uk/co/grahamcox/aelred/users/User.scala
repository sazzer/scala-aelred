package uk.co.grahamcox.aelred.users

import java.security.MessageDigest

/**
 * Representation of a User ID
 * @param id The actual User ID
 */
case class UserId(id: String)

/**
 * Representation of an encoded password
 * @param encoded The encoded password
 */
case class Password(val encoded: String)

/**
 * Companion object for passwords
 */
object Password {
    private val Sha1 = MessageDigest.getInstance("SHA-1")
    /**
     * Construct a password by encoding the provided plain text password
     * @param plaintext the password to encode
     * @return the Password
     */
    def encode(plaintext: String) = {
        val encoded = Sha1.digest(plaintext.getBytes("UTF-8")).map("%02x".format(_)).mkString
        new Password(encoded)
    }
}

/**
 * Representation of a User
 * @param id the unique ID of the user
 * @param username The username
 * @param password The encoded password
 * @param email The users email address
 */
class User(val id: UserId, val username: String, val password: Password, val email: String)

/**
 * Service to manage users with
 */
class UserService {
    private val users: Map[UserId, User] = Map(UserId("1") -> new User(UserId("1"), "graham", Password.encode("password"), "graham@grahamcox.co.uk"))
    /**
     * Get the User with the given ID
     * @param id The ID of the user
     * @return the user
     */
    def getById(id: UserId): Option[User] = users.get(id)

    /**
     * Get the User with the given Username
     * @param username The username of the user
     * @return the user
     */
    def getByUsername(username: String): Option[User] = (users.find {
        case (id, user) => user.username == username
    }).map {
        case (id, user) => user
    }

    /**
     * Get the User with the given Username and Password
     * @param username The username of the user
     * @param password The password of the user
     * @return the user
     */
    def authenticate(username: String, password: Password): Option[User] = 
        getByUsername(username).filter {
            user => user.password == password
        }
}
