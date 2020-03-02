
import org.swissbib.ml.CreateSlaves
import org.swissbib.ml.utilities.OptionsParser

object JobCreateSlaves extends App {


  val usage = """
    Usage:  --indir path --outdir path
  """


  if (args.length == 0) {println(usage); System.exit(0)}
  val arglist = args.toList

  val options = OptionsParser.nextOption(Map(),arglist)
  if (!options.isDefinedAt(Symbol("indir")) || !options.isDefinedAt(Symbol("outdir"))) {
    println(usage)
    System.exit(0)
  }
  println(options)

  val createSlaves = new CreateSlaves(options)

  createSlaves.processFiles()

}
