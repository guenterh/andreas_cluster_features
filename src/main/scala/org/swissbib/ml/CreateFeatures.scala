package org.swissbib.ml

import java.io.File
import java.nio.file._
import java.util.stream
import java.util.stream.Collectors

import org.swissbib.ml.utilities._

import scala.io.Source
import scala.xml.Elem

class CreateFeatures(val args:Array[String]) extends Transformators  {

  def processFiles(): Unit = {

    //val t = Paths.get(args(0))

    val fileentries: java.util.List[String] = Files.walk(Paths.get(args(0))).
      filter(Files.isRegularFile(_)).map[String](_.toString).collect(Collectors.toList[String])

    def attributeValue(elem: Elem) = true

    try fileentries.forEach(p => {

      val source = Source.fromFile(new File(p.toString))
      val it = source.getLines()
      for (line <- it if isRecord(line) ) {
        val elem = parseRecord(line)

        val rfield020_isbn = getRField(elem)("020").
          map(node => (getNRSubfieldContent(node)("a")).get)
        val rfield022_isbn = getRField(elem)("022").
          map(node => (getNRSubfieldContent(node)("a")).get)

        val rfield245a_ttlfull = getRField(elem)("245").
          map(node => (getNRSubfieldContent(node)("a")).get)


        val rfield245b_ttlfull = getRField(elem)("245").
          map(node => (getNRSubfieldContent(node)("b")).get)

        val rfield245p_n_ttlfull = getRField(elem)("245").
          map(node => (getNRSubfieldContent(node)("p")).getOrElse(
            getRField(elem)("245").
              map(node => (getNRSubfieldContent(node)("c")).get

          )))


        val isDefinded = (getNRSubfieldContent(getNRField(elem)("245"))("b")).isDefined




        val isbn1 =  getNRSubfieldContent(elem)("a")

        val rf = getRField(elem) ("035" )
        val id = getField001Content(elem)
        val content_035 = getField035Contents(elem)

        val test = elem.toString()
        //val e_008 = elem \ "<record>" \ "<controlfield" filter {
        val e_008 = elem  \\ "record" \\ "controlfield" filter {
          n => {
            val t = n
            val att = t.attribute("tag")
            println(t)
            true
          }
        }

        println("test")

      }

    })


  }

}
