lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.6"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := baseDirectory.value.getName,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
      "com.typesafe.akka" %% "akka-testkit" % "2.4-SNAPSHOT",
      "com.typesafe.akka" %% "akka-persistence" % "2.4-SNAPSHOT",
      "org.iq80.leveldb" % "leveldb" % "0.7",
      "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
      "org.scalatest" %% "scalatest" % "2.2.4" % "test",
      "junit" % "junit" % "4.12" % "test",
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.scalaz" % "scalaz-core_2.10" % "7.1.4"
    ),
    resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
  )

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

//name in Compile := "hello in compile conf"
//
//name in packageBin := "hello in package bin task"
//
//name in packageSrc := "hello in package src task"
//
//name in (Compile, packageBin) := "hello in compile and packageBin"

// ProjectFilter, ConfigurationFilter, TaskFilter

val cf: ConfigurationFilter = (c: String) => c.startsWith("r")
val mf: ModuleFilter = (m: ModuleID) => m.organization contains "sbt"
val af: ArtifactFilter = (a: Artifact) => a.classifier.isEmpty

val df: DependencyFilter = cf && mf || af

lazy val hello = taskKey[Unit]("Prints 'Hello World'")

hello := println(df)

lazy val intTask = taskKey[Int]("An int task")
lazy val stringTask = taskKey[String]("A string task")
lazy val sampleTask = taskKey[Int]("An int task")

intTask := {
  val res: Int = 1 + 2
  println("int " + res)
  res
}

stringTask := "int: " + intTask.value

sampleTask in Test := (intTask in Test).value * 3
