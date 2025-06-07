package it.unibo.skalamon.model.types


trait Type:
  def name: String
  
object Type:
  def apply(name: String): Type =
    TypeImpl(name)

  private case class TypeImpl(_name: String) extends Type {
    override def name: String = _name
  }
  

object TypesRegister:

  private var typesList: List[Type] = List()

  def size: Int = typesList.size
  
  def createType(newType: Type): Unit =
    require(!typesList.exists(_.name == newType.name))
    typesList = newType :: typesList

  def reset(): Unit =
    typesList = List.empty
  