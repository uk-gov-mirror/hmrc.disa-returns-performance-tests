import sbt.*

object Dependencies {

  val test = Seq(
    "uk.gov.hmrc"       %% "performance-test-runner" % "6.2.0"  % Test,
    "com.typesafe.play" %% "play-json"               % "2.10.6" % Test
  )

}
