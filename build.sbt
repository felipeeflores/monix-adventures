name := "monix-adventures"

version := "0.1"

scalaVersion := "2.13.4"

val specs2Version = "4.10.5"
val monixVersion = "3.3.0"
val catsVersion = "2.3.0"

libraryDependencies ++= Seq(
  "io.monix"                        %% "monix"                                 % monixVersion,
  "org.typelevel"                   %% "cats-core"                             % catsVersion,
  "org.specs2"                      %% "specs2-core"                           % specs2Version        % "test",
  "org.specs2"                      %% "specs2-matcher-extra"                  % specs2Version        % "test"
)

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xlint",
  "-language:higherKinds",
  "-language:postfixOps")
