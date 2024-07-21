
import { useEffect, useState } from "react";
import { Dropdown, Empty, Menu, Skeleton, Tree } from "antd";
import { TbPlugOff } from "react-icons/tb";
import { fetchConnections } from "./ConnectionService";
import axios from "axios";
import './Connection.css'
import { CheckCircleOutlined, FileTextOutlined, InfoCircleOutlined, NumberOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { SiClojure } from "react-icons/si";

const ConnectionList = () => {

    const [ connectionList,  setConnectionList  ] = useState([]);
    const [loading , setLoading ] = useState(false);
    const navigate = useNavigate();


    const menuItems = [
      {
        label: 'Open SQL Editor',
        key: 'sqleditor'
      },
      {
        label: 'Edit Connection',
        key: 'edit'
      },
      {
        label: 'Refresh Connection',
        key: 'refresh'
      },
      {
        label: 'Delete Connection',
        key: 'delete'
      }
    ];

    const getColumnTypeIcon = (dataType) => {
      switch (dataType) {
        case 4:
        case -7:
        case -6:
        case 5:
        case -5:
        case 6:
        case 7:
        case 8:
        case 2:
        case 3:

          return <NumberOutlined />;
        case 2:
          return <CheckCircleOutlined />;
        case 12:
        default:
          return <FileTextOutlined />;
      }
    };
  

      const prepareSchemas = (schemas, connectionId) => {
        return schemas.map(schema => ({
          title: (
            <div>{schema}</div>
          ),
          key: `${connectionId}-catalog-${schema}`,
          isLeaf: false
        }));
      }

    const handleTableDoubleClick =  (connectionId, tableName) => {
      navigate(`data/${connectionId}/${tableName}`,{state:{connectionId: connectionId, tableName:tableName}});
    }

    const handleConnectionDoubleClick =  (connectionId) => {
      navigate(`editor/${connectionId}/`,{state:{connectionId: connectionId}});
    }

    const expandTableCatalog = async(connectionId,catalog) => {
      const response = await axios.post("/metadata/tables", {
        connectionId : connectionId,
        catalog: catalog,
        schema: ''
      })
      const tables = response.data.data;
      const tablesR=tables.map(table => ({
      title: (<div onDoubleClick={() => handleTableDoubleClick(connectionId, table.name)}>{table.name}</div>),
      key: `${connectionId}-catalog-${catalog}-schema-undefined-table-${table.name}`,
      isLeaf: false
    }));
      setConnectionList(prevState => {
        const newState = [...prevState];
        let connectionNode = newState.find(item => item.key == connectionId);
        if (catalog) {
          connectionNode = connectionNode.children.find(item => item.key == `${connectionId}-catalog-${catalog}`)
        }
        connectionNode.children = tablesR;
        return newState;
      })
    }

    const transformDataToTreeNodes = (data) => {
        return data.map(item => ({
          
          title: (<div onDoubleClick={() => handleConnectionDoubleClick(item.id)}>{item.name}</div>),
          key: `${item.id}`,
          isLeaf: false
        }));
      };

      const handleConnectionExpand = async (connectionId,catalog=undefined,schema=undefined) => {
          const response = await axios.post("/metadata/catalog",{
            connectionId: connectionId
          })
          const data = response.data.data;
          if  ( data === null || Object.keys(data).length == 0  ) {
            return expandTableCatalog(connectionId,catalog);
          }
          else {
            const catalogs = data.catalogs;
            if ( catalogs == undefined || catalogs == null ) {
               // set schemas
            }
            else {
              setConnectionList(prevState => {
                const newState = [...prevState];
                const connectionNode = newState.find(item => item.key == connectionId);
                connectionNode.children = catalogs.map(catalog => ({
                  title: (
                    <div>{catalog}</div>
                  ),
                  isLeaf: false,
                  key: `${connectionId}-catalog-${catalog}`
                }));
                return newState;
              })
            }
          }
      }

      const handleExpandTable = async(connectionId, catalog , schema,tableName) => {
            
        let catalogExists =  catalog != "undefined";
        let schemaExists =   schema != "undefined";

        let key = connectionId;
        let catKey = key +"-catalog-"+catalog;
        let schemaKey = catKey+"-schema-"+schema;
        let fullKey = schemaKey;
       
        const response = await axios.post('/metadata/columns',{
              connectionId : connectionId,
              catalog : catalog,
              schema: schema,
              table: tableName
        });

        const columnsData = response.data.data;
        const columns = columnsData.map(column => ({
          title: (
            <div className="column-title">
              {getColumnTypeIcon(column.dataType)} {column.name}
            </div>
          ),
          key: `${fullKey}-table-${tableName}-column-${column.name}`,
          isLeaf: true
        }));
        
        setConnectionList(prevState => {
          const newState = [...prevState];
          let connectionNode = newState.find(item => item.key == connectionId);
          if ( catalogExists ) {
             connectionNode = connectionNode.children.find(item => item.key == `${catKey}`);
          }
          
          if ( schemaExists ) {
             connectionNode = connectionNode.children.find(item => item.key == `${schemaKey}`);
          }
          let tableNode = connectionNode.children.find(item => item.key == `${fullKey}-table-${tableName}`);
          tableNode.children = columns;
          return newState;
        })

      }

      const onExpand = async (expandedKeys, {expanded, node}) => {

        if ( !expanded ) return ;



        if ( !isNaN(node.key)) {
            return handleConnectionExpand(node.key)
        } 

        const keyParts = node.key.split("-");
        const keyPartsLen = keyParts.length;
        const connectionId = keyParts[0];
        const tablesIdx = keyParts.indexOf('table');
        const colsIdx = keyParts.indexOf('column');
        const catIdx = keyParts.indexOf('catalog');
        const schemaIdx = keyParts.indexOf('schema');

        if ( tablesIdx === (keyPartsLen-2) ) {
          const catalog = keyParts[catIdx+1];
          const schema = keyParts[schemaIdx+1];
          const tableName = keyParts[keyPartsLen-1];
          await handleExpandTable(connectionId,catalog,schema,tableName);
        }
        else if (colsIdx === (keyPartsLen-2)) {
            const tableName = keyParts[tablesIdx];
            const response = await axios.post(`/metadata/columns`,{
              table: tableName,
              connectionId: connectionId
            });
            const columns = response.data.data.map(column => ({
              title: (
                <div className="column-title">
                  {getColumnTypeIcon(column.dataType)} {column.name}
                </div>
              ),
              key: `${connectionId}-column-${tableName}-${column.name}`,
              isLeaf: true
            }));

            setConnectionList(prevState => {
                const newState = [...prevState];
                const connectionNode = newState.find(item => item.key == connectionId);
                const tableNode = connectionNode.children[0].children.find(item => item.key == `${connectionId}-table-${tableName}`);
                tableNode.children = columns;
                return newState;
            })
        } 
        else if (catIdx === (keyPartsLen-2))   {
            const catalog = keyParts[catIdx+1];
            const response = await axios.post('/metadata/schema', {
              connectionId : connectionId,
              catalog: catalog
            });
            const data = response.data.data;
            if ( data === undefined || Object.keys(data).length == 0) {
              return expandTableCatalog(connectionId,catalog);
            }
            const schemas = data.schemas;
            const schemasR = schemas.map(schema => ({
              title: (
                <div>{schema}</div>
              ),
              key: `${connectionId}-catalog-${catalog}-schema-${schema}`,
              isLeaf: false
            }));
            
            setConnectionList(prevState => {
              const newState = [...prevState];
              const connectionNode = newState.find(item => item.key == connectionId);
              const catalogNode = connectionNode.children.find(item => item.key == `${connectionId}-catalog-${catalog}`);
              catalogNode.children = schemasR;
              return newState;
          })

        }
        else if (schemaIdx === keyPartsLen-2)   {
            let catalog = keyParts[catIdx+1];
            let schema = keyParts[schemaIdx+1];

            const response = await axios.post("/metadata/tables", {
              connectionId : connectionId,
              catalog: catalog,
              schema: schema
            })
            const tables = response.data.data;

            const tablesR=tables.map(table => ({
            title: (<div onDoubleClick={() => handleTableDoubleClick(connectionId, table.name)}>{table.name}</div>),
            key: `${connectionId}-catalog-${catalog}-schema-${schema}-table-${table.name}`,
            isLeaf: false
          }));

            setConnectionList(prevState => {
              const newState = [...prevState];
              const connectionNode = newState.find(item => item.key == connectionId);
              const catalogNode = connectionNode.children.find(item => item.key == `${connectionId}-catalog-${catalog}`);
              const schemaNode = catalogNode.children.find(item => item.key == `${connectionId}-catalog-${catalog}-schema-${schema}`);
              schemaNode.children = tablesR;
              return newState;
          })

        }
        
      }

    useEffect(() => {
        
        const getConnections = async () => {
            const data = await fetchConnections();
            setConnectionList(transformDataToTreeNodes(data.data));
        }
        setLoading(true);
        getConnections();
        setLoading(false);
    },[])
   

    return (
    <div className='connection-items'>
            { loading ? <Skeleton/> :   connectionList.length < 1 ? (
                <Empty  description="No Connections available" image={<TbPlugOff style={{fontSize:'40px'}}/>} className='empty' />
            ) : (
              <div className="scrollable-tree">
                <Tree
                    showLine
                    onExpand={onExpand}
                    treeData={connectionList}
              />
              </div>
            )}
            </div>
    );
}

export default ConnectionList;