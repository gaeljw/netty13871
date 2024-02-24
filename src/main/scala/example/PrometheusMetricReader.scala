package example

import io.opentelemetry.exporter.prometheus.PrometheusHttpServer
import zio.{ZIO, ZLayer}

object PrometheusMetricReader {

  val live: ZLayer[Any, Throwable, PrometheusMetricReader] =
    ZLayer.scoped {
      ZIO.acquireRelease {
        startReader()
      } { reader =>
        ZIO.succeed(reader.prometheusServer.close())
      }
    }

  private def startReader(): ZIO[Any, Throwable, PrometheusMetricReader] = {
    for {
      _ <- ZIO.succeed(println("Starting Prometheus server..."))
      server <- ZIO.attempt(PrometheusHttpServer.builder().setPort(10170).build())
    } yield new PrometheusMetricReader(server)
  }

}

class PrometheusMetricReader(val prometheusServer: PrometheusHttpServer) {}
