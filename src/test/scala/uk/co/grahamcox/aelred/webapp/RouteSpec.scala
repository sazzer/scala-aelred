package uk.co.grahamcox.aelred.webapp

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class DebugControllerSpec extends FunSpec with ShouldMatchers {
    describe("A Route of /a/b") {
        val route = new Route("/a/b")

        describe("When parsing") {
            describe("/a/b") {
                val parsed = route.parse("/a/b")
                it("Should match") {
                    parsed should be ('defined)
                }
                it("Should return no parameters") {
                    parsed should be(Some(Map.empty[String, String]))
                }
            }

            describe("/a/b/c") {
                val parsed = route.parse("/a/b/c");
                it("Should not match") {
                    parsed should not be ('defined)
                }
            }

            describe("/a/c") {
                val parsed = route.parse("/a/c");
                it("Should not match") {
                    parsed should not be ('defined)
                }
            }

            describe("/b/b") {
                val parsed = route.parse("/b/b");
                it("Should not match") {
                    parsed should not be ('defined)
                }
            }
        }

        describe("When building") {
            describe("with no params") {
                val built = route.build(Map.empty[String, String])
                it("Should build") {
                    built should be ('defined)
                }
                it("Should built /a/b") {
                    built should be(Some("/a/b"))
                }
            }
            describe("with a param") {
                val built = route.build(Map("a" -> "b"))
                it("Should build") {
                    built should be ('defined)
                }
                it("Should built /a/b") {
                    built should be(Some("/a/b?a=b"))
                }
            }
        }

    }
    describe("A Route of /a/:b") {
        val route = new Route("/a/:b")

        describe("When parsing") {
            describe("/a/b") {
                val parsed = route.parse("/a/b")
                it("Should match") {
                    parsed should be ('defined)
                }
                it("Should return one parameter") {
                    parsed should be(Some(Map("b" -> "b")))
                }
            }

            describe("/a/b/c") {
                val parsed = route.parse("/a/b/c");
                it("Should not match") {
                    parsed should not be ('defined)
                }
            }

            describe("/b/b") {
                val parsed = route.parse("/b/b");
                it("Should not match") {
                    parsed should not be ('defined)
                }
            }
        }

        describe("When building") {
            describe("with no params") {
                val built = route.build(Map.empty[String, String])
                it("Should not build") {
                    built should not be ('defined)
                }
            }
            describe("with the wrong params") {
                val built = route.build(Map("c" -> "d"))
                it("Should not build") {
                    built should not be ('defined)
                }
            }
        }
    }
    describe("A Route of /a/:b/:c/:d") {
        val route = new Route("/a/:b/:c/:d")

        describe("When parsing") {
            describe("/a/1/2/3") {
                val parsed = route.parse("/a/1/2/3")
                it("Should match") {
                    parsed should be ('defined)
                }
                it("Should return one parameter") {
                    parsed should be(Some(Map("b" -> "1", "c" -> "2", "d" -> "3")))
                }
            }

            describe("/a/b/c") {
                val parsed = route.parse("/a/b/c");
                it("Should not match") {
                    parsed should not be ('defined)
                }
            }

            describe("/b/b") {
                val parsed = route.parse("/b/b");
                it("Should not match") {
                    parsed should not be ('defined)
                }
            }
        }

        describe("When building") {
            describe("with no params") {
                val built = route.build(Map.empty[String, String])
                it("Should not build") {
                    built should not be ('defined)
                }
            }
            describe("with some params") {
                val built = route.build(Map("a" -> "b", "c" -> "d"))
                it("Should not build") {
                    built should not be ('defined)
                }
            }
            describe("with all params") {
                val built = route.build(Map("b" -> "1", "c" -> "2", "d" -> "3"))
                it("Should build") {
                    built should be ('defined)
                }
                it("Should build the correct URL") {
                    built should be(Some("/a/1/2/3"))
                }
            }
        }
    }
}



