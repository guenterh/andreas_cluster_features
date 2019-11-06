package org.swissbib.ml

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.stream.Collectors

import org.swissbib.ml.utilities.{MarcXmlHandlers, Transformators}

import scala.io.Source
import scala.xml.Elem

class Testclass (val args: Array[String]) extends Transformators{

  val rec = "<record><leader>     nam a22     2  4500</leader><controlfield tag=\"001\">559865511</controlfield><controlfield tag=\"003\">CHVBK</controlfield><controlfield tag=\"005\">20190412211855.0</controlfield><controlfield tag=\"006\">m        d        </controlfield><controlfield tag=\"007\">cr |||||||||||</controlfield><controlfield tag=\"008\">190412s2014    xxu||||||---- 00   |eng-d</controlfield><datafield tag=\"035\" ind1=\" \" ind2=\" \"><subfield code=\"a\">(IDSSG)000664707</subfield></datafield><datafield tag=\"035\" ind1=\" \" ind2=\" \"><subfield code=\"a\">(SERSOL)ssib023978742</subfield></datafield><datafield tag=\"035\" ind1=\" \" ind2=\" \"><subfield code=\"a\">(WaSeSS)ssib023978742</subfield></datafield><datafield tag=\"040\" ind1=\" \" ind2=\" \"><subfield code=\"a\">SzZuIDS HSG</subfield><subfield code=\"b\">ger</subfield><subfield code=\"e\">rda</subfield></datafield><datafield tag=\"100\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Foerster</subfield><subfield code=\"D\">Stephen</subfield><subfield code=\"e\">Verfasser</subfield><subfield code=\"4\">aut</subfield></datafield><datafield tag=\"245\" ind1=\"1\" ind2=\"0\"><subfield code=\"a\">Retail Financial Advice</subfield><subfield code=\"a\">das ist ein fake</subfield><subfield code=\"b\">Does One Size Fit All?</subfield><subfield code=\"c\">Stephen Foerster, Juhani T. Linnainmaa, Brian T. Melzer, Alessandro Previtero</subfield></datafield><datafield tag=\"264\" ind1=\" \" ind2=\"1\"><subfield code=\"a\">Cambridge, MA</subfield><subfield code=\"b\">National Bureau of Economic Research</subfield><subfield code=\"c\">2014</subfield></datafield><datafield tag=\"300\" ind1=\" \" ind2=\" \"><subfield code=\"a\">1 Online-Ressource</subfield></datafield><datafield tag=\"490\" ind1=\"0\" ind2=\" \"><subfield code=\"a\">NBER working paper series</subfield><subfield code=\"v\">20712</subfield><subfield code=\"i\">20712</subfield><subfield code=\"w\">(IDSSG)000043070</subfield><subfield code=\"9\">523202970</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Title from content provider.</subfield></datafield><datafield tag=\"506\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Lizenzbedingungen können den Zugang einschränken. License restrictions may limit access.</subfield></datafield><datafield tag=\"520\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Using unique data on Canadian households, we assess the impact of financial advisors on their clients&#39; portfolios. We find that advisors induce their clients to take more risk, thereby raising expected returns. On the other hand, we find limited evidence of customization: advisors direct clients into similar portfolios independent of their clients&#39; risk preferences and stage in the life cycle. An advisor&#39;s own portfolio is a good predictor of the client&#39;s portfolio even after controlling for the client&#39;s characteristics. This one-size-fits-all advice does not come cheap. The average client pays more than 2.7% each year in fees and thus gives up all of the equity premium gained through increased risk-taking.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Linnainmaa</subfield><subfield code=\"D\">Juhani T.</subfield><subfield code=\"e\">Verfasser</subfield><subfield code=\"4\">aut</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Melzer</subfield><subfield code=\"D\">Brian T.</subfield><subfield code=\"e\">Verfasser</subfield><subfield code=\"4\">aut</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Previtero</subfield><subfield code=\"D\">Alessandro</subfield><subfield code=\"e\">Verfasser</subfield><subfield code=\"4\">aut</subfield></datafield><datafield tag=\"856\" ind1=\" \" ind2=\" \"><subfield code=\"u\">http://www.nber.org/papers/w20712</subfield><subfield code=\"z\">Full text for the University of St. Gallen</subfield></datafield><datafield tag=\"909\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Metadata Rights Reserved</subfield><subfield code=\"2\">ids I</subfield></datafield><datafield tag=\"909\" ind1=\" \" ind2=\"4\"><subfield code=\"f\">NBER Working Papers</subfield></datafield><datafield tag=\"909\" ind1=\" \" ind2=\"4\"><subfield code=\"a\">E-Books von 360MarcUpdates</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSSG</subfield><subfield code=\"P\">100</subfield><subfield code=\"E\">1-</subfield><subfield code=\"a\">Foerster</subfield><subfield code=\"D\">Stephen</subfield><subfield code=\"e\">Verfasser</subfield><subfield code=\"4\">aut</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSSG</subfield><subfield code=\"P\">490</subfield><subfield code=\"E\">0-</subfield><subfield code=\"a\">NBER working paper series</subfield><subfield code=\"v\">20712</subfield><subfield code=\"i\">20712</subfield><subfield code=\"w\">(IDSSG)000043070</subfield><subfield code=\"9\">523202970</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSSG</subfield><subfield code=\"P\">700</subfield><subfield code=\"E\">1-</subfield><subfield code=\"a\">Linnainmaa</subfield><subfield code=\"D\">Juhani T.</subfield><subfield code=\"e\">Verfasser</subfield><subfield code=\"4\">aut</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSSG</subfield><subfield code=\"P\">700</subfield><subfield code=\"E\">1-</subfield><subfield code=\"a\">Melzer</subfield><subfield code=\"D\">Brian T.</subfield><subfield code=\"e\">Verfasser</subfield><subfield code=\"4\">aut</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSSG</subfield><subfield code=\"P\">700</subfield><subfield code=\"E\">1-</subfield><subfield code=\"a\">Previtero</subfield><subfield code=\"D\">Alessandro</subfield><subfield code=\"e\">Verfasser</subfield><subfield code=\"4\">aut</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSSG</subfield><subfield code=\"P\">856</subfield><subfield code=\"E\">--</subfield><subfield code=\"u\">http://www.nber.org/papers/w20712</subfield><subfield code=\"z\">Full text for the University of St. Gallen</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSBB</subfield><subfield code=\"P\">700</subfield><subfield code=\"E\">1-</subfield><subfield code=\"a\">Foerster</subfield><subfield code=\"D\">Stephen</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSBB</subfield><subfield code=\"P\">856</subfield><subfield code=\"E\">40</subfield><subfield code=\"u\">http://www.nber.org/papers/w20712</subfield><subfield code=\"z\">Uni Basel: Volltext</subfield></datafield><datafield tag=\"900\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Metadata rights reserved</subfield><subfield code=\"2\">idssg</subfield></datafield><datafield tag=\"898\" ind1=\" \" ind2=\" \"><subfield code=\"a\">BK020053</subfield><subfield code=\"b\">XK020053</subfield><subfield code=\"c\">XK020000</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSBB</subfield><subfield code=\"C\">SERSOL</subfield><subfield code=\"E\">ssib023978742</subfield><subfield code=\"F\">A145</subfield><subfield code=\"b\">A145</subfield><subfield code=\"c\">145VT</subfield><subfield code=\"1\">Volltext nur im Uninetz zugänglich</subfield><subfield code=\"3\">BOOK</subfield><subfield code=\"u\">http://www.nber.org/papers/w20712</subfield><subfield code=\"x\">NELA1451904</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">IDSSG</subfield><subfield code=\"C\">HSD50</subfield><subfield code=\"D\">HSB01</subfield><subfield code=\"E\">000664707</subfield><subfield code=\"F\">HSG</subfield><subfield code=\"b\">HSG</subfield><subfield code=\"c\">ONL</subfield><subfield code=\"j\">HONL000664707</subfield><subfield code=\"q\">000664707</subfield><subfield code=\"r\">000010</subfield><subfield code=\"0\">HSG</subfield><subfield code=\"1\">Online</subfield><subfield code=\"3\">ONL</subfield><subfield code=\"4\">54</subfield></datafield></record>"


  def processFiles(): Unit = {

    //val t = Paths.get(args(0))

    val fileentries: java.util.List[String] = Files.walk(Paths.get(args(0))).
      filter(Files.isRegularFile(_)).map[String](_.toString).collect(Collectors.toList[String])

    def attributeValue(elem: Elem) = true

    try fileentries.forEach(p => {

      if (p.contains("simple")) {

        val source = Source.fromFile(new File(p.toString))
        val it = source.getLines()
        for (line <- it if isRecord(line) ) {
          val elem = parseRecord(line)

          val rF = getRField(elem)("020")


          val isbn = (getRField(elem)("020").map(getRSubfieldContent(_)("a")) ++
            (getRField(elem)("022").map(getRSubfieldContent(_)("a")))).flatten.
              map(_.text).mkString("")

          val b = getRField(elem)("100").isEmpty


          //if ((getRSubfieldContent(getNRField(elem)("245"))("p")).nonEmpty)

          val part = ((getRField(elem)("773").map(getRSubfieldContent(_)("g"))) ++
            (getRField(elem)("490").map(getRSubfieldContent(_)("v"))) ++
            (getRField(elem)("830").map(getRSubfieldContent(_)("v"))) ++
            (getRField(elem)("440").map(getRSubfieldContent(_)("v"))) ++
            (getRField(elem)("245").map(getRSubfieldContent(_)("n")))).flatten.
              map(_.text)

          val format = ((getRField(elem)("898").map(getRSubfieldContent(_)("a")))).flatten.map(_.text)

          println()
          val subfields = rF.map(getRSubfieldContent(_)("a"))

          val texte = subfields.flatten.map(

              _.text

          ).mkString("#")

          println(texte)
          /*
          for (seq <- subfields) {

            for (elem <- seq) {
              println(elem.text)
            }

          }

           */

          //println(rF)



        }


      }

    })
  }




}
