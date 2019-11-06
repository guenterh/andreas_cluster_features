package org.swissbib.ml.utilities

import scala.collection.immutable
import scala.xml.{Elem, NodeSeq}

trait MarcXMLHandlersFeatures extends MarcXmlHandlers {

  def isbnFeatures(elem: Elem): immutable.Seq[String] = {

    (getRField(elem)("020").map(getRSubfieldContent(_)("a")) ++
      (getRField(elem)("022").map(getRSubfieldContent(_)("a")))).flatten.
      map(_.text)

  }

  def ttl245Feature(elem: Elem): immutable.Seq[String] = {

      (((getRField(elem)("245").map(getRSubfieldContent(_)("a")) ++
        (getRField(elem)("245").map(getRSubfieldContent(_)("b")))) ++
        (if ((getRSubfieldContent(getNRField(elem)("245"))("p")).nonEmpty)
          (getRField(elem)("245").map(getRSubfieldContent(_)("p")))
        else
          (getRField(elem)("245").map(getRSubfieldContent(_)("n"))))).flatten.
        map(_.text))

  }

  def ttl246Feature(elem: Elem): immutable.Seq[String] = {

    if (getRField(elem)("246").nonEmpty)
      (((getRField(elem)("246").map(getRSubfieldContent(_)("a")) ++
        (getRField(elem)("246").map(getRSubfieldContent(_)("b")))) ++
        (if ((getRSubfieldContent(getNRField(elem)("246"))("p")).nonEmpty)
          (getRField(elem)("246").map(getRSubfieldContent(_)("p")))
        else
          (getRField(elem)("246").map(getRSubfieldContent(_)("n"))))).flatten.
        map(_.text))
    else Nil

  }


  def ttlFullFeature(elem: Elem): immutable.Seq[String] = {

      ttl245Feature(elem) ++ ttl246Feature(elem)

  }

  def personFeature(elem: Elem): immutable.Seq[String] = {

    //all100(elem) ++ all700(elem) ++ all800(elem) ++
    //  (getRField(elem)("245").map(getRSubfieldContent(_)("c"))).map(_.text)

    allGeneric(elem)( "100") ++ allGeneric(elem)("700") ++ allGeneric(elem)("800") ++
      (getRField(elem)("245").map(getRSubfieldContent(_)("c"))).map(_.text)



  }


  def all100(elem: Elem): immutable.Seq[String] = {
    //todo: make it better - should be able to collect all subfields at once
    // then it would be a general function
    if (getRField(elem)("100").nonEmpty)
      ((getRField(elem)("100").map(getRSubfieldContent(_)("a")) ++
        (getRField(elem)("100").map(getRSubfieldContent(_)("b"))) ++
        (getRField(elem)("100").map(getRSubfieldContent(_)("c"))))).flatten.
        map(_.text)
    else Nil

  }

  def all700(elem: Elem): immutable.Seq[String] = {
    //todo: make it better - should be able to collect all subfields at once
    // then it would be a general function
    if (getRField(elem)("700").nonEmpty)
      (getRField(elem)("700").map(getRSubfieldContent(_)("a")) ++
        (getRField(elem)("700").map(getRSubfieldContent(_)("b"))) ++
        (getRField(elem)("700").map(getRSubfieldContent(_)("c")))).flatten.
        map(_.text)
    else Nil

  }

  def all100Generic(elem: Elem): immutable.Seq[String] = {
    //todo: make it better - should be able to collect all subfields at once
    // then it would be a general function
    if (getRField(elem)("100").nonEmpty)
      (getRField(elem)("100").map(getAllSubfieldContent(_)).flatten.
        map(_.text))
    else Nil

  }

  def allGeneric(elem: Elem)( datafield:String): immutable.Seq[String] = {
    //todo: make it better - should be able to collect all subfields at once
    // then it would be a general function
    if (getRField(elem)(datafield).nonEmpty)
      (getRField(elem)(datafield).map(getAllSubfieldContent(_)).flatten.
        map(_.text))
    else Nil

  }

  def corporateFeature(elem: Elem): immutable.Seq[String] = {
    val _110 = if (getRField(elem)("110").nonEmpty)
      ((getRField(elem)("110").map(getRSubfieldContent(_)("a")) ++
        (getRField(elem)("110").map(getRSubfieldContent(_)("b")))) ++
        (getRField(elem)("110").map(getRSubfieldContent(_)("c")))).flatten.
        map(_.text)
      else Nil
    val _710 = if (getRField(elem)("710").nonEmpty)
      ((getRField(elem)("710").map(getRSubfieldContent(_)("a")) ++
        (getRField(elem)("710").map(getRSubfieldContent(_)("b")))) ++
        (getRField(elem)("710").map(getRSubfieldContent(_)("c")))).flatten.
        map(_.text)
    else Nil

    val _810 = if (getRField(elem)("810").nonEmpty)
      ((getRField(elem)("810").map(getRSubfieldContent(_)("a")) ++
        (getRField(elem)("810").map(getRSubfieldContent(_)("b")))) ++
        (getRField(elem)("810").map(getRSubfieldContent(_)("c")))).flatten.
        map(_.text)
    else Nil

    _110 ++ _710 ++ _810


  }


  def all800(elem: Elem): immutable.Seq[String] = {
    //todo: make it better - should be able to collect all subfields at once
    // then it would be a general function
    if (getRField(elem)("800").nonEmpty)
      ((getRField(elem)("800").map(getRSubfieldContent(_)("a")) ++
        (getRField(elem)("800").map(getRSubfieldContent(_)("b"))) ++
        (getRField(elem)("800").map(getRSubfieldContent(_)("c"))))).flatten.
        map(_.text)
    else Nil

  }


  def partOf008Feature(elem: Elem) (start:Int, end: Int): String = {

    val text008 = getNRControlfieldField(elem)("008").text
    (text008(start) to text008(end)).mkString
  }

  def editionFeature(elem: Elem): immutable.Seq[String] = {
    //todo: make it better - should be able to collect all subfields at once
    // then it would be a general function
    if (getRField(elem)("250").nonEmpty)
      (getRField(elem)("250").map(getRSubfieldContent(_)("a"))).flatten.
        map(_.text)
    else Nil
  }

  def partFeature(elem: Elem): immutable.Seq[String] = {

    ((getRField(elem)("773").map(getRSubfieldContent(_)("g"))) ++
      (getRField(elem)("490").map(getRSubfieldContent(_)("v"))) ++
      (getRField(elem)("830").map(getRSubfieldContent(_)("v"))) ++
      (getRField(elem)("440").map(getRSubfieldContent(_)("v"))) ++
      (getRField(elem)("245").map(getRSubfieldContent(_)("n")))).flatten.
      map(_.text)

  }
  def _300a(elem: Elem): immutable.Seq[String] = {
    //todo: make it better - should be able to collect all subfields at once
    // then it would be a general function
    if (getRField(elem)("300").nonEmpty)
      (getRField(elem)("300").map(getRSubfieldContent(_)("a"))).flatten.
        map(_.text)
    else Nil
  }

  def pagesFeature(elem: Elem): immutable.Seq[String] = {

    _300a(elem)
  }

  def volumesFeature(elem: Elem): immutable.Seq[String] = {

    _300a(elem)
  }

  def pubFeature(elem: Elem): immutable.Seq[String] = {

    (getRField(elem)("260").map(getRSubfieldContent(_)("b"))).flatten.map(_.text)

  }

  def scaleFeature(elem: Elem): immutable.Seq[String] = {

    (getRField(elem)("034").map(getRSubfieldContent(_)("b")) match {

      case Nil => (getRField(elem)("255").map(getRSubfieldContent(_)
                                    ("a")).map(_.text))

        //we have found something for 034$b
      case seq => seq.map(_.text)

    })

  }

  def coordinateFeature(elem: Elem): immutable.Seq[String] = {

    ((getRField(elem)("034").map(getRSubfieldContent(_)("d"))) ++
      (getRField(elem)("034").map(getRSubfieldContent(_)("f")))).flatten.
      map(_.text)

  }

  def _24a(elem: Elem): immutable.Seq[String] = {

    ((getRField(elem)("024").map(getRSubfieldContent(_)("a")))).flatten.
      map(_.text)

  }

  def doiFeature(elem: Elem): immutable.Seq[String] = {

    _24a(elem)

  }

  def ismnFeature(elem: Elem): immutable.Seq[String] = {

    _24a(elem)

  }

  def musicIdFeature(elem: Elem): immutable.Seq[String] = {

    ((getRField(elem)("028").map(getRSubfieldContent(_)("a")))).flatten.
      map(_.text)

  }

  def formatFeature(elem: Elem): immutable.Seq[String] = {

    ((getRField(elem)("898").map(getRSubfieldContent(_)("a")))).flatten.
      map(_.text)

  }


}
