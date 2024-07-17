
import CreateConnection from '../components/connection/CreateConnection';
import DriverList from '../components/driver/DriverList';
import DataViewer from '../components/editor/DataViewer';
import SQLEditor from '../components/editor/SQLEditor';
import Error from '../components/error/Error';
import AppLayout from '../components/layout/AppLayout';
import './Styles.css';

const { Empty } = require("antd");
const { createBrowserRouter } = require("react-router-dom");

export const router = createBrowserRouter([
    {
        path: "/",
        element: <AppLayout/>,
        errorElement: <Error/>,
        children: [
            {
                path: '',
                element: (
                    <Empty className='main-content-nodata' />
                )
            },
            {
                path: 'connections',
                element: <CreateConnection/>
            },
            {
                path: 'drivers',
                element: <DriverList/>
            },
            {
                path: 'editor/*',
                element: <SQLEditor/>
            },
            {
                path: 'data/*',
                element: <DataViewer/>
            }
        ]
    }
]);