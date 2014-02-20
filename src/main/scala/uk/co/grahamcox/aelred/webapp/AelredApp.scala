package uk.co.grahamcox.aelred.webapp

import scala.collection.JavaConversions._
import com.twitter.finatra._
import org.springframework.context.support.ClassPathXmlApplicationContext
import com.twitter.finagle.Filter
import com.twitter.finagle.http.{Request, Response}

/**
 * The core application. This is the actual server that does all of the work
 */
object AelredApp extends FinatraServer {
    def startup() {
        val context = new ClassPathXmlApplicationContext("classpath:/uk/co/grahamcox/aelred/spring/aelred.xml")

        context.getBean("filters", classOf[Seq[Filter[Request, Response, Request, Response]]]).foreach { filter => 
            addFilter(filter)
        }

        context.getBeansOfType(classOf[Controller]).foreach { case (name, controller) =>
            register(controller)
        }
    }

    startup()
}

