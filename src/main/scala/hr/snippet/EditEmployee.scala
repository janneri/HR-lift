package hr.snippet

import net.liftweb.http._
import hr.model.Employee
import net.liftweb.common.{Full, Empty}
import xml.{Text, NodeSeq}

class EditEmployee {

    var id = S.param("id") openOr ""

    var employee = try {
        Employee.findByKey(id.toLong)
    } catch {
        case e:NumberFormatException => Empty
    }

    def edit (html: NodeSeq): NodeSeq ={
        employee map ({ emp =>
            emp.toForm(Full("save"), "/index")
        }) openOr Text("Invalid Employee")
    }

}