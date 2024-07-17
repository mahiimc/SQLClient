import { Editor } from '@monaco-editor/react'
import React, { useRef, useState } from 'react'
import './Editor.css';
import { Button } from 'antd';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import View from './View';

const SQLEditor = () => {

    const [query,setQuery] = useState('');
    const [connection,setConnection] = useState(null);
    const [columns,setColumns] = useState([]);
    const [data,setData] = useState([]);
    const [loading,setLoading] = useState(false);


    const editorRef = useRef(null);
    const location = useLocation();
    function handleEditorDidMount(editor, monaco) {
        editorRef.current = editor;
    }


    const executeQuery = async () => {
        setLoading(true);
        const connectionId = location.state.connectionId;
        setConnection(connectionId);
        const response = await axios.post("/data/query",{
            connectionId: connectionId,
            query: query
        })
        const columns = response.data.data.columns;
        const data = response.data.data.result;
        setData(data);
        setColumns(columns);
        setLoading(false);
    }


    return (
        <div className='editor-container'>
            {/* <div className='connection-details'>
               <p className='chip'>Active Connection  </p>
               <p>Ative Database: </p>
               <p>Connection ID : </p>
            </div> */}

            <Editor
                className='editor'
                defaultLanguage='sql'
                onMount={handleEditorDidMount}
                defaultValue="-- Write your SQL query here"
                options={{
                    selectOnLineNumbers: true,
                    roundedSelection: false,
                    readOnly: false,
                    cursorStyle: 'line',
                    automaticLayout: true,
                }}
                    onChange={(value,event) => setQuery(value)}
                />
                <div class="actions">
                    <Button>Cancel</Button>
                    <Button type='primary' onClick={executeQuery}>Execute</Button>
                </div>
                <div>
                    {data.length !== 0 && (<View
                        loading={loading}
                        cols={columns}
                        data={data}
                    />)}
                </div>
        </div>
    )
}

export default SQLEditor