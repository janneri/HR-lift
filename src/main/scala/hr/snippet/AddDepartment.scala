package hr.snippet

import net.liftweb._
import http._
import hr.model.Department

object AddDepartment extends LiftScreen {
  val name = field("Name", "");

  override def screenTop = <b>Add Department</b>

  def finish() {
    val dept = Department.create.name(name)
    dept.save
    //S.notice("Name: " + name)
  }
}