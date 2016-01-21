package fhj.swengb.courseware

import java.net.URL
import java.sql.{Connection, ResultSet, Statement}
import javafx.beans.property.{SimpleStringProperty, SimpleIntegerProperty}

import fhj.swengb.GitHub

import scala.collection.mutable.ListBuffer


object Group extends DB.DBEntity[Group] {
  def fromDB(stringToSet: (String) => ResultSet) = ???


  val dropTableSql = "drop table if exists Groups"
  val createTableSql = "create table Groups (ID int, name string)"
  val insertSql = "insert into Groups (ID, name) VALUES (?, ?)"


  def reTable(stmt: Statement): Int = {
    stmt.executeUpdate(Group.dropTableSql)
    stmt.executeUpdate(Group.createTableSql)
  }

  def toDB(c: Connection)(s: Group): Int = {
    val pstmt = c.prepareStatement(insertSql)
    pstmt.setInt(1, s.ID)
    pstmt.setString(2, s.name)
    pstmt.executeUpdate()
  }

  def fromDB(rs: ResultSet): List[Group] = {
    val lb: ListBuffer[Group] = new ListBuffer[Group]()
    while (rs.next()) lb.append(
      Group(
        rs.getInt("ID"),
        rs.getString("name")
        )
    )
    lb.toList
  }
}

sealed trait Groups {

  def ID: Int

  def name: String


  def normalize(in: String): String = {
    val mapping =
      Map("ä" -> "ae",
        "ö" -> "oe",
        "ü" -> "ue",
        "ß" -> "ss")
    mapping.foldLeft(in) { case ((s, (a, b))) => s.replace(a, b) }
  }

}

case class Group(ID: Int,
                    name: String
                    ) extends Groups

object groupquery {
  val selectall = "select * from Groups"
  def query():String = {
    selectall
  }
}

object GroupData {
  def asMap(): Map[_, Group] = {
    val connection = DB.maybeConnection
    val data = if (connection.isSuccess) {
      val c = connection.get
      Group.fromDB(Group.query(c)(groupquery.query())
      ).map(l => (l.ID,l)).toMap
    } else { Map.empty }
    data
  }
}


class MutableGroup {

  val p_ID: SimpleIntegerProperty = new SimpleIntegerProperty()
  val p_name: SimpleStringProperty = new SimpleStringProperty()


  def setID(ID: Int) = p_ID.set(ID)
  def setName(name: String) = p_name.set(name)

}

object MutableGroup {

  def apply(l: Group): MutableGroup = {
    val ml = new MutableGroup
    ml.setID(l.ID)
    ml.setName(l.name)
    ml
  }

}
