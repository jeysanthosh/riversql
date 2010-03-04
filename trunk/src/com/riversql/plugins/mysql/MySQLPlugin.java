
package com.riversql.plugins.mysql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.databases.DialectFactory;
import com.riversql.dbtree.CatalogNode;
import com.riversql.dbtree.DatabaseNode;
import com.riversql.dbtree.IStructureNode;
import com.riversql.dbtree.SchemaNode;
import com.riversql.dbtree.TableNode;
import com.riversql.plugin.Plugin;
import com.riversql.plugins.mysql.actions.CreateDBDDL;
import com.riversql.plugins.mysql.actions.DropTable;
import com.riversql.plugins.mysql.actions.EmptyTable;
import com.riversql.plugins.mysql.actions.GetCollations;
import com.riversql.plugins.mysql.actions.RenameTable;
import com.riversql.plugins.mysql.actions.ShowCharacterSets;
import com.riversql.plugins.mysql.actions.ShowCollations;
import com.riversql.plugins.mysql.actions.ShowDatabases;
import com.riversql.plugins.mysql.actions.ShowEngines;
import com.riversql.plugins.mysql.actions.ShowPrivileges;
import com.riversql.plugins.mysql.actions.ShowProcesses;
import com.riversql.plugins.mysql.actions.ShowStatus;
import com.riversql.plugins.mysql.actions.ShowTableStatus;
import com.riversql.plugins.mysql.actions.ShowVariables;

public class MySQLPlugin implements Plugin {


	public JSONArray[] getContextMenu(SQLConnection conn, String nodeType) {
		if(isMysql(conn)){
			List<JSONArray> ls=new ArrayList<JSONArray>();
			if("tb".equals(nodeType)){
				JSONArray obj=new JSONArray();
				obj.put("-");
				obj.put("");
				obj.put("");
				ls.add(obj);
				
				obj=new JSONArray();
				obj.put("Rename Table...");
				obj.put("icons/textfield_rename.png");
				obj.put("mysql_renameTable(menuTreeC.nodeid.id,menuTreeC.nodeid.attributes.qname,menuTreeC.nodeid)");
				ls.add(obj);
				obj=new JSONArray();
				obj.put("Empty Table...");
				obj.put("icons/cut.png");
				obj.put("mysql_emptyTable(menuTreeC.nodeid.id,menuTreeC.nodeid.attributes.qname,menuTreeC.nodeid)");
				ls.add(obj);
				obj=new JSONArray();
				obj.put("Drop Table...");
				obj.put("icons/bomb.png");
				obj.put("mysql_dropTable(menuTreeC.nodeid.id,menuTreeC.nodeid.attributes.qname,menuTreeC.nodeid)");
				ls.add(obj);
				
				obj=new JSONArray();
				obj.put("-");
				obj.put("");
				obj.put("");
				ls.add(obj);
				
				obj=new JSONArray();
				obj.put("Analyze Table");
				obj.put("icons/chart_pie.png");
				obj.put("newEditor('analyze table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
				obj=new JSONArray();
				obj.put("Check Table");
				obj.put("icons/chart_bar.png");
				obj.put("newEditor('check table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
				obj=new JSONArray();
				obj.put("Explain Table");
				obj.put("icons/chart_line.png");
				obj.put("newEditor('explain '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
				obj=new JSONArray();
				obj.put("Show Create Table");
				obj.put("icons/database_edit.png");
				obj.put("newEditor('show create table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
				
				obj=new JSONArray();
				obj.put("Optimize Table");
				obj.put("icons/wrench.png");
				obj.put("newEditor('optimize table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
				obj=new JSONArray();
				obj.put("Repair Table");
				obj.put("icons/wrench_orange.png");
				obj.put("newEditor('repair table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
				
				
				
			}
			else if("dtbs".equals(nodeType)){
				JSONArray obj=new JSONArray();
				obj.put("Create Database...");
				obj.put("icons/database_add.png");
				obj.put("mysql_createDatabase(menuTreeC.nodeid.id)");
				ls.add(obj);
			}
                        else if("tbs".equals(nodeType)){
				JSONArray obj=new JSONArray();
				obj.put("Tables...");
				obj.put("icons/chart_pie.png");
				obj.put("newEditor('analyze table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);

                                obj=new JSONArray();
                                obj.put("Create Table...");
                                obj.put("icons/table_edit.png");
                                obj.put("createTable(menuTreeC.nodeid);");
                                ls.add(obj);
			}
                        else if("table".equals(nodeType)){
				JSONArray obj=new JSONArray();
				obj.put("Table...");
				obj.put("icons/chart_pie.png");
				obj.put("newEditor('analyze table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
			}
                        else if("view".equals(nodeType)){
				JSONArray obj=new JSONArray();
				obj.put("View...");
				obj.put("icons/chart_pie.png");
				obj.put("newEditor('analyze table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
			}
                        else if("ct".equals(nodeType)){
				JSONArray obj=new JSONArray();
				obj.put("Catalog...");
				obj.put("icons/chart_pie.png");
				obj.put("newEditor('analyze table '+menuTreeC.nodeid.attributes.qname).execute()");
				ls.add(obj);
			}
			return ls.toArray(new JSONArray[0]);
		}
		return null;
	}

	public List<IStructureNode> getSchemaAddedChildren(SchemaNode schemaNode,
			SQLConnection conn) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean isMysql(SQLConnection conn)
	{
		return DialectFactory.isMySQL(conn);
	}

	public JSONArray[] getAddedTabs(SQLConnection conn, String nodeType) {
		if(isMysql(conn)){
			List<JSONArray> ls=new ArrayList<JSONArray>();
			if("dtbs".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Databases");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showDatabases");
				ls.add(record);
				
				record=new JSONArray();
				record.put("Status");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showStatus");
				ls.add(record);
				record=new JSONArray();
				record.put("Variables");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showVariables");
				ls.add(record);
				record=new JSONArray();
				record.put("Processes");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showProcesses");
				ls.add(record);
				record=new JSONArray();
				record.put("Privileges");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showPrivileges");
				ls.add(record);
				record=new JSONArray();
				record.put("Engines");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showEngines");
				ls.add(record);
				
				record=new JSONArray();
				record.put("Character Sets");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showCharacterSets");
				ls.add(record);
				
				record=new JSONArray();
				record.put("Collations");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showCollations");
				ls.add(record);
			}
			if("ct".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Status");
				record.put("do?action=pluginAction&pluginName=MySQLPlugin&method=showTableStatus");
				ls.add(record);
			}
			return ls.toArray(new JSONArray[0]);
			}
		return null;
	}

	
	public List<IStructureNode> getCatalogAddedChildren(CatalogNode catalogNode,
			SQLConnection conn) {
		List<IStructureNode> added=new ArrayList<IStructureNode>();
		if(isMysql(conn)){
			
			added.add(new FunctionTypeNode(catalogNode,conn));
			added.add(new ProcedureTypeNode(catalogNode,conn));
                        added.add(new UsersNode(catalogNode,conn));
		}
		return added;
	}

	public JSONObject executeAction(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		String method=request.getParameter("method");
		String id=request.getParameter("id");
		if("showStatus".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			dn.getConn();
			return new ShowStatus(dn.getConn()).execute();
		}
		else if("showVariables".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			return new ShowVariables(dn.getConn()).execute();
		}else if("showDatabases".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			return new ShowDatabases(dn.getConn()).execute();
		}
		else if("showProcesses".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			return new ShowProcesses(dn.getConn()).execute();
		}
		else if("showEngines".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			return new ShowEngines(dn.getConn()).execute();
		}
		else if("showPrivileges".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			return new ShowPrivileges(dn.getConn()).execute();
		}
		else if("showTableStatus".equals(method)){
			CatalogNode cn=(CatalogNode)IDManager.get().get(id);
			return new ShowTableStatus(cn.getConn(),cn).execute();
		}
		else if("showCharacterSets".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			return new ShowCharacterSets(dn.getConn()).execute();
		}
		else if("showCollations".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			return new ShowCollations(dn.getConn()).execute();
		}else if ("getCollations_cset".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			JSONObject objsr = new GetCollations(dn.getConn()).execute();
			return objsr;
		}else if ("createDB".equals(method)){
			DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
			String name=request.getParameter("name");
			String collation=request.getParameter("collation");
			CreateDBDDL ddl=new CreateDBDDL(dn.getConn(),name,collation);
			return ddl.execute();
		}else if ("renameTable".equals(method)){
			String tableName=request.getParameter("tableName");
			String newName=request.getParameter("newName");
			TableNode tn=(TableNode)IDManager.get().get(id);
			CatalogNode cn=(CatalogNode)tn.getParent().getParent();
			String catalogName=cn.getName();
			RenameTable rt=new RenameTable(tn.getConn(),tableName,"`"+catalogName+"`.`"+newName+"`");
			return rt.execute();
		}else if ("emptyTable".equals(method)){
			String tableName=request.getParameter("tableName");
			TableNode tn=(TableNode)IDManager.get().get(id);
			EmptyTable et1=new EmptyTable(tn.getConn(), tableName);
			return et1.execute();
		}
		else if ("dropTable".equals(method)){
			String tableName=request.getParameter("tableName");
			TableNode tn=(TableNode)IDManager.get().get(id);
			DropTable dt=new DropTable(tn.getConn(), tableName);
			return dt.execute();
		}
		return null;

	}

	public JSONArray[] getDynamicPluginScripts(SQLConnection conn) {
		if(isMysql(conn)){
			List<JSONArray> ls=new ArrayList<JSONArray>();
			JSONArray obj=new JSONArray();
			obj.put( "mysql.js");
			ls.add(obj);
			return ls.toArray(new JSONArray[0]);
		}
		return null;
	}

}
