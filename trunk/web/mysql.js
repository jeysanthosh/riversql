
function mysql_createNewUser(id,tableName,node)
{
    var dialogTabPanel = new Ext.TabPanel( {
            autoTabs :true,
            activeTab :0,
            deferredRender :false,
            border :false
    });

    var userInfo = new Ext.form.FormPanel( {
            title: 'User Information',
            labelWidth :95,
            onSubmit :Ext.emptyFn,
            baseCls :'x-plain',

            items: [{
                xtype:'fieldset',
                title: 'Login Information',
                autoHeight:true,
                defaults: {width: 210},
                defaultType: 'textfield',
                collapsible: true,
                items :[{
                        fieldLabel: 'User Name',
                        name: 'username',
                        allowBlank:false
                    },{
                        fieldLabel: 'Password',
                        name: 'pass',
                        id: 'pass'
                    },{
                        fieldLabel: 'Confirm Password',
                        name: 'pass-cfrm',
                        vtype: 'password',
                        initialPassField: 'pass'
                    }, {
                        fieldLabel: 'Host',
                        name: 'host'
                    }
                ]
            },{
                xtype:'fieldset',
                title: 'Additional Information',
                autoHeight:true,
                defaults: {width: 210},
                defaultType: 'textfield',
                collapsible: true,
                items :[{
                        fieldLabel: 'Full Name',
                        name: 'fullName'
                    },{
                        fieldLabel: 'Description',
                        name: 'description'
                    },{
                        fieldLabel: 'Email',
                        name: 'email',
                        vtype:'email'
                    }
                ]
            }]
    });

    var privilegeInfo = new Ext.form.FormPanel( {
            title: 'User Privilege',
            labelWidth :95,
            onSubmit :Ext.emptyFn,
            baseCls :'x-plain'
    });

    var resourceInfo = new Ext.form.FormPanel( {
            title: 'Resource',
            labelWidth :95,
            onSubmit :Ext.emptyFn,
            baseCls :'x-plain',

            items: [{
                xtype:'fieldset',
                title: 'Limiting User Resources',
                autoHeight:true,
                defaults: {width: 210},
                defaultType: 'textfield',
                collapsible: true,
                items :[{
                        fieldLabel: 'Max Questions',
                        name: 'max_questions'
                    },{
                        fieldLabel: 'Max Updates',
                        name: 'max_updates'
                    },{
                        fieldLabel: 'Max Connections',
                        name: 'max_connections'
                    }, {
                        fieldLabel: 'Max User Connections',
                        name: 'max_user_connections'
                    }
                ]
            }]
    });

    dialogTabPanel.add(userInfo);
    dialogTabPanel.add(privilegeInfo);
    dialogTabPanel.add(resourceInfo);

    var dialog = new Ext.Window( {
            layout :'fit',
            width :650,
            height :500,
            modal :true,
            items :dialogTabPanel,
            title :'Create User ' + node.text
    });
    function onClose() {
            dialog.close();
    }
    function onCreateUserSubmit()
    {
        dialog.close();
    }
    
    dialog.addButton('Close', onClose, dialog);
    dialog.addButton('Done', onCreateUserSubmit, dialog);

    dialog.show();
}

function mysql_emptyTable(id,tableName,node){
	Ext.Msg.confirm('Empty Table '+tableName,'Do you really want to TRUNCATE TABLE '+tableName+'?',function(btn){
		
		if(btn=='yes'){
			new Ext.data.Connection().request( {
	    		url :'do?action=pluginAction&pluginName=MySQLPlugin&method=emptyTable',
	    		method :'post',
	    		scope :this,
	    		params :{id:id,tableName: tableName},
	    		callback : function(options, bSuccess, response) {
	    		}
	    	});
		}
	});
}
function mysql_dropTable(id,tableName,node){
	Ext.Msg.confirm('Drop Table '+tableName,'Do you really want to DROP TABLE '+tableName+'?',function(btn){
		
		if(btn=='yes'){
			new Ext.data.Connection().request( {
	    		url :'do?action=pluginAction&pluginName=MySQLPlugin&method=dropTable',
	    		method :'post',
	    		scope :this,
	    		params :{id:id,tableName: tableName},
	    		callback : function(options, bSuccess, response) {
	    			refreshNode(node.parentNode);
	    		}
	    	});
		}
	});
}
function mysql_renameTable(id,tableName,node){
	Ext.Msg.prompt('Rename Table '+tableName, 'Please enter the new table name:', function(btn, text_){
	    if (btn == 'ok'){
	    	new Ext.data.Connection().request( {
	    		url :'do?action=pluginAction&pluginName=MySQLPlugin&method=renameTable',
	    		method :'post',
	    		scope :this,
	    		params :{id:id,tableName: tableName,newName:text_},
	    		callback : function(options, bSuccess, response) {
	    			refreshNode(node.parentNode);
	    		}
	    	});
	    	
	    }
	});
}

function mysql_createDatabase(id){
	var collationsReader = new Ext.data.JsonReader({
		root: 'result.collations',				 
		 	id: 'collation_name'							
		},['collation_name' ,'character_set_name']);
	
	var collationsProxy = new Ext.data.HttpProxy({
		   url: 'do?action=pluginAction&pluginName=MySQLPlugin&method=getCollations_cset&id='+id,
		   method : "POST"
		});
	
	var collationsDataStore = new Ext.data.Store({
	    proxy: collationsProxy,
	    reader: collationsReader
	});
	collationsDataStore.load();
	var xCollationsTempl=new Ext.XTemplate('<tpl for="."><div class="search-item">',
            '<span nowrap="nowrap">&nbsp;{character_set_name} <b>{collation_name}</b> </span>',
        '</div></tpl>'
       );
	
	var combo = new Ext.form.ComboBox({
		
			store:collationsDataStore,
	        typeAhead: true,
	        editable:false,
	        mode: 'local',
	        triggerAction: 'all',
	        selectOnFocus:true,
	        width:200,
	        forceSelection:true,
	        fieldLabel:'collation',
	        itemSelector: 'div.search-item',
	        tpl: xCollationsTempl,
	        displayField:'collation_name',
	    	valueField:'collation_name', 
	    	resizable:true

	});
	
	var config = {
			width:400,
			height:120,
			shadow:true,
			minWidth:300,
			minHeight:80,
			modal: true,
			collapsible: false,
			closable: true,
			title:'New Database...' 
		};
		
		var dialog = new Ext.Window(config);
		dialog.addButton('Cancel', dialog.close, dialog);
		var createDBForm =  new Ext.form.FormPanel({
			labelWidth: 95, 
			url:'do?action=pluginAction&pluginName=MySQLPlugin&method=createDB' ,
			onSubmit: Ext.emptyFn,
			baseCls: 'x-plain'             
		});
		dbname=new Ext.form.TextField({
	        fieldLabel: 'Database Name',
	        name: 'name',
	        width:200,
	        readOnly:false,
	        allowBlank:false
	    });
		createDBForm.add(
				dbname,combo
		);
		function onCreateDBSubmit(){
			if (createDBForm.form.isValid()) {
				createDBForm.form.submit(
				{
					params:{
						id:id ,
						collation:combo.getValue() 
					},
					waitMsg:'...',
					failure: submitFailed,			
					success: submitSuccessful
				}
				);
			}else{
				Ext.MessageBox.alert('Error Message', 'Please fix the errors noted.');
			}
		}
		dialog.addButton('Generate DDL', onCreateDBSubmit, dialog);
		
		combo.setValue('');
		dialog.show();
		createDBForm.render(dialog.body);
		function submitSuccessful(form, action) {
			dialog.close();
			var object=Ext.decode(action.response.responseText);
			
			var str=object.result.string;
			newEditor(str);
			
		}
}


loaded_plugin_scripts["mysql.js"] = true;