package example

import zio.http.{Client, Request, URL}
import zio.{IO, ZIO, ZIOAppDefault}

object Hello extends ZIOAppDefault {

  private val program: ZIO[Client, Throwable, Unit] = for {
    client <- ZIO.service[Client]
    _ <- ZIO.succeed(println(s"Starting my-app..."))

    url <- ZIO.fromEither(URL.fromString("https://www.github.com"))
    response <- client.request(Request.get(url))
    _ <- ZIO.succeed(println(response.status))
    bodyResponse <- response.body.asString
    _ <- ZIO.succeed(println(bodyResponse))

    _ <- ZIO.succeed(println(s"Closing my-app..."))
  } yield ()

  override def run: IO[Throwable, Unit] = {
    program
      .provide(
        Client.default, // Provided to use ZClient
        OTELService.live.unit, // Force load it even if not required by something else
        PrometheusMetricReader.live
        // Uncomment to display dependency tree
        // zio.ZLayer.Debug.tree
      )
      .tapError(throwable =>
        ZIO.succeed(println("Error in the program", throwable))
      )
      .tapDefect(defect =>
        ZIO.succeed(println(s"Defect in the program: ${defect.prettyPrint}"))
      )
  }

}
