package hr.snippet

import net.liftweb.common.{Logger, Box, Empty, Full}
import net.liftweb.http._
import hr.model.{Department, Employee}
import scala.xml.{Group, NodeSeq}
import net.liftweb.widgets.autocomplete.AutoComplete

object AddEmployee extends LiftScreen {

  object emp extends ScreenVar(Employee.create)

  /*

  val names = List("John", "Jack", "Bob", "Marly", "Madison", "Ab", "Sally", "Lol", "Hankok", "Don", "Ewan")
  def autocomplete = {
    var name: String = ""
    def buildNames(current: String, limit: Int): Seq[String] = {
      names.filter(_.matches(".*" + current + ".*"))
    }
    ".autocomplete" #> net.liftweb.widgets.autocomplete.AutoComplete("", buildNames _, name = _)
  }

  val deptField = new Field {
    val depts = Department.findAll().map(deptSel => deptSel.name.is)
    type ValueType = String
    override def name = "Department"
    override implicit def manifest = buildIt[String]
    override def default = depts.head

    override def toForm: Box[NodeSeq] =
      AutoComplete("", (current: String, limit: Int) => depts.filter(_.toLowerCase.startsWith(current.toLowerCase)))
  }
  */

  //val deptSel = select("Department", Full(depts.head), depts)

  override def screenTop = <b>Add Employee</b>

  addFields(() => emp.is)

  def finish() {
      emp.is.save
      S.notice(emp.is.firstname + " " + emp.is.lastname + " saved")
  }

}
