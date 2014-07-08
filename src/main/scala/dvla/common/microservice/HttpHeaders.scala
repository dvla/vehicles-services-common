package dvla.common.microservice

import spray.http.HttpHeaders.RawHeader

trait HeaderObject {
  val name = {
    val n = getClass.getName
    n.substring(n.indexOf('$') + 1, n.length - 1).replace("$minus", "-")
  }
}

object HttpHeaders {

  object `Tracking-Id` extends HeaderObject {
    def apply(value: String) = RawHeader(name, value)
  }

  object `X-Real-Ip` extends HeaderObject {
    def apply(value: String) = RawHeader(name, value)
  }
}