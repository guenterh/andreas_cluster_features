import org.swissbib.ml.{CreateFeatures}


object Job extends App {

  val features = new CreateFeatures(args)
  //val features = new Testclass(args)
  features.processFiles()


}
