/*
 * Copyright 2015-2020 Noel Welsh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
lazy val scala213 = "2.13.8"
lazy val scala3 = "3.1.2"

ThisBuild / crossScalaVersions := List(scala3, scala213)
ThisBuild / scalaVersion := crossScalaVersions.value.head
ThisBuild / useSuperShell := false
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

// enablePlugins(AutomateHeaderPlugin)

// Run this (build) to do everything involved in building the project
commands += Command.command("build") { state =>
  "dependencyUpdates" ::
    "compile" ::
    "test" ::
    "golden/test" ::
    "scalafixAll" ::
    "scalafmtAll" ::
    state
}

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    Dependencies.munit.value,
    Dependencies.miniTest.value,
    Dependencies.miniTestLaws.value
  ),
  scalacOptions ++= Seq("-release", "8"),
  javacOptions ++= Seq("-source", "8", "-target", "8"),
  startYear := Some(2015),
  licenses := List(
    "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
  ),
  libraryDependencies ++= (
    if (scalaBinaryVersion.value == "2.13")
      compilerPlugin(
        "org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full
      ) :: Nil
    else Nil
  )
)

lazy val root = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(
    moduleName := "doodle",
    paradoxTheme := Some(builtinParadoxTheme("generic")),
    ScalaUnidoc / unidoc / unidocProjectFilter :=
      inAnyProject -- inProjects(
        coreJs,
        interactJs,
        examplesJs,
        golden,
        imageJs,
        plotJs,
        reactorJs,
        turtleJs
      )
  )
  .jvmSettings(
    console / initialCommands := """
      |import cats.instances.all._
      |import doodle.java2d._
      |import doodle.syntax.all._
      |import doodle.effect.Writer._
      |import doodle.examples._
      |import doodle.image._
      |import doodle.image.syntax.all._
      |import doodle.image.examples._
      |import doodle.interact.syntax.all._
      |import doodle.core._
      |import cats.effect.unsafe.implicits.global
    """.trim.stripMargin,
    console / cleanupCommands := """
      |doodle.java2d.effect.Java2dRenderer.stop()
    """.trim.stripMargin
  )
  .enablePlugins(ScalaUnidocPlugin)
lazy val rootJvm = root.jvm
  .dependsOn(
    coreJvm,
    java2d,
    imageJvm,
    interactJvm,
    reactorJvm,
    turtleJvm,
    golden
  )
  .aggregate(
    coreJvm,
    java2d,
    imageJvm,
    interactJvm,
    reactorJvm,
    turtleJvm,
    golden
  )
lazy val rootJs = root.js
  .dependsOn(coreJs, imageJs, interactJs, reactorJs, turtleJs)
  .aggregate(coreJs, imageJs, interactJs, reactorJs, turtleJs)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .in(file("core"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      Dependencies.catsCore.value,
      Dependencies.catsEffect.value,
      Dependencies.catsFree.value
    ),
    moduleName := "doodle-core"
  )

lazy val coreJvm = core.jvm
lazy val coreJs = core.js

lazy val docs = project
  .in(file("docs"))
  .settings(
    mdocIn := file("docs/src/main/mdoc"),
    mdocOut := file("docs/src/main/paradox"),
    (Compile / paradoxProperties) ++= Map(
      "scaladoc.base_url" -> ".../api/"
    ),
    mdocVariables := Map("VERSION" -> version.value),
    // Something in mdoc is creating code using deprecated + for string
    // concatenation. Turn off fatal warnings so we can compile the
    // documentation.
    scalacOptions ~= (_ filterNot (flag => flag == "-Xfatal-warnings"))
  )
  .enablePlugins(MdocPlugin, ParadoxPlugin)
  .dependsOn(rootJvm)

lazy val copyScalaDoc =
  taskKey[Unit]("Copy ScalaDoc to mdoc's expected location")
docs / copyScalaDoc := {
  println("Copying Scaladoc")
  sbt.io.IO.copyDirectory(
    file("jvm/target/scala-3.1.0/unidoc/"),
    file("docs/src/main/mdoc/api")
  )
}
lazy val copyFinalDoc = taskKey[Unit]("Copy site to expected location")
docs / copyFinalDoc := {
  println("Copying documentation to docs/target/docs/")
  sbt.io.IO.copyDirectory(
    file("docs/target/paradox/site/main"),
    file("docs/target/docs")
  )
  sbt.io.IO.copyDirectory(
    file("docs/src/main/img"),
    file("docs/target/docs/img")
  )
}
lazy val documentation = taskKey[Unit]("Generate documentation")
docs / documentation :=
  Def
    .sequential(
      (rootJvm / Compile / unidoc),
      (docs / copyScalaDoc),
      (docs / Compile / mdoc).toTask(""),
      (docs / Compile / paradox).toTask,
      (docs / copyFinalDoc)
    )
    .value

lazy val interact = crossProject(JSPlatform, JVMPlatform)
  .in(file("interact"))
  .settings(
    commonSettings,
    libraryDependencies += Dependencies.fs2.value,
    moduleName := "doodle-interact"
  )

lazy val interactJvm =
  interact.jvm.dependsOn(coreJvm % "compile->compile;test->test")
lazy val interactJs =
  interact.js.dependsOn(coreJs % "compile->compile;test->test")

lazy val java2d = project
  .in(file("java2d"))
  .settings(
    commonSettings,
    moduleName := "doodle-java2d",
    libraryDependencies ++= Seq(
      "de.erichseifert.vectorgraphics2d" % "VectorGraphics2D" % "0.13"
    ),
    libraryDependencies += Dependencies.fs2.value,
    libraryDependencies ++=
      (if (scalaBinaryVersion == "2.13")
         List(
           "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
         )
       else Nil)
  )
  .dependsOn(coreJvm, interactJvm)

lazy val image = crossProject(JSPlatform, JVMPlatform)
  .in(file("image"))
  .settings(commonSettings, moduleName := "doodle-image")

lazy val imageJvm = image.jvm.dependsOn(coreJvm, java2d)
lazy val imageJs = image.js.dependsOn(coreJs)

lazy val plot = crossProject(JSPlatform, JVMPlatform)
  .in(file("plot"))
  .settings(commonSettings, moduleName := "doodle-plot")

lazy val plotJvm = plot.jvm.dependsOn(coreJvm, interactJvm)
lazy val plotJs = plot.js.dependsOn(coreJs, interactJs)

lazy val turtle = crossProject(JSPlatform, JVMPlatform)
  .in(file("turtle"))
  .settings(commonSettings, moduleName := "doodle-turtle")

lazy val turtleJvm = turtle.jvm.dependsOn(coreJvm, imageJvm)
lazy val turtleJs = turtle.js.dependsOn(coreJs, imageJs)

lazy val reactor = crossProject(JSPlatform, JVMPlatform)
  .in(file("reactor"))
  .settings(
    commonSettings,
    libraryDependencies += Dependencies.fs2.value,
    moduleName := "doodle-reactor"
  )

lazy val reactorJvm =
  reactor.jvm.dependsOn(coreJvm, java2d, imageJvm, interactJvm)
lazy val reactorJs = reactor.js.dependsOn(coreJs, imageJs, interactJs)

// Just for testing
lazy val golden = project
  .in(file("golden"))
  .settings(
    commonSettings,
    moduleName := "doodle-golden",
    libraryDependencies ++= Seq(
      Dependencies.munit.value,
      Dependencies.batik.value
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
  .dependsOn(coreJvm, imageJvm, interactJvm, java2d)

// To avoid including this in the core build
lazy val examples = crossProject(JSPlatform, JVMPlatform)
  .in(file("examples"))
  .settings(
    commonSettings,
    moduleName := "doodle-examples"
  )

lazy val examplesJvm =
  examples.jvm.dependsOn(coreJvm, interactJvm, imageJvm, java2d)
lazy val examplesJs = examples.js.dependsOn(coreJs, interactJs, imageJs)
