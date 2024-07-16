import React, { useEffect, useState } from 'react';
import { Tabs, Input, Table } from 'antd';
import { CloseOutlined, EditOutlined } from '@ant-design/icons';
import './Editor.css'; // Import custom styles
import { useLocation } from 'react-router-dom';
import axios from 'axios';

const { TabPane } = Tabs;

const Notepad = () => {
  const location = useLocation();

  
  const [activeKey, setActiveKey] = useState('1');
  const [tabs, setTabs] = useState([]);
  const [table,setTable] = useState('');
  const [columns,setColumns] = useState([]);
  const [dataSource,setDataSource] = useState([]);
  const prepareDataSource = (data) => 
    data.map( (item,index) => (item));
  

  const prepareColumns = (cols) => 
    cols.map( (column,index) => ({
      title: (<div><span className='colName'>{column.name}</span></div>),
      dataIndex: column.name,
      key: column.name,
    }));
  

  const addTab = () => {
    const newKey = `${tabs.length + 1}`;
    setTabs([...tabs, { key: newKey, title: `Tab ${newKey}`, content: `Content of Tab ${newKey}` }]);
    setActiveKey(newKey);
  };

  const removeTab = (targetKey) => {
    const newTabs = tabs.filter(tab => tab.key !== targetKey);
    if (newTabs.length) {
      setActiveKey(newTabs[0].key);
    }
    setTabs(newTabs);
  };

  const renameTab = (key, newTitle) => {
    const newTabs = tabs.map(tab => (tab.key === key ? { ...tab, title: newTitle } : tab));
    setTabs(newTabs);
  };

  const handleEdit = (targetKey, action) => {
    if (action === 'add') {
      addTab();
    } else if (action === 'remove') {
      removeTab(targetKey);
    }
  };

  useEffect(async () => {
    const tableName = location.state.tableName;
    const connectionId = location.state.connectionId;
    console.log(tableName)
    setTable(tableName)
    setTabs([{key: '1', title: `${tableName}` }])
    const response = await  axios.get(`/data/${connectionId}/${tableName}`);
    const columns = response.data.data.columns;
    setColumns(prepareColumns(columns));
    const result = response.data.data.result;
    setDataSource(prepareDataSource(result));

  },[location])

  return (
    <Tabs
      type="editable-card"
      activeKey={activeKey}
      onChange={setActiveKey}
      onEdit={handleEdit}
    >
      {tabs.map(tab => (
        <TabPane
          tab={
            <div className="tab-title">
              <span>{tab.title}</span>
              <EditOutlined
                onClick={(e) => {
                  e.stopPropagation();
                  const newTitle = prompt('Enter new title:', tab.title);
                  if (newTitle) {
                    renameTab(tab.key, newTitle);
                  }
                }}
                style={{ marginLeft: 8 }}
              />
            </div>
          }
          key={tab.key}
          closable={tabs.length > 1}
        >
          <div>
           <Table  scroll={{ x: 300, y: 500 }} style={{width:'100%',height:'40vh'}} columns={columns} dataSource={dataSource}/>
          </div>
        </TabPane>
      ))}
    </Tabs>
  );
};

export default Notepad;
