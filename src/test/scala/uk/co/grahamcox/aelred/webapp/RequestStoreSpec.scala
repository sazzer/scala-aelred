package uk.co.grahamcox.aelred.webapp

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.OptionValues._
import com.twitter.finagle.http.Request

class RequestStoreSpec extends FunSpec with ShouldMatchers {
    describe("When the request has started") {
        val request = Request("http://www.google.com")
        RequestStore.startRequest(request)
        describe("Getting an unknown value") {
            val value = RequestStore.get(request, "unknown")
            it("Should not be defined") {
                value should not be('defined)
            }
        }
        describe("Storing a value") {
            RequestStore.set(request, "value", 1)
            describe("And retrieving it") {
                val value = RequestStore.get(request, "value")
                it("Should be defined") {
                    value should be('defined)
                }
                it("Should be the correct value") {
                    value.value should be(1)
                }
            }
            describe("And removing it") {
                RequestStore.remove(request, "value")
                val value = RequestStore.get(request, "value")
                it("Should not be defined") {
                    value should not be('defined)
                }
            }
        }
    }
    describe("When the request has not started") {
        val request = Request("http://www.google.com")
        describe("Getting a value") {
            it("Should throw") {
                evaluating {
                    val value = RequestStore.get(request, "unknown")
                } should produce [NoSuchElementException]
            }
        }
        describe("Setting a value") {
            it("Should throw") {
                evaluating {
                    RequestStore.set(request, "value", 1)
                } should produce [NoSuchElementException]
            }
        }
        describe("Removing a value") {
            it("Should throw") {
                evaluating {
                    RequestStore.remove(request, "value")
                } should produce [NoSuchElementException]
            }
        }
    }
    describe("When the request has been started and stopped") {
        val request = Request("http://www.google.com")
        RequestStore.startRequest(request)
        RequestStore.stopRequest(request)
        describe("Getting a value") {
            it("Should throw") {
                evaluating {
                    val value = RequestStore.get(request, "unknown")
                } should produce [NoSuchElementException]
            }
        }
        describe("Setting a value") {
            it("Should throw") {
                evaluating {
                    RequestStore.set(request, "value", 1)
                } should produce [NoSuchElementException]
            }
        }
        describe("Removing a value") {
            it("Should throw") {
                evaluating {
                    RequestStore.remove(request, "value")
                } should produce [NoSuchElementException]
            }
        }
    }
}
