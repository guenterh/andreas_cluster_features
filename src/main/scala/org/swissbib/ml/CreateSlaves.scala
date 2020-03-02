package org.swissbib.ml

import java.io.{BufferedWriter, File, FileInputStream, FileWriter, InputStream}
import java.nio.file.{Files, Paths}
import java.util.Optional
import java.util.stream.Collectors
import java.util.zip.GZIPInputStream

import org.swissbib.ml.utilities.{FetchSRUJava, MarcXMLHandlersFeatures, Transformators}

import scala.io.Source

class CreateSlaves(val args: Map[Symbol, Any]) extends Transformators
                                    with MarcXMLHandlersFeatures  {


  val fileentries: java.util.List[String] = Files.walk(Paths.get(args(Symbol("indir")).asInstanceOf[String])).
    filter(Files.isRegularFile(_)).map[String](_.toString).collect(Collectors.toList[String])

  def processFiles(): Unit = {


    val mySlavesSet = scala.collection.mutable.Set[String]()
    val writtenMasterSet = scala.collection.mutable.Set[String]()

    try fileentries.forEach(p => {

      val infile = new File(p.toString)

      val nameInFile: String = infile.getAbsoluteFile.getName
      val zipped = if (nameInFile.matches(""".*?.gz$""")) true else false
      val source: InputStream = if (zipped) {
        new GZIPInputStream(new FileInputStream(infile))
      } else {
        new FileInputStream(infile)
      }
      val outdir = args(Symbol("outdir")).asInstanceOf[String]
      val outf = if (zipped) {
        """(.*?)\.xml.gz$""".r.findFirstMatchIn(nameInFile) match {
          case Some(g) => g.group(1) + ".json"
          case None => "defaultname.zipped.json"
        }

      } else {
        """(.*?)\.xml$""".r.findFirstMatchIn(nameInFile) match {
          case Some(g) => g.group(1) + ".json"
          case None => "defaultname.json"
        }
      }

      val masterRecordsInFile = new BufferedWriter(new FileWriter(outdir + File.separator + outf + ".master.xml"))
      val it = Source.fromInputStream(source).getLines()

      for (line <- it if isRecord(line)) {
        val elem = parseRecord(line)

        val all35SlaveFields = allRelevantSlave35(elem)
        if (all35SlaveFields.size == 1) {
          val elem: String = all35SlaveFields.head.replace("(", "").replace(")", "")

          val body: Optional[String] = new FetchSRUJava(elem).fetch()

          if (body.isPresent) {
            val master = parseRecord(body.get())


            val nR = (master \ "numberOfRecords").map(_.text).head.toInt
            if (nR == 1) {
              val masterRecord = (master \\ "recordData" \ "record").head.toString()
                .replace("""<record xmlns:xs="http://www.w3.org/2001/XMLSchema">""", "<record>")
              //println(sr)

              val allSlavesInMaster = allRelevantSlave35(master)
              for (elem <- allSlavesInMaster) {
                mySlavesSet += elem
              }
              val docidMaster =  recordid(master)
              if (!writtenMasterSet.contains(docidMaster)) {
                masterRecordsInFile.write(masterRecord + "\n")
                masterRecordsInFile.flush()
                writtenMasterSet += docidMaster
              }

            }

          }
          println()
        }


      }
      masterRecordsInFile.flush()
      masterRecordsInFile.close()

      })

    val outdir = args(Symbol("outdir")).asInstanceOf[String]
    val slaveIdsFile = new BufferedWriter(new FileWriter(outdir + File.separator + "slaveIDs" + ".txt"))
    for (slaveId <- mySlavesSet) {
      slaveIdsFile.write(slaveId)
      slaveIdsFile.flush()
    }
    slaveIdsFile.flush()
    slaveIdsFile.close()
  }

}
