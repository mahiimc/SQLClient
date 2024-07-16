import React, { useEffect, useState } from 'react'
import { Button, Card, Divider, List, message, Popover, Row, Skeleton, Table, Tooltip, Upload } from 'antd'
import axios from 'axios';
import './Drivers.css';
import { DeleteOutlined, ReloadOutlined, UploadOutlined } from '@ant-design/icons';
import { deleteDriver, fetchDrivers } from './DriverService';
import { useDispatch, useSelector } from 'react-redux';
import { addDriver, addDriverList, removeDriver } from '../../redux/slices/DriverSlice';





const DriverList = () => {

  const [fileList,setFileList] = useState([]);
  const [loading,setLoading] = useState(false);
  const [formattedDrivers, setFormattedDrivers] = useState([]);

  let driverList = useSelector((state) => state.driver.drivers);
  const dispatch =  useDispatch();


  const columns = [
    {
      title: 'S.No',
      dataIndex: 'sno',
      key: 'sno',
    },
    {
      title: 'Name',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: 'Actions',
      dataIndex: 'action',
      key: 'action',
    }
  ]

const handleDelete = (name,id) => {
  deleteDriver(name).then(response => {
    message.success(response);
    dispatch(removeDriver(id));
    const newList = driverList.map((item,index) => formatDrivers(item,index));
    setFormattedDrivers(newList);
   
  })
  .catch(error => {
    message.error(error);
  })

}
const formatDrivers =  (item,index) => 
     ( {
      key: item.id,
      sno: index + 1,
      title: ( 
        <Popover placement='right' content={(
          <div>
            <p>Path : {item.name}</p>
            <Divider/>
            <p>Last updated : {new Date(item.lastUpdatedTime).toISOString()}</p>
          </div>
        )}>
          {item.title} 
        </Popover>
        ),
      action: ( <Tooltip title='Delete'> <DeleteOutlined onClick={() => handleDelete(item.name,item.id)} /></Tooltip> )
    }
    )
  

  async function loadDrivers() {
        setLoading(true);
        fetchDrivers().then(data => {
          dispatch(addDriverList(data));
          setLoading(false);
          const formattedDrivers = data.map((item,index) => formatDrivers(item,index));
          setFormattedDrivers(formattedDrivers);
        });
  }



  const handleUpload = async ({file, onSuccess, onError}) => {
    
    console.log("Intial:: ",driverList);
    
    const formData = new FormData();
      formData.append('file', file);
      try {
      const response = await axios.post('/driver/',formData,{
        headers: {
          'Content-Type': 'multipart/form-data',
        }
      });
      const data =  response.data.data;
      dispatch(addDriver(data));
      setFormattedDrivers([...formattedDrivers, formatDrivers(data,driverList.length)]);
      onSuccess("Ok")
      
      message.success(response.data.message);
    }
    catch(error) {
      console.log(error.response.data);
      setFileList((prevList) =>
        prevList.map((item) =>
          item.uid === file.uid ? { ...item, status: 'error' } : item
        )
      );
      onError(error);
      message.error(error);
    }
  }

  useEffect(() => {
    loadDrivers();
  }, [])

  return (
    <div className='driver-list'>
      <Card className='drivers-card' 
            title='Available Drivers'
            extra={
              <ReloadOutlined onClick={() => fetchDrivers()} />
            }
            >
        {loading ? (<Skeleton active/>) : ( 
         <Table  columns={columns} dataSource={formattedDrivers}
          />
        ) }
        
        <div className='driver-upload'>
          <Upload
            customRequest={handleUpload}
            fileList={fileList}
            onChange={({file,fileList}) => setFileList(fileList)}
            maxCount={1}
          
          >
              <Button type='primary' icon={<UploadOutlined/>}>Upload Driver</Button>
          </Upload>
      </div>

      </Card>
      
    </div>
  )
}

export default DriverList