import Search from 'antd/es/input/Search'
import React from 'react'
import './Styles.css'
import { FilterOutlined, ReloadOutlined } from '@ant-design/icons'
import { Tooltip } from 'antd'
import { AiOutlineAppstore } from 'react-icons/ai'
import { LuPlug } from 'react-icons/lu'
import ConnectionList from '../connection/ConnectionList'
import { useNavigate } from 'react-router-dom'


const Sidebar = () => {

    const navigate = useNavigate();


    return (
        <div className='side-bar'>
            <div className='action-items'>
                <Tooltip title="Add Connection"><LuPlug onClick={() => navigate('connections')} className='icon-btn' /></Tooltip>
                <Tooltip title="Driver Manager"><AiOutlineAppstore onClick={() => navigate('drivers')} className='icon-btn' /> </Tooltip>
            </div>
            <div className='search-box'>
                <Search placeholder="Connection name" style={{ width: 200 }} />
                <Tooltip title='Filter'><FilterOutlined /></Tooltip>
                <Tooltip title='Reload' ><ReloadOutlined /></Tooltip>
            </div>
            <ConnectionList />
            <p className='app-version'> App Version : 1.0.0 </p>
        </div>
    )
}

export default Sidebar