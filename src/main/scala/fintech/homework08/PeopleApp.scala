package fintech.homework08

import java.time.LocalDate

// Я написал небольшое API для работы с базами данных через jdbc (DBRes.scala)
// и использовал его для написания PeopleApp
// К сожалению когда я запускаю приложение я вижу что оно коннектится к базе много раз

// Добавьте в DBRes методы map и flatMap и перепешите код используя for
// и выполняя execute только один раз


object PeopleApp extends PeopleModule {

  val uri = "jdbc:h2:~/dbres"

  def getOldPerson(): Person =
    DBRes.select("SELECT * FROM people WHERE birthday < ?", List(LocalDate.of(1979, 2, 20)))(readPerson).execute(uri).head

  def clonePerson(person: Person): Person = {
    val clone = person.copy(birthday = LocalDate.now())
    storePerson(clone).execute(uri)
    clone
  }

  def main(args: Array[String]): Unit = {

    val program = for {
      _ <- DBRes.update("DROP TABLE people", List.empty)
      _ <- DBRes.update("CREATE TABLE people(name VARCHAR(256), birthday DATE)", List.empty)
      _ <- storePerson(Person("Alice", LocalDate.of(1970, 1, 1)))
      _ <- storePerson(Person("Bob", LocalDate.of(1981, 5, 12)))
      _ <- storePerson(Person("Charlie", LocalDate.of(1979, 2, 20)))
      person <- DBRes.select("SELECT * FROM people WHERE birthday < ?", List(LocalDate.of(1979, 2, 20)))(readPerson)
      clone <- storePerson(Person(person.head.name, birthday = LocalDate.now()))
    } yield person

     val person = program.execute(uri).head
      println(person)
  }
}
