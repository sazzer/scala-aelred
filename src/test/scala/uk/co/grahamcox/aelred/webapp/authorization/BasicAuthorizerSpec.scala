package uk.co.grahamcox.aelred.webapp.authorization

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.OptionValues._

class BasicAuthorizerSpec extends FunSpec with ShouldMatchers {
    describe("The Basic Authorizer") {
        val authorizer = new BasicAuthorizer()
        describe("When decoding username:password") {
            val decoded = authorizer.authorize("Basic dXNlcm5hbWU6cGFzc3dvcmQ=")
            it("Should be valid") {
                decoded should be('defined)
            }
            it("Should be a BasicCredentials object with the right values") {
                decoded.value should be(BasicCredentials("username", "password"))
            }
        }
        describe("When decoding username:password:extra") {
            val decoded = authorizer.authorize("Basic dXNlcm5hbWU6cGFzc3dvcmQ6ZXh0cmE=")
            it("Should be valid") {
                decoded should be('defined)
            }
            it("Should be a BasicCredentials object with the right values") {
                decoded.value should be(BasicCredentials("username", "password:extra"))
            }
        }
        describe("When decoding a badly encoded value") {
            it("Should throw") {
                evaluating {
                    authorizer.authorize("Basic abcd")
                } should produce [DecodeException]
            }
        }
        describe("When decoding the wrong scheme") {
            val decoded = authorizer.authorize("Digest asdf")
            it("Should not be valid") {
                decoded should not be('defined)
            }
        }
    }
}

