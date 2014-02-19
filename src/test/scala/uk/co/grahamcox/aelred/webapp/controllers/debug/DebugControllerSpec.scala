package uk.co.grahamcox.aelred.webapp.debug

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.twitter.finatra.test._
import com.twitter.finatra.FinatraServer
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.module.scala._

class DebugControllerSpec extends FlatSpecHelper {

  override val server = new FinatraServer
  server.register(new DebugController)

  def fromJson(json: String) = {
      val mapper = new ObjectMapper();
      mapper.registerModule(DefaultScalaModule)
      mapper.readValue(json, classOf[Map[String, Any]])
  }

  "GET /api/debug/version" should """respond with the expected value""" in {
    get("/api/debug/version")
    response.code should equal(200)

    fromJson(response.body) should have size(4)
    fromJson(response.body).get("groupId") should be(Some("uk.co.grahamcox.aelred"))
    fromJson(response.body).get("artifactId") should be(Some("aelred"))
    fromJson(response.body).get("version") should be(Some("0.0.1-SNAPSHOT"))
    fromJson(response.body).get("scalaVersion") should be(Some("2.10.3"))
  }
}


