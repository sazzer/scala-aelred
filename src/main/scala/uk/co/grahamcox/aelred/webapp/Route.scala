package uk.co.grahamcox.aelred.webapp

import com.twitter.finatra._

/**
 * Representation of a Route for parsing/generating URLs
 * @param path The path of the route
 */
class Route(val path: String) {
    val parser = SinatraPathPatternParser(path)
    /**
     * Parse a URL to determine the parameters that can be extracted from it.
     * These are both Query String parameters and Path Parameters as appropriate
     * @param url The URL String to parse
     * @return the parsed out parameters
     */
    def parse(url: String): Option[Map[String, String]] = parser(url) match {
        case Some(params) => Some(params.foldLeft(Map.empty[String, String]) {
            case (output, (_, Nil)) => output
            case (output, (k, v:Traversable[_])) => output ++ Map(k.toString() -> v.head.toString())
            case (output, (k, v)) => output ++ Map(k.toString() -> v.toString())
        })
        case None => None
    }
    /**
     * Build a URL from the provided parameters. Any parameters that don't fit into Path Parameters
     * will be added on as Query String parameters instead
     * @param params The parameters to mix into the URL
     * @return the URL
    */
    def build(params: Map[String, String]): Option[String] = {
        val (builtPath:String, qsParts: Map[_,_]) = params.foldLeft( (path, Map.empty[String, String]) ) {
            case ((path, qs), (k, v)) if path.contains(":" + k) => {
                (path.replaceAll(":" + k, v), qs)
            }
            case ((path, qs), e) => {
                (path, qs ++ Map(e))
            }
        }
        
        val query = qsParts.foldLeft("") {
            case (out, (k, v)) => {
                (out match {
                    case "" => "?"
                    case _ => out + "&"
                }) + k + "=" + v
            }
        }

        builtPath match {
            case bp if bp contains(":") => None
            case bp => Some(bp  + query)

        }
    }
}
