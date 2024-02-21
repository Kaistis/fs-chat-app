name := "fs-chat-app"

version := "1.0"

scalaVersion := s"2.13.12"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

lazy val akkaVersion = "2.9.0"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.11",
  "com.typesafe.akka" %% "akka-http" % "10.6.0",
  "com.typesafe.akka" %% "akka-stream" % "2.9.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.6.0",
  "ch.megard" %% "akka-http-cors" % "1.2.0",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.postgresql" % "postgresql" % "42.7.1",
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.12" % Test
)
