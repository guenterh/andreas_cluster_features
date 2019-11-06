import org.swissbib.ml.{CreateFeatures, CreateFeaturesOld, Testclass}


object Job extends App {

  val features = new CreateFeatures(args)
  //val features = new Testclass(args)
  features.processFiles()


}
