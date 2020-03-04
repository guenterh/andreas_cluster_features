import org.swissbib.ml.CreateSlavesFromIds
import org.swissbib.ml.utilities.OptionsParser

object JobCreateSlavesFromIds extends App {

  val usage =
    """
    Usage:  --indir path --outFileSlaves --outFileForSlaves --inFileSlaveIds
  """


  if (args.length == 0) {
    println(usage); System.exit(0)
  }
  val arglist = args.toList

  val options = OptionsParser.nextOption(Map(), arglist)
  if (!options.isDefinedAt(Symbol("inFileSlaveIds"))
    || !options.isDefinedAt(Symbol("outFileSlaves"))
    || !options.isDefinedAt(Symbol("indir"))) {
    println(usage)
    System.exit(0)
  }
  println(options)

  val createSlaveFromIds = new CreateSlavesFromIds(options)

  createSlaveFromIds.processFiles()
}


