package hr.snippet

import net.liftweb.http._
import hr.model.Employee
import net.liftweb.common.{Full, Empty, Failure}
import xml.{Text, NodeSeq}
import net.liftweb.util.Helpers.tryo

class EditEmployee {

    def edit (html: NodeSeq): NodeSeq = {
        (for { idStr <- S.param("id") ?~ "param missing"
               id <- tryo(idStr.toLong)
               employee <- Employee.findByKey(id) ?~ "emp not found"
        }
        yield employee.toForm(Full("save"), "/index")) match { //openOr Text("Invalid Employee")
           case Full(editEmp) => editEmp
           case Failure(msg,_,_) => Text("Invalid Employee: " + msg)
         }
    }

}