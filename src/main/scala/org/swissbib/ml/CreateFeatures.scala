package org.swissbib.ml

import java.io.{BufferedWriter, File, FileInputStream, FileWriter, InputStream}
import java.nio.file.{Files, Paths}
import java.util.Optional
import java.util.stream.Collectors
import java.util.zip.GZIPInputStream

import org.swissbib.ml.utilities.{FetchSRU, FetchSRUJava, MarcXMLHandlersFeatures, Transformators}
import play.api.libs.json.{JsObject, JsString, Json}

import scala.io.Source
import scala.xml.{Elem, NodeSeq}

class CreateFeatures (val args: Map[Symbol, Any]) extends Transformators
                                                with MarcXMLHandlersFeatures {


  val fileentries: java.util.List[String] = Files.walk(Paths.get(args(Symbol("indir")).asInstanceOf[String])).
    filter(Files.isRegularFile(_)).map[String](_.toString).collect(Collectors.toList[String])

  def attributeValue(elem: Elem) = true

  def processFiles(): Unit = {

    try fileentries.forEach(p => {

          val infile = new File(p.toString)
          //var source:InputStream
          //val name =  infile.getAbsoluteFile.getName.split("\\.")(1)
          //val name =  infile.getAbsoluteFile.getName.split("\\.")(0)
          //val name =  infile.getAbsoluteFile.getName.split("\\.")(1)



          val nameInFile: String =  infile.getAbsoluteFile.getName
          val zipped = if (nameInFile.matches(""".*?.gz$""")) true else false
          val  source: InputStream = if (zipped) {
            new GZIPInputStream(new FileInputStream(infile))
          } else {
            new FileInputStream(infile)
          }
          val outdir = args(Symbol("outdir")).asInstanceOf[String]
          val outf =  if (zipped) {
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
          //val outfile = new File("goldstandard.scale/" + name + ".json")
          //val outfile = new File("cbsoutput/" + name + ".json")
          val bw = new BufferedWriter(new FileWriter(outdir + File.separator + outf))

          val it = Source.fromInputStream(source).getLines()
          for (line <- it if isRecord(line)) {
            val elem = parseRecord(line)
            //println(elem)

            //if ( recordid(elem) == "504389122" )
            //  println("stop")

            val jsonfeatures = JsObject (Seq (
              "docid" -> JsString(recordid(elem)),
              "035liste" -> Json.toJson(all35(elem)),
              "isbn" -> Json.toJson( isbnFeatures(elem)),
              "ttlfull" -> Json.toJson(ttlFullFeature(elem)),
              "ttlpart" -> Json.toJson( ttl245Feature(elem)),
              "person" -> Json.toJson( personFeature(elem)),
              "corporate" -> Json.toJson(corporateFeature(elem)),
              "pubyear" -> JsString( partOf008Feature(elem)(7,14)),
              "decade" -> JsString( partOf008Feature(elem)(7,10)),
              "century" -> JsString(partOf008Feature(elem)(7,10)),
              "exactDate" -> JsString( partOf008Feature(elem)(7,14)),
              "edition" -> JsString( editionFeature(elem).mkString),
              "part" -> Json.toJson( partFeature(elem)),
              "pages" -> Json.toJson( pagesFeature(elem)),
              "volumes" -> Json.toJson( volumesFeature(elem)),
              "pubinit" -> Json.toJson( pubFeature(elem)),
              "pubword" -> Json.toJson( pubFeature(elem)),
              "scale" -> JsString( scaleFeature(elem).mkString),
              "coordinate" -> Json.toJson( coordinateFeature(elem)),
              "doi" -> Json.toJson( doiFeature(elem)),
              "ismn" -> Json.toJson( ismnFeature(elem)),
              "musicid" -> JsString( musicIdFeature(elem).mkString),
              "format" -> Json.toJson( formatFeature(elem))
            ))



            //println(jsonfeatures.toString())
            bw.write(jsonfeatures.toString())
            bw.write("\n")
        }
        bw.flush()
        bw.close()

      }
    )
  }
}
