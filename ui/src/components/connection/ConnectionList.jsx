
import { useEffect, useState } from "react";
import { Dropdown, Empty, Menu, Skeleton, Tree } from "antd";
import { TbPlugOff } from "react-icons/tb";
import { fetchConnections } from "./ConnectionService";
import axios from "axios";
import './Connection.css'
import { CheckCircleOutlined, FileTextOutlined, InfoCircleOutlined, NumberOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

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
  

    const handleTableDoubleClick = async (connectionId, tableName) => {
      // const response = await  axios.get(`/data/${connectionId}/${tableName}`);
      // console.log(response.data.data.result);
      navigate(`notepad/${connectionId}/${tableName}`,{state:{connectionId: connectionId, tableName:tableName}});
    }

    const transformDataToTreeNodes = (data) => {
        return data.map(item => ({
          
          title: (<div onDoubleClick={() => alert('double click')}>{item.name}</div>),
          key: item.id,
          children: [
            {
              title: 'Tables',
              key: `${item.id}-tables`,
              isLeaf: false,
              children:[]
            },
            {
              title: 'Views',
              key: `${item.id}-views`,
              isLeaf: false,
              children:[]
            }
          ],
        }));
      };


      const onExpand = async (expandedKeys, {expanded, node}) => {

        if ( !expanded ) return ;

        if ( !isNaN(node.key)) return ;
        const keyParts = node.key.split("-");
       
        

        if ( keyParts.length === 2 &&  keyParts[1] === "tables"  ) {
          const connectionId = keyParts[0];
          const response = await axios.get(`/metadata/${connectionId}`);
          const tables = response.data.data.map(table => ({
            title: (<div onDoubleClick={() => handleTableDoubleClick(connectionId, table.name)}>{table.name}</div>),
            key: `${connectionId}-table-${table.name}`,
            isLeaf: false
          }));

          setConnectionList(prevState => {
            const newState = [...prevState];
            const connectionNode = newState.find(item => item.key == connectionId)
            connectionNode.children[0].children = tables;
            return newState;
          })

        }
        else if (keyParts.length === 3 &&  keyParts[1] === "table") {
            const connectionId = keyParts[0];
            const tableName = keyParts[2];
            const response = await axios.get(`/metadata/${connectionId}/${tableName}`);
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