package uk.co.grahamcox.aelred

import org.springframework.beans.factory.config.AbstractFactoryBean
import com.mongodb.casbah.Imports._

/**
 * Factory Bean for constructing the Mongo DB connection
 * This is needed because Spring can't natively call Scala Objects
 */
class MongoFactory extends AbstractFactoryBean[MongoDB] {
    /**
     * Actually create the instance of the MongoDB connection to use
     * @return the MongoDB connection
     */
    protected override def createInstance() = MongoClient("localhost", 27017)("aelred")

    /**
     * Determine the type of object that will be built
     * @return the class of object that will be built
     */
    override def getObjectType() = classOf[MongoDB]
}
