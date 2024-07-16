import { DeleteOutlined } from "@ant-design/icons";
import { Divider, Popover, Tooltip } from "antd";
import axios from "axios";

export async function fetchDrivers() {
   return  axios.get("/driver/")
      .then(async response => {
        let data = await response.data.data
        return data;
      })
      .catch(error => {
        console.log(error);
        throw error;
      })
  }

  export const deleteDriver = async (path) => {
    
    axios.post("/driver/delete",{
      path: path
    }).then(async response => {
      let data = await response.data.message;
      return data;
    })
    .catch(error => {
      throw  error;
    })
}

