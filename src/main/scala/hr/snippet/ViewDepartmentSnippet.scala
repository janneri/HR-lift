package hr.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import Helpers._
import hr.model.{Employee, Department}

class ViewDepartmentSnippet {

  var departmentId = S.param("id") openOr ""

  def del(id : Long) {
    Employee.findByKey(id) match {
      case Full(emp) => emp.delete_!
      case _ => Nil
    }
  }

  def listEmployees(xhtml : NodeSeq) : NodeSeq = {

    if ("" == departmentId) return Text("department not selected")

    Department.findByKey(departmentId.toLong) match {
      case Full(dept) => {

        val employees = dept.employees

        if (employees.size == 0) return Text("department empty")

        val entries : NodeSeq = employees.flatMap({
          emp => bind("emp",
                       chooseTemplate("employees", "entry", xhtml),
                       "firstname" -> emp.firstname,
                       "lastname" -> emp.lastname,
                       "email" -> emp.email,
                       "edit" -> <a href={"/edit_employee/" + emp.id}>Edit</a>,
                       "delete" -> { SHtml.link("#", () => del(emp.id), Text("Delete")) }
                 )
        })

        bind("employees", xhtml, "entry" -> entries)
      }

      case _ => Text("department not selected")
    }

  }
}
