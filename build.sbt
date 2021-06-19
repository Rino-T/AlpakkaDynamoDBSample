lazy val root = (project in file("."))
  .settings(
    name := "AlpakkaDynamoDBSample",
    version := "0.1",
    scalaVersion := "2.13.6",
    libraryDependencies ++= {

      val AkkaVersion     = "2.6.14"
      val AkkaHttpVersion = "10.1.11"
      Seq(
        "com.lightbend.akka" %% "akka-stream-alpakka-dynamodb" % "3.0.1",
        "com.typesafe.akka"  %% "akka-stream"                  % AkkaVersion,
        "com.typesafe.akka"  %% "akka-http"                    % AkkaHttpVersion,
        "com.typesafe.akka"  %% "akka-slf4j"                   % AkkaVersion,
        "ch.qos.logback"      % "logback-classic"              % "1.1.3"
      )
    }
  )

lazy val web = (project in file("web"))
  .enablePlugins(PlayScala)
  .settings(
    name := "playAlpakkaDynamoDBSample",
    version := "0.1",
    scalaVersion := "2.13.6",
    libraryDependencies ++= {

      /** Play と Alpakka が利用するAkkaのVersionを合わせている。
        * Playの Akka Http の version を古いものにオーバーライドすることはできず、
        * alpakka dynamoDB も変えられないので dynamoDB の方に合わせてPlay2.8.1を利用
        *
        * @see [[https://www.playframework.com/documentation/2.8.x/ScalaAkka#Updating-Akka-version]]
        * @see [[https://doc.akka.io/docs/alpakka/current/dynamodb.html]]
        */
      val akkaVersion = "2.6.14"
      Seq(
        guice,
        "org.scalatestplus.play" %% "scalatestplus-play"           % "5.0.0" % Test,
        "net.codingwell"         %% "scala-guice"                  % "4.2.6",
        "com.lightbend.akka"     %% "akka-stream-alpakka-dynamodb" % "3.0.1",
        "com.typesafe.akka"      %% "akka-actor"                   % akkaVersion,
        "com.typesafe.akka"      %% "akka-stream"                  % akkaVersion,
        "com.typesafe.akka"      %% "akka-slf4j"                   % akkaVersion,
        "com.typesafe.akka"      %% "akka-actor-typed"             % akkaVersion,
        "com.typesafe.akka"      %% "akka-serialization-jackson"   % akkaVersion
      )
    }
  )
