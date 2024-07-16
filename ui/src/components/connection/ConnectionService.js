import axios from "axios";

export async function fetchConnections() {
    try {
         const response = await axios.get("/connection/");
         return response.data;
    }
    catch (error) {
        console.log(error);
        return [];
    }
}


export async function saveConnection(values) {
    axios.post("connection/save", values)
        .then(async response => {
            const data = await response.data;
            return data;
        })
        .catch(async error => {
            console.log(error)
        })
}

export async function loadDriverClasses() {
   const  response = await  axios.get("driver/registered")
    return response.data;
}