package fintech.homework08
import java.time.LocalDate

import fintech.homework08.PeopleApp.{readPerson, uri}
import org.scalatest.{FlatSpec, Matchers}

class SortingSpec extends FlatSpec with Matchers {

  it should "flatMap correctly 1" in {
    val composed = for {
    _ <- DBRes.update("DROP TABLE people", List.empty)
    _ <- DBRes.update("CREATE TABLE people(name VARCHAR(256), birthday DATE)", List.empty)
    _ <-  DBRes.update("INSERT INTO people(name, birthday) VALUES (?, ?)", List("Al", LocalDate.now()))
    _ <-  DBRes.update("INSERT INTO people(name, birthday) VALUES (?, ?)", List("Bo", LocalDate.now()))
    } yield ()

    composed.execute(uri)
    val res = DBRes.select("SELECT * FROM people", List())(readPerson).execute(uri)
    res.length shouldEqual 2
  }
}
