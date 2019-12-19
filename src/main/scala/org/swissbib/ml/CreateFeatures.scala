package org.swissbib.ml

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{Files, Paths}
import java.util.stream.Collectors

import org.swissbib.ml.utilities.{MarcXMLHandlersFeatures, Transformators}
import play.api.libs.json.{JsObject, JsString, Json}

import scala.io.Source
import scala.xml.Elem

class CreateFeatures (val args:Array[String]) extends Transformators
                                                with MarcXMLHandlersFeatures {

  val fileentries: java.util.List[String] = Files.walk(Paths.get(args(0))).
    filter(Files.isRegularFile(_)).map[String](_.toString).collect(Collectors.toList[String])

  def attributeValue(elem: Elem) = true

  def processFiles(): Unit = {

    try fileentries.forEach(p => {

          val infile = new File(p.toString)
          val source = Source.fromFile(infile)
          val name =  infile.getAbsoluteFile.getName.split("\\.")(1)

          val outfile = new File("goldstandard/" + name + ".json")
          val bw = new BufferedWriter(new FileWriter(outfile))

          val it = source.getLines()
          for (line <- it if isRecord(line)) {
            val elem = parseRecord(line)


            val jsonfeatures = JsObject (Seq (
              "docid" -> JsString(recordid(elem)),
              "035liste" -> Json.toJson(all35(elem)),
              "isbn" -> JsString (isbnFeatures(elem).mkString),
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
              "pages" -> Json.toJson( partFeature(elem)),
              "volumes" -> JsString( volumesFeature(elem).mkString),
              "pubinit" -> JsString( pubFeature(elem).mkString),
              "pubword" -> Json.toJson( pubFeature(elem)),
              "scale" -> JsString( scaleFeature(elem).mkString),
              "coordinate" -> Json.toJson( coordinateFeature(elem)),
              "doi" -> JsString( doiFeature(elem).mkString),
              "ismn" -> JsString( ismnFeature(elem).mkString),
              "musicid" -> JsString( musicIdFeature(elem).mkString),
              "format" -> JsString( formatFeature(elem).mkString)
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
