package dvla.common.soap

import javax.xml.ws.{Response => JaxwsResponse, AsyncHandler}
import scala.concurrent.{Promise, Future}
import scala.util.Try

object JaxwsAsyncAdapter {

  def invoke[A](invoker: AsyncHandler[A] => Unit): Future[A] = {
    val promise = Promise[A]()
    val handler = new AsyncHandler[A] {
      override def handleResponse(response: JaxwsResponse[A]) = promise.complete(Try(response.get))
    }

    invoker(handler)
    promise.future
  }
}