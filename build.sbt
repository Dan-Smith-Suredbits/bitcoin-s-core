cancelable in Global := true

lazy val commonCompilerOpts = {
  List(
    "-Xmax-classfile-name",
    "128"
  )
}
//https://docs.scala-lang.org/overviews/compiler-options/index.html
lazy val compilerOpts = Seq(
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
  "-unchecked",
  "-feature",
  "-deprecation",
  "-Xfuture",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard",
  "-Ywarn-unused",
  "-unchecked",
  "-deprecation",
  "-feature"
) ++ commonCompilerOpts

lazy val testCompilerOpts = commonCompilerOpts

lazy val commonSettings = List(
  scalacOptions in Compile := compilerOpts,
  scalacOptions in Test := testCompilerOpts,
  assemblyOption in assembly := (assemblyOption in assembly).value
    .copy(includeScala = false),

  bintrayOrganization := Some("bitcoin-s"),

  bintrayRepository := "bitcoin-s-core",

  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),

  resolvers += Resolver.bintrayRepo("bitcoin-s", "bitcoin-s-core")

)

lazy val root = project
  .in(file("."))
  .aggregate(
    secp256k1jni,
    core,
    coreTest,
    zmq,
    rpc,
    bench,
    eclairRpc,
    testkit
  )
  .settings(commonSettings: _*)


lazy val secp256k1jni = project
  .in(file("secp256k1jni"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Deps.secp256k1jni,
    unmanagedResourceDirectories in Compile += baseDirectory.value / "natives"
  )
  .enablePlugins()

lazy val core = project
  .in(file("core"))
  .enablePlugins()
  .settings(commonSettings: _*)
  .dependsOn(
    secp256k1jni
  )

lazy val coreTest = project
  .in(file("core-test"))
  .enablePlugins()
  .settings(commonSettings: _*)
  .dependsOn(
    core,
  )

lazy val zmq = project
  .in(file("zmq"))
  .enablePlugins()
  .settings(commonSettings: _*)
  .dependsOn(
    core
  )

lazy val rpc = project
  .in(file("rpc"))
  .enablePlugins()
  .settings(commonSettings: _*)
  .dependsOn(
    core
  )
  .settings(
    testOptions in Test += Tests.Argument("-oF")
  )

lazy val bench = project
  .in(file("bench"))
  .enablePlugins()
  .settings(assemblyOption in assembly := (assemblyOption in assembly).value
    .copy(includeScala = true))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Deps.bench,
    name := "bitcoin-s-bench"
  )
  .dependsOn(core)

lazy val eclairRpc = project
  .in(file("eclair-rpc"))
  .enablePlugins()
  .settings(commonSettings: _*)
  .dependsOn(
    core,
    rpc
  )

lazy val testkit = project
  .in(file("testkit"))
  .enablePlugins()
  .settings(commonSettings: _*)
  .dependsOn(
    core,
    rpc,
    eclairRpc
  )


publishArtifact in root := false
