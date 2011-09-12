package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, StandardDBVendor}
import _root_.java.sql.{Connection, DriverManager}
import _root_.hr.model._
import net.liftweb.widgets.autocomplete.AutoComplete


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
            "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    //AutoComplete.init

    // where to search snippet
    LiftRules.addToPackages("hr")
    Schemifier.schemify(true, Schemifier.infoF _, User, Department, Employee)

    LiftRules.rewrite.append {
      case RewriteRequest(ParsePath(List("view_department", id), _, _, _), _, _) =>
           RewriteResponse("view_department" :: Nil, Map("id" -> id))
      case RewriteRequest(ParsePath(List("edit_employee", id), _, _, _), _, _) =>
           RewriteResponse("edit_employee" :: Nil, Map("id" -> id))

    }

    /*
       LiftRules.statefulRewrite.append {
     case RewriteRequest(ParsePath("user" :: username :: Nil,_,_,_),_,_)
     if(User.findByUserName(username).isDefined) =>
       if(username == User.currentUser.map(_.username.is).openOr(null))
         RewriteResponse("profile" :: "view_self" :: Nil)
       else
         RewriteResponse("profile" :: "view" :: Nil, Map("username" -> username))
    }
    */
    // Build SiteMap
    def sitemap() = SiteMap(
      Menu("Home") / "index",
      Menu("View Department") / "view_department",
      Menu("Add employee") / "add_employee",
      Menu("Edit employee") / "edit_employee",
      Menu("Add department") / "add_department",
      Menu(Loc("Static", Link(List("static"), true, "/static/index"), "Static Content"))
    )

    LiftRules.setSiteMap(sitemap)
    //LiftRules.setSiteMapFunc(() => User.sitemapMutator(sitemap()))

    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    S.addAround(DB.buildLoanWrapper)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
}
