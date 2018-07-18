import com.github.tototoshi.csv.{CSVReader, CSVWriter, DefaultCSVFormat}

case class Person(
                   id_enseigne: String,
                   id_client: String,
                   civilite: Option[String],
                   nom: Option[String],
                   prenom: Option[String],
                   adr2: Option[String],
                   adr3: Option[String],
                   adr4: Option[String],
                   adr5: Option[String],
                   code_postal: Option[String],
                   ville: Option[String],
                   code_pays: Option[String],
                   tel_fixe: Option[String],
                   tel_mobile: Option[String],
                   email: Option[String],
                   id_email: Option[String],
                   adr1: Option[String],
                   pays: Option[String]
                 )

case class GroupedPerson(
                          idPerson: String,
                          persons: List[Person]
                        )

object Main {

  val headerG = List(
    "profile_id",
    "id_enseigne",
    "id_client",
    "civilite",
    "nom",
    "prenom",
    "adr2",
    "adr3",
    "adr4",
    "adr5",
    "code_postal",
    "ville",
    "code_pays",
    "tel_fixe",
    "tel_mobile",
    "email",
    "id_email",
    "adr1",
    "pays"
  )

  def personToCsv(person: Person) = {

    List(
      person.id_enseigne,
      person.id_client,
      person.civilite.orNull,
      person.nom.orNull,
      person.prenom.orNull,
      person.adr2.orNull,
      person.adr3.orNull,
      person.adr4.orNull,
      person.adr5.orNull,
      person.code_postal.orNull,
      person.ville.orNull,
      person.code_pays.orNull,
      person.tel_fixe.orNull,
      person.tel_mobile.orNull,
      person.email.orNull,
      person.id_email.orNull,
      person.adr1.orNull,
      person.pays.orNull
    )
  }

  def main(args: Array[String]): Unit = {

    implicit object CSVFormat extends DefaultCSVFormat {
      override val delimiter = ','
    }


    val writer = CSVWriter.open("/home/rait/adr.csv.grouped")(CSVFormat)
    writer.writeRow(headerG)

    CSVReader
      .open("/home/rait/adr.csv")(CSVFormat)
      .iteratorWithHeaders
      .map { personneMap =>
        Person(
          id_enseigne = personneMap("id_enseigne"),
          id_client = personneMap("id_client"),
          civilite = personneMap.get("civilite").filter(_.nonEmpty),
          nom = personneMap.get("nom").filter(_.nonEmpty),
          prenom = personneMap.get("prenom").filter(_.nonEmpty),
          adr2 = personneMap.get("adr2").filter(_.nonEmpty),
          adr3 = personneMap.get("adr3").filter(_.nonEmpty),
          adr4 = personneMap.get("adr4").filter(_.nonEmpty),
          adr5 = personneMap.get("adr5").filter(_.nonEmpty),
          code_postal = personneMap.get("code_postal").filter(_.nonEmpty),
          ville = personneMap.get("ville").filter(_.nonEmpty),
          code_pays = personneMap.get("code_pays").filter(_.nonEmpty),
          tel_fixe = personneMap.get("tel_fixe").filter(_.nonEmpty),
          tel_mobile = personneMap.get("tel_mobile").filter(_.nonEmpty),
          email = personneMap.get("email").filter(_.nonEmpty),
          id_email = personneMap.get("id_email").filter(_.nonEmpty),
          adr1 = personneMap.get("adr1").filter(_.nonEmpty),
          pays = personneMap.get("pays").filter(_.nonEmpty)

        )

      }
      .toList
      .filter { person =>
        person.tel_mobile.isDefined &&
          person.nom.isDefined &&
          person.prenom.isDefined
      }
      .groupBy { person: Person =>

        s"${person.nom}-${person.prenom}-${person.tel_mobile}".toLowerCase

      }.toList
      .sortBy(_._2.size)
      .foreach { case (idTel, samePerson) =>
        samePerson.foreach { p =>
          writer.writeRow(idTel :: personToCsv(p))
        }

      }

  }

}
