package hr.model

import _root_.net.liftweb.mapper._
import net.liftweb.common.{Box, Full}


class Employee extends LongKeyedMapper[Employee] with IdPK {
  def getSingleton = Employee

  object firstname extends MappedString(this, 50) {
    override def validations = { List(valMinLen(2, "firstname too short"),
                                      valMaxLen(50, "firstname too big")) }
  }

  object lastname extends MappedString(this, 50) {
    override def validations = { List(valMinLen(2, "firstname too short"),
                                      valMaxLen(50, "firstname too big")) }
  }

  object email extends MappedEmail(this, 50)

  object department extends MappedLongForeignKey(this, Department) {
    override def validSelectValues: Box[List[(Long, String)]] =
        Full(Department.findAll.map(d => (d.id.is, d.name.is)))
  }

}

object Employee extends Employee with LongKeyedMetaMapper[Employee] {
  override def fieldOrder = List(firstname, lastname, department)
}