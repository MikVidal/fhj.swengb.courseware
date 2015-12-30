package fhj.swengb.courseware

import java.sql.{ResultSet,Connection}
import javafx.beans.property.{SimpleStringProperty, SimpleIntegerProperty}

import scala.collection.mutable.ListBuffer

case class Student(ID: Int, firstname:String, lastname: String) extends DB.DBEntity[Student] {
  def tabletolist(rs: ResultSet): List[Student] = List()
  def getID() = this.ID
  def getFirstname() = this.firstname
  def getLastname() = this.lastname
}

object Student extends DB.DBEntity[Student] {

  def tabletolist(rs: ResultSet): List[Student] = {
    val lb: ListBuffer[Student] = new ListBuffer[Student]()
    while (rs.next()) lb.append(Student(rs.getInt("ID"), rs.getString("firstname"), rs.getString("lastname")))
    lb.toList
  }

}

//Queries - add additional if necessary
object studentquery {
  val selectall = "select * from Student"
}

object StudentData {
  def main(args: Array[String]) {
    this.asString()
  }

  def asString() {
    val connection = DB.maybeConnection
    var i = 0
    if (connection.isSuccess) {
      //println("connection established")
      println("printing results:")
      println("___________________________________________________________________________")
      println("| StudentId \t| Title \t| ArtistId \t|")
      println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯")
      for {c <- connection
           s <- Student.tabletolist(Student.query(c,studentquery.selectall))
      } {

        println("| " + s.getID() + "\t| " + s.getFirstname() + "\t| " + s.getLastname() + "\t|"  )
        i += 1
      }
      println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯")
      println(i + " lines printed")
    }
  }
  def asMap(): Map[_ <: Int, Student] = {
    val connection = DB.maybeConnection
    val data = if (connection.isSuccess) {
      val c = connection.get
      Student.tabletolist(Student.query(c,studentquery.selectall)).map(s => (s.getID(),s)).toMap
    } else { Map.empty }
    data
  }
}

class MutableStudent {

  val pID: SimpleIntegerProperty = new SimpleIntegerProperty()
  val pFirstname: SimpleStringProperty = new SimpleStringProperty()
  val pLastname: SimpleStringProperty = new SimpleStringProperty()

  def setID(ID: Int) = pID.set(ID)
  def setFirstname(firstname: String) = pFirstname.set(firstname)
  def setLastname(lastname: String) = pLastname.set(lastname)
}

object MutableStudent {

  def apply(s: Student): MutableStudent = {
    val ms = new MutableStudent
    ms.setID(s.ID)
    ms.setFirstname(s.firstname)
    ms.setLastname(s.lastname)
    ms
  }
}