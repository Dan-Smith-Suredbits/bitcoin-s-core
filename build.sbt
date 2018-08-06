
lazy val root = project
    .in(file("."))
    .aggregate(
      secp256k1jni,
      core,
      coreGen,
      coreTest,
      zmq,
      rpc
    )

lazy val secp256k1jni = project
  .in(file("secp256k1jni"))
  .enablePlugins()

lazy val core = project
  .in(file("core"))
  .enablePlugins()
  .dependsOn(
    secp256k1jni
  )

lazy val coreGen = project
  .in(file("core-gen"))
  .enablePlugins()
  .dependsOn(
    core
  )

lazy val coreTest = project
  .in(file("core-test"))
  .enablePlugins()
  .dependsOn(
    core,
    coreGen % "test->test"
  )

lazy val zmq = project
  .in(file("zmq"))
  .enablePlugins()
  .dependsOn(
    core
  )

lazy val rpc = project
  .in(file("rpc"))
  .enablePlugins()
  .dependsOn(
    core,
    coreGen % "test->test"
  ).settings(
    testOptions in Test += Tests.Argument("-oF")
  )

publishArtifact in root := false
