import org.swissbib.ml.CreateFeatures


object Job extends App {

  val features = new CreateFeatures(args)
  features.processFiles()


}
