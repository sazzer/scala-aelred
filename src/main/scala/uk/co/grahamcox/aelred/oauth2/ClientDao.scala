package uk.co.grahamcox.aelred.oauth2

import com.mongodb.casbah.Imports._

/**
 * DAO for accessing Client Details
 * @param mongoClient the MongoDB Client
 */
class ClientDao(mongoDB: MongoDB) {
    /** The MongoDB Collection to use */
    private val collection = mongoDB("clients")

    /**
     * Get the Client Details with the given Key
     * @param key The key of the client to get
     * @return the client details
     */
    def getByKey(key: String): Option[ClientDetails] = {
        collection.findOne(MongoDBObject("key" -> key)).map {
            doc => {
                new ClientDetails(
                    key = doc("key").toString,
                    secret = doc match {
                        case doc if doc.containsField("secret") => Some(doc("secret").toString)
                        case _ => None
                    }
                )
            }
        }
    }
}
