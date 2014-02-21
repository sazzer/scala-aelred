package uk.co.grahamcox.aelred.users

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class PasswordSpec extends FunSpec with ShouldMatchers {
    describe("A Password of '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8'") {
        val password = Password("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8")
        it("Should be encoded correctly") {
            password.encoded should be("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8")
        }
        it("Should match a password of 'password'") {
            password should be(Password.encode("password"))
        }
    }
    describe("A Password of plaintext 'password'") {
        val password = Password.encode("password")
        it("Should be encoded correctly") {
            password.encoded should be("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8")
        }
        it("Should match an encoded password") {
            password should be(Password("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8"))
        }
        it("Should match a password of 'password'") {
            password should be(Password.encode("password"))
        }
    }
}




