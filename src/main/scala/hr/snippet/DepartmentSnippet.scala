package hr.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import hr.lib._
import Helpers._
import hr.model.{Department, Employee, DepartmentEmpCountDto}
import js.JsCmds.SetHtml
import js.{JsCmd, JsCmds}

class DepartmentSnippet {

  def delDept(deptId : Long) {
    Department.findByKey(deptId) match {
      case Full(dept) => dept.delete_!
      case _ => Nil
    }
  }

  def viewEmps(deptId: Long, empCount: Long) : JsCmd = {
    if (empCount == 0)
      SetHtml("employees", Text("department empty"))
    else
      SetHtml("employees", buildEmployeeTable(deptId))
  }

  def buildEmployeeTable(deptId: Long) : NodeSeq = {
    Department.findByKey(deptId) match {
      case Full(dept) => dept.employees.flatMap(
          emp => <tr>
                   <td>{emp.firstname.is}</td>
                   <td>{emp.lastname.is}</td>
                 </tr>)

      case _ => Text("You are screwed")
    }
  }

  def listDepartments = {

    ".department" #> Department.getWithEmpCount.map(dept =>
        ".name" #> <a href={"/view_department/" + dept.id}>{dept.name}</a> &
        ".empCount" #> dept.empCount &
        ".viewEmps" #> SHtml.ajaxButton("view emps", () => viewEmps(dept.id, dept.empCount)) &
        ".actions" #> SHtml.link("#", () => delDept(dept.id) , Text("Delete"))
    )

  }
}
