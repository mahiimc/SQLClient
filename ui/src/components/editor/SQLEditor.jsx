import { Editor } from '@monaco-editor/react'
import React, { useRef } from 'react'
import './Editor.css';
import { Button } from 'antd';

const SQLEditor = () => {

    const editorRef = useRef(null);

    function handleEditorDidMount(editor, monaco) {
        editorRef.current = editor;
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
                
                />
                <div class="actions">
                    <Button>Cancel</Button>
                    <Button type='primary'>Execute</Button>
                </div>
        </div>
    )
}

export default SQLEditor