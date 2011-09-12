package hr.model

import _root_.net.liftweb.mapper._

class Department extends LongKeyedMapper[Department] with IdPK with OneToMany[Long, Department] {
  def getSingleton = Department

  object name extends MappedString(this, 100)

  object employees extends MappedOneToMany(Employee, Employee.department, OrderBy(Employee.id, Ascending))
  //def employees = Employee.findAll(By(Employee.department, this.id))

  def getWithEmpCount = DB.runQuery("select d.id, d.name, count(e.department) " +
                                    "from Department d left outer join Employee e on d.id = e.department " +
                                    "group by d.id, d.name")
                        ._2
                        .map(list => new DepartmentEmpCountDto(
                                            list(0).toLong,
                                            list(1),
                                            list(2).toLong));

}

object Department extends Department with LongKeyedMetaMapper[Department] {
  override def fieldOrder = List(name)
}