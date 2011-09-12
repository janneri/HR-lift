package hr.model

class DepartmentEmpCountDto(id: Long, name: String, empCount: Long) {
  def id() : Long = id;
  def name(): String = name;
  def empCount(): Long = empCount;
}

