package example

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.semconv.ResourceAttributes
import zio.ZLayer

object OTELService {

  val live: ZLayer[PrometheusMetricReader, Nothing, OpenTelemetry] =
    ZLayer.fromFunction { (prometheusMetricReader: PrometheusMetricReader) =>
      new OTELService(prometheusMetricReader).getApi()
    }

}

class OTELService(prometheusMetricReader: PrometheusMetricReader) {

  println("Initializing OpenTelemetry...")

  private val reportingApiResource: Resource = Resource
    .getDefault()
    .merge(
      Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "my-app"))
    )

  private val meterProvider: SdkMeterProvider = SdkMeterProvider
    .builder()
    .setResource(reportingApiResource)
    .registerMetricReader(prometheusMetricReader.prometheusServer)
    .build()

  private val openTelemetrySdk: OpenTelemetrySdk = OpenTelemetrySdk
    .builder()
    .setMeterProvider(meterProvider)
    .buildAndRegisterGlobal()

  def getApi(): OpenTelemetry = openTelemetrySdk

}
