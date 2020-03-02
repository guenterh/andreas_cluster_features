package org.swissbib.ml.utilities

import sttp.client._

class FetchSRU(val parameter: String) {

  def fetch():Either[String, String] = {
    //val query = s"https://sru.swissbib.ch/sru/search/defaultdb?query=+dc.anywhere+%3D+$parameter&operation=searchRetrieve&recordSchema=info%3Asrw%2Fschema%2F1%2Fmarcxml-v1.1-light&maximumRecords=10&x-info-10-get-holdings=true&startRecord=0&recordPacking=XML&availableDBs=defaultdb"
    val query = s"https://sru.swissbib.ch/sru/search/defaultdb?query=dc.anywhere=$parameter&operation=searchRetrieve&recordSchema=info:srw/schema/1/marcxml-v1.1-light&maximumRecords=10&x-info-10-get-holdings=true&startRecord=0&recordPacking=XML&availableDBs=defaultdb"
    val request = basicRequest.get(uri"https://sru.swissbib.ch/sru/search/defaultdb?query=dc.anywhere=$parameter&operation=searchRetrieve&recordSchema=info:srw/schema/1/marcxml-v1.1-light&maximumRecords=10&x-info-10-get-holdings=true&startRecord=0&recordPacking=XML&availableDBs=defaultdb")

    implicit val backend: SttpBackend[Identity, Nothing, NothingT] = HttpURLConnectionBackend()




    val response = request.send()

    // response.header(...): Option[String]
    println(response.header("Content-Length"))

    // response.body: by default read into an Either[String, String]
    // to indicate failure or success
    println(response.body)


    response.body
  }

}
