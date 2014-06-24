package dvla.common.soap

import javax.xml.ws.{Response => JaxwsResponse, AsyncHandler}
import scala.concurrent.{Promise, Future}
import scala.util.Try

// This code comes from https://github.com/vmencik/soap-async/blob/master/play-client/app/controllers/AsyncSoapController.scala
object JaxwsAsyncAdapter {

  /**
   * Returns a Future containing a result of an asynchronous JAX-WS proxy method invocation.
   * @param invoker function that invokes a JAX-WS proxy method with the specified AsyncHandler
   * @return Future containing the eventual result of JAX-WS proxy method invocation
   */
  def invoke[A](invoker: AsyncHandler[A] => Unit): Future[A] = {
    val promise = Promise[A]()
    val handler = new AsyncHandler[A] {
      // javax.xml.ws.Response is a java.util.concurrent.Future implementation
      // that might throw an exception (like java.util.concurrent.ExecutionException)
      // when the computation result is retrieved (response.get)
      override def handleResponse(response: JaxwsResponse[A]) = promise.complete(Try(response.get))
    }

    invoker(handler)
    promise.future
  }

}