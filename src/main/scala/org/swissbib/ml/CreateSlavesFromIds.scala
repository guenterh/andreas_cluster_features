package org.swissbib.ml

import java.io.{BufferedWriter, File, FileInputStream, FileWriter, InputStream}
import java.nio.file.{Files, Paths}
import java.util.stream.Collectors
import java.util.zip.GZIPInputStream

import org.swissbib.ml.utilities.{MarcXMLHandlersFeatures, OptionsParser, Transformators}

import scala.collection.immutable
import scala.io.Source

class CreateSlavesFromIds (val args: Map[Symbol, Any]) extends Transformators
                        with MarcXMLHandlersFeatures {

  private val inFile = Source.fromFile(new File(args(Symbol("inFileSlaveIds")).asInstanceOf[String]))
  val fileentries: java.util.List[String] = Files.walk(Paths.get(args(Symbol("indir")).asInstanceOf[String])).
    filter(Files.isRegularFile(_)).map[String](_.toString).collect(Collectors.toList[String])

  def processFiles(): Unit = {

    val outfileSlaves = args(Symbol("outFileSlaves")).asInstanceOf[String]
    val slaveRecordsInFile = new BufferedWriter(new FileWriter(outfileSlaves))
    val allSlaveIds = inFile.getLines().toList

    val alreadyWrittenSlaves = scala.collection.mutable.Set[String]()

    try fileentries.forEach(p => {

      val infile = new File(p.toString)

      val nameInFile: String = infile.getAbsoluteFile.getName
      val zipped = if (nameInFile.matches(""".*?.gz$""")) true else false
      val source: InputStream = if (zipped) {
        new GZIPInputStream(new FileInputStream(infile))
      } else {
        new FileInputStream(infile)
      }


      val it = Source.fromInputStream(source).getLines()


      for (line <- it) if (isRecord(line)) {

        val slaveElem = parseRecord(line)
        val docidSlave = recordid(slaveElem)
        val allSourceIds: immutable.Seq[String] = allRelevantSlave35(slaveElem)
        if (allSourceIds.exists(allSlaveIds.contains(_))) {
          if (!alreadyWrittenSlaves.contains(docidSlave)) {
            slaveRecordsInFile.write(slaveElem.toString() + System.lineSeparator)
            alreadyWrittenSlaves += docidSlave
            slaveRecordsInFile.flush()
          }
        }


      }


    }
    )

    slaveRecordsInFile.flush()
    slaveRecordsInFile.close()

  }

}
