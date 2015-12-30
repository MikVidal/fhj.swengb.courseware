package fhj.swengb.courseware

import java.sql.{ResultSet,Connection}
import javafx.beans.property.{SimpleStringProperty, SimpleIntegerProperty}

import scala.collection.mutable.ListBuffer

case class Album(AlbumId: Int, Title:String, ArtistId: Int) extends DB.DBEntity[Album] {
  def tabletolist(rs: ResultSet): List[Album] = List()
  def getAlbumId() = this.AlbumId
  def getTitle() = this.Title
  def getArtistId() = this.ArtistId
}

object Album extends DB.DBEntity[Album] {

  def tabletolist(rs: ResultSet): List[Album] = {
    val lb: ListBuffer[Album] = new ListBuffer[Album]()
    while (rs.next()) lb.append(Album(rs.getInt("AlbumId"), rs.getString("Title"), rs.getInt("ArtistId")))
    lb.toList
  }

}

//Queries - add additional if necessary
object q {
  val selectall = "select * from Album"
}

object AlbumData {
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
      println("| AlbumId \t| Title \t| ArtistId \t|")
      println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯")
      for {c <- connection
           album <- Album.tabletolist(Album.query(c,q.selectall))
      } {

        println("| " + album.getAlbumId() + "\t| " + album.getTitle() + "\t| " + album.getArtistId() + "\t|"  )
        i += 1
      }
      println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯")
      println(i + " lines printed")
    }
  }
  def asMap(): Map[_ <: Int, Album] = {
    val connection = DB.maybeConnection
    val data = if (connection.isSuccess) {
      val c = connection.get
      Album.tabletolist(Album.query(c,q.selectall)).map(a => (a.getAlbumId(),a)).toMap
    } else { Map.empty }
    data
  }
}

class MutableAlbum {

  val pAlbumId: SimpleIntegerProperty = new SimpleIntegerProperty()
  val pTitle: SimpleStringProperty = new SimpleStringProperty()

  def setAlbumId(AlbumId: Int) = pAlbumId.set(AlbumId)
  def setTitle(Title: String) = pTitle.set(Title)
}

object MutableAlbum {

  def apply(a: Album): MutableAlbum = {
    val ma = new MutableAlbum
    ma.setAlbumId(a.AlbumId)
    ma.setTitle(a.Title)
    ma
  }
}