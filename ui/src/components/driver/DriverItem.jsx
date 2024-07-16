import { DeleteOutlined } from "@ant-design/icons";
import { Divider, Popover, Tooltip } from "antd";
import axios from "axios";

const DriverItem = ({item,index}) => {


    


    return ({

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
      })
}

export default DriverItem;