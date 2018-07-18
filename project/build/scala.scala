import sbt.Keys._
import sbt._

object ApplicationBuild extends Build {

  val appName = "dataplusplus"
  val version = "0.0.1"

  javacOptions += "-Xmx2G"

  lazy val offers = Project("dataplusplus", base = file("dataplusplus"))

  val main = Project(id = "dataplusplus", base = file(".")) aggregate(offers)
}