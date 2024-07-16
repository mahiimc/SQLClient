import React from 'react'
import Sidebar from './Sidebar'
import MainContent from '../connection/MainContent'
import { Outlet } from 'react-router-dom';


const AppLayout = () => {


  return (
    <div className='layout'>
        <Sidebar />
        <MainContent >
            <Outlet/>
          </MainContent>
    </div>
  )
}

export default AppLayout