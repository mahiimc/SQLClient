import {  useState } from "react";

const useConnections = () => {
    
    const [loading,setLoading] = useState(false);
    const [connectionList, setConnectionList ] = useState([]);

    const url = 'connection/'

    async function  fetchConnections() {
        try {
            setLoading(true);
            let response = await fetch(url)
            response = await response.json();
            setConnectionList(response.data);
        }
        catch(error) {
           
        }
        finally {
            setLoading(false);
        }
     }

     return {
        connectionList , loading,  fetchConnections
     }
}

export default useConnections;